package business_android_client.wechatassistant;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import business_android_client.wechatassistant.base.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 增加toolbar  修改baseactivity
     */
    private Toolbar toolbar;
    private Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;

    }

    @Override
    protected void initView() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        showHearts.openWechat();
//        getContentResolver().notifyChange(Uri.parse(Constants.notify), null);
    }
}
