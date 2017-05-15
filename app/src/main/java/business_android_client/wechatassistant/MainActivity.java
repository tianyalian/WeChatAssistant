package business_android_client.wechatassistant;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import business_android_client.wechatassistant.base.BaseActivity;
import business_android_client.wechatassistant.presenter.MainActivityPresenter;
import business_android_client.wechatassistant.utils.Constants;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 增加toolbar  修改baseactivity
     */
    public MainActivityPresenter presenter;
    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            if (uri.toString().contains("back")) {

            }
        }
    };


    @Override
    protected View getLayoutId() {
        View view = View.inflate(MainActivity.this, R.layout.activity_settting, null);
        presenter = new MainActivityPresenter(MainActivity.this);
        presenter.initView(view);
        return view;
    }

    @Override
    protected void initView() {

        getContentResolver().registerContentObserver(Uri.parse(Constants.back), false, observer);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
//        Constants.person = editText.getText().toString().trim();
//        showHearts.openWechat();
    }

}
