package w.cong.hostapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

import w.cong.mypluginlibrary.DLUtils;
import w.cong.mypluginlibrary.PluginManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emain);

    }

    public void startServiceInPlugin(View view) {
        try {
            Intent intent = new Intent();
            String serviceName = PluginManager.plugins.get(0).packageInfo.packageName+".TestService1";
            intent.setClass(this,Class.forName(serviceName));
            startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startActivityInPlugin(View view) {
        try {
            Intent intent = new Intent();
            String serviceName = PluginManager.plugins.get(0).packageInfo.packageName+".TestActivity1";
            intent.setClass(this,Class.forName(serviceName));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
