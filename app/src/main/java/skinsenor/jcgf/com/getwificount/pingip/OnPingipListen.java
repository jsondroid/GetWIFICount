package skinsenor.jcgf.com.getwificount.pingip;

/**
 * Created by wenbaohe on 2018/5/17.
 */

public interface OnPingipListen {

    public void onBackAvailableIp(String ip);

    public void onBackPingCmdmsg(String msg);

    public void onBackPingFial(String msg);
}
