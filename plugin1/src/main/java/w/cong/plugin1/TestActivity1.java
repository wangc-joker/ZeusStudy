package w.cong.plugin1;

import android.os.Bundle;

import w.cong.mypluginlibrary.ZeusBaseActivity;

public class TestActivity1 extends ZeusBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent();
//
//                    String activityName = "w.cong.hostapp.ActivityA";
//                    intent.setComponent(new ComponentName("w.cong.hostapp", activityName));
//
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
