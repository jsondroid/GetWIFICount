package skinsenor.jcgf.com.getwificount.uicontrol;

import android.app.Activity;
import android.view.View;

/**
 * Created by wenbaohe on 2018/5/17.
 */

public class FindByid {

    public <V extends View> V findById(Activity activity, int id) {
        return (V) activity.findViewById(id);
    }

    public <V extends View> V findById(View view, int id) {
        return (V) view.findViewById(id);
    }
}
