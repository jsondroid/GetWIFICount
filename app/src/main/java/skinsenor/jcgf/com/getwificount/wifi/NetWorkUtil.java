package skinsenor.jcgf.com.getwificount.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Jsondroid on 2017/5/12.
 * 监控网络工具类
 */

public class NetWorkUtil {

    public static int WIFI_CONNET = 0x290;
    public static int MOBLL_CONNET = 0x291;
    public static int DISCONNET = 0x292;


    private WifiBroadcastReceiver wifiBroadcastReceiver;
    private static NetWorkUtil instance;

    public static NetWorkUtil getInstance() {    //对获取实例的方法进行同步
        if (instance == null) {
            synchronized (NetWorkUtil.class) {
                if (instance == null)
                    instance = new NetWorkUtil();
            }
        }
        return instance;
    }

    public synchronized void registerWifiBroadcastReceiver(Context context) {
        if (wifiBroadcastReceiver == null) {
            wifiBroadcastReceiver = new WifiBroadcastReceiver(handler);
            wifiBroadcastReceiver.setCheckEnterNet(false);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.setPriority(1000);
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(wifiBroadcastReceiver, intentFilter);
        }
    }

    public synchronized void unWifiBroadcastReceiver(Context context) {
        if (wifiBroadcastReceiver != null) {
            context.unregisterReceiver(wifiBroadcastReceiver);
            handler.removeCallbacksAndMessages(null);
        }
    }

    private WeakReference<NetCallBack> netCallBackWeakReference;

    public void addActionCallback(NetCallBack netCallBack) {
        netCallBackWeakReference = new WeakReference<NetCallBack>(netCallBack);
    }

    public void removActionCallback() {
        if (netCallBackWeakReference != null) {
            netCallBackWeakReference.clear();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WIFI_CONNET) {
                if (netCallBackWeakReference != null) {
                    netCallBackWeakReference.get().connet((String) msg.obj, WIFI_CONNET);
                }
            }
            if (msg.what == MOBLL_CONNET) {
                if (netCallBackWeakReference != null) {
                    netCallBackWeakReference.get().connet((String) msg.obj, MOBLL_CONNET);
                }
            }
            if (msg.what == DISCONNET) {
                if (netCallBackWeakReference != null) {
                    netCallBackWeakReference.get().disconnet();
                }
            }
        }
    };

    public interface NetCallBack {
        public void connet(String ip, int type);

        public void disconnet();
    }


    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();//获取网络的连接情况
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            int sttype = activeNetInfo.getType();//这里的获取状态值和activeNetInfo.getType()是不一样的
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && sttype == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }
}
