package business_android_client.wechatassistant.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

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
        Intent intent = new Intent();
        ComponentName cmp = new ComponentName(" com.tencent.mm ","com.tencent.mm.ui.LauncherUI");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        ctx.startActivity(intent);
    }
}
