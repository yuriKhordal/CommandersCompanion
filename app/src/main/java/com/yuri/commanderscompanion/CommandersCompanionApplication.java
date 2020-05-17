package com.yuri.commanderscompanion;

import android.app.Application;

import com.yuri.commanderscompanion.api.AppSettings;
import com.yuri.commanderscompanion.api.Database;

import dbAPI.AutoIncrementConstraint;

public class CommandersCompanionApplication extends Application {
    public CommandersCompanionApplication(){
        super();
        AutoIncrementConstraint.SqlString = "AUTOINCREMENT";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppSettings.init(this);
        if (BuildConfig.DEBUG){
            AppSettings.setDebugMode(true, this);
        }
        LoadingScreen.dbExists = getFileStreamPath(Database.FILE_NAME).exists();
    }
}