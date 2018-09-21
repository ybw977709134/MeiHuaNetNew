package com.MeiHuaNet.listener;

import java.util.Observable;

public class LoginSuccessObserver extends Observable{

	public void notifySucess(){
		setChanged();
		notifyObservers();
		deleteObservers();
	}
}
