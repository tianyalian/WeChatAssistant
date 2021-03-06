package business_android_client.wechatassistant.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by seeker on 2017/5/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public  Context ctx;
//    public RedPacketPresenter redPacket;
//    public ShowHeartsPresenter showHearts;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ctx = this;
//        redPacket = new RedPacketPresenter(ctx);
//        showHearts = new ShowHeartsPresenter(ctx);
        initView();
        initData();
    }

    protected abstract View getLayoutId();

    protected abstract void initView();

    protected abstract void initData();



}
