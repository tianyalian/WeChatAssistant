package business_android_client.wechatassistant.presenter;

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

public boolean isFirst=true;

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

    //进入联系人界面
    public  void gotoContacts(AccessibilityNodeInfo info){//Constants.new_friends
        if (isFirst) {
            clickText(info, Constants.contacts,false);
//            scrollscreen(info,"");
            isFirst = false;
        }
    }

    public void priseAtNameInContacts(AccessibilityNodeInfo info){
        scrollAndClick("二姨夫",800,info,true,"新的朋友");
        sendNotify(true, Constants.uri_scroll);
    }

    /**
     * listView 集合,实际只有一个listview
     * @param scrollableChildren
     */
    public void clickFirstPhoto(List<AccessibilityNodeInfo> scrollableChildren) {
        if ((scrollableChildren!=null)) {
            if (scrollableChildren.size() > 0) {
                AccessibilityNodeInfo accessibilityNodeInfo = scrollableChildren.get(0);//ListView  朋友圈的那个listview
//                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(1).getChild(2).getChild(1);//转发的内容 或只有文字:朋友圈父布局右边可以点击的部分
                AccessibilityNodeInfo child = accessibilityNodeInfo.getChild(1).getChild(0);//转发的内容 或只有文字:朋友圈父布局右边可以点击的部分

//                if (!child.isClickable()) {
//                     child = child.getChild(0);//图文: 父布局右边可以点击的
//                }
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
        List<AccessibilityNodeInfo> nodeInfos = rootInActiveWindow.findAccessibilityNodeInfosByText("赞");
        if (nodeInfos != null && nodeInfos.size()>0) {
            nodeInfos.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }
}
