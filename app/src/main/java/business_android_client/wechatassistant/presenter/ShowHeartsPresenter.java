package business_android_client.wechatassistant.presenter;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import business_android_client.wechatassistant.utils.Constants;

/**
 * Created by seeker on 2017/5/8.
 * 自动点赞页面
 */


public class ShowHeartsPresenter extends BasePresenter {

    public static  boolean isFirst=true;
    public static int count;//标记朋友圈点赞执行步骤

    private AlertDialog.Builder builder;
    public static boolean isDebug=false,isClickedPraise=false;
    private boolean NeedBackTop=true;
    private boolean NeedBackHome=true;

    public ShowHeartsPresenter(Context ctx) {
        super(ctx);
        count = -3;
    }

    /**
     * 弹出显示朋友列表的对话框   此功能放最后
     * @param friends
     * @param isckecked
     */
    public void  showFriendsListDialog(String[] friends,boolean[] isckecked){

        if (builder==null) {
            builder = new AlertDialog.Builder(ctx);
            builder.setTitle("最多选10位好友");

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        builder.setMultiChoiceItems(friends, isckecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {

            }
        });
        builder.show();

    }


    /**
     * 进入联系人界面
     * @param info
     */
    public  void gotoContacts(AccessibilityNodeInfo info){//Constants.new_friends
        if (isFirst) {
            clickText(info, Constants.contacts,false);
            isFirst = false;
        }
    }

    /**
     * 查找通讯录并点击指定的人名
     * @param info
     * @param personName
     */
    public void priseAtNameInContacts(AccessibilityNodeInfo info, String personName){
        scrollAndClick(personName,300,info,false,Constants.new_friends);
        sendNotify(false, Constants.uri_scroll);
    }

    /**
     * listView 集合,实际只有一个listview
     * @param scrollableChildren
     */
    public void clickFirstPhoto(List<AccessibilityNodeInfo> scrollableChildren) {
        if ((scrollableChildren!=null)) {
            if (scrollableChildren.size() > 0) {
                AccessibilityNodeInfo accessibilityNodeInfo = scrollableChildren.get(0);//ListView  朋友圈的那个listview
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(1).getChild(0).getChild(1);//转发的内容 或只有文字:朋友圈父布局右边可以点击的部分
                if (child.isClickable()) {
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }
    }

    /**
     * 点击赞
     * @param rootInActiveWindow
     */
    public void clickPraise(AccessibilityNodeInfo rootInActiveWindow) {
        clickText(rootInActiveWindow, Constants.praise, false);
    }


    /**
     * 从通讯录界面开始,找到指定朋友,进入朋友圈相册页面,给第一条点赞
     * @param rootInActiveWindow
     * @param personName
     */
    public synchronized void praiseOneInContacts(AccessibilityNodeInfo rootInActiveWindow, AccessibilityService service, String personName){
        if (rootInActiveWindow != null && !isClickedPraise) {
            if (NeedBackHome) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                isClickedPraise = true;
            }

            if (rootInActiveWindow.findAccessibilityNodeInfosByText("微信").size()>1
                    && rootInActiveWindow.getChildCount()==8
                    && rootInActiveWindow.getChild(0).getChild(2).isVisibleToUser()){//当前为通讯录界面
                if (NeedBackTop) {
                    NeedBackTop=false;
                    scrollTop(rootInActiveWindow, "微信");
                }

                List<AccessibilityNodeInfo> infos = rootInActiveWindow.findAccessibilityNodeInfosByText("位联系人");
                if (infos.size() > 0 && infos.get(0).isVisibleToUser()) {//已经滚动到最底部了,就返回顶部
                    NeedBackTop=true;
                }
                priseAtNameInContacts(rootInActiveWindow,personName);
            }else if (rootInActiveWindow.getContentDescription()!=null &&
                    rootInActiveWindow.getContentDescription().toString().contains(Constants.details)) {//当前详细资料页面
                gotoPhoto(rootInActiveWindow);//跳转到个人相册
            } else if (rootInActiveWindow.getContentDescription()!=null &&
                    rootInActiveWindow.getContentDescription().toString().contains(personName)) {//相册列表
                List<AccessibilityNodeInfo> scrollableChildren = getScrollableChildren(rootInActiveWindow, true);
                clickFirstPhoto(scrollableChildren);
            } else if ((rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.comment) != null &&
                    rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.comment).size() > 0)
                    ||(rootInActiveWindow.getContentDescription()!=null &&
                    Constants.detailPage.equals(rootInActiveWindow.getContentDescription().toString()))) {//相册详情页
                if (!clickText(rootInActiveWindow, Constants.praise, false)) {//详情为照片
                    AccessibilityNodeInfo child = rootInActiveWindow.getChild(0).getChild(0);
                    if (child != null) {//当前为照片,并且已经点过赞了
                        if (Constants.imageButton.equals(child.getChild(5).getClassName())) {//详情为连接
                            child.getChild(5).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else {
                            child.getChild(6).performAction(AccessibilityNodeInfo.ACTION_CLICK);//详情为一句话
                        }
                    }
                    clickText(rootInActiveWindow, Constants.praise, false);
                }
//                isClickedPraise = true;
                NeedBackTop=true;
                NeedBackHome = true;
                NeedBackHome =clickText(rootInActiveWindow, Constants.backbutton, false);
                performGloabEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
                performGloabEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
                performGloabEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
                sendNotify(true, Constants.finsh_one_praise);

            } else if (rootInActiveWindow.getContentDescription()!=null &&
                 Constants.detailPage.equals(rootInActiveWindow.getContentDescription().toString())){//如果是朋友圈音乐  或者已经评论过的朋友圈详情
                NeedBackHome =clickText(rootInActiveWindow, Constants.backbutton, false);
                performGloabEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
                performGloabEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
                performGloabEvent(service, AccessibilityService.GLOBAL_ACTION_BACK);
                sendNotify(true, Constants.finsh_one_praise);
               NeedBackTop=true;
            }else{
                performGloabEvent(service,AccessibilityService.GLOBAL_ACTION_BACK);
                performGloabEvent(service,AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }
    }

    /**
     * 到发现页面
     * @param rootInActiveWindow
     */
    public void gotoDiscover(AccessibilityNodeInfo rootInActiveWindow){
        clickText(rootInActiveWindow, Constants.discover_text, false);
    }

    /**
     * 到朋友圈界面
     * @param rootInActiveWindow
     */
    public void gotoFriendsCircle(AccessibilityNodeInfo rootInActiveWindow){
        clickText(rootInActiveWindow, Constants.frieds_circle, false);
    }

    /**
     * 在朋友圈页面滚动 pageTurningTime次内的所有朋友点赞
     */
    public synchronized void praiseInFirendsCircle(AccessibilityNodeInfo rootInActiveWindow,AccessibilityService service){
        if (count > 0) {
            if (rootInActiveWindow==null) {
                return;
            }

            if (rootInActiveWindow.getContentDescription() != null &&
                    rootInActiveWindow.getContentDescription().toString().equals(Constants.friendsCirclePage)) {//发现页面
                List<AccessibilityNodeInfo> scrollableChildren = getScrollableChildren(rootInActiveWindow, true);
                if (scrollableChildren.size() > 0) {
                    List<AccessibilityNodeInfo> list = scrollableChildren.get(0).findAccessibilityNodeInfosByText(Constants.comment);
                    for (int i = 0; i < list.size(); i++) {
                        AccessibilityNodeInfo nodeInfo = list.get(i);
                        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        clickText(rootInActiveWindow, Constants.praise, false);
                    }
                    scrollableChildren.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    count--;
                }

            } else if (rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.discover_text) != null &&
                    rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.discover_text).size() > 0) {//微信
                gotoDiscover(rootInActiveWindow);
                gotoFriendsCircle(rootInActiveWindow);
            } else {
                openWechat();//如果当前不是微信主页面,并且界面有后退按钮,就一直返回
                clickText(rootInActiveWindow, Constants.backbutton, false);
                clickText(rootInActiveWindow, Constants.backbutton, false);
                clickText(rootInActiveWindow, Constants.backbutton, false);
            }

        } else if (count==0){

            performGloabEvent(service,AccessibilityService.GLOBAL_ACTION_BACK);
            performGloabEvent(service,AccessibilityService.GLOBAL_ACTION_BACK);
//            performGloabEvent(service,AccessibilityService.GLOBAL_ACTION_HOME);
            if (isDebug) {
                startMainActivity();
                isDebug = false;
            }
            count--;
        } else if (count == -1) {
            count--;
            openWechat();
            startMainActivity();
        }else if(count==-2){
            startMainActivity();
        }


    }



}
