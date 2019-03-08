package w.cong.hostapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import w.cong.mypluginlibrary.BaseDexClassLoaderHoolHelper;
import w.cong.mypluginlibrary.PluginManager;
import w.cong.mypluginlibrary.Utils;
import w.cong.mypluginlibrary.ams_hook.AMSHookHelper;


public class MyApplication extends Application {

    public static HashMap<String, String> pluginServices;
    private static final String apkName = "plugin1.apk";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        pluginServices = new HashMap<String, String>();

//        try {
//            init(base);
//        } catch (Exception e) {
//            Log.d("cong", e.getMessage());
//        }

        PluginManager.init(this);
//
        try {
            AMSHookHelper.hookAMN();
            AMSHookHelper.hookActivityThreadH();
        } catch (Exception e) {
            Log.d("cong",e.toString());
        }
    }

    private void init(Context context)  throws Exception{

        Utils.extractAssets(context, apkName);

        File dexFile = getFileStreamPath(apkName);
        File optDexFile = getFileStreamPath("plugin1.dex");
        BaseDexClassLoaderHoolHelper.pathClassLoader(getClassLoader(), dexFile, optDexFile);

        AMSHookHelper.hookAMN();
        AMSHookHelper.hookActivityThreadH();

        String strJSON = Utils.readZipFileString(dexFile.getAbsolutePath(), "assets/plugin_config.json");
        if(strJSON != null && !TextUtils.isEmpty(strJSON)) {
            JSONObject jObject = new JSONObject(strJSON.replaceAll("\r|\n", ""));
            JSONArray jsonArray = jObject.getJSONArray("plugins");
            for(int i = 0; i< jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                pluginServices.put(
                        jsonObject.optString("PluginService"),
                        jsonObject.optString("StubService"));
            }
        }
    }
}
