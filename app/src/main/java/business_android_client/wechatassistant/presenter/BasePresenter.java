package business_android_client.wechatassistant.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import business_android_client.wechatassistant.MainActivity;
import business_android_client.wechatassistant.utils.Constants;

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
        Intent intent= packageManager.getLaunchIntentForPackage(Constants.wechatPackageName);
        ctx.startActivity(intent);
    }

    /**
     * 获取微信朋友列表
     * @return
     */
    public List<String> getFriendsList(){
        List<String> list = new ArrayList<>();


        return list;
    }

    /**
     * 主页跳转到朋友圈,其他页无效
     */
    public void gotoFriendsCircle( AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null) {
            Log.d(Constants.TAG, "rootWindow为空");
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(Constants.discover_id);
        for (AccessibilityNodeInfo info:list) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


   public void  startMainActivity(){
        Intent intent = new Intent(ctx,MainActivity.class);
        ctx.startActivity(intent);
    }

}
