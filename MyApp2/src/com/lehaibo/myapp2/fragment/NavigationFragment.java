package com.lehaibo.myapp2.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lehaibo.myapp2.R;
import com.lehaibo.myapp2.MyApplication;
import com.lehaibo.myapp2.adapter.NavChannelAdapter;
import com.lehaibo.myapp2.model.BaseType;
import com.lehaibo.myapp2.model.BaseType.ListItem;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.LogFactory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NavigationFragment extends Fragment implements OnItemClickListener{

private static final CommonLog log = LogFactory.createLog();
	
    private Context mContext;
	private View mView;
	private ListView mListView;
	
	private List<BaseType.ListItem> mDataList = new ArrayList<BaseType.ListItem>();
	private NavChannelAdapter mAdapter;
	
	private FragmentControlCenter mControlCenter;
	private boolean loginStatus = false;
	
	public NavigationFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		log.e("NavigationFragment onCreate");
		mContext = MyApplication.getInstance();
		mControlCenter = FragmentControlCenter.getInstance(getActivity());
	    loginStatus = MyApplication.getInstance().getLoginStatus();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		
		log.e("NavigationFragment onDestroy");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		log.e("NavigationFragment onCreateView");
		
		mView = inflater.inflate(R.layout.listview_layout, null);
		return mView;	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("NavigationFragment onActivityCreated");
		
		if (loginStatus){
			setupViews();
			initData();
		}
	
	}
	
	
	private void setupViews(){
		mListView = (ListView) mView.findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
	}
	
	
	private void initData(){
		mDataList = MyApplication.getInstance().getUserLoginResult().mDataList;
		
		mAdapter = new NavChannelAdapter(mContext, mDataList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3) {
			
		
		BaseType.ListItem item = (ListItem) adapter.getItemAtPosition(pos);
	//	log.e("pos = " + pos + ", item = " + "\n" + item.getShowString());
		
	
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseType.ListItem.KEY_TYPEID, item.mTypeID);
		map.put(BaseType.ListItem.KEY_TITLE, item.mTitle);
		MyApplication.getInstance().onEvent("UMID0020", map);
		
		CommonFragmentEx fragmentEx = mControlCenter.getCommonFragmentEx(item);
		if (getActivity() == null)
			return;

//		if (getActivity() instanceof MainLookAroundActivity) {
//			MainLookAroundActivity ra = (MainLookAroundActivity) getActivity();
//			ra.switchContent(fragmentEx);
//		}
	}



}
