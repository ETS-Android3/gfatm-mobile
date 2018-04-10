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

public class OnlineFormSyncService extends Service {

    private static final String TAG = "HelloService";

    static boolean isRunning  = false;

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
    }

    static public Boolean isRunning(){
        return isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Creating new thread for my service
        new Thread(new Runnable() {
            @Override
            public void run() {

                isRunning = true;
                ServerService serverService = new ServerService(getApplicationContext());

                Boolean flag = true;
                while(serverService.getPendingOnlineSavedFormsCount(App.getUsername()) != 0) {
                    Object[][] forms = serverService.getOnlineSavedForms(App.getUsername());

                    for (int i = 0; i < forms.length; i++) {

                        int tries = Integer.parseInt(String.valueOf(forms[i][10]));

                        if(tries < 3) {

                            String returnString = serverService.submitForm(String.valueOf(forms[i][0]), false);
                            if (!returnString.equals("SUCCESS")) {
                                flag = false;

                                tries = tries + 1;
                                serverService.syncTriesIncrementForm(String.valueOf(forms[i][0]), tries);

                            }

                        } else flag = false;
                    }
                }

                if(!flag){
                    Intent intent = new Intent("background-offline-sync");
                    intent.putExtra("message", "completed_with_error_online");
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