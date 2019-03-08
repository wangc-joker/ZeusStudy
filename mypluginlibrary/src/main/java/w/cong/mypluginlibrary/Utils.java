package w.cong.mypluginlibrary;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Utils {

    public static Context context;

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

    /**
     * 待加载插件经过opt优化之后存放odex得路径
     */
    public static File getPluginOptDexDir(String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(packageName), "odex"));
    }

    /**
     * 插件得lib库路径, 这个demo里面没有用
     */
    public static File getPluginLibDir(String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(packageName),"lib"));
    }



    private static File sBaseDir;

    // 需要加载得插件得基本目录 /data/data/<package>/files/plugin/
    private static File getPluginBaseDir(String packageName) {
        if (sBaseDir == null && context != null) {
            sBaseDir = context.getFileStreamPath("plugin");
            enforceDirExists(sBaseDir);
        }
        return enforceDirExists(new File(sBaseDir,packageName));
    }

    private static synchronized File enforceDirExists(File sBaseDir) {
        if (!sBaseDir.exists()) {
            boolean ret = sBaseDir.mkdir();
            if (!ret) {
                throw new RuntimeException("create dir "+sBaseDir+" failed");
            }
        }
        return sBaseDir;
    }

    /**
     * 读取zip文件中某个文件为字符串，参看自Zeus的PluginUtil
     *
     * @param zipFile     压缩文件
     * @param fileNameReg 需要获取的文件名
     * @return 获取的字符串
     */
    public static String readZipFileString(String zipFile, String fileNameReg) {
        final int BUF_SIZE = 8192;

        String result = null;
        byte[] buffer = new byte[BUF_SIZE];

        InputStream is = null;
        ZipInputStream zis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(zipFile);
            if (!file.exists()) {
                return null;
            }

            is = new FileInputStream(file);
            zis = new ZipInputStream(is);
            ZipEntry zipEntry = null;
            while ((zipEntry = zis.getNextEntry()) != null) {
                String zipName = zipEntry.getName();
                if (zipName .equals(fileNameReg)) {
                    int bytes = 0;
                    int count = 0;

                    bos = new  ByteArrayOutputStream();
                    while ((bytes = zis.read(buffer,0,BUF_SIZE))!=-1){
                        bos.write(buffer,0,bytes);
                        count += bytes;
                    }

                    if (count >0) {
                        result = bos.toString();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSilently(is);
            closeSilently(zis);
            closeSilently(bos);
        }
        return result;
    }
}
