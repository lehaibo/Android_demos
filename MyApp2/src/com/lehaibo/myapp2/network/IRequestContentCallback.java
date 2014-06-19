package com.lehaibo.myapp2.network;

public interface IRequestContentCallback {

	public void  onResult(int requestAction, Boolean isSuccess, String content, Object extra);
}
