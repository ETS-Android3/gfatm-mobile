package com.example.backupservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Owais on 7/27/2017.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context pContext, Intent intent) {
        // Do your work related to alarm manager
        AppPreferences appPrefs = new AppPreferences(pContext);
        Params params = appPrefs.getParams();
        Backup backup = new Backup(pContext);
        backup.setupService(params);
    }
}
