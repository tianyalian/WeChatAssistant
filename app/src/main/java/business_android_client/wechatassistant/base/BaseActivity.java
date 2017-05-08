package business_android_client.wechatassistant.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import business_android_client.wechatassistant.presenter.RedPacketPresenter;
import business_android_client.wechatassistant.presenter.ShowHeartsPresenter;

/**
 * Created by seeker on 2017/5/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public  Context ctx;
    public RedPacketPresenter redPacket;
    public ShowHeartsPresenter showHearts;

     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ctx = this;
        redPacket = new RedPacketPresenter(ctx);
        showHearts = new ShowHeartsPresenter(ctx);
        initView();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();



}
