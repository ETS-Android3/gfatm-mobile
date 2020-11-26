package com.ihsinformatics.gfatmmobile.util;

import android.app.Application;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DaoMaster;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DaoSession;

import org.greenrobot.greendao.database.Database;

public class App extends Application {

    public static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "commonlab-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }


}
