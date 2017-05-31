package business_android_client.wechatassistant.presenter;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import business_android_client.wechatassistant.MainActivity;
import business_android_client.wechatassistant.bean.PraiseBean;
import business_android_client.wechatassistant.utils.Constants;

/**
 * Created by seeker on 2017/5/8.
 * 1. 处理RedPacket 红包助手页面和 ShowHearts自动点赞页面共同的逻辑
 * 2. 其中AccessibilityService的共同功能代码也写到这里
 */

public class BasePresenter {
    public static Context ctx;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PraiseBean bean = (PraiseBean) msg.obj;
            if (bean != null) {
                scrollscreen(bean.nodeInfo, bean.keywords);
                clickText(bean.nodeInfo, bean.text, bean.isNeedNotify);
            }

        }
    };
    private Runnable runnable;
    private PraiseBean bean;
    ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.toString().contains("scroll")) {
//                scrollscreen(bean.nodeInfo, bean.keywords);
//                clickText(bean.nodeInfo, bean.text, bean.isNeedNotify);
            }
        }
    };

    public BasePresenter(Context ctx) {
        this.ctx = ctx;
        ctx.getContentResolver().registerContentObserver(Uri.parse(Constants.notify),true, contentObserver);
    }

    public AccessibilityNodeInfo scrollview;



    /**
     * 打开微信
     */
    public static void openWechat() {
        PackageManager packageManager = ctx.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(Constants.wechatPackageName);
        ctx.startActivity(intent);
    }

    /**
     * 获取微信朋友列表  最后在做
     *
     * @return
     */
    public List<String> getFriendsList() {
        List<String> list = new ArrayList<>();


        return list;
    }

    /**
     * @param nodeInfo
     * @param text
     * @param isNeedNotify 是否要发送全局通知
     * @return
     */
    public boolean clickText(AccessibilityNodeInfo nodeInfo, String text, boolean isNeedNotify) {
        if (nodeInfo == null) {
            Log.d(Constants.TAG, "rootWindow为空");
            return false;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        for (AccessibilityNodeInfo info : list) {
            if (info.isCheckable()) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                sendNotify(isNeedNotify,Constants.uri_click);
                return true;
            } else {
                performClick(info.getParent());
            }
        }
        return false;
    }

    //滚动列表
    public void scrollscreen(AccessibilityNodeInfo nodeInfo, String keywords) {
        if (scrollview == null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(keywords);
            if (list != null && list.size() > 0) {
                AccessibilityNodeInfo info = list.get(0);
                for (int i = 0; i < 10; i++) {
                    if (!info.isScrollable() && !getParentInfo(info).isScrollable()) {//自己不能滚动,并且父类不能滚动,递归
                        info = getParentInfo(info);
                    } else {//跳出
                        scrollview = getParentInfo(info);
                        break;
                    }
                }
            }
        } else {
            scrollview.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }


    }

    /**
     * @param text      要点击的文本
     * @param frequency 每次滚动时间间隔
     * @param nodeInfo  根节点信息
     */
    public void scrollAndClick(final String text, int frequency,
                               final AccessibilityNodeInfo nodeInfo,
                               final boolean isNeedNotify,
                               final String keywords) {

                             scrollscreen(nodeInfo, keywords);
                            try {
                                Thread.sleep(frequency);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
        clickText(nodeInfo, text, isNeedNotify);
        bean = new PraiseBean(text, frequency, nodeInfo, isNeedNotify, keywords);
    }


    /**
     * 打开主界面
     */
    public void startMainActivity() {
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public void sendNotify(boolean isNeedNotify,String uri) {
        if (isNeedNotify) {
            ctx.getContentResolver().notifyChange(Uri.parse(uri), null);
        }
    }

    public AccessibilityNodeInfo getParentInfo(AccessibilityNodeInfo nodeInfo) {
        return nodeInfo.getParent();
    }

    /**
     * 跳转到个人相册
     */
    public void gotoPhoto(AccessibilityNodeInfo nodeInfo){
//        clickText(nodeInfo, Constants.photo, true);
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(Constants.photo);
        if (list != null && list.size() > 0) {
            AccessibilityNodeInfo accessibilityNodeInfo = list.get(0);
//            accessibilityNodeInfo.setClickable(true);
            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     * 查找当前节点下可以滚动的节点
     * @param nodeInfo
     * @param onlyListView 是否只要listview
     * @return List<AccessibilityNodeInfo>
     */
    public List<AccessibilityNodeInfo> getScrollableChildren(AccessibilityNodeInfo nodeInfo,
                                                             boolean onlyListView) {
        int childCount = nodeInfo.getChildCount();
        List<AccessibilityNodeInfo> scrollList = new ArrayList<>();
        List<AccessibilityNodeInfo> listview = new ArrayList<>();
        for (int i=0;i< childCount;i++) {
            AccessibilityNodeInfo child = nodeInfo.getChild(i);
            if (child.isScrollable() ) {
                if (onlyListView && child.getClassName().toString().contains("ListView")) {
                listview.add(child);
            } else {
                scrollList.add(child);
            }
        }
        }
        return onlyListView?listview:scrollList;
    }

    /**
     * 获取节点下的listView
     * @param listScroll 可以滚动的控件
     * @return
     */
    public List<AccessibilityNodeInfo> getListViewFromScrollable(List<AccessibilityNodeInfo> listScroll) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        for (AccessibilityNodeInfo nodeInfo:listScroll) {
            if (nodeInfo.getClassName().toString().contains("ListView")) {
                list.add(nodeInfo);
            }
        }
        return list;
    }



    //模拟点击事件
    public  void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    /**
     * 判断如果控件可以滚动就让控件滚动
     *
     * @param nodeInfo
     */
    public  void performScroll(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isScrollable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        } else {
            performScroll(nodeInfo.getParent());
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行全局事件
     * @param service
     * @param action AccessibilityService.当中的常量
     */
    public  void performGloabEvent(AccessibilityService service,int action) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.performGlobalAction(action);
        }
    }

    /**
     *  双击微信 ToolBar 列表滚动返回
     */
    public void scrollTop(AccessibilityNodeInfo nodeInfo,String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("微信");
        Rect rect = new Rect();
        if (list != null && list.size() > 0) {
            for (AccessibilityNodeInfo info: list) {
                info.getBoundsInScreen(rect);
                if (rect.top< 150) {//在720*1280手机上,左上角的"微信"boundinScreen 为Rect(28, 67 - 100, 116)
                    performClick(info);
                    performClick(info);
                    return;
                }
            }
        }
    }
}
