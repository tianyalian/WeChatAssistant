package business_android_client.wechatassistant.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import business_android_client.wechatassistant.presenter.RedPacketPresenter;
import business_android_client.wechatassistant.presenter.ShowHeartsPresenter;

/**
 * Created by seeker on 2017/5/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public  Context context;
    public RedPacketPresenter redPacket;
    public ShowHeartsPresenter showHearts;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
        redPacket = new RedPacketPresenter(context);
        showHearts = new ShowHeartsPresenter(context);
        initView();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();



}
