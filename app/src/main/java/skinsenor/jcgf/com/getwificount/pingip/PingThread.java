package skinsenor.jcgf.com.getwificount.pingip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wenbaohe on 2018/5/17.
 */

public class PingThread implements Runnable {

    private String ip;
    private OnPingipListen onPingipListen;
    private int pingcount;
    private int timeOut;

    public PingThread(String ip, OnPingipListen onPingipListen, int pingcount, int timeOut) {
        this.ip = ip;
        this.onPingipListen = onPingipListen;
        this.pingcount = pingcount;
        this.timeOut = timeOut;
    }

    @Override
    public void run() {
        if (ping(ip, pingcount, timeOut)) {
            if (onPingipListen != null) {
                onPingipListen.onBackAvailableIp(ip);
            }
        }
    }

    public boolean ping(String ipAddress, int pingcount, int timeOut) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();  // 将要执行的ping命令,此命令是linux ping -c 1 -w 5
//        Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
        String pingCommand = "ping -c " + pingcount + " -w " + timeOut + " " + ipAddress;
        try {
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            String line = null;
            while ((line = in.readLine()) != null) {
//                Log.e("信息---->", "" + line);
                if (onPingipListen != null) {
                    onPingipListen.onBackPingCmdmsg(line);
                }
            }
            // PING的状态
            int status = p.waitFor();  //status 为 0 ，ping成功，即为可以对外访问；为2则失败，即联网但不可以上网
            if (status == 0) {
//                Log.e("ping", "网络可用");
                return true;
            } else {
//                Log.e("ping", "网络bu可用");
                return false;
            }
        } catch (Exception ex) {
            if (onPingipListen != null) {
                onPingipListen.onBackPingFial(ex.getMessage());
            }
            ex.printStackTrace();   // 出现异常则返回假
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                if (onPingipListen != null) {
                    onPingipListen.onBackPingFial(e.getMessage());
                }
                e.printStackTrace();
            }
        }
    }

}
