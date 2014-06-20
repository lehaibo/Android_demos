package com.lehaibo.myapp2.fragment;

import java.util.HashMap;
import java.util.Map;

import com.lehaibo.myapp2.model.BaseType;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.LogFactory;

import android.content.Context;

 

public class FragmentControlCenter {

	private static final CommonLog log = LogFactory.createLog();
	
	private static  FragmentControlCenter instance;
	private Context mContext;
	
	private Map<String,CommonFragmentEx> mFragmentModelMaps= new HashMap<String, CommonFragmentEx>();
	

	private FragmentControlCenter(Context context) {
		mContext = context;
	}
	
	public static synchronized FragmentControlCenter getInstance(Context context) {
		if (instance == null){
			instance  = new FragmentControlCenter(context);
		}
		return instance;
	}

	public CommonFragmentEx getCommonFragmentEx(BaseType.ListItem object){
		CommonFragmentEx fragment = mFragmentModelMaps.get(object.mTypeID);
		if (fragment == null){
			fragment = new CommonFragmentEx(object);
			mFragmentModelMaps.put(object.mTypeID, fragment);
		}
		
		return fragment;
	}

	

}
