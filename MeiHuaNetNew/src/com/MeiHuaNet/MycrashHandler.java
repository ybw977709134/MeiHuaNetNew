package com.MeiHuaNet;

import java.lang.Thread.UncaughtExceptionHandler;

import com.umeng.analytics.MobclickAgent;


import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * 自定义的系统异常处理器
 * @author Administrator
 *
 */
public class MycrashHandler implements UncaughtExceptionHandler{

	private static UncaughtExceptionHandler sdefaultHandler;
	private static MycrashHandler smycrashHandler;
	private static Context mContext;
	
	private MycrashHandler(){
	}
	
	public static MycrashHandler getInstance(Context context){
		if(smycrashHandler == null){
			smycrashHandler = new MycrashHandler();
			mContext = context;
			sdefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		}
		return smycrashHandler;
	}
	
	/*当某个异常 没有代码显示的捕获的时候, 系统会调用默认的异常处理的代码 处理这个异常*/
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
		if (!handleException(ex) && sdefaultHandler != null) {  
            //如果用户没有处理则让系统默认的异常处理器来处理 
            sdefaultHandler.uncaughtException(thread, ex);  
        } else {  
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
            }  
            //退出程序  
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(1);  
        }  
	}
	
	/** 
     * 自定义错误处理 ，收集错误信息 发送错误报告等操作均在此完成. 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false. 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return false;  
        }  
        MobclickAgent.reportError(mContext, ex.getMessage());
        //使用Toast来显示异常信息
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                Toast.makeText(mContext, "很抱歉程序出现异常", Toast.LENGTH_LONG).show();  
                MyApplication myApplication = (MyApplication) mContext.getApplicationContext();
                myApplication.setmIsExit(true);
                Looper.loop();  
            }  
        }.start();  
        return true;  
    }  

}
