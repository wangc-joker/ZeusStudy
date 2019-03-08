package w.cong.mypluginlibrary.ams_hook;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Proxy;

import w.cong.mypluginlibrary.RefInvoke;

public class HookCallback implements Handler.Callback {

    private Handler mBase;

    public HookCallback(Handler handler) {
        this.mBase = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            // ActivityThread 里面 "LAUNCH_ACTIVITY"这个字段的值是100
            //本来使用反射的方式获取是最好的，这里是为了简便直接的使用硬编码
            case 100:
                handleLauncherActivity(msg);
                break;
            case 114:
                handleCreateService(msg);
                break;

        }
        mBase.handleMessage(msg);
        return true;
    }

    private void handleCreateService(Message msg) {
        // 这里简单起见,直接取出插件Service
        Log.d("cong", "handleCreateService s");
        Object obj = msg.obj;
        ServiceInfo serviceInfo = (ServiceInfo) RefInvoke.getFieldObject(obj, "info");

        serviceInfo.name = "w.cong.plugin1.TestService1";
    }

    private void handleLauncherActivity(Message msg) {
//        //这里简单起见，直接取出TargetActivity
//        Object object = msg.obj;
//
//        //把替身恢复成真身
//        Intent intent = (Intent) RefInvoke.getFieldObject(object,"intent");
////        HookHelper.hookPackageManager();
//        Intent targetIntent = intent.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
//        intent.setComponent(targetIntent.getComponent());
////        TextLogUtil.textLog("把替身恢复成真身:"+targetIntent);
//        //修改packageName，这样缓存才能命中
//        ActivityInfo activityInfo = (ActivityInfo) RefInvoke.getFieldObject(object,"activityInfo");
//        activityInfo.applicationInfo.packageName = targetIntent.getPackage() == null ?
//                targetIntent.getComponent().getPackageName():targetIntent.getPackage();
//
////        try {
////            hookPackageManager();
////        } catch (Exception e) {
////            Log.d("cong","ex "+e.getMessage());
////        }
        Object obj = msg.obj;

        // 把替身恢复成真身
        Intent raw = (Intent) RefInvoke.getFieldObject(obj, "intent");

        Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
        if(target != null) {
            Log.d("cong","target.getComponent():"+target.getComponent().toString());
            raw.setComponent(target.getComponent());
        }

    }

    private static void hookPackageManager() throws Exception {
        Log.d("cong","hookPackageManager begin");
        // 这一步是因为 initializeJavaContextClassLoader 这个方法内部无意中检查了这个包是否在系统安装
        // 如果没有安装, 直接抛出异常, 这里需要临时Hook掉 PMS, 绕过这个检查.
        Object currentActivityThread = RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");

        // 获取ActivityThread里面原始的 sPackageManager
        Object sPackageManager = RefInvoke.getFieldObject(currentActivityThread, "sPackageManager");

        // 准备好代理对象, 用来替换原始的对象
        Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
        Object proxy = Proxy.newProxyInstance(
                iPackageManagerInterface.getClassLoader(),
                new Class[]{iPackageManagerInterface},
                new HookPMProxy(sPackageManager)
        );
        Log.d("cong","hookPackageManager end");
        RefInvoke.setFieldObject(currentActivityThread,"sPackageManager",proxy);
    }
}
