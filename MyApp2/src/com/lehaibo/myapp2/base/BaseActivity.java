package com.lehaibo.myapp2.base;

import roboguice.activity.RoboActivity;
import android.os.Bundle;

import com.lehaibo.myapp2.MyApplication;

public class BaseActivity extends RoboActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MyApplication.onCatchError(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

}
