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
        LoadingScreen.dbExists = getFileStreamPath(Database.FILE_NAME).exists();
        Database.init(getFilesDir().getAbsolutePath() + "/" + Database.FILE_NAME);
    }
}
