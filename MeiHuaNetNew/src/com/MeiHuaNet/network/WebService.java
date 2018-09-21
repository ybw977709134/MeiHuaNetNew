package com.MeiHuaNet.network;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.MeiHuaNet.UIManager;
import com.MeiHuaNet.listener.ActivityCancelObserver;
import com.MeiHuaNet.utils.RequestThread;
import com.MeiHuaNet.utils.Utils;

import android.os.Handler;

/**
 * 
 * @description 从服务器获取数据或提交数据到服务器
 * @author lee
 * @createTime 2013-6-21下午2:24:56
 * 
 */
public class WebService {

	/**
	 * 主动取消本次联网请求
	 */
	public static final String REQUEST_CANCEL = "request_cancel";
	/**
	 * 联网请求出现未知的异常
	 */
	public static final String REQUEST_EXCEPTION = "request_exception";

	private static ExecutorService executorService = Executors
			.newFixedThreadPool(10);

	private static Handler handler = null;

	/**
	 * 异步请求网络获取解析好的对应的数据
	 * 
	 * @param <T>
	 * 
	 * @param requestParams
	 *            请求的webservice的接口名和接口参数的实体类
	 * @param callbackListener
	 *            联网处理完后回调接口，在主线程中去重写的这个接口
	 * @param is
	 *            是否弹出阻塞对话框
	 * @param observer
	 *            观察者（观察一个页面中的网络请求，当页面销毁时同时取消页面中的所有网络请求。传入null时不监测请求）
	 */
	public static <T> void requestAsynHttp(
			final WebServiceParams requestParams,
			final HttpCallbackListener callbackListener, boolean is,
			final ActivityCancelObserver cancelObserver) {
		checkHandler();
		if (!NetworkUtils.isAlived()) {
			// 没有网络时，回调方法中传个空字符串，这样界面不会卡住
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (callbackListener != null) {
						callbackListener.onSuccessCallBack("");
					}
				}
			});
			return;
		}
		try {
			RequestThread t = new RequestThread() {
				@Override
				public void run() {
					final String result = getStringFromService(requestParams,
							obsever, cancelObserver);
					// 在handler中调用回调方法，这样回调方法中的操作就是在主线程中执行
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (callbackListener != null) {
								if (REQUEST_CANCEL.equals(result)) {
									callbackListener.onCancelCallBack();
								} else if (REQUEST_EXCEPTION.equals(result)) {
									callbackListener.onErrorCallBack();
								} else {
									callbackListener.onSuccessCallBack(result);
								}
							}
						}
					});
				}
			};
			executorService.submit(t);
			if (is) {
				UIManager.cancelAllProgressDialog();
				UIManager.showProgressDialog(null, t, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void checkHandler() {
		try {
			if (handler == null) {
				handler = new Handler();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handler = null;
		}

	}

	/**
	 * 通过webservice请求从服务器获取数据
	 * 
	 * @return
	 */
	public static String getStringFromService(WebServiceParams requestParams,
			RequestThread.Obsever obsever, ActivityCancelObserver cancelObserver) {
		String nameSpace = "http://tempuri.org/";
		String methodName = requestParams.methodName;
		String webServiceUrl = requestParams.webServiceUrl;
		String soapAction = nameSpace + methodName;
		String result = null;
		Utils.log("request method is : " + requestParams.methodName);

		SoapObject sobject = new SoapObject(nameSpace, methodName);

		setParams(sobject, requestParams.paramList);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = sobject;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(sobject);
		envelope.encodingStyle = "UTF-8";

		MyAndroidHttpTransport transport = new MyAndroidHttpTransport(
				webServiceUrl, 20000);
		if (cancelObserver != null) {
			//将这次请求和所在的页面关联
			cancelObserver.addObserver(transport);
		}

		//将阻塞对话框和这次联网请求关联
		obsever.addObserver(transport);
		
		try {
			transport.call(soapAction, envelope);
			if (envelope.getResponse() != null) {
				Object soapobject = envelope.getResponse();
				result = soapobject.toString();
			}
		} catch (SocketTimeoutException e) {
			// 这个异常主要是有webservice请求超时产生的
			e.printStackTrace();
			result = REQUEST_EXCEPTION;
		} catch (SocketException e) {
			// 这个异常主要是由于调用MyAndroidHttpTransport的disconnection（）之后产生的,（socket关闭了之后，继续使用这个socket）
			// 简单的通过这个异常来判断是自己主动取消一次请求的，实际上还有别的原因也可以产生这个异常（别的原因不明，偶尔碰到）
			e.printStackTrace();
			result = REQUEST_CANCEL;
		} catch (IOException e) {
			// 这个异常主要是由于调用MyAndroidHttpTransport的disconnection（）之后产生的
			e.printStackTrace();
			result = REQUEST_CANCEL;
		} catch (Exception e) {
			e.printStackTrace();
			result = REQUEST_EXCEPTION;
		}
		Utils.log(result);
		//一次联网请求完成后，删除这次的观察对象
		obsever.deleteObservers();
		if(cancelObserver!=null){
			cancelObserver.deleteObserver(transport);
		}
		return result;
	}

	/**
	 * 设置webservice请求方法的参数
	 * 
	 * @param sobject
	 * @param params
	 */
	private static void setParams(SoapObject sobject,
			HashMap<String, Object> params) {
		Set<String> keys = params.keySet();
		Iterator<String> itor = keys.iterator();
		while (itor.hasNext()) {
			String key = itor.next();
			Object value = params.get(key);
			sobject.addProperty(key, value);
			Utils.log("request key is : " + key + "  value is : " + value);
		}
	}
}
