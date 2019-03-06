package w.cong.mypluginlibrary;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Utils {

    public static void extractAssets(Context context, String sourceName) {
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            is = assetManager.open(sourceName);
            File extractFile = context.getFileStreamPath(sourceName);
            fos = new FileOutputStream(extractFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer))> 0) {
                fos.write(buffer,0,count);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeSilently(is);
            closeSilently(fos);
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (Exception e) {

        }
    }

//    /**
//     * 待加载插件经过opt优化之后存放odex得路径
//     */
//    public static File getPluginOptDexDir(String packageName) {
//        return enforceDirExists(new File(getPluginBaseDir(packageName), "odex"));
//    }
//
//    /**
//     * 插件得lib库路径, 这个demo里面没有用
//     */
//    public static File getPluginLibDir(String packageName) {
//        return enforceDirExists(new File(getPluginBaseDir(packageName),"lib"));
//    }
//
//
//
//    private static File sBaseDir;
//
//    // 需要加载得插件得基本目录 /data/data/<package>/files/plugin/
//    private static File getPluginBaseDir(String packageName) {
//        if (sBaseDir == null) {
//            sBaseDir = MyApplication.getContext().getFileStreamPath("plugin");
//            enforceDirExists(sBaseDir);
//        }
//        return enforceDirExists(new File(sBaseDir,packageName));
//    }
//
//    private static synchronized File enforceDirExists(File sBaseDir) {
//        if (!sBaseDir.exists()) {
//            boolean ret = sBaseDir.mkdir();
//            if (!ret) {
//                throw new RuntimeException("create dir "+sBaseDir+" failed");
//            }
//        }
//        return sBaseDir;
//    }
}
