package skinsenor.jcgf.com.getwificount;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import skinsenor.jcgf.com.getwificount.pingip.PingManager;
import skinsenor.jcgf.com.getwificount.uicontrol.UIContract;
import skinsenor.jcgf.com.getwificount.uicontrol.UiPersonter;

public class MainActivity extends AppCompatActivity implements UIContract.activity {

    private UIContract.UIFrame uiFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new UiPersonter(this);
        uiFrame.oncreat(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        uiFrame.onstart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiFrame.onresume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        uiFrame.onrestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        uiFrame.onstop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiFrame.onpuse();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiFrame.ondestroy();
    }

    @Override
    public void setFrameView(UIContract.UIFrame frameView) {
        this.uiFrame = frameView;
    }

    @Override
    public Context getcontext() {
        return this;
    }
}
