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

        // Tapping the notification will open the specified Activity.
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // This always shows up in the notifications area when this Service is running.
        // TODO: String localization
        Notification not = new Notification.Builder(this).
                setContentTitle("NOTIFICATION").
                setContentInfo("Doing stuff in the background...").
                setContentIntent(pendingIntent).build();
        startForeground(1, not);

        //Creating new thread for my service
        new Thread(new Runnable() {
            @Override
            public void run() {

                ServerService serverService = new ServerService(getApplicationContext());
                final Object[][] forms = serverService.getSavedForms(App.getUsername(), App.getProgram());

                Boolean flag = true;
                for (int i = 0; i < forms.length; i++) {
                    String returnString = serverService.submitOfflineForm(String.valueOf(forms[i][0]));
                    if (!returnString.equals("SUCCESS"))
                        flag = false;
                }

                /*if(flag) {
                    Intent intent = new Intent("background-offline-sync");
                    intent.putExtra("message", "completed");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                else{
                    Intent intent = new Intent("background-offline-sync");
                    intent.putExtra("message", "completed_with_error");
                }*/
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