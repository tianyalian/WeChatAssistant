package business_android_client.wechatassistant.presenter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import business_android_client.wechatassistant.R;

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


    public void initListener(View.OnClickListener listener){
        test_all_praise.setOnClickListener(listener);
        test_contacts_praise.setOnClickListener(listener);
        tv_start_time.setOnClickListener(listener);
        tv_end_time.setOnClickListener(listener);
    }


    public void initData(){

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
