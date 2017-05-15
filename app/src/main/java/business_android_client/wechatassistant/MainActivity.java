package business_android_client.wechatassistant;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import business_android_client.wechatassistant.base.BaseActivity;
import business_android_client.wechatassistant.presenter.MainActivityPresenter;
import business_android_client.wechatassistant.utils.Constants;
import business_android_client.wechatassistant.utils.SPUtil;

public class MainActivity extends BaseActivity implements
        View.OnClickListener,
        View.OnFocusChangeListener,RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener {

    /**http://www.cnblogs.com/bloglkl/p/5770279.html   时间日期选择器
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
        switch (view.getId()) {
            case R.id.tv_end_time:
                presenter.showTimeDialog(false);
                break;
            case R.id.tv_start_time:
                presenter.showTimeDialog(true);
                break;
            case R.id.test_all_praise:
                break;
            case R.id.test_contacts_praise:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.friends1:
                if (!hasFocus) {
                    SPUtil.put(ctx, Constants.name1, ((EditText)v).getText().toString().trim());
                }
                break;
            case R.id.friends2:
                if (!hasFocus) {
                    SPUtil.put(ctx, Constants.name2, ((EditText)v).getText().toString().trim());
                }
                break;
            case R.id.friends3:
                if (!hasFocus) {
                    SPUtil.put(ctx, Constants.name3, ((EditText)v).getText().toString().trim());
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_all:
                SPUtil.put(ctx,Constants.praise_all,true);
                break;
            case R.id.rb_single:
                SPUtil.put(ctx,Constants.praise_all,false);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_praise:
                SPUtil.put(ctx,Constants.sw_praise,isChecked);
                break;
            case R.id.sw_red_packet:
                SPUtil.put(ctx,Constants.sw_red_packet,isChecked);
                break;
        }
    }
}
