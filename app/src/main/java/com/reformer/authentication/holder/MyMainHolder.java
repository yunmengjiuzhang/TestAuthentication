package com.reformer.authentication.holder;

import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Routon.iDRHIDLib.iDRHIDDev;
import com.reformer.authentication.R;
import com.reformer.authentication.utils.CameraUtils;
import com.reformer.authentication.utils.HIDUtils;
import com.reformer.authentication.utils.UIUtils;


/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class MyMainHolder extends BaseHolder {

    private HIDUtils hid;
    public SurfaceView sv_face;
    private CameraUtils mCamera;
    private String idToken;
    public TextView tv_time;
    public TextView tv_month;
    public TextView tv_week;
    public ImageView iv_icon;
    public ImageView iv_text;
    //    private TextView tv_percent;
    public TextView tv_name;
    public TextView tv_male;
    public TextView tv_birthday;
    public TextView tv_add;
    public RelativeLayout rl_yanzheng;
    private RelativeLayout rl_paizhao;
    public RelativeLayout rl_card;
    public RelativeLayout rl_no_net;
    private iDRHIDDev.SecondIDInfo mIDInfo;
    protected long mEndTime;
    protected long mStartTime;
    private int testTime = 0;
    private int verifyStationNo = 1;//0设备激活,1设备未激活
    private int FACE_RANK = 2;//0,照片模糊;1:验证失败,2.低级别;3中级别,4;高级别
    public TextView tv_lifang;

    public MyMainHolder(View.OnClickListener in) {
        super(in);
    }

    @Override
    protected View initView(View.OnClickListener innerOnclick) {
        View view = UIUtils.inflate(R.layout.activity_main);
        sv_face = (SurfaceView) view.findViewById(R.id.sv_face);
        rl_no_net = (RelativeLayout) view.findViewById(R.id.rl_no_net);
        rl_yanzheng = (RelativeLayout) view.findViewById(R.id.rl_yanzheng);
        rl_paizhao = (RelativeLayout) view.findViewById(R.id.rl_paizhao);
        rl_card = (RelativeLayout) view.findViewById(R.id.rl_card);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_text = (ImageView) view.findViewById(R.id.iv_text);
//        tv_percent = (TextView) findViewById(R.id.tv_percent);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_male = (TextView) view.findViewById(R.id.tv_male);
        tv_birthday = (TextView) view.findViewById(R.id.tv_birthday);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_lifang = (TextView) view.findViewById(R.id.tv_lifang);
        return view;
    }

    @Override
    protected void refreshView(Object o) {

    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_setting:
//                drawerLayout.openDrawer(Gravity.LEFT);
//                break;
//        }
//    }
}
