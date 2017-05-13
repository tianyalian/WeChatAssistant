package business_android_client.wechatassistant;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import business_android_client.wechatassistant.base.BaseActivity;
import business_android_client.wechatassistant.utils.Constants;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 增加toolbar  修改baseactivity
     */
    private Toolbar toolbar;
    private Button button;
    private EditText editText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;

    }

    @Override
    protected void initView() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.editText);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        Constants.person = editText.getText().toString().trim();
        showHearts.openWechat();
    }
}
