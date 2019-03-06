package w.cong.mypluginlibrary;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import w.cong.mypluginlibrary.RefInvoke;

public class BaseDexClassLoaderHoolHelper {

    public static void pathClassLoader(ClassLoader cl, File apkFile, File optDexFile)throws IllegalAccessException,
            NoSuchMethodException, IOException, InvocationTargetException,InstantiationException,NoSuchFieldException {
        // 获取 BaseDexClassLoader : pathList
        Object pathListObj = RefInvoke.getFieldObject(DexClassLoader.class.getSuperclass(), cl, "pathList");

        // 获取 PathList: Element[] dexElements
        Object[] dexElements = (Object[]) RefInvoke.getFieldObject(pathListObj, "dexElements");
        Log.d("cong,","dexElements:"+ Arrays.toString(dexElements));

        // Element 类型
        Class<?> elementClass = dexElements.getClass().getComponentType();

        // 创建一个数组, 用来替换原始的数组
        Object[] newElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);

        // 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile) 这个构造函数
        Class[] p1 = {DexFile.class, File.class};
        Object[] v1 = {DexFile.loadDex(apkFile.getCanonicalPath(), optDexFile.getAbsolutePath(), 0),apkFile};
        Object o = RefInvoke.createObject(elementClass, p1, v1);

        Object[] toAddElementArray = new Object[] { o };

        Log.d("cong,","toAddElementArray:"+ Arrays.toString(toAddElementArray));
        // 把原始的elements复制进去
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        // 插件的那个element复制进去
        System.arraycopy(toAddElementArray, 0, newElements, dexElements.length, toAddElementArray.length);

        Log.d("cong,","newElements:"+ Arrays.toString(newElements));

        // 替换
        RefInvoke.setFieldObject(pathListObj, "dexElements", newElements);
    }
}
