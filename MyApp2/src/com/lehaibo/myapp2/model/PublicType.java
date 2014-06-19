package com.lehaibo.myapp2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lehaibo.myapp2.base.AbstractBaseProtocol;
import com.lehaibo.myapp2.base.IParseJson;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.LogFactory;



public class PublicType {

	private static final CommonLog log = LogFactory.createLog();
	
	// 用户注册
	public final static int USER_REGISTER_MASID = 0x0001;
	public static class UserRegister extends AbstractBaseProtocol
	{	

		public final static String KEY_MAC = "mac";
		public final static String KEY_IMSI = "imsi";
		public final static String KEY_MFG = "mfg";
		public final static String KEY_BRAND = "brand";
		public final static String KEY_MODEL = "model";
		public final static String KEY_TELCO = "telco";
		public final static String KEY_OSVER = "osver";
		public final static String KEY_VER = "ver";
		public final static String KEY_SCREENSIZE = "screensize";

	
		public String mMac = "";
		public String mImsi = "";
		public String mMfg = "";
		public String mBrand = "";
		public String mModel = "";
		public String mTelco = "";
		public String mOsver = "";
		public String mVer = "";
		public String mScreensize = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_MAC, mMac);
			mapValue.put(KEY_IMSI, mImsi);
			mapValue.put(KEY_MFG, mMfg);
			mapValue.put(KEY_BRAND, mBrand);
			mapValue.put(KEY_MODEL, mModel);
			mapValue.put(KEY_TELCO, mTelco);
			mapValue.put(KEY_OSVER, mOsver);
			mapValue.put(KEY_VER, mVer);
			mapValue.put(KEY_SCREENSIZE, mScreensize);
			return mapValue;
		}
	}
	
	
	// 用户注册回应
	public static class UserRegisterResult implements IParseJson{

		public final static String KEY_KEYS = "keys";
		
		public String mKeys = "";

		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
				
			mKeys = jsonObject.getString(KEY_KEYS);
			return true;

		}	
	}
	
	// 用户登录
	public final static int USER_LOGIN_MASID = 0x0003;
	public static class UserLogin extends AbstractBaseProtocol
	{
		

		public final static String KEY_CONN = "conn";
		public final static String KEY_MODEL = "model";
		public final static String KEY_TELCO = "telco";
		public final static String KEY_OSVER = "osver";
		public final static String KEY_VER = "versioncode";
		
		public String mConn = "";
		public String mModel = "";
		public String mTelco = "";
		public String mOsver = "";
		public String mVer = "";
		
		

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_CONN, mConn);
			mapValue.put(KEY_MODEL, mModel);	
			mapValue.put(KEY_TELCO, mTelco);
			mapValue.put(KEY_OSVER, mOsver);
			mapValue.put(KEY_VER, mVer);
			return mapValue;
		}
	}
	
	// 用户登录回应
	public static class UserLoginResult implements IParseJson{


		public final static String KEY_DATALIST = "DataList";
		public final static String KEY_ADMIN = "isAdmin";
		public final static String KEY_ADTYPE = "adType";

	
		public List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
		public int mIsAdmin = 0;
		public int mAdType = 0;
		
		
		//-----------------------------------------------
		public final static String KEY_HavNewVer = "haveNewVer";	
		public final static String KEY_VerCode = "verCode";		
		public final static String KEY_VerName = "verName";		
		public final static String KEY_AppUrl = "appUrl";		
		public final static String KEY_VerDescribe = "verDescribe";		
		public final static String KEY_FORCEUPDATE = "forceUpdate";	
		
		public int mForceUpdate = 0;
		public int mHaveNewVer = 0;
		public int mVerCode = 0;
		public String mVerName = "";
		public String mAppUrl = "";
		public String mVerDescribre = "";
		//-----------------------------------------------
		
		
		
		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {

			mIsAdmin = jsonObject.getInt(KEY_ADMIN);
			mAdType = jsonObject.getInt(KEY_ADTYPE);
			
			JSONArray jsonArray = jsonObject.getJSONArray(KEY_DATALIST);
			int size = jsonArray.length();
			for(int i = 0; i < size; i++){
				JSONObject tmp = jsonArray.getJSONObject(i);
				BaseType.ListItem item = new BaseType.ListItem();
				try {
					item.parseJson(tmp);
					mDataList.add(item);

				} catch (JSONException e) {
					e.printStackTrace();
				}							
			}
			
			mForceUpdate = jsonObject.optInt(KEY_FORCEUPDATE);
			mHaveNewVer = jsonObject.optInt(KEY_HavNewVer);
			if (mHaveNewVer != 0){
				mVerCode = jsonObject.optInt(KEY_VerCode);
				mVerName = jsonObject.optString(KEY_VerName);
				mAppUrl = jsonObject.optString(KEY_AppUrl);
				mVerDescribre = jsonObject.optString(KEY_VerDescribe);
			}
			
			
			return true;

		}	
	}
	
	
	// 绑定TOKEN
	public final static int BIND_TOKEN_MSGID  = 0x0005;
	public static class BindToken extends AbstractBaseProtocol
	{
		
		public final static String KEY_TOKEN = "token";

		public String mToken = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_TOKEN, mToken);		
			return mapValue;
		}
	}
	
	// 点击广告
	public final static int AD_CLICK_MSGID  = 0x0007;
	public static class AdClick extends AbstractBaseProtocol
	{

		public final static String KEY_ADTYPE = "adType";	

		public String mAdType = "";

		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ADTYPE, mAdType);		
			return mapValue;
		}
	}
	
	// About页面
	public final static int ABOUT_MSGID  = 0x0009;
	public static class AboutPage extends AbstractBaseProtocol
	{
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			return mapValue;
		}
	}
	
	// 获取资讯类型列表
	public final static int GET_TYPELIST_MSGID  = 0x0011;
	public static class GetTypeList extends AbstractBaseProtocol
	{
		public final static String KEY_ARCTYPEID = "arcTypeID";	

		public String mArcTypeID = "";
		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);		
			return mapValue;
		}
	}
	
	// 获取资讯
	public final static int GET_INFO_MSGID  = 0x0013;
	public static class GetInfo extends AbstractBaseProtocol
	{
		public final static String KEY_ARCTYPEID = "arcTypeID";	
		public final static String KEY_PAGE = "page";	

		
		public String mArcTypeID = "";
		public String mPage = "";

		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);	
			mapValue.put(KEY_PAGE, mPage);	
			return mapValue;
		}
	}
	
	// 获取资讯回应
	public static class GetInfoResult  implements IParseJson{

		public final static String KEY_DATALIST = "DataList";
		
		public List<BaseType.InfoItem> mDataList = new ArrayList<BaseType.InfoItem>();

		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
			
			JSONArray jsonArray = jsonObject.optJSONArray(KEY_DATALIST);
			if (jsonArray == null){
				return true;
			}
			int size = jsonArray.length();
			for(int i = 0; i < size; i++){
				JSONObject tmp = jsonArray.getJSONObject(i);
				BaseType.InfoItem item = new BaseType.InfoItem();
				try {
					item.parseJson(tmp);
					mDataList.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}							
			}
			return true;
		}
	
	}
	
	// 删除资讯
	public final static int DELETE_INFO_MSGID  = 0x0015;
	public static class DeleteInfo extends AbstractBaseProtocol
	{
		public final static String KEY_ARCTYPEID = "arcTypeID";	
		public final static String KEY_TOPICID = "topic_id";		
		
		public String mArcTypeID = "";
		public String mTopicID = "";
	
		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_ARCTYPEID, mArcTypeID);	
			mapValue.put(KEY_TOPICID, mTopicID);		
			return mapValue;
		}
	}
	
	// 检测升级
	public final static int CHECK_UPDATE_MSGID  = 0x0017;
	public static class CheckUpdate extends AbstractBaseProtocol
	{
		public final static String KEY_VERCODE = "versioncode";	
		public final static String KEY_OSNAME = "osname";		
		
		public String mVercode = "";
		public String mOSName = "";
	
		
		@Override
		public Map<String, String> toStringMap() {
			super.toStringMap();
			mapValue.put(KEY_VERCODE, mVercode);	
			mapValue.put(KEY_OSNAME, mOSName);
			return mapValue;
		}
	}

	public static class CheckUpdateResult implements IParseJson
	{
		public final static String KEY_HavNewVer = "haveNewVer";	
		public final static String KEY_VerCode = "verCode";		
		public final static String KEY_VerName = "verName";		
		public final static String KEY_AppUrl = "appUrl";		
		public final static String KEY_VerDescribe = "verDescribe";		
		
		public int mHaveNewVer = 0;
		public int mVerCode = 0;
		public String mVerName = "";
		public String mAppUrl = "";
		
		public List<String> mContentList = new ArrayList<String>();
		
		@Override
		public boolean parseJson(JSONObject jsonObject) throws JSONException {
			

			mHaveNewVer = jsonObject.getInt(KEY_HavNewVer);
			if (mHaveNewVer != 0){
				mVerCode = jsonObject.getInt(KEY_VerCode);
				mVerName = jsonObject.getString(KEY_VerName);
				mAppUrl = jsonObject.getString(KEY_AppUrl);
				JSONArray jsonArray = jsonObject.getJSONArray(KEY_VerDescribe);
				
				int size = jsonArray.length();
				for(int i = 0; i < size; i++){
					String value = jsonArray.getString(i);
					mContentList.add(value);
				}
			}
			
			return true;
		}
	}
}
