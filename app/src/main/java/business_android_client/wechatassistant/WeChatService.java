package business_android_client.wechatassistant;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import business_android_client.wechatassistant.presenter.RedPacketPresenter;
import business_android_client.wechatassistant.presenter.ShowHeartsPresenter;

/**
 * Created by seeker on 2017/5/8.
 */

public class WeChatService extends AccessibilityService {
    public RedPacketPresenter redPacket;
    public ShowHeartsPresenter showHearts;

    /**
     * 监听事件的回调
     * @param accessibilityEvent
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    /**
     * 系统中断服务时
     */
    @Override
    public void onInterrupt() {

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
    }


    /**
     *  在系统要关闭此service时调用。
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
