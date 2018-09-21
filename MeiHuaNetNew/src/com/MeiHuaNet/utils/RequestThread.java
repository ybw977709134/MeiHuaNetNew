package com.MeiHuaNet.utils;

import java.util.Observable;

/**
 *
 * @description
 * @author lee
 * @createTime 2013-7-30下午2:52:20
 *
 */
public class RequestThread extends Thread{

	public Obsever obsever = new Obsever();
	
	public static class Obsever extends Observable{
//		public boolean is = false;
		public void notifyCancel(){
				setChanged();
				notifyObservers();
				deleteObservers();
		}
	}
}
