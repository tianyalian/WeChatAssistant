package business_android_client.wechatassistant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import business_android_client.wechatassistant.presenter.BasePresenter;
import business_android_client.wechatassistant.presenter.MainActivityPresenter;

/**
 * Created by seeker on 2017/5/16.
 */

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "大喇叭开始广播了!", Toast.LENGTH_SHORT).show();
        MainActivityPresenter.ctx = MainApplication.getAppContext();
        MainActivityPresenter.openPraise();
//        MainActivityPresenter.ctx.getContentResolver().notifyChange(Uri.parse(Constants.));
        BasePresenter.openWechat();
    }
}
