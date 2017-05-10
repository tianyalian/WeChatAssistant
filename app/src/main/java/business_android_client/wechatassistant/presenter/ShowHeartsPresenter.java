package business_android_client.wechatassistant.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.accessibility.AccessibilityNodeInfo;

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
            clickText(info, Constants.contacts,true);
            scrollscreen(info);
            isFirst = false;
        }
    }

    public void priseAtNameInContacts(AccessibilityNodeInfo info){
        scrollAndClick("二姨夫",500,info,true);
    }
}
