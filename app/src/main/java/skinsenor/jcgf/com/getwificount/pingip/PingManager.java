package skinsenor.jcgf.com.getwificount.pingip;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wenbaohe on 2018/5/17.
 */

public class PingManager implements OnPingipListen {

    private final int MSG_Available = 0x31;
    private final int MSG_msg = 0x32;
    private final int MSG_Fail = 0x33;

    private static PingManager instance;
    private WeakReference<Context> contextw;

    private PingCallback pingCallback;

    public interface PingCallback {
        public void onBackAvailableIp(String ip);

        public void onBackPingCmdmsg(String msg);

        public void onBackPingFial(String msg);
    }

    public static PingManager getInstance(Context context) {
        if (instance == null) {
            synchronized (PingManager.class) {
                if (instance == null) {
                    instance = new PingManager(context);
                }
            }
        }

        return instance;
    }


    public PingManager(Context context) {
        this.contextw = new WeakReference<Context>(context);
        handler = new WeakHandler(context);
        poolsize = Runtime.getRuntime().availableProcessors();
        Log.e("处理器个数--->", "" + poolsize);
        executorService = Executors.newFixedThreadPool(poolsize * 3);
    }

    private WeakHandler handler;
    private int poolsize = 0;
    private ExecutorService executorService;


    class WeakHandler extends Handler {
        WeakReference<Context> weakReference;

        public WeakHandler(Context context) {
            this.weakReference = new WeakReference<Context>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (weakReference.get() != null) {
                switch (msg.what) {
                    case MSG_Available:
                        if (pingCallback != null) pingCallback.onBackAvailableIp((String) msg.obj);
                        break;
                    case MSG_msg:
                        if (pingCallback != null) pingCallback.onBackPingCmdmsg((String) msg.obj);
                        break;
                    case MSG_Fail:
                        if (pingCallback != null) pingCallback.onBackPingFial((String) msg.obj);
                        break;
                }
            }
        }
    }

    public void ping(String ip, PingCallback pingCallback) {
        this.pingCallback = pingCallback;
        String subip=ip.substring(0, ip.lastIndexOf("."));
        String ips = "";
        for (int i = 0; i < 255; i++) {
            ips = subip+ "."+i;
            executorService.execute(new PingThread(ips, this, 2, 100));
        }
    }


    public void ping(String ip, int pingcount, int timeOut, PingCallback pingCallback) {
        this.pingCallback = pingCallback;
        String ips = ip.substring(0, ip.lastIndexOf("."));
        for (int i = 0; i < 255; i++) {
            ips = ips + i;
            executorService.execute(new PingThread(ips, this, pingcount, timeOut));
        }
    }

    @Override
    public void onBackAvailableIp(String ip) {
        sendmsg(MSG_Available, ip);
    }

    @Override
    public void onBackPingCmdmsg(String msg) {
        sendmsg(MSG_msg, msg);
    }

    @Override
    public void onBackPingFial(String msg) {
        sendmsg(MSG_Fail, msg);
    }


    private void sendmsg(int what, Object obj) {
        if (handler == null) {
            return;
        }
        handler.sendMessage(creatmsg(what, obj));
    }

    private Message creatmsg(int what, Object obj) {
        if (handler == null) {
            return null;
        }
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = obj;
        return message;
    }

    public void relese() {
        handler.removeCallbacksAndMessages(null);
        pingCallback=null;
    }

}
