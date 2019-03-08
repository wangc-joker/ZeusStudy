package w.cong.mypluginlibrary;

import java.util.ArrayList;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class ZeusClassLoader extends PathClassLoader {

    private List<DexClassLoader> mClassLoaderList = null;

    public ZeusClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);

        mClassLoaderList = new ArrayList<DexClassLoader>();
    }

    protected void addPluginClassLoader(DexClassLoader dexClassLoader) {
        mClassLoaderList.add(dexClassLoader);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        return super.loadClass(name, resolve);

        Class<?> clazz = null;
        try {
            //先查找parent classLoader,这里实际就是系统帮我们创建的classLoader,目标对应宿主apk
            clazz = getParent().loadClass(name);
        } catch (ClassNotFoundException cnf) {

        }

        if (clazz != null) {
            return clazz;
        }

        //挨个到插件里进行查找
        if (mClassLoaderList !=null) {
            for (DexClassLoader dexClassLoader: mClassLoaderList) {
                if (dexClassLoader == null) {
                    continue;
                }
                try {
                    //这里只是查找插件自己的apk,不需要查parent,避免多次无用查询，提高性能
                    clazz = dexClassLoader.loadClass(name);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (Exception e) {

                }
            }
        }
        throw new ClassNotFoundException(name+" in loader " + this);

    }
}
