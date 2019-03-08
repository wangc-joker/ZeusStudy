package w.cong.plugin1;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class TestService1 extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("cong","service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("cong","service onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("cong","service onBind");
        return null;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.d("cong","service unbindService");
        super.unbindService(conn);
    }
}
