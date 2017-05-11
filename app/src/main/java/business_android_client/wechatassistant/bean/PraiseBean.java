package business_android_client.wechatassistant.bean;

import android.view.accessibility.AccessibilityNodeInfo;

import java.io.Serializable;

/**
 * Created by seeker on 2017/5/11.
 */

public class PraiseBean implements Serializable{
    public String text;
    public int frequency;
    public AccessibilityNodeInfo nodeInfo;
    public boolean isNeedNotify;
    public String keywords;

    public PraiseBean(String text, int frequency, AccessibilityNodeInfo nodeInfo, boolean isNeedNotify, String keywords) {
        this.text = text;
        this.frequency = frequency;
        this.nodeInfo = nodeInfo;
        this.isNeedNotify = isNeedNotify;
        this.keywords = keywords;
    }
}
