package business_android_client.wechatassistant.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by seeker on 2017/5/8.
 * 1. 处理RedPacket 红包助手页面和 ShowHearts自动点赞页面共同的逻辑
 * 2. 其中AccessibilityService的共同功能代码也写到这里
 *
 */

public class BasePresenter {
    public  Context ctx;

    public BasePresenter(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * 打开微信
     */
    public void openWechat(){
        PackageManager packageManager = ctx.getPackageManager();
        Intent intent= packageManager.getLaunchIntentForPackage("com.tencent.mm");
        ctx.startActivity(intent);
    }
}
