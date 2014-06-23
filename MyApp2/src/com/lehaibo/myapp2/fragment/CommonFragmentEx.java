package com.lehaibo.myapp2.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.lehaibo.myapp2.MyApplication;
import com.lehaibo.myapp2.R;
import com.lehaibo.myapp2.adapter.InfoContentAdapter;
import com.lehaibo.myapp2.content.ContentCache;
import com.lehaibo.myapp2.content.WebViewActivity;
import com.lehaibo.myapp2.model.BaseType;
import com.lehaibo.myapp2.model.BaseType.InfoItem;
import com.lehaibo.myapp2.network.InfoRequestProxy;
import com.lehaibo.myapp2.util.CommonLog;
import com.lehaibo.myapp2.util.CommonUtil;
import com.lehaibo.myapp2.util.LogFactory;
import com.lehaibo.myapp2.widget.RefreshListView;

@SuppressLint("ValidFragment")
public  class CommonFragmentEx extends CommonFragment implements InfoRequestProxy.IRequestResult,
                                                        RefreshListView.IOnRefreshListener, RefreshListView.IOnLoadMoreListener,
                                                        OnItemClickListener{

    private static final CommonLog log = LogFactory.createLog();
    
	@InjectView (R.id.invalid_view) View mInvalidView;
	@InjectView (R.id.load_view) View mLoadView;
	@InjectView (R.id.content_view) View mContentView;  
	@InjectView (R.id.listview) RefreshListView mListView;  
   
    private BaseType.ListItem mTypeData;
    private InfoContentAdapter mAdapter;        
    
    private Context mContext;
    private List<BaseType.InfoItem> mContentData = new ArrayList<BaseType.InfoItem>();
    private InfoRequestProxy mInfoRequestProxy;
    
    private Handler mHandler;
    private boolean loginStatus = false;
    public CommonFragmentEx(BaseType.ListItem data){
            mTypeData = data;        
    }
    
    public CommonFragmentEx(){
    
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
            log.e("CommonFragmentEx onCreate");
            super.onCreate(savedInstanceState);
        loginStatus = MyApplication.getInstance().getLoginStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                    Bundle savedInstanceState) {
            log.e("CommonFragmentEx onCreateView");
            View view = inflater.inflate(R.layout.common_layout, null);
  
            
            return view;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            log.e("CommonFragmentEx onActivityCreated");
            
            if (loginStatus){
                    setupViews();                        
                    initData();                
                    mInfoRequestProxy.requestRefreshInfo();
            }

    
    }
    
    private void setupViews(){
            switchToLoadView();
    
    }
    
    private void initData(){

            mContext = getActivity();
    
            mAdapter = new InfoContentAdapter(mContext, mContentData);
            mListView.setAdapter(mAdapter);
            mListView.setOnRefreshListener(this);
            mListView.setOnLoadMoreListener(this);
            mListView.setOnItemClickListener(this);           
            mInfoRequestProxy = new InfoRequestProxy(mContext, mTypeData, this);          
            mHandler = new Handler(){
                  
            };
    }

    @Override
    public void onDestroy() {
            log.e("CommonFragmentEx onDestroy");
            if (loginStatus){
                    mInfoRequestProxy.cancelRequest();
            }
            
            super.onDestroy();
    }
    
    
    public BaseType.ListItem getData(){
            return mTypeData;
    }
    
    private void switchToFailView(){
            mInvalidView.setVisibility(View.VISIBLE);
            mLoadView.setVisibility(View.GONE);
            mContentView.setVisibility(View.GONE);
    }
    
    private void switchToLoadView(){
            mInvalidView.setVisibility(View.GONE);
            mLoadView.setVisibility(View.VISIBLE);
            mContentView.setVisibility(View.GONE);
    }
    
    private void switchToContentView(){
            mInvalidView.setVisibility(View.GONE);
            mLoadView.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);
    }


    @Override
    public void OnLoadMore() {
            mInfoRequestProxy.requestMoreInfo();
    }


    @Override
    public void OnRefresh() {
            mInfoRequestProxy.requestRefreshInfo();
    }

    private boolean isFirst = true;
    @Override
    public void onSuccess(boolean isLoadMore) {

            switchToContentView();
            mContentData = mInfoRequestProxy.getData();
            mAdapter.refreshData(mContentData);
            
            log.e("onSuccess isLoadMore = " + isLoadMore + ", isfrist = "  + isFirst);
            if (isLoadMore){
                    mListView.onLoadMoreComplete(false);
            }else{
                    mListView.onRefreshComplete();       
            }
    }


    @Override
    public void onRequestFailure(boolean isLoadMore) {
            CommonUtil.showToast(R.string.toast_getdata_fail, mContext);
            
            if (mContentData.size() == 0){
                    switchToFailView();
                    return ;
            }
            
            switchToContentView();
            if (isLoadMore){
                    mListView.onLoadMoreComplete(false);
            }else{
                    mListView.onRefreshComplete();
            }
    }


    @Override
    public void onAnylizeFailure(boolean isLoadMore) {
            CommonUtil.showToast(R.string.toast_anylizedata_fail, mContext);
            
            if (mContentData.size() == 0){
                    switchToFailView();
                    return ;
            }
            
            switchToContentView();
            if (isLoadMore){
                    mListView.onLoadMoreComplete(false);
            }else{
                    mListView.onRefreshComplete();
            }
    }


    @Override
    public void onItemClick(AdapterView<?> adapter, View arg1, int pos, long arg3) {
            
            
            BaseType.InfoItem item = (InfoItem) adapter.getItemAtPosition(pos);                
            BaseType.InfoItemEx itemEx = new BaseType.InfoItemEx(item, mTypeData);
            ContentCache.getInstance().setTypeItem(mTypeData);
            ContentCache.getInstance().setInfoItem(itemEx);
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(BaseType.ListItem.KEY_TYPEID, mTypeData.mTypeID);
            map.put(BaseType.ListItem.KEY_TITLE, itemEx.mTitle);
            MyApplication.getInstance().onEvent("UMID0002", map);
            
            
            //goContentActivity();
            goWebviewActivity(item);
    }
    
	private void goWebviewActivity(BaseType.InfoItem item){
		log.e("goWebviewActivity ");
 
		Intent intent = new Intent();
		intent.setClass(mContext, WebViewActivity.class);
		intent.putExtra(WebViewActivity.INTENT_EXTRA_URL, item.mSourceUrl);
		startActivity(intent);
	}

    private void goContentActivity(){
//            Intent intent = new Intent();
//            intent.setClass(mContext, ContentActivity.class);
//            startActivity(intent);
    }
        
        
}