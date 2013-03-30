package com.example.virus;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Util {

	public enum CommandType {
		REBOOT(1),
		SHUTDOWN(2),
		ALARM_REBOOT(3),
		LAUNCH_CAMERA(4),
		CONTROL_RECORD(5);
		private int c;
		private CommandType(int c) {
			this.c = c;
		}
		public int getNumber() {
			return c;
		}
		public static CommandType getCommandType(int t) {
			for (CommandType type : CommandType.values()) {
				if (type.getNumber() == t) {
					return type;
				}
			}
			return null;
		}
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
