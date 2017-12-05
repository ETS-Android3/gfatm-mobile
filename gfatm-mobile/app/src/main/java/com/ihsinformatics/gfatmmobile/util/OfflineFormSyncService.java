package com.ihsinformatics.gfatmmobile.util;

/**
 * Created by Rabbia on 3/28/2017.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;

public class OfflineFormSyncService extends Service {

    private static final String TAG = "HelloService";

    private boolean isRunning  = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Creating new thread for my service
        new Thread(new Runnable() {
            @Override
            public void run() {

                ServerService serverService = new ServerService(getApplicationContext());
                final Object[][] forms = serverService.getSavedForms(App.getUsername());

                Boolean flag = true;
                for (int i = 0; i < forms.length; i++) {

                    int tries = Integer.parseInt(String.valueOf(forms[i][10]));

                    if(tries < 3) {

                        String returnString = serverService.submitOfflineForm(String.valueOf(forms[i][0]), false);
                        if (!returnString.equals("SUCCESS")) {
                            flag = false;

                            tries = tries + 1;
                            serverService.syncTriesIncrementOfflineform(String.valueOf(forms[i][0]), tries);

                        }

                    }
                }

                if(flag) {
                    Intent intent = new Intent("background-offline-sync");
                    intent.putExtra("message", "completed");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                else{
                    Intent intent = new Intent("background-offline-sync");
                    intent.putExtra("message", "completed_with_error");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }
                //Stop service once it finishes its task
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {

        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }

}