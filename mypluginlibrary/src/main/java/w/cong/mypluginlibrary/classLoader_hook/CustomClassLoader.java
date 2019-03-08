package w.cong.mypluginlibrary.classLoader_hook;

import dalvik.system.DexClassLoader;

public class CustomClassLoader extends DexClassLoader {

    //自定义ClassLoader,用于加载插件资源和代码
    public CustomClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath,optimizedDirectory,libraryPath,parent);
    }
}
