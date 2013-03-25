package com.example.virus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        Intent service = new Intent(context,BackgroudService.class);  
        context.startService(service);  
        Log.v("TAG", "开机自动服务自动启动.....");  
  
    }  
  
}  