package w.cong.mypluginlibrary;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dalvik.system.DexClassLoader;

public class PluginManager {
    public final static List<PluginItem> plugins = new ArrayList<PluginItem>();

    public static volatile Resources mNowResources;

    //原始的application中的BaseContext,不能是其他的，否则会内存泄露
    public static volatile Context mBaseContext;

    //ContextImpl中的LoadAPK对象的mPackageInfo
    private static Object mPackageInfo = null;

    public static void init(Application application){
        //初始化一些成员变量和加载已安装的插件
        mPackageInfo = RefInvoke.getFieldObject(application.getBaseContext(),"mPackageInfo");
        mBaseContext = application.getBaseContext();
        mNowResources = application.getResources();

        Utils.context = application;

        try {
            AssetManager assetManager = application.getAssets();
            String[] paths = assetManager.list("");
            Log.d("cong","path:"+ Arrays.toString(paths));

            ArrayList<String> pluginPaths = new ArrayList<String>();
            for (String path : paths) {
                if (path.endsWith("apk")){
                    String apkName = path;
                    String dexName = apkName.replace(".apk",".dex");

                    Utils.extractAssets(mBaseContext,apkName);
                    MergeDexs(apkName,dexName);

                    PluginItem item = generatePluginItem(apkName);
                    plugins.add(item);

                    pluginPaths.add(item.pluginPath);
                }
            }
            Log.d("cong,","pluginPaths:"+pluginPaths.toString());
            reloadInstalledPluginResurces(pluginPaths);
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    private static PluginItem generatePluginItem(String apkName) {
        File file = mBaseContext.getFileStreamPath(apkName);
        PluginItem item = new PluginItem();
        item.pluginPath = file.getAbsolutePath();
        item.packageInfo = DLUtils.getPackageInfo(mBaseContext,item.pluginPath);
        return item;
    }

    static void MergeDexs(String apkName, String dexName) {
        File dexFile = mBaseContext.getFileStreamPath(apkName);
        File optDexFile = mBaseContext.getFileStreamPath(dexName);

        try {
            Log.d("cong,","dexFile:"+dexFile+" optDexFile:"+optDexFile);
            BaseDexClassLoaderHoolHelper.pathClassLoader(mBaseContext.getClassLoader(),dexFile,optDexFile);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    private static void reloadInstalledPluginResurces(ArrayList<String> pluginPaths) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAsserPath = AssetManager.class.getMethod("addAssetPath", String.class);

            addAsserPath.invoke(assetManager,mBaseContext.getPackageResourcePath());

            for (String pluginPath : pluginPaths) {
                addAsserPath.invoke(assetManager,pluginPath);
            }

            Resources newResource = new Resources(assetManager,
                    mBaseContext.getResources().getDisplayMetrics(),
                    mBaseContext.getResources().getConfiguration());

            RefInvoke.setFieldObject(mBaseContext,"mResources",newResource);
            //这里最主要的需要替换的，如果不支持插件运行时更新,只留这一个就可以
            RefInvoke.setFieldObject(mPackageInfo,"mResources",newResource);

            mNowResources = newResource;

            //需要清理mTheme对象，否则通过inflate方式加载资源会报错
            //如果是activity动态加载插件，则需要把activity的mTheme对象也设置为null
            RefInvoke.setFieldObject(mBaseContext,"mTheme",null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static volatile ClassLoader mNowClassLoader = null;
    public static volatile ClassLoader mBaseClassLoader = null;

    public static void init2(Application application){
        mPackageInfo = RefInvoke.getFieldObject(application.getBaseContext(),"mPackageInfo");
        mBaseContext = application.getBaseContext();
        mNowResources = application.getResources();

        try {
            AssetManager assetManager = application.getAssets();
            String[] paths = assetManager.list("");

            ArrayList<String> pluginPaths = new ArrayList<String>();
            for(String path : paths) {
                if(path.endsWith(".apk")) {
                    String apkName = path;

                    Utils.extractAssets(mBaseContext, apkName);

                    PluginItem item = generatePluginItem(apkName);
                    plugins.add(item);

                    pluginPaths.add(item.pluginPath);
                }
            }

            reloadInstalledPluginResurces(pluginPaths);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mNowClassLoader = mBaseContext.getClassLoader();
        mBaseClassLoader = mBaseContext.getClassLoader();

        ZeusClassLoader classLoader = new ZeusClassLoader(mBaseContext.getPackageCodePath(),
                mBaseContext.getClassLoader());

        File dexOutputDir = mBaseContext.getDir("dex",Context.MODE_PRIVATE);

        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        for (PluginItem pluginItem:plugins) {
            DexClassLoader dexClassLoader = new DexClassLoader(pluginItem.pluginPath,
                    dexOutputPath,null,
                    mBaseClassLoader);
            classLoader.addPluginClassLoader(dexClassLoader);
        }

        RefInvoke.setFieldObject(mPackageInfo, "mClassLoader", classLoader);
        Thread.currentThread().setContextClassLoader(classLoader);
        mNowClassLoader = classLoader;
    }

}
