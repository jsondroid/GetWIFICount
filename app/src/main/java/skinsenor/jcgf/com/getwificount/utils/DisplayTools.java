package skinsenor.jcgf.com.getwificount.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by wenbaohe on 2018/3/15.
 */

public class DisplayTools {

    public static float dp2px(Context context,float dpValue) {
        float px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return px;
    }
}
