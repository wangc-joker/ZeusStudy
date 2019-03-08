package w.cong.mypluginlibrary.ams_hook;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HookPMProxy implements InvocationHandler {

    private Object mBase;

    public HookPMProxy(Object o) {
        this.mBase = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        if ("getActivityInfo".equals(method.getName())) {
//            Class[] clazz = new Class[]{
//                    ComponentName.class,
//                    int.class,
//                    int.class
//            };
//            ComponentName componentName = new ComponentName("com.example.hook","com.example.hook.TargetActivity");
//            Object[] values = new Object[]{
//                    componentName,
//                    args[1],
//                    args[2]
//            };
//            Log.d("cong","args"+ Arrays.toString(args));
//            return method.invoke(mBase,values);
//        }
        if ("getPackageInfo".equals(method.getName())) {
            Log.d("cong","getPackageInfo");
            return new PackageInfo();
        }
        return method.invoke(mBase,args);
    }
}
