package skinsenor.jcgf.com.getwificount.uicontrol;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import skinsenor.jcgf.com.getwificount.R;
import skinsenor.jcgf.com.getwificount.pingip.PingManager;
import skinsenor.jcgf.com.getwificount.uicontrol.adapter.GridDecoration;
import skinsenor.jcgf.com.getwificount.uicontrol.adapter.ItemRecViewAdapter;
import skinsenor.jcgf.com.getwificount.utils.DisplayTools;
import skinsenor.jcgf.com.getwificount.wifi.NetWorkUtil;

/**
 * Created by wenbaohe on 2018/5/17.
 */

public class UiPersonter extends FindByid implements UIContract.UIFrame, NetWorkUtil.NetCallBack, PingManager.PingCallback {

    private UIContract.activity activity;

    private SwipeRefreshLayout swiperefreshlayout;
    private RecyclerView recyclerView;
    private TextView text_count;

    public UiPersonter(UIContract.activity activity) {
        this.activity = activity;
        this.activity.setFrameView(this);
    }

    @Override
    public void oncreat(Activity activity) {
        swiperefreshlayout = findById(activity, R.id.swiperefreshlayout);
        recyclerView = findById(activity, R.id.recyclerview);
        text_count = findById(activity, R.id.text_count);
        swiperefreshlayout.setOnRefreshListener(onRefreshListener);
        setData();
        NetWorkUtil.getInstance().registerWifiBroadcastReceiver(activity);
        NetWorkUtil.getInstance().addActionCallback(this);

        swiperefreshlayout.post(new Runnable() {
            @Override
            public void run() {
                onRefreshListener.onRefresh();
            }
        });
    }

    private ItemRecViewAdapter viewAdapter;
    private ArrayList<String> listData;
    private ArrayList<String> datas;
    private LinearLayoutManager layoutManager;

    private void setData() {
        layoutManager = new LinearLayoutManager(activity.getcontext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置分割线
        recyclerView.addItemDecoration(new GridDecoration((int) DisplayTools.dp2px(activity.getcontext(), 10)));
        listData = new ArrayList<>();
        datas = new ArrayList<>();
        viewAdapter = new ItemRecViewAdapter(activity.getcontext(), listData);
        recyclerView.setAdapter(viewAdapter);
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            text_count.setText("0");
            listData.clear();
            viewAdapter.notifyDataSetChanged();
            swiperefreshlayout.setRefreshing(true);
            if (NetWorkUtil.isNetworkAvailable(activity.getcontext())) {
                WifiManager wifiManager = (WifiManager) activity.getcontext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                PingManager.getInstance(activity.getcontext()).ping(ipAddress, UiPersonter.this);
            } else {
                text_count.setText("0");
                swiperefreshlayout.setRefreshing(false);
                Toast.makeText(activity.getcontext(), "请打开WIFI连接！", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    public void onstart() {

    }

    @Override
    public void onresume() {

    }

    @Override
    public void onrestart() {

    }

    @Override
    public void onpuse() {

    }

    @Override
    public void onstop() {

    }

    @Override
    public void ondestroy() {
        NetWorkUtil.getInstance().unWifiBroadcastReceiver(activity.getcontext());
    }

    @Override
    public void connet(String ip, int type) {
        listData.clear();
        viewAdapter.notifyDataSetChanged();
        if (type == NetWorkUtil.WIFI_CONNET) {
            swiperefreshlayout.setRefreshing(true);
            PingManager.getInstance(activity.getcontext()).ping(ip, this);
        }
        if (type == NetWorkUtil.MOBLL_CONNET) {
            swiperefreshlayout.setRefreshing(false);
            disconnet();
        }
    }

    @Override
    public void disconnet() {
        text_count.setText("0");
        swiperefreshlayout.setRefreshing(false);
        listData.clear();
        viewAdapter.notifyDataSetChanged();
        PingManager.getInstance(activity.getcontext()).relese();
    }

    @Override
    public void onBackAvailableIp(String ip) {
        Log.e("地址----》", "" + ip);
        swiperefreshlayout.setRefreshing(false);
        listData.clear();
        if (!datas.contains(ip)) {
            datas.add(ip);
        }
        listData.addAll(datas);
        viewAdapter.notifyDataSetChanged();
        text_count.setText("" + listData.size());
    }

    @Override
    public void onBackPingCmdmsg(String msg) {

    }

    @Override
    public void onBackPingFial(String msg) {
        swiperefreshlayout.setRefreshing(false);
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
