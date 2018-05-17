package skinsenor.jcgf.com.getwificount.uicontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import skinsenor.jcgf.com.getwificount.R;


/**
 * 作者：wenbaohe on 2017/3/22.
 */

public class ItemRecViewAdapter extends RecyclerView.Adapter<ItemRecViewAdapter.MyViewHolder> {

    private ItemClickListener itemClickListener;
    private ArrayList<String> listData;
    private Context context;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ItemRecViewAdapter(Context context, ArrayList<String> listData) {
        this.context = context;
        this.listData = listData;
    }

    /**
     * 初始化视图
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recview_itemlayout, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.text_tv.setText(listData.get(position));
    }

    /**
     * 返回总数大小
     */
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView text_tv;

        public MyViewHolder(final View itemView) {
            super(itemView);
            /**初始化控件*/
            text_tv = (TextView) itemView.findViewById(R.id.text_tv);
        }
    }
}


