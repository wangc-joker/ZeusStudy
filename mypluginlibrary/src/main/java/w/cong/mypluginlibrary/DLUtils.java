package w.cong.mypluginlibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class DLUtils {

    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageArchiveInfo(apkFilepath,PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static Drawable getAppIcon(Context context, String apkFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = getPackageInfo(context,apkFilePath);
        if (packageInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = packageInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir =apkFilePath;
            appInfo.publicSourceDir = apkFilePath;
        }

        return pm.getApplicationIcon(appInfo);
    }

    public static CharSequence getAppLable(Context context, String apkFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = getPackageInfo(context,apkFilePath);

        if (packageInfo == null) {
            return null;
        }

        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            applicationInfo.sourceDir =apkFilePath;
            applicationInfo.publicSourceDir = apkFilePath;
        }

        return pm.getApplicationLabel(applicationInfo);
    }
}
