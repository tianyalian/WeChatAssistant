package business_android_client.wechatassistant;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import business_android_client.wechatassistant.base.BaseActivity;
import business_android_client.wechatassistant.presenter.MainActivityPresenter;
import business_android_client.wechatassistant.utils.Constants;
import business_android_client.wechatassistant.utils.SPUtil;

public class MainActivity extends BaseActivity implements
        View.OnClickListener,
        TextWatcher, RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener {

    /**
     * http://www.cnblogs.com/bloglkl/p/5770279.html   时间日期选择器
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
        presenter.initListener(MainActivity.this, MainActivity.this, MainActivity.this, MainActivity.this);
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
                presenter.testPraiseAll();
                break;
            case R.id.test_contacts_praise:
                presenter.testPraiseContact();
                break;
            case R.id.imageView_del:
                presenter.delTime();
                break;
        }
    }

    /**
     * 点赞类型的监听
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_all:
                SPUtil.put(ctx, Constants.praise_all, true);
                WeChatService.isAllPraise = true;
                break;
            case R.id.rb_single:
                WeChatService.isAllPraise = false;
                SPUtil.put(ctx, Constants.praise_all, false);
                break;
        }
    }

    /**
     * 红包开关& 点赞开关的监听
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_praise:
                SPUtil.put(ctx, Constants.sw_praise, isChecked);
                if (isChecked) {
                    presenter.openPraise();
                } else {
                    presenter.closePraise(false);
                }
                break;
            case R.id.sw_red_packet:
                SPUtil.put(ctx, Constants.sw_red_packet, isChecked);
//                if (isChecked) {
//                } else {
//                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String name = s.toString().trim();
        if (presenter.et_friends1.hasFocus()) {
            SPUtil.put(ctx, Constants.name1, name);
        } else if (presenter.et_friends2.hasFocus()) {
            SPUtil.put(ctx, Constants.name2, name);
        } else if (presenter.et_friends3.hasFocus()) {
            SPUtil.put(ctx, Constants.name3, name);
        }
    }
}
