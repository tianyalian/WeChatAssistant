package business_android_client.wechatassistant.presenter;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import business_android_client.wechatassistant.AlertReceiver;
import business_android_client.wechatassistant.R;
import business_android_client.wechatassistant.WeChatService;
import business_android_client.wechatassistant.utils.Constants;
import business_android_client.wechatassistant.utils.SPUtil;

import static business_android_client.wechatassistant.presenter.BasePresenter.openWechat;

/**
 * Created by seeker on 2017/5/15.
 */

public class MainActivityPresenter {
    public static final int PRAISE_TYPE_ALL = 0;//朋友圈所有朋友
    public static final int PRAISE_TYPE_CONTACTS = 1;//通讯录指定朋友
    public static Context ctx;
    private TextView tv_start_time, tv_end_time;
    public EditText et_friends1, et_friends2, et_friends3;
    private RadioButton rb_all, rb_single;
    private RadioGroup rg;
    private Switch sw_praise, sw_red_packet;
    private Button test_all_praise, test_contacts_praise;
    private TimePickerDialog timePickerDialog;
    private ImageView iv_del;
    public boolean isTime1;
    private static PendingIntent sender1;
    private static PendingIntent sender2;


    public MainActivityPresenter(Context ctx) {
        this.ctx = ctx;
    }


    public void initView(View v) {
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
        iv_del = (ImageView) v.findViewById(R.id.imageView_del);
        tv_start_time = (TextView) v.findViewById(R.id.tv_start_time);
    }


    public void initListener(View.OnClickListener listener,
                             TextWatcher watcher,
                             RadioGroup.OnCheckedChangeListener rg_checkchangelistener,
                             CompoundButton.OnCheckedChangeListener cb_checkchangelistener) {
        test_all_praise.setOnClickListener(listener);
        test_contacts_praise.setOnClickListener(listener);
        tv_start_time.setOnClickListener(listener);
        iv_del.setOnClickListener(listener);
        tv_end_time.setOnClickListener(listener);
        sw_praise.setOnCheckedChangeListener(cb_checkchangelistener);
        sw_red_packet.setOnCheckedChangeListener(cb_checkchangelistener);
        rg.setOnCheckedChangeListener(rg_checkchangelistener);
        et_friends3.addTextChangedListener(watcher);
        et_friends2.addTextChangedListener(watcher);
        et_friends1.addTextChangedListener(watcher);
    }


    public void initData() {
        et_friends1.setText(SPUtil.getString(Constants.name1, ""));
        et_friends2.setText(SPUtil.getString(Constants.name2, ""));
        et_friends3.setText(SPUtil.getString(Constants.name3, ""));
        String time1 = SPUtil.getString(Constants.end_time, "");
        if (!TextUtils.isEmpty(time1)) {
            tv_end_time.setText(time1);
            iv_del.setVisibility(View.VISIBLE);
            getMillonTime(time1);
        }


        String time2 = SPUtil.getString(Constants.start_time, "");
        if (!TextUtils.isEmpty(time2)) {
            tv_start_time.setText(time2);
        }
        rg.check(SPUtil.getBoolean(Constants.praise_all, false) ? R.id.rb_all : R.id.rb_single);
        sw_praise.setChecked(SPUtil.getBoolean(Constants.sw_praise, false));
        sw_red_packet.setChecked(SPUtil.getBoolean(Constants.sw_red_packet, false));
    }

    public static long getMillonTime(String time) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String format_time = formatter.format(c.getTime());
        format_time = format_time + " " + time;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = formatter.parse(format_time);
            c.setTime(date);
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
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


    /**
     * 获取点赞类型:通讯录指定朋友  或  朋友圈所有朋友
     *
     * @return
     */
    public int getPraiseType() {
        return rb_single.isChecked() ? PRAISE_TYPE_CONTACTS : PRAISE_TYPE_ALL;
    }

    /**
     * 获取点赞开关状态
     *
     * @return
     */
    public boolean getPraiseSwitchState() {
        return sw_praise.isChecked();
    }

    /**
     * 获取红包开关状态
     *
     * @return
     */
    public boolean getRedPacketSwitchState() {
        return sw_red_packet.isChecked();
    }

    /**
     * 显示时间的对话框
     *
     * @param istime1
     */
    public void showTimeDialog(boolean istime1) {
        isTime1 = istime1;
        if (timePickerDialog == null) {
            timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (isTime1) {
                        String time1 = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute + "");
                        tv_start_time.setText(time1);
                        SPUtil.put(ctx, Constants.start_time, time1);
                    } else {
                        String time2 = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute + "");
                        tv_end_time.setText(time2);
                        iv_del.setVisibility(View.VISIBLE);
                        SPUtil.put(ctx, Constants.end_time, time2);
                    }
                    if (getPraiseSwitchState()) {
                        openPraise();
                    }
                }
            }, 8, 0, false);
        }
        timePickerDialog.show();
    }

    /**
     * 网络连接检查
     *
     * @return
     */
    public boolean checkIntentConnection() {
        ConnectivityManager manager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = manager.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        Toast.makeText(ctx, "请打开网络连接", Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * 朋友圈点赞测试
     */
    public void testPraiseAll() {
        ShowHeartsPresenter.count = Constants.pageTurningTime;//朋友圈点赞翻页初始化
        WeChatService.isRefreshName=true;//获取名单
        ShowHeartsPresenter.isClickedPraise = false;
        sendNotify(Constants.uri_allPraise);
        if (prePareTest()) {
            openWechat();
        }
    }

    /**
     * 联系人点赞测试
     */
    public void testPraiseContact() {
        sendNotify(Constants.uri_contacts);
        WeChatService.isRefreshName=true;//获取名单
        ShowHeartsPresenter.isClickedPraise = false;
        if (prePareTest()) {
//            for (int i=1;i<4;i++) {
//                getFriendsName(i);
//            }
            openWechat();
        }
    }

    public void sendNotify(String action) {
        ctx.getContentResolver().notifyChange(Uri.parse(action),null);
    }

    /**
     * 检查当前的AccessibleService是否开启
     *
     * @return true 开启
     */
    public boolean checkServiceState() {
        AccessibilityManager am = (AccessibilityManager) ctx.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceList = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (AccessibilityServiceInfo info : serviceList) {
            if (info.getId().contains(Constants.myPackageName)) {
                return true;
            }
        }
        showTipsDialog();
        return false;
    }

    /**
     * 显示提示对话框
     */
    public void showTipsDialog() {
        new AlertDialog.Builder(ctx)
                .setTitle("提示信息")
                .setMessage("请到设置中心--辅助功能--无障碍--微信助手--打开设置。")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        intent.setData(Uri.parse("package:" + ctx.getPackageName()));
        ctx.startActivity(intent);
    }

    public boolean prePareTest() {
        ShowHeartsPresenter.isDebug=true;
        WeChatService.isFirst=true;
        ShowHeartsPresenter.isFirst = true;
        if (checkIntentConnection() && checkServiceState()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开点赞功能
     */
    public static void openPraise() {
        String time1 =  SPUtil.getString(Constants.start_time, "");
        String time2 =  SPUtil.getString(Constants.end_time, "");
        creatAleram(time1, time2);
    }

    /**
     * 创建闹钟,最多写两个时间
     * @param times
     */
    public static void  creatAleram(String...times){
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        long aday = 24 * 60 * 60 * 1000;
        for (int i = 0; i < times.length; i++) {
            if (!TextUtils.isEmpty(times[i])) {
                long longtime = getMillonTime(times[i]);
                if (longtime < System.currentTimeMillis()) {
                    longtime+= aday;
                }
                getPenddingIntent();
                am.setExact(AlarmManager.RTC_WAKEUP,longtime,i==0?sender1:sender2);
            }
        }

    }

    public void closePraise(boolean single) {
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (sender1 != null) {
            if (single) {
                am.cancel(sender2);
            } else {
                am.cancel(sender2);
                am.cancel(sender1);
            }
        }
    }

    public static void getPenddingIntent() {
        if (sender1 == null) {
            Intent intent = new Intent(ctx, AlertReceiver.class);
            sender1 = PendingIntent.getBroadcast(ctx, 888, intent, 0);
            sender2 = PendingIntent.getBroadcast(ctx, 666, intent, 0);
        }
    }

    public void delTime(){
        if (!tv_end_time.getText().toString().contains("时间")) {
            SPUtil.put(ctx, Constants.end_time, "");
            tv_end_time.setText("时间② ▼");
            iv_del.setVisibility(View.GONE);
            closePraise(true);
        }
    }

}
