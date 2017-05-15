package business_android_client.wechatassistant;

import android.app.Instrumentation;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.toString().contains("back")) {
                backPress();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settting;

    }

    @Override
    protected void initView() {
//        button = (Button) findViewById(R.id.button);
//        button.setOnClickListener(this);
//        editText = (EditText) findViewById(R.id.editText);
        getContentResolver().registerContentObserver(Uri.parse(Constants.back),false,observer);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        Constants.person = editText.getText().toString().trim();
//        showHearts.openWechat();
    }

    private void backPress() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
//                    Log.d(TAG, "run: "+e.toString());
                }
            }
        }.start();
    }
}
