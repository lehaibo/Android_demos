package com.lehaibo.myapp2;

import java.util.HashMap;

public interface ItatisticsEvent {

	public void onEvent(String eventID);

	public void onEvent(String eventID, HashMap<String, String> map);
}
