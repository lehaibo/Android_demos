package com.lehaibo.myapp2.base;

import com.lehaibo.myapp2.model.PublicType;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.LogFactory;

/***
 * 参考代码
 * @author  
 *
 */
public class ServerUrlBuilder {

	private static final CommonLog log = LogFactory.createLog();
	
	public static final String baseURL = "http://www.runapp.cn/Topics/public/";
//	public static final String baseURL = "http://192.168.1.108/Topics/public/App/";
	
	public static final String registerURL = "App/Register";
	public static final String loginURL = "App/Login";
	public static final String bindtokenURL = "App/BandExternInfo";
	public static final String adclickURL = "App/AdClick";
	public static final String aboutURL = "App/About";
	public static final String getInfotURL = "Topic/list";
	public static final String deleeteITopicURL = "Topic/Delete";
	public static final String checkUpdateURL = "App/versioncheck";
	
	public static String getServerURL(int action){
		switch(action){
			case PublicType.USER_REGISTER_MASID:
				return getRegisterURL();
			case PublicType.USER_LOGIN_MASID:
				return getLoginURL();
			case PublicType.BIND_TOKEN_MSGID:
				return getBindtokenURL();
			case PublicType.AD_CLICK_MSGID:
				return getAdClickURL();
			case PublicType.ABOUT_MSGID:
				return getAboutPageURL();
			case PublicType.GET_INFO_MSGID:
				return getInfoURL();
			case PublicType.DELETE_INFO_MSGID:
				return deleteInfoURL();
			case PublicType.CHECK_UPDATE_MSGID:
				return getCheckUpdateURL();
		}
		
		return "";
	}
	
	
	public static String getRegisterURL(){
		return baseURL + registerURL;
	}
	
	public static String getLoginURL(){
		return baseURL + loginURL;
	}
	
	public static String getBindtokenURL(){
		return baseURL + bindtokenURL;
	}
	
	public static String getAdClickURL(){
		return baseURL + adclickURL;
	}
	
	public static String getAboutPageURL(){
		return baseURL + aboutURL;
	}
	
	
	public static String getInfoURL(){
		return baseURL + getInfotURL;
	}
	
	public static String deleteInfoURL(){
		return baseURL + deleeteITopicURL;
	}
	
	
	public static String getCheckUpdateURL(){
		return baseURL + checkUpdateURL;
	}
}
