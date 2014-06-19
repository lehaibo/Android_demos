package com.lehaibo.myapp2.network;

import com.lehaibo.myapp2.base.IToStringMap;

import android.content.Context;

public class BaseRequestPacket {
	public Context context;
	public int action;
	public IToStringMap object;
	public Object extra;

	public BaseRequestPacket() {

	}
}
