package business_android_client.wechatassistant;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
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
    public String [] constacts;
    int position = 0;
    public boolean  isNeedPrise = true, isContactsPage=false ,isClickedPraise=false;
    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.toString().contains("finsh_one_praise")) {//完成通讯录一次点赞
                if (position < 3) {
                    personName = constacts[position];
                    position++;
                } else {
                    personName = "";
                }

            }
        }
    };
    private AccessibilityNodeInfo rootInActiveWindow;
    public static boolean isAllPraise,isFirst = true;
    String personName;


    /**
     * 监听事件的回调
     *
     * @param accessibilityEvent
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        rootInActiveWindow = getRootInActiveWindow();
        if (isFirst) {
            isFirst = false;
            showHearts.gotoContacts(rootInActiveWindow);//到联系人列表
        } else {
            if (isAllPraise) {//朋友圈所有好友点赞
                showHearts.praiseInFirendsCircle(rootInActiveWindow,WeChatService.this);
            } else {//通过通讯录给指定的人点赞
                if (!TextUtils.isEmpty(personName)) {
                 showHearts.praiseOneInContacts(rootInActiveWindow,WeChatService.this,personName);
                }
            }
        }
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
    private  synchronized void  initData() {
        if (redPacket == null) {
//            redPacket = new RedPacketPresenter(WeChatService.this);
            showHearts = new ShowHeartsPresenter(WeChatService.this);
            Toast.makeText(WeChatService.this, "服务启动!", Toast.LENGTH_SHORT).show();
            isAllPraise = SPUtil.getBoolean(Constants.praise_all, false);
            showHearts.openWechat();
            constacts = new String[3];
            String string = SPUtil.getString(Constants.name1, "");
            position = 0;
            if (!TextUtils.isEmpty(string)) {
                constacts[position] = string;
                position++;
            }
             string = SPUtil.getString(Constants.name2, "");
            if (!TextUtils.isEmpty(string)) {
                constacts[position] = string;
                position++;
            }
             string = SPUtil.getString(Constants.name3, "");
            if (!TextUtils.isEmpty(string)) {
                constacts[position] = string;
            }
                position=0;
        }
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
