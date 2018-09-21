package com.MeiHuaNet.network;

import java.util.HashMap;


/**
 * 
 * @description 联网请求时的参数的实体类
 * @author lee
 * @createTime 2013-6-25上午11:13:46
 * 
 */
public class WebServiceParams {

	/**
	 * 请求webservice时调用的方法名
	 */
	public String methodName;
	
	/**
	 * 请求的远程接口的url
	 */
	public String webServiceUrl;
	

	/**
	 * 请求的方法所需要的参数列表，key是参数的名称，value是参数的值
	 */
	public HashMap<String, Object> paramList = new HashMap<String, Object>();

	
	public WebServiceParams(){
	}
	
}
