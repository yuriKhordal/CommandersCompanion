package com.yuri.commanderscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;

import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;

import androidx.appcompat.app.AppCompatActivity;
import dbAPI.ITable;
import dbAPI.SinglePrimaryKeyCacheTable;

/**The first activity on screen, for loading resources*/
public class LoadingScreen extends AppCompatActivity {
    /**The name of the class for logging*/
    public static final String TAG = "LoadingScreen";

    /**The progress bar that represents loading*/
    public ProgressBar loading_load_bar;

    /**Whether the database file {@value Database#FILE_NAME} exists in the 'files/' folder*/
    public static boolean dbExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar, should be before setContentView()
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading_screen);

        setViews();
        configureViews();

        if (!dbExists){
            for (ITable table : Database.TABLES){
                if (table == Database.COMMANDERS){
                    continue;
            }
                Database.HELPER.create(table);
                /*int diff = loading_load_bar.getMax() / Database.TABLES.length;
                loading_load_bar.incrementProgressBy(diff);
                loading_load_bar.invalidate();*/
            }
            GeneralHelper.testDataInsert();
            dbExists = true;
        } else {
            for (SinglePrimaryKeyCacheTable table : Database.TABLES){
                if (table == Database.COMMANDERS){
                    continue;
                }
                table.load();
                int diff = loading_load_bar.getMax() / Database.TABLES.length;
                loading_load_bar.incrementProgressBy(diff);
                loading_load_bar.invalidate();
            }
        }

        Intent intent = UnitsOverviewActivity.makeIntent(this);
        startActivity(intent);
    }

    /**Set the view objects*/
    public void setViews(){
        loading_load_bar = findViewById(R.id.loading_load_bar);
    }

    /**Configure the views after setting them*/
    public void configureViews(){

    }
}
