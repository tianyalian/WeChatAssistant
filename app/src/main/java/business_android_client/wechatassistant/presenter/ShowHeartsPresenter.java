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

public boolean isFirst=true,isClickedPraise=false;

    private AlertDialog.Builder builder;

    public ShowHeartsPresenter(Context ctx) {
        super(ctx);
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
     */
    public void priseAtNameInContacts(AccessibilityNodeInfo info){
        scrollAndClick(Constants.person,800,info,false,Constants.new_friends);
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
     */
    public void praiseOneInContacts( AccessibilityNodeInfo rootInActiveWindow){
        if (rootInActiveWindow != null && !isClickedPraise) {

//            if (rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.new_friends) != null &&
//                    rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.new_friends).size() > 0) {//当前为通讯录界面
//                isContactsPage = true;
//                priseAtNameInContacts(rootInActiveWindow);
//            }
            if (rootInActiveWindow.getChildCount()==8 && rootInActiveWindow.getChild(0).getChild(2).isVisibleToUser()){//当前为通讯录界面
                priseAtNameInContacts(rootInActiveWindow);
            }else if (rootInActiveWindow.getContentDescription().toString().contains(Constants.details)) {//当前详细资料页面
                gotoPhoto(rootInActiveWindow);//跳转到个人相册
            } else if (rootInActiveWindow.getContentDescription().toString().contains(Constants.person)) {//相册列表
                List<AccessibilityNodeInfo> scrollableChildren = getScrollableChildren(rootInActiveWindow, true);
                clickFirstPhoto(scrollableChildren);
            } else if (rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.comment) != null &&
                    rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.comment).size() > 0) {//相册详情页
                clickPraise(rootInActiveWindow);
                rootInActiveWindow.performAction(AccessibilityService.GLOBAL_ACTION_BACK);
                isClickedPraise = true;
            } else if (Constants.detailPage.equals(rootInActiveWindow.getContentDescription().toString())){//如果是朋友圈音乐
                isClickedPraise = true;
//                rootInActiveWindow.performAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
//            else {
//                rootInActiveWindow.performAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
//                rootInActiveWindow.performAction(AccessibilityService.GLOBAL_ACTION_BACK);
//            }
//            else if (isContactsPage) {
//                priseAtNameInContacts(rootInActiveWindow);
//            }
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
    public void praiseInFirendsCircle(AccessibilityNodeInfo rootInActiveWindow){
        if (rootInActiveWindow.getContentDescription()!=null &&
                rootInActiveWindow.getContentDescription().toString().equals(Constants.friendsCirclePage)) {//发现页面
            List<AccessibilityNodeInfo> scrollableChildren = getScrollableChildren(rootInActiveWindow, true);
            if (scrollableChildren.size() > 0) {
                List<AccessibilityNodeInfo> list = scrollableChildren.get(0).findAccessibilityNodeInfosByText(Constants.comment);
                for (int i=0;i< list.size();i++) {
                    list.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);

                }

            }

        } else if (rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.discover_text) != null &&
                rootInActiveWindow.findAccessibilityNodeInfosByText(Constants.discover_text).size() > 0) {//微信
            gotoDiscover(rootInActiveWindow);
            gotoFriendsCircle(rootInActiveWindow);
        }

    }


}
