package business_android_client.wechatassistant.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import business_android_client.wechatassistant.R;
import business_android_client.wechatassistant.utils.Constants;
import business_android_client.wechatassistant.utils.SPUtil;

/**
 * Created by seeker on 2017/5/15.
 */

public class MainActivityPresenter {
    public static final int PRAISE_TYPE_ALL = 0;
    public static final int PRAISE_TYPE_CONTACTS = 1;
    private final Context ctx;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private EditText et_friends1;
    private EditText et_friends2;
    private EditText et_friends3;
    private RadioButton rb_all;
    private RadioButton rb_single;
    private RadioGroup rg;
    private Switch sw_praise;
    private Switch sw_red_packet;
    private Button test_all_praise;
    private Button test_contacts_praise;

    public MainActivityPresenter(Context ctx) {
        this.ctx=ctx;
    }


    public void initView(View v){
        test_all_praise = (Button) v.findViewById(R.id.test_all_praise);
        test_contacts_praise = (Button) v.findViewById(R.id.test_contacts_praise);
        sw_praise = (Switch) v.findViewById(R.id.sw_praise);
        sw_red_packet = (Switch) v.findViewById(R.id.sw_red_packet);
        rg = (RadioGroup) v.findViewById(R.id.rg);
        rb_single = (RadioButton) v.findViewById(R.id.rb_single);
        rb_all = (RadioButton) v.findViewById(R.id.rb_all);
        et_friends1 = (EditText) v.findViewById(R.id.friends1);
        et_friends2 = (EditText) v.findViewById(R.id.friends2);
        et_friends3 = (EditText) v.findViewById(R.id.friends3);
        tv_end_time = (TextView) v.findViewById(R.id.tv_end_time);
        tv_start_time = (TextView) v.findViewById(R.id.tv_start_time);
    }


    public void initListener(View.OnClickListener listener,
                             View.OnFocusChangeListener focus,
                             RadioGroup.OnCheckedChangeListener rg_checkchangelistener,
                             CompoundButton.OnCheckedChangeListener cb_checkchangelistener){
        test_all_praise.setOnClickListener(listener);
        test_contacts_praise.setOnClickListener(listener);
        tv_start_time.setOnClickListener(listener);
        tv_end_time.setOnClickListener(listener);
        sw_praise.setOnCheckedChangeListener(cb_checkchangelistener);
        sw_red_packet.setOnCheckedChangeListener(cb_checkchangelistener);
        rg.setOnCheckedChangeListener(rg_checkchangelistener);
        et_friends1.setOnFocusChangeListener(focus);
        et_friends2.setOnFocusChangeListener(focus);
        et_friends3.setOnFocusChangeListener(focus);
    }


    public void initData(){
        et_friends1.setText(SPUtil.getString(Constants.name1, ""));
        et_friends2.setText(SPUtil.getString(Constants.name2, ""));
        et_friends3.setText(SPUtil.getString(Constants.name3, ""));
        tv_end_time.setText(SPUtil.getString(Constants.end_time, ""));
        tv_start_time.setText(SPUtil.getString(Constants.start_time, ""));
        rg.check(SPUtil.getBoolean(Constants.praise_all,false)?R.id.rb_all:R.id.rb_single);
        sw_praise.setChecked(SPUtil.getBoolean(Constants.sw_praise,false));
        sw_red_packet.setChecked(SPUtil.getBoolean(Constants.sw_red_packet,false));
    }


    public String getFriendsName(int location) {
        String name = "";
        switch (location) {
            case 1:
                name = et_friends1.getText().toString().trim();
                break;
            case 2:
                name = et_friends2.getText().toString().trim();
                break;
            case 3:
                name = et_friends3.getText().toString().trim();
                break;
        }
        return name;
    }


    public int getPraiseType() {
        return rb_single.isChecked()?PRAISE_TYPE_CONTACTS:PRAISE_TYPE_ALL;
    }

    public boolean getPraiseSwitchState() {
        return sw_praise.isChecked();
    }

    public boolean getRedPacketSwitchState() {
        return sw_red_packet.isChecked();
    }
}
