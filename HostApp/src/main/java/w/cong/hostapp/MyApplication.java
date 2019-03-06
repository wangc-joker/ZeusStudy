package w.cong.hostapp;

import android.app.Application;
import android.content.Context;

import w.cong.mypluginlibrary.PluginManager;

public class MyApplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        PluginManager.init(this);
    }
}
