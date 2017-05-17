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
import business_android_client.wechatassistant.utils.SPUtil;

/**
 * Created by seeker on 2017/5/8.
 * 微信助手--辅助功能主要功能实现的服务
 */

public class WeChatService extends AccessibilityService {
    public RedPacketPresenter redPacket;
    public ShowHeartsPresenter showHearts;
    public boolean isFirst = true, isNeedPrise = true, isContactsPage=false ,isClickedPraise=false;
    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.toString().contains("click")) {
                isNeedPrise = false;
            } else if (uri.toString().contains("allfriends")) {
                isAllPraise = true;
            } else if(uri.toString().contains("contacts")){
                isAllPraise = false;
            }
        }
    };
    private AccessibilityNodeInfo rootInActiveWindow;
    private boolean isAllPraise;


    /**
     * 监听事件的回调
     *
     * @param accessibilityEvent
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        if (isNeedPrise) {
        rootInActiveWindow = getRootInActiveWindow();
        if (isFirst) {
            isFirst = false;
            showHearts.gotoContacts(rootInActiveWindow);//到联系人列表
        } else {
            if (isAllPraise) {//朋友圈所有好友点赞
                showHearts.praiseInFirendsCircle(rootInActiveWindow,WeChatService.this);
            } else {//通过通讯录给指定的人点赞
                showHearts.praiseOneInContacts(rootInActiveWindow,WeChatService.this);
            }
        }
//        getContentResolver().notifyChange(Uri.parse(Constants.back),null);

//        performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
//        performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
//        performGlobalAction(AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);

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
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (redPacket == null) {
            redPacket = new RedPacketPresenter(WeChatService.this);
            showHearts = new ShowHeartsPresenter(WeChatService.this);
            Toast.makeText(WeChatService.this, "服务启动!", Toast.LENGTH_SHORT).show();
//        showHearts.startMainActivity();
            isAllPraise = SPUtil.getBoolean(Constants.praise, false);
            showHearts.openWechat();
//            getContentResolver().registerContentObserver(Uri.parse(Constants.notify), true, observer);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    /**
     * 在系统要关闭此service时调用。
     */
    @Override
    public boolean onUnbind(Intent intent) {
        getContentResolver().unregisterContentObserver(observer);
        return super.onUnbind(intent);
    }



}
