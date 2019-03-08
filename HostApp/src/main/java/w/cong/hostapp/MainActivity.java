package w.cong.hostapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

import w.cong.mypluginlibrary.DLUtils;
import w.cong.mypluginlibrary.PluginManager;
import w.cong.mypluginlibrary.Utils;
import w.cong.mypluginlibrary.ams_hook.AMSHookHelper;
import w.cong.mypluginlibrary.classLoader_hook.LoadedApkClassLoaderHookHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Utils.context = getApplicationContext();
//        Utils.extractAssets(newBase, "plugin1.apk");

//        try {
//            LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(getFileStreamPath("plugin1.apk"));
//
//            AMSHookHelper.hookAMN();
//            AMSHookHelper.hookActivityThreadH();
//        } catch (Exception e) {
//            Log.d("cong",e.toString());
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emain);

    }

    public void startActivityInPlugin(View view) {
//        Intent intent = new Intent();
//        String activityName = PluginManager.plugins.get(0).packageInfo.packageName + ".TestActivity1";
//        intent.setComponent(new ComponentName(PluginManager.plugins.get(0).packageInfo.packageName, activityName));
//
//        startActivity(intent);

//        Intent intent = new Intent();
//        intent.setComponent(
//                new ComponentName("w.cong.plugin1",
//                        "w.cong.plugin1.TestService1"));
//        startService(intent);
        bindServ();
    }

    private void bindServ(){
        Intent intent = new Intent();
        intent.setComponent(
                new ComponentName("w.cong.plugin1",
                        "w.cong.plugin1.TestService1"));

        bindService(intent, connection, Service.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

//    public void startServiceInPlugin(View view) {
//        try {
//            Intent intent = new Intent();
//            String serviceName = PluginManager.plugins.get(0).packageInfo.packageName+".TestService1";
//            intent.setClass(this,Class.forName(serviceName));
//            startService(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void (View view) {
//        try {
//            Intent intent = new Intent();
//            String serviceName = PluginManager.plugins.get(0).packageInfo.packageName+".TestActivity1";
//            intent.setClass(this,Class.forName(serviceName));
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
