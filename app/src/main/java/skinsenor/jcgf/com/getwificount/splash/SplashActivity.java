package skinsenor.jcgf.com.getwificount.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;

import skinsenor.jcgf.com.getwificount.MainActivity;
import skinsenor.jcgf.com.getwificount.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        weakHandle = new WeakHandle(this);
        post(1000);
    }

    private void post(long time) {
        weakHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                weakHandle.sendEmptyMessage(0x23);
            }
        }, time);
    }

    private WeakHandle weakHandle;

    class WeakHandle extends Handler {
        WeakReference<Activity> weakReference;

        public WeakHandle(Activity activity) {
            this.weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                if (msg.what == 0x23) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        post(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        weakHandle.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weakHandle.removeCallbacksAndMessages(null);
    }
}
