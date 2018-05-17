package skinsenor.jcgf.com.getwificount.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by  温宝河 on 2017/5/12.
 * 监听网络变化
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private boolean isCheckEnterNet = false;//是否检测网络可用

    public boolean isCheckEnterNet() {
        return isCheckEnterNet;
    }

    public void setCheckEnterNet(boolean checkEnterNet) {
        isCheckEnterNet = checkEnterNet;
    }

    private Handler handler;

    public WifiBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();//获取网络的连接情况
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            int sttype = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, -1);//这里的获取状态值和activeNetInfo.getType()是不一样的
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && sttype == ConnectivityManager.TYPE_WIFI) {
                Log.e("打印21", "已连接到wifi" + activeNetInfo.getTypeName());
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                if (isCheckEnterNet) {
                    /**检测是否可以上网*/
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ping(true);
                        }
                    }).start();
                } else {
                    senMsg(NetWorkUtil.WIFI_CONNET,ipAddress);
                }

            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE && sttype == ConnectivityManager.TYPE_MOBILE) {
                Log.e("打印22", "已连接到3G网络" + activeNetInfo.getTypeName());
                senMsg(NetWorkUtil.MOBLL_CONNET,"");
            }
        } else {
            Log.e("打印23", "未连接网络");
            handler.sendEmptyMessage(NetWorkUtil.DISCONNET);
        }
    }


    public boolean ping(boolean networkState) {
        InputStream input = null;
        BufferedReader in;
        StringBuffer stringBuffer;
        if (networkState) {
            try {
                String ip = "www.baidu.com";//之所以写百度是因为百度比较稳定，一般不会出现问题，也可以ping自己的服务器
                Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping3次
                // 读取ping的内容
                input = p.getInputStream();
                in = new BufferedReader(new InputStreamReader(input));
                stringBuffer = new StringBuffer();
                String content = "";
                while ((content = in.readLine()) != null) {
                    stringBuffer.append(content);
                }
                // PING的状态
                int status = p.waitFor();  //status 为 0 ，ping成功，即为可以对外访问；为2则失败，即联网但不可以上网
                if (status == 0) {
                    Log.e("ping", "网络可用");
                    handler.sendEmptyMessage(NetWorkUtil.WIFI_CONNET);
                    return true;
                } else {
                    Log.e("ping", "网络不可用");
                    handler.sendEmptyMessage(NetWorkUtil.DISCONNET);
                    return false;
                }
            } catch (IOException e) {
                Log.e("IOException", "IOException");
            } catch (InterruptedException e) {
                Log.e("InterruptedException", "InterruptedException");
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }
        return false;
    }

    private void senMsg(int what,Object obj){
        if(handler!=null){
            Message message=handler.obtainMessage();
            message.what=what;
            message.obj=obj;
            handler.sendMessage(message);
        }
    }
    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
