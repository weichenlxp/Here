package edu.hebut.here.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("BootReceiver", "onReceive");
        Intent service = new Intent(context, LongRunningService.class);
        context.startService(service);
    }
}