package business_android_client.wechatassistant;

import android.support.v7.widget.Toolbar;

import business_android_client.wechatassistant.base.BaseActivity;

public class MainActivity extends BaseActivity {

    /**
     * 增加toolbar  修改baseactivity
     */
    private Toolbar toolbar       ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;

    }

    @Override
    protected void initView() {
//        showHearts.openWechat();
    }

    @Override
    protected void initData() {


    }
}
