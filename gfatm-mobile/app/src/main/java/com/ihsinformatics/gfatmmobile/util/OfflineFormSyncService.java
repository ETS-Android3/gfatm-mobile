package com.ihsinformatics.gfatmmobile.util;

/**
 * Created by Rabbia on 3/28/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ihsinformatics.gfatmmobile.App;

public class OfflineFormSyncService extends Service {

    private static final String TAG = "HelloService";
    private static OfflineFormSyncService instance = null;

    static boolean isRunning  = false;

    static public Boolean isRunning(){
        return isRunning;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");

        isRunning = true;
    }

    public static boolean isInstanceCreated() {
        return instance != null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        instance = this;

        //Creating new thread for my service
        new Thread(new Runnable() {
            @Override
            public void run() {

                isRunning = true;
                ServerService serverService = new ServerService(getApplicationContext());
                final Object[][] forms = serverService.getOfflineSavedForms(App.getUsername());

                Boolean flag = true;
                for (int i = 0; i < forms.length; i++) {

                    int tries = Integer.parseInt(String.valueOf(forms[i][10]));

                    if(tries < 3) {

                        String returnString = serverService.submitForm(String.valueOf(forms[i][0]), false);
                        if (!returnString.equals("SUCCESS")) {
                            flag = false;

                            tries = tries + 1;
                            serverService.syncTriesIncrementForm(String.valueOf(forms[i][0]), tries);

                        }

                    }else flag = false;
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
                isRunning = false;
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

        instance = null;
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }

}