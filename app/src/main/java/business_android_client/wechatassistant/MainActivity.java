package business_android_client.wechatassistant;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import business_android_client.wechatassistant.base.BaseActivity;
import business_android_client.wechatassistant.presenter.MainActivityPresenter;
import business_android_client.wechatassistant.utils.Constants;

public class MainActivity extends BaseActivity implements
        View.OnClickListener,
        View.OnFocusChangeListener,RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener {

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
        presenter.initListener(MainActivity.this,MainActivity.this,MainActivity.this,MainActivity.this);
        getContentResolver().registerContentObserver(Uri.parse(Constants.back), false, observer);
    }

    @Override
    protected void initData() {
        presenter.initData();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
