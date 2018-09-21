package com.MeiHuaNet.network;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;

import com.MeiHuaNet.utils.Utils;

/**
 * 
 * @description 修改webservice请求的超时用的类
 * @author lee
 * @createTime 2013-7-1下午3:38:46
 * 
 */
public class MyAndroidHttpTransport extends HttpTransportSE implements Observer {

	private int timeout = 30000; // 默认超时时间为30s
	private String url;
	ServiceConnectionSE serviceConnection;

	public MyAndroidHttpTransport(String url) {
		super(url);
		this.url = url;
	}

	public MyAndroidHttpTransport(String url, int timeout) {
		super(url);
		this.timeout = timeout;
		this.url = url;
	}

	@Override
	protected ServiceConnection getServiceConnection() throws IOException {
		serviceConnection = new ServiceConnectionSE(this.url);
		serviceConnection.setConnectionTimeOut(timeout);
		return serviceConnection;
	}

	// public void ObseverDis(final RequestThread.Obsever obsever) {
	// Thread t = new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// long curtime = System.currentTimeMillis();
	// while (true) {
	// if ((System.currentTimeMillis() - curtime) > 60 * 1000) {
	// break;
	// }
	// if (obsever.is) {
	// if (serviceConnection != null)
	// serviceConnection.disconnect();
	// break;
	// }
	// }
	// }
	// });
	// t.start();
	//
	// }

	@Override
	public void update(Observable observable, Object data) {
		Utils.log("disconnect()---");
		try {
			if (serviceConnection != null)
				serviceConnection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
