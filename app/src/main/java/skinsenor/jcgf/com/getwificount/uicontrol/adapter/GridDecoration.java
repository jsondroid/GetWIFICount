package skinsenor.jcgf.com.getwificount.uicontrol.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者：soilman on 16/3/19 18:48
 * 邮箱：1812681724@qq.com
 */
public class GridDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private Drawable mDivider;
    private Context context;

    public GridDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        outRect.left = space / 2;
//        outRect.right = space / 2;
        outRect.bottom = space / 2;
        outRect.top = space / 2;
    }

}


