package business_android_client.wechatassistant;

import android.app.Application;
import android.content.Context;

/**
 * Created by seeker on 2017/5/8.
 */

public class MainApplication  extends Application{

    private static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx=this;
    }

    public static Context getAppContext() {
        return ctx;
    }
}
