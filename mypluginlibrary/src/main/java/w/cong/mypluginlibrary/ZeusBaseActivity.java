package w.cong.mypluginlibrary;

import android.app.Activity;
import android.content.res.Resources;

public class ZeusBaseActivity extends Activity {

    @Override
    public Resources getResources() {
        return PluginManager.mNowResources;
    }
}
