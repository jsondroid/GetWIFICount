package skinsenor.jcgf.com.getwificount.uicontrol;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by wenbaohe on 2018/5/17.
 */

public interface UIContract {

    public interface UIFrame {
        public void oncreat(Activity activity);

        public void onstart();

        public void onresume();

        public void onrestart();

        public void onpuse();

        public void onstop();

        public void ondestroy();
    }

    public interface activity extends IUIViews<UIFrame>{

        public Context getcontext();
    }

}
