package business_android_client.wechatassistant.presenter;

import android.app.AlertDialog;
import android.content.Context;

import java.util.List;

/**
 * Created by seeker on 2017/5/8.
 * 自动点赞页面
 */

public class ShowHeartsPresenter extends BasePresenter {
    public ShowHeartsPresenter(Context ctx) {
        super(ctx);
    }

    public void  showFriendsListDialog(List<String> list){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

    }
}
