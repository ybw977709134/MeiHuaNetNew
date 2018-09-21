package com.MeiHuaNet.listener;

import java.util.Observable;

public class ActivityCancelObserver extends Observable{

	/**
	 * 通知观察的对象，页面取消了
	 */
	public void notifyCancel(){
		setChanged();
		notifyObservers();
		deleteObservers();
	}
}
