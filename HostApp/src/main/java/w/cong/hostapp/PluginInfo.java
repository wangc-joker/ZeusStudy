package w.cong.hostapp;

import dalvik.system.DexClassLoader;

public class PluginInfo {

    private String dexPath;
    private DexClassLoader dexClassLoader;

    public PluginInfo(String dexPath,DexClassLoader classLoader) {
        this.dexPath = dexPath;
        this.dexClassLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return this.dexClassLoader;
    }

    public String getDexPath() {
        return this.dexPath;
    }
}
