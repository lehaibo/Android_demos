package com.lehaibo.myapp2.activity;

import java.util.List;

import org.json.JSONException;

import com.lehaibo.myapp2.R;
import com.lehaibo.myapp2.MyApplication;
import com.lehaibo.myapp2.base.BaseActivity;
import com.lehaibo.myapp2.base.PublicTypeBuilder;
import com.lehaibo.myapp2.datastore.LocalConfigSharePreference;
import com.lehaibo.myapp2.dialog.DialogBuilder;
import com.lehaibo.myapp2.dialog.IDialogInterface;
import com.lehaibo.myapp2.model.PublicType;
import com.lehaibo.myapp2.network.BaseRequestPacket;
import com.lehaibo.myapp2.network.ClientEngine;
import com.lehaibo.myapp2.network.IRequestDataPacketCallback;
import com.lehaibo.myapp2.network.ResponseDataPacket;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.CommonUtil;
import com.lehaibo.myapp2.util.LogFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class WelcomActivity extends BaseActivity implements IRequestDataPacketCallback, IDialogInterface{

	private static final CommonLog log = LogFactory.createLog("WelcomActivity");
	
	private static final int SEND_MSG_ID = 0x0001;
	
	private MyApplication mApplication;
	private ClientEngine mClientEngine;
	
	private Handler mHandler;
	PublicType.UserLoginResult object = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupViews();	
		initData();
	
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SEND_MSG_ID:
					boolean ret = requestRegister();
					if (!ret){
						finish();
					}
					break;
				default:
					break;
				}
			}
			
		};
		
		mHandler.sendEmptyMessageDelayed(SEND_MSG_ID, 1000);
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	 
	
	private void setupViews(){
		setContentView(R.layout.welcome_layout);
	}
	
	private void initData(){
		mApplication = MyApplication.getInstance();
		
		mClientEngine = ClientEngine.getInstance(getApplicationContext());
	}
	
	private boolean requestRegister(){
		if (!CommonUtil.isNetworkConnect(this)){
			CommonUtil.showToast(R.string.toast_connet_fail, this);		
			return false;
		}
		
		String keys = LocalConfigSharePreference.getKeys(this);
		if (keys.equals("")){
			PublicType.UserRegister object = PublicTypeBuilder.buildUserRegister(this);		
			
			
			BaseRequestPacket packet = new BaseRequestPacket();
			packet.context = this;
			packet.action = PublicType.USER_REGISTER_MASID;
			packet.object = object;
			
			mClientEngine.httpGetRequestEx(packet, this);
		}else{
			return requestLogin(keys);
		}
		
		return true;
	}
	
	private boolean requestLogin(String keys){
		if (!CommonUtil.isNetworkConnect(this)){
			CommonUtil.showToast(R.string.toast_connet_fail, this);		
			return false;
		}
		PublicType.UserLogin object = PublicTypeBuilder.buildUserLogin(this, keys);
		
		BaseRequestPacket packet = new BaseRequestPacket();
		packet.context = this;
		packet.action = PublicType.USER_LOGIN_MASID;
		packet.object = object;
		
		
		mClientEngine.httpGetRequestEx(packet, this);
		
		return true;
	}

	@Override
	public void onBackPressed() {
		mClientEngine.cancelTask(this);	
		exit();
	}
	
	
	private void exit(){
		finish();
	}

	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket, Object extra) {
		switch (requestAction) {
		case PublicType.USER_REGISTER_MASID:
			onTransdelRegister(dataPacket);
			break;
		case PublicType.USER_LOGIN_MASID:
			onTransdelLogin(dataPacket);
			break;
		default:
			break;
		}
	}

	@Override
	public void onRequestFailure(int requestAction, String content, Object extra) {
		log.e("WelcomActivity --> onRequestFailure \nrequestAction = " + requestAction + "\ncontent = " + content);
		
		switch (requestAction) {
		case PublicType.USER_REGISTER_MASID:
			CommonUtil.showToast(R.string.toast_register_fail, this);
			exit();
			break;
		case PublicType.USER_LOGIN_MASID:
			CommonUtil.showToast(R.string.toast_login_fail, this);
			exit();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onAnylizeFailure(int requestAction, String content, Object extra) {
		log.e("WelcomActivity --> onAnylizeFailure \nrequestAction = " + requestAction + "\ncontent = " + content);
		
		switch (requestAction) {
		case PublicType.USER_REGISTER_MASID:
			CommonUtil.showToast(R.string.toast_register_fail, this);
			exit();
			break;
		case PublicType.USER_LOGIN_MASID:
			CommonUtil.showToast(R.string.toast_login_fail, this);
			exit();
			break;
		default:
			break;
		}
		
	}

	
	
	
	private void onTransdelRegister(ResponseDataPacket dataPacket){
		log.e("Register success...");
		PublicType.UserRegisterResult object = new PublicType.UserRegisterResult();
		try {
			object.parseJson(dataPacket.data);
			log.e("register JSON = "+dataPacket.data.toString());
			LocalConfigSharePreference.commintKeys(this, object.mKeys);
			requestLogin(object.mKeys);
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_register_fail, this);
			exit();
		}
	}
	
	private void onTransdelLogin(ResponseDataPacket dataPacket){
		log.e("Login success...");
		object = new PublicType.UserLoginResult();
		try {
			object.parseJson(dataPacket.data);
			log.e("Login JSON = "+dataPacket.data.toString());
			
			
			log.e("mForceUpdate = " + object.mForceUpdate + "\n" + 
					"mHaveNewVer = " + object.mHaveNewVer + "\n" + 
					"mVerCode = " + object.mVerCode + "\n" + 
					"mVerName = " + object.mVerName + "\n" + 
					"mAppUrl = " + object.mAppUrl + "\n" + 
					"mVerDescribre = " + object.mVerDescribre);
			
			if (object.mForceUpdate != 0){
				if (forceUpdateDialog != null){
					forceUpdateDialog.dismiss();
				}
				
				forceUpdateDialog = getForceUpdateDialog(object);
				forceUpdateDialog.show();
			
				return ;
			}
			
			if (object.mHaveNewVer != 0){				
				CommonUtil.showToast(R.string.toast_update_warn, this);
			}
					
			
			
		
			
			mApplication.setUserLoginResult(object);
			mApplication.setLoginStatus(true);
			goMainActivity();
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_login_fail, this);
			exit();
		}
	}
	
	private void goMainActivity(){
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	
	private Dialog forceUpdateDialog;
	private Dialog getForceUpdateDialog(PublicType.UserLoginResult object){
		
		Dialog dialog = DialogBuilder.buildNormalDialog(this, 
				"版本更新" + object.mVerName, "您当前的版本过低，请升级至最新版本！", this);
		return dialog;
	}


	@Override
	public void onSure() {
		if (forceUpdateDialog != null){
			forceUpdateDialog.dismiss();
		}
		
		log.e("object:" + object);
		if (object != null){
			Intent intents = new Intent(Intent.ACTION_VIEW);
			intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intents.setData(Uri.parse(object.mAppUrl));
			startActivity(intents);
			log.e("jump to url:" + object.mAppUrl);
		}
		
	}

	
	@Override
	public void onCancel() {
		if (forceUpdateDialog != null){
			forceUpdateDialog.dismiss();
		}
		
		finish();
		
	}
}
