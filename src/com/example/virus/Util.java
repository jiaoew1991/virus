package com.example.virus;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Util {

	public enum CommandType {
		REBOOT,
		SHUTDOWN,
		ALARM_REBOOT,
		LAUNCH_CAMERA,
		OPEN_HEADPHONE;
	}
	public static String int2ip(long ipInt) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(ipInt & 0xFF).append(".");
	    sb.append((ipInt >> 8) & 0xFF).append(".");
	    sb.append((ipInt >> 16) & 0xFF).append(".");
	    sb.append((ipInt >> 24) & 0xFF);
	    return sb.toString();
	}
    public static String getHostIp(Context context) {         
    	String mIpAddress = null;
    	WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
    	if (null != info) {
    		mIpAddress = Util.int2ip(info.getIpAddress());
    	}
    	return mIpAddress;
	}
}
