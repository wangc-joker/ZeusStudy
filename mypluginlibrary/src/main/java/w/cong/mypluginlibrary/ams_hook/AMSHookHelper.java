package w.cong.mypluginlibrary.ams_hook;

import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Proxy;

import w.cong.mypluginlibrary.RefInvoke;

public class AMSHookHelper {

    public static final String EXTRA_TARGET_INTENT = "extra_target_intent";

    /**
     * Hook AMS
     * 主要完成的操作是 "把真正要启动的Activity临时替换为在AndroidManifest.xml中声明的替身Activity",进而骗过AMS
     */
    public static void hookAMN() throws Exception {
        Object gDefault = RefInvoke.getStaticFieldObject("android.app.ActivityManager","IActivityManagerSingleton");

        // gDefault是一个 android.util.Singleton<T>对象; 我们取出这个单例里面的mInstance字段
        Object mInstance = RefInvoke.getFieldObject("android.util.Singleton", gDefault, "mInstance");

        // 创建一个这个对象的代理对象MockClass1, 然后替换这个字段, 让我们的代理对象帮忙干活
        Class<?> classB2Interface = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { classB2Interface },
                new HookAMProxy(mInstance));

        //把gDefault的mInstance字段，修改为proxy
        RefInvoke.setFieldObject("android.util.Singleton", gDefault, "mInstance", proxy);
    }

    /**
     * 由于之前我们用替身欺骗了AMS; 现在我们要换回我们真正需要启动的Activity
     * 不然就真的启动替身了, 狸猫换太子...
     * 到最终要启动Activity的时候,会交给ActivityThread 的一个内部类叫做 H 来完成
     * H 会完成这个消息转发; 最终调用它的callback
     */
    public static void hookActivityThreadH() {
        Log.d("cong","hookActivityThreadH start");
        Object currentThreadActivity = RefInvoke.getStaticFieldObject("android.app.ActivityThread"
                ,"sCurrentActivityThread");

        Handler mH = (Handler) RefInvoke.getFieldObject("android.app.ActivityThread"
                ,currentThreadActivity, "mH");
        RefInvoke.setFieldObject(Handler.class, mH, "mCallback",new HookCallback(mH));
        Log.d("cong","hookActivityThreadH success");
    }
}
