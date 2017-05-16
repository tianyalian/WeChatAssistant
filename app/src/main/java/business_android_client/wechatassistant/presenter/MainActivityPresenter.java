package business_android_client.wechatassistant.presenter;

import android.app.TimePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

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
    private TextView tv_start_time,tv_end_time;
    public EditText et_friends1,et_friends2,et_friends3;
    private RadioButton rb_all,rb_single;
    private RadioGroup rg;
    private Switch sw_praise,sw_red_packet;
    private Button test_all_praise,test_contacts_praise;
    private TimePickerDialog timePickerDialog;
    public boolean isTime1;

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
                             TextWatcher watcher,
                             RadioGroup.OnCheckedChangeListener rg_checkchangelistener,
                             CompoundButton.OnCheckedChangeListener cb_checkchangelistener){
        test_all_praise.setOnClickListener(listener);
        test_contacts_praise.setOnClickListener(listener);
        tv_start_time.setOnClickListener(listener);
        tv_end_time.setOnClickListener(listener);
        sw_praise.setOnCheckedChangeListener(cb_checkchangelistener);
        sw_red_packet.setOnCheckedChangeListener(cb_checkchangelistener);
        rg.setOnCheckedChangeListener(rg_checkchangelistener);
        et_friends3.addTextChangedListener(watcher);
        et_friends2.addTextChangedListener(watcher);
        et_friends1.addTextChangedListener(watcher);
    }


    public void initData(){
        et_friends1.setText(SPUtil.getString(Constants.name1, ""));
        et_friends2.setText(SPUtil.getString(Constants.name2, ""));
        et_friends3.setText(SPUtil.getString(Constants.name3, ""));
        String time1 = SPUtil.getString(Constants.end_time, "");
        if (!TextUtils.isEmpty(time1)) {
           tv_end_time.setText(time1);
        }
        String time2 = SPUtil.getString(Constants.start_time, "");
        if (!TextUtils.isEmpty(time2)) {
            tv_start_time.setText(time2);
        }
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

    public void showTimeDialog(boolean istime1){
        isTime1 = istime1;
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (isTime1) {
                        String time1 = hourOfDay + ":" + (minute < 10 ? "0"+minute: minute+"");
                        tv_start_time.setText(time1);
                        SPUtil.put(ctx,Constants.start_time, time1);
                    } else {
                        String time2 = hourOfDay + ":" + (minute < 10 ? "0"+minute: minute+"");
                        tv_end_time.setText(time2);
                        SPUtil.put(ctx,Constants.end_time,time2);
                    }
                }
            }, 8, 0, false);
        }
        timePickerDialog.show();
    }

}
