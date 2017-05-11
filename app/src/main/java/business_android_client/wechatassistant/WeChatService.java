package business_android_client.wechatassistant;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import business_android_client.wechatassistant.presenter.RedPacketPresenter;
import business_android_client.wechatassistant.presenter.ShowHeartsPresenter;
import business_android_client.wechatassistant.utils.Constants;

/**
 * Created by seeker on 2017/5/8.
 * 微信助手--辅助功能主要功能实现的服务
 */

public class WeChatService extends AccessibilityService {
    public RedPacketPresenter redPacket;
    public ShowHeartsPresenter showHearts;
    public boolean isFirst = true,isNeedPrise=true;
    private ContentObserver observer=new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.toString().contains("click")) {
                isNeedPrise = false;
            }
        }
    };
    private AccessibilityNodeInfo rootInActiveWindow;


    /**
     * 监听事件的回调
     * @param accessibilityEvent
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (isNeedPrise) {
                rootInActiveWindow = getRootInActiveWindow();
            if (rootInActiveWindow.getContentDescription().toString().contains("详细资料")) {//当前详细资料页面
                showHearts.gotoPhoto(rootInActiveWindow);//跳转到个人相册
            } else  if (rootInActiveWindow.getContentDescription().toString().contains("二姨夫")){//相册列表
//                rootInActiveWindow.
            }

//            if (isFirst) {
//                isFirst = false;
//                showHearts.gotoContacts(rootInActiveWindow);
//            }
//            showHearts.priseAtNameInContacts(getRootInActiveWindow());
//            getContentResolver().notifyChange(Uri.parse(Constants.uri_scroll), null);
        }
//        getContentResolver().notifyChange(Uri.parse(Constants.uri_click), null);
    }

    /**
     * 系统中断服务时
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(WeChatService.this, "谁把我干掉了!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 当打开辅助功能时候回调
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        redPacket = new RedPacketPresenter(WeChatService.this);
        showHearts = new ShowHeartsPresenter(WeChatService.this);
        Toast.makeText(WeChatService.this, "服务启动!", Toast.LENGTH_SHORT).show();
//        showHearts.startMainActivity();
        showHearts.openWechat();
        getContentResolver().registerContentObserver(Uri.parse(Constants.notify),true, observer);
    }


    /**
     *  在系统要关闭此service时调用。
     */
    @Override
    public boolean onUnbind(Intent intent) {
        getContentResolver().unregisterContentObserver(observer);
        return super.onUnbind(intent);
    }

}
