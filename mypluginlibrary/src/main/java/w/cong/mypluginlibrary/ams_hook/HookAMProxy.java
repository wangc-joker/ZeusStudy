package w.cong.mypluginlibrary.ams_hook;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class HookAMProxy implements InvocationHandler {

    private static final String TAG = "HookAMProxy";

    private Object mBase;

    public HookAMProxy(Object base){
        this.mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        if ("startActivity".equals(method.getName())) {

            Intent raw;
            int index = 0;

            for (int i=0; i<args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }

            raw = (Intent) args[index];

            Intent newIntent = new Intent();

            //替身Activity的包名，也就是我们自己的包名
            String stubPackage = raw.getComponent().getPackageName();

            //这里把启动的Activity临时替换为占位Activity
            ComponentName componentName = new ComponentName("w.cong.hostapp","w.cong.hostapp.StubActivity");
            newIntent.setComponent(componentName);

            //把我们原始要启动的HookActivity先存起来
            newIntent.putExtra(AMSHookHelper.EXTRA_TARGET_INTENT,raw);

            //替换掉Intent,达到欺骗AMS的目的
            args[index] = newIntent;
            Log.d("cong","raw:"+raw.getComponent().toString());
        } else if ("startService".equals(method.getName()) || "bindService".equals(method.getName())) {
            //只拦截这个方法
            //替换参数，任你所为；甚至替换原始StubService 启动别的Service偷梁换柱

            //扎到参数里面的第一个Intent对象
            int index = 0;
            for (int i = 0;i <args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }

            //替换 Plugin Service of StubService
            ComponentName componentName = new ComponentName("w.cong.hostapp","w.cong.hostapp.StubService");
            Intent newIntent = new Intent();
            newIntent.setComponent(componentName);

            args[index] = newIntent;
            Log.d("cong", method.getName() +" hook success");
            return method.invoke(mBase,args);
        } else if ("stopService".equals(method.getName())) {
            int index = 0;
            for (int i = 0;i <args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            ComponentName componentName = new ComponentName("w.cong.hostapp", "w.cong.hostapp.StubService");
            Intent newIntent = new Intent();
            newIntent.setComponent(componentName);

            // Replace Intent, cheat AMS
            args[index] = newIntent;

            Log.d("cong", "stopService hook success");
            return method.invoke(mBase, args);
        }


        return method.invoke(mBase,args);
    }
}
