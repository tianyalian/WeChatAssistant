package business_android_client.wechatassistant.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
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
    public Handler handler=new Handler();
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
     * 获取微信朋友列表  最后在做
     * @return
     */
    public List<String> getFriendsList(){
        List<String> list = new ArrayList<>();


        return list;
    }

    /**
     *
     * @param nodeInfo
     * @param text
     * @param isNeedNotify  是否要发送全局通知
     * @return
     */
    public boolean clickText(AccessibilityNodeInfo nodeInfo,String text,boolean isNeedNotify){
        if (nodeInfo == null) {
            Log.d(Constants.TAG, "rootWindow为空");
            return false;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        for (AccessibilityNodeInfo info:list) {
            if (info.isCheckable()) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendNotify(isNeedNotify);
                return true;
            } else {
                AccessibilityNodeInfo parent = info.getParent();
                if (parent.isClickable()) {
                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sendNotify(isNeedNotify);
                    return true;
                } else {
                    parent.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    sendNotify(isNeedNotify);
                    return true;
                }
            }
        }
        return false;
    }

    //滚动列表
    public void scrollscreen(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo.isCheckable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        } else {
            AccessibilityNodeInfo parent = nodeInfo.getParent();
            if (parent.isClickable()) {
                parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            } else {
                parent.getParent().performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
        }
    }

    /**
     *
     * @param text  要点击的文本
     * @param frequency 每次滚动时间间隔
     * @param nodeInfo  根节点信息
     */
    public void scrollAndClick(final String text, int frequency, final AccessibilityNodeInfo nodeInfo,final boolean isNeedNotify) {
           scrollscreen(nodeInfo);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    clickText(nodeInfo, text,isNeedNotify);
                }
            }, frequency);
    }


    /**
     * 打开主界面
     */
   public void  startMainActivity(){
        Intent intent = new Intent(ctx,MainActivity.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public void sendNotify(boolean isNeedNotify){
        if (isNeedNotify) {
        ctx.getContentResolver().notifyChange(Uri.parse(Constants.uri),null);
        }
    }

}
