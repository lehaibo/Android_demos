package com.lehaibo.myapp2.activity;

import java.util.List;

import org.json.JSONException;

import roboguice.inject.InjectView;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lehaibo.myapp2.MyApplication;
import com.lehaibo.myapp2.R;
import com.lehaibo.myapp2.base.BaseActivity;
import com.lehaibo.myapp2.base.PublicTypeBuilder;
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
 

 

public class AboutActivity extends BaseActivity implements OnClickListener,
															IRequestDataPacketCallback,
															IDialogInterface {

	private static final CommonLog log = LogFactory.createLog();
	
	@InjectView (R.id.btn_back) Button mBtnBack;  
	@InjectView (R.id.ll_advise)  View mAdviseView;
	@InjectView (R.id.ll_attention) View mAttentionWeiboView; 
	@InjectView (R.id.ll_checkupdate) View mCheckUpdateView; 
	@InjectView (R.id.ll_support) View mSupportDevelopterView; 
	@InjectView (R.id.iv_updateicon) ImageView mIVUpageIcon; 
	@InjectView (R.id.tv_version) TextView mTVVersion; 	
		
	private ClientEngine mClientEngine;	
	private PublicType.CheckUpdateResult object = null; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abount_layout);
        
        setupViews();
        initData();
    }
    
    
    private void setupViews(){

    	mBtnBack.setOnClickListener(this);    		
    	mSupportDevelopterView.setOnClickListener(this);
    	mAttentionWeiboView.setOnClickListener(this);
    	mCheckUpdateView.setOnClickListener(this);
    	mAdviseView.setOnClickListener(this);

    }
    
    private void initData(){
    	mClientEngine=  ClientEngine.getInstance(this);	
    	
    	object = MyApplication.getInstance().getNewVersion();
    	if (object != null && object.mHaveNewVer != 0){
    		showUpdateIcon(true);
    	}else{
    		showUpdateIcon(false);
    		
    		PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(this);		
    		BaseRequestPacket packet = new BaseRequestPacket();
    		packet.action = PublicType.CHECK_UPDATE_MSGID;
    		packet.object = object;
    		packet.extra = new Object();
    		mClientEngine.httpGetRequestEx(packet, this);
    	}
    	
    	updateView();
    }
    
    private void showUpdateIcon(boolean flag){
    	if (flag){
    		mIVUpageIcon.setVisibility(View.VISIBLE);
    	}else{
    		mIVUpageIcon.setVisibility(View.GONE);
    	}
    }

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.btn_back:
				finish();
				break;
			case R.id.ll_advise:
				goAdviseActivity();
				break;
			case R.id.ll_attention:
				attention();
				break;
			case R.id.ll_checkupdate:
				checkUpdate();
				break;
			case R.id.ll_support:
				support();
				break;
		}
	}
	
	private void updateView(){
		
		String value = getResources().getString(R.string.tvt_ver_pre) + CommonUtil.getSoftVersion(this);
		mTVVersion.setText(value);
	}
	
	private void goAdviseActivity(){
//		Intent intent = new Intent();
//		intent.setClass(this, AdviseActivity.class);
//		startActivity(intent);
	}
	
	private void attention(){
//		Platform plat = ShareSDK.getPlatform(this, SinaWeibo.NAME);
//		plat.setPlatformActionListener(this);
//		plat.followFriend("2881812642");
	}
	

	private void checkUpdate(){
		
		if (object != null && object.mHaveNewVer != 0){
			if (updateDialog != null){
				updateDialog.dismiss();
			}
			
			updateDialog = getUpdateDialog(object.mContentList);
			updateDialog.show();
		}else{
			PublicType.CheckUpdate object = PublicTypeBuilder.buildCheckUpdate(this);
			
			
			BaseRequestPacket packet = new BaseRequestPacket();
			packet.action = PublicType.CHECK_UPDATE_MSGID;
			packet.object = object;
			
			mClientEngine.httpGetRequestEx(packet, this);
			CommonUtil.showToast(R.string.toast_checking_update, this);
		}


	}
	
	private void support(){
//		Intent intent = new Intent();
//		intent.setClass(this, SupportActivity.class);
//		startActivity(intent);
	}
	
	@Override
	public void onSuccess(int requestAction, ResponseDataPacket dataPacket,
			Object extra) {
		log.e("onSuccess! requestAction = " + requestAction + ", dataPacket ==> \n" + dataPacket.toString());
		
		switch(requestAction){
		case PublicType.CHECK_UPDATE_MSGID:
			onGetCheckUpdate(dataPacket, extra);
			break;
			
		}
	}


	@Override
	public void onRequestFailure(int requestAction, String content, Object extra) {
		log.e("onRequestFailure --> requestAction = " + requestAction);
		
		CommonUtil.showToast(R.string.toast_getdata_fail, this);
	}


	@Override
	public void onAnylizeFailure(int requestAction, String content, Object extra) {
		log.e("onAnylizeFailure! requestAction = " + requestAction + "\ncontent = " + content);
		
	}

//	public void onComplete(Platform platform, int action,
//			HashMap<String, Object> res) {
//		log.e("onComplete Platform = " + platform.getName() + ", action = " + action);
//
//		runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				CommonUtil.showToast(R.string.toast_attention_success, AboutActivity.this);
//				
//			}
//		});
//		
//		
//	}

//	public void onError(Platform platform, int action, Throwable t) {
//		t.printStackTrace();
//		log.e("onError Platform = " + platform.getName() + ", action = " + action);
//
//		runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				CommonUtil.showToast(R.string.toast_attention_fail, AboutActivity.this);
//				
//			}
//		});
//	
//		
//	}

//	public void onCancel(Platform platform, int action) {
//		log.e("onCancel Platform = " + platform.getName() + ", action = " + action);
//
//	}

	private Dialog updateDialog;
	private void onGetCheckUpdate( ResponseDataPacket dataPacket, Object extra){
		object = new PublicType.CheckUpdateResult();
		
		try {
			object.parseJson(dataPacket.data);
		//	log.e("mHaveNewVer = " + object.mHaveNewVer +  "\nmVerCode = " + object.mVerCode + 
		//			"\nmVerName = " + object.mVerName + "\nmAppUrl = " + object.mAppUrl + "\nmContent.size = " + object.mContentList.size());
		} catch (JSONException e) {
			e.printStackTrace();
			CommonUtil.showToast(R.string.toast_anylizedata_fail, this);
			object = null;
			return ;
		}
		
		if (object.mHaveNewVer != 0){	
			showUpdateIcon(true);
			if (extra == null){
				if (updateDialog != null){
					updateDialog.dismiss();
				}
				
				updateDialog = getUpdateDialog(object.mContentList);
				updateDialog.show();
			}
			MyApplication.getInstance().setNewVersionFlag(object);
		}else{
			if (extra == null){
				CommonUtil.showToast(R.string.toast_no_update, this);
			}
			
		}
		
		

	}
	
	private Dialog getUpdateDialog(List<String > list){
		int size = list.size();
		StringBuffer sBuffer = new StringBuffer();
		for(int i = 0; i < size; i++){
			String value = String.valueOf(i + 1) + "." + list.get(i);
			sBuffer.append(value);
			if (i != size - 1){
				sBuffer.append("\n");	
			}
		}
		log.e("msg = " + sBuffer.toString());
		Dialog dialog = DialogBuilder.buildNormalDialog(this, "版本更新" + object.mVerName, sBuffer.toString(), this);
		return dialog;
	}


	@Override
	public void onSure() {
		if (updateDialog != null){
			updateDialog.dismiss();
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
		if (updateDialog != null){
			updateDialog.dismiss();
		}
	}

}
