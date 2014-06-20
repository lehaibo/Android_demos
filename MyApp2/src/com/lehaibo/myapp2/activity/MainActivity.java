package com.lehaibo.myapp2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.RoboGuice;
import roboguice.util.RoboContext;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.google.inject.Key;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lehaibo.myapp2.MyApplication;
import com.lehaibo.myapp2.R;
import com.lehaibo.myapp2.adapter.NavChannelAdapter;
import com.lehaibo.myapp2.dialog.DialogBuilder;
import com.lehaibo.myapp2.dialog.IDialogInterface;
import com.lehaibo.myapp2.fragment.CommonFragmentEx;
import com.lehaibo.myapp2.fragment.FragmentControlCenter;
import com.lehaibo.myapp2.fragment.NavigationFragment;
import com.lehaibo.myapp2.model.BaseType;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.CommonUtil;
import com.lehaibo.myapp2.util.LogFactory;


public class MainActivity extends SlidingFragmentActivity implements 
															OnClickListener, IDialogInterface,
															RoboContext {

	private static final CommonLog log = LogFactory.createLog();
	
	private Button mLeftIcon;  
	private  Button mRightIcon;
	private TextView mTitleTextView; 
	
	private CommonFragmentEx mContentFragment;	
	private FragmentControlCenter mControlCenter;
	
	private List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
	private NavChannelAdapter mAdapter;
	
	protected HashMap<Key<?>,Object> scopedObjects = new HashMap<Key<?>, Object>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.e("MainLookAroundActivity  onCreate!!!");
	    RoboGuice.getInjector(this).injectMembersWithoutViews(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_slidemenu_layout);	
		boolean loginStatus = MyApplication.getInstance().getLoginStatus();
//		if (!loginStatus){
//			log.e("loginStatus is false ,jump to welcome view!!!");		
//			//MyApplication.getInstance().startToWelcomeActivity();
//			finish();
//			return ;
//		}
		
		setupViews();	
		initData();		
		MyApplication.onCatchError(this);		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		MyApplication.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		MyApplication.onResume(this);
	}
	
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        RoboGuice.getInjector(this).injectViewMembers(this);
    }
    
	@Override
	protected void onDestroy() {	
		RoboGuice.destroyInjector(this);	   
		super.onDestroy();
	}
	
	
	private void setupViews(){		
		initActionBar();	
		initSlideMenu();	
	}
	
	private void initSlideMenu(){
	//	log.e("MainLookAroundActivity initSlideMenu"); 
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);

		setBehindContentView(R.layout.left_menu_frame);
		sm.setSlidingEnabled(true);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.left_menu_frame, new NavigationFragment())
		.commit();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindScrollScale(0);
		sm.setFadeDegree(0.25f);	
	}
	
	private void initActionBar(){
	
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.actionbar_layout);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);	
	    mLeftIcon = (Button) findViewById(R.id.iv_left_icon);
        mRightIcon = (Button) findViewById(R.id.iv_right_icon);
		mLeftIcon.setOnClickListener(this);
		mRightIcon.setOnClickListener(this);
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
	}
	
	private void initData(){
		mDataList = MyApplication.getInstance().getUserLoginResult().mDataList;
		mControlCenter = FragmentControlCenter.getInstance(this);
		
		
		int size = mDataList.size();
		if (size > 0){
			mContentFragment = mControlCenter.getCommonFragmentEx(mDataList.get(0));
			switchContent(mContentFragment);
		}
	}
	
	
	public void switchContent(final CommonFragmentEx fragment) {
		mContentFragment = fragment;

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, mContentFragment)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
		
		mTitleTextView.setText(mContentFragment.getData().mTitle);
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.iv_left_icon:
			toggle();
			break;
		case R.id.iv_right_icon:
			goSettingActivity();
			break;
		}
	}	

	
	private void goSettingActivity(){
//		Intent intent = new Intent();
//		intent.setClass(this, SettingActivity.class);
//		startActivity(intent);
	}
	
	
	@Override
	public void onBackPressed() {
//		if (exitDialog != null){
//			exitDialog.dismiss();
//		}
//		
//		exitDialog = getExitDialog();
//		exitDialog.show();
		if(showExitToast()){
			finish();
		}else{
			CommonUtil.showToast(R.string.toast_exit_again, this);
		}
		
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_MENU) {
			boolean ret = getSlidingMenu().isMenuShowing();
			if (!ret){
				goSettingActivity();
			}
			return false;
		
		}
		return super.onKeyDown(keyCode, event);
    }
	
	private long curMillios = 0;
	private boolean showExitToast(){
		long time = System.currentTimeMillis();
		if (time - curMillios < 2000){
			return true;
		}
		
		curMillios = time;
		return false;
	}
	
	
	private Dialog exitDialog;
	private Dialog getExitDialog(){
		Dialog dialog = DialogBuilder.buildNormalDialog(this,
				getResources().getString(R.string.dia_msg_exit_title),
				getResources().getString(R.string.dia_msg_exit_msg),
				this);
		return dialog;
	}



	@Override
	public void onSure() {
		if (exitDialog != null){
			exitDialog.dismiss();
		}
		finish();
	}



	@Override
	public void onCancel() {
		if (exitDialog != null){
			exitDialog.dismiss();
		}
		
	}

	@Override
	public Map<Key<?>, Object> getScopedObjectMap() {
		// TODO Auto-generated method stub
		return scopedObjects;
	}

}

