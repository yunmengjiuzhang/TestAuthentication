package com.reformer.authentication;

import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Routon.iDRHIDLib.iDRHIDDev;
import com.reformer.authentication.utils.CameraUtils;
import com.reformer.authentication.utils.DateUtils;
import com.reformer.authentication.utils.FaceUtils;
import com.reformer.authentication.utils.HIDUtils;
import com.reformer.authentication.utils.HttpUtils;
import com.reformer.authentication.utils.LogUtil;
import com.reformer.authentication.utils.ThreadUtils;
import com.reformer.authentication.utils.ToastUtils;
import com.reformer.authentication.utils.UIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private HIDUtils hid;
    private SurfaceView sv_face;
    private CameraUtils mCamera;
    private String idToken;
    private TextView tv_time;
    private TextView tv_month;
    private TextView tv_week;
    private ImageView iv_icon;
    private ImageView iv_text;
    //    private TextView tv_percent;
    private TextView tv_name;
    private TextView tv_male;
    private TextView tv_birthday;
    private TextView tv_add;
    private RelativeLayout rl_yanzheng;
    private RelativeLayout rl_paizhao;
    private RelativeLayout rl_card;
    private RelativeLayout rl_no_net;
    private iDRHIDDev.SecondIDInfo mIDInfo;
    protected long mEndTime;
    protected long mStartTime;
    private int testTime = 0;
    private int verifyStationNo = 1;//0设备激活,1设备未激活
    private int FACE_RANK = 2;//0,照片模糊;1:验证失败,2.低级别;3中级别,4;高级别
    private TextView tv_lifang;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);
        sv_face = (SurfaceView) findViewById(R.id.sv_face);
        rl_no_net = (RelativeLayout) findViewById(R.id.rl_no_net);
        rl_yanzheng = (RelativeLayout) findViewById(R.id.rl_yanzheng);
        rl_paizhao = (RelativeLayout) findViewById(R.id.rl_paizhao);
        rl_card = (RelativeLayout) findViewById(R.id.rl_card);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_text = (ImageView) findViewById(R.id.iv_text);
//        tv_percent = (TextView) findViewById(R.id.tv_percent);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_male = (TextView) findViewById(R.id.tv_male);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_lifang = (TextView) findViewById(R.id.tv_lifang);
//        tv_lifang.setDegrees(90);
//        tv_time.setDegrees(90);
//        tv_month.setDegrees(90);
//        tv_week.setDegrees(90);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://刷身份证
                case 1://拍照
                case 2://验证中
                case 3://验证成功
                case 4://验证失败
                case 5://网络错误
                    showPage(msg.what);
                    break;
                case 10://更新时间
                    mHandler.sendEmptyMessageDelayed(10, 1000);
                    tv_time.setText(DateUtils.getCurrentDate(DateUtils.dateFormatHM));
                    tv_month.setText(DateUtils.getCurrentDate(DateUtils.dateFormatMD));
                    tv_week.setText(DateUtils.getWeek());
                    break;
                case 11://激活设备
                    getAllowFromServer();
                    hid.startReadCard(500);
                    break;
            }
        }
    };

    private void showPage(final int a) {
        switch (a) {
            case 0://刷卡
                tv_lifang.setText("杭州立方控股股份有限公司");
                mStartTime = System.currentTimeMillis();
                hid.startReadCard(0);
                break;
            case 1://拍照不显示界面
//                hid.beep(500);
                mCamera.takePicture();//不显示拍照
                mHandler.sendEmptyMessage(2);//校验中
                return;
            case 2://校验中
                iv_icon.setBackground(UIUtils.getDrawable(R.mipmap.icon_yanzhengzhong));
                iv_text.setBackground(UIUtils.getDrawable(R.mipmap.zi_yanzhengzhong));
//                hid.beep(500);
                Animation anim = AnimationUtils.loadAnimation(UIUtils.getContext(), R.anim.rotate);//匀速旋转
                LinearInterpolator lir = new LinearInterpolator();
                anim.setInterpolator(lir);
                iv_icon.startAnimation(anim);
                break;
            case 3:
                iv_icon.clearAnimation();
                iv_icon.setBackground(UIUtils.getDrawable(R.mipmap.icon_yanzhengchenggong));
                iv_text.setBackground(UIUtils.getDrawable(R.mipmap.zi_yanzhengchenggong));
                hid.beep(500);
                mHandler.sendEmptyMessageDelayed(0, 500);//3s后跳转刷卡页面
                mEndTime = System.currentTimeMillis();
                LogUtil.d("校验成功_______" + (mEndTime - mStartTime));
                LogUtil.d("_________________testTime" + testTime++);
                break;
            case 4:
                iv_icon.clearAnimation();
                iv_icon.setBackground(UIUtils.getDrawable(R.mipmap.icon_yanzhengshibai));
                iv_text.setBackground(UIUtils.getDrawable(R.mipmap.zi_yanzhengshibai));
                hid.beep(500);
                mHandler.sendEmptyMessageDelayed(0, 500);//3s后跳转刷卡页面
                mEndTime = System.currentTimeMillis();
                LogUtil.d("校验失败_______" + (mEndTime - mStartTime));
                LogUtil.d("_________________testTime" + testTime++);
                break;
        }
        rl_no_net.setVisibility(a == 5 ? View.VISIBLE : View.INVISIBLE);//无网络
        rl_yanzheng.setVisibility(a == 2 || a == 3 || a == 4 ? View.VISIBLE : View.INVISIBLE);//验证
//        rl_paizhao.setVisibility(a == 1 ? View.VISIBLE : View.INVISIBLE);//拍照
        rl_card.setVisibility(a == 0 ? View.VISIBLE : View.INVISIBLE);//刷身份证
//        tv_percent.setVisibility(a == 3 || a == 4 ? View.VISIBLE : View.INVISIBLE);

    }

    private void setIDInfos(final iDRHIDDev.SecondIDInfo idInfo) {
        if (idInfo == null)
            return;
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_name.setText("姓名 : " + idInfo.name);
                tv_male.setText("性别 : " + idInfo.gender);
                String birthday = idInfo.birthday;
                tv_birthday.setText("出生 : " + birthday.substring(0, 4) + " 年 " + birthday.substring(4, 6) + " 月 " + birthday.substring(6) + " 日");
                tv_add.setText("地址 : " + idInfo.address);
            }
        });
    }

    @Override
    protected void initDatas() {
        getAllowFromServer();
        initHID();
        initCamera();
//        String androidId = DeviceUtis.getDeviceId(mCtx);//获取设备id
    }

    private void getAllowFromServer() {
        HttpUtils.getAllow(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtils.showToast("网络异常!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject1 = new JSONObject(response.body().string());
                    verifyStationNo = jsonObject1.optInt("result");
                    FACE_RANK = jsonObject1.optInt("rank") + 2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initHID() {
        if (hid != null) {
            hid.setOnUsbListener(null);
            hid = null;
        }
        hid = new HIDUtils(mCtx);
        hid.setOnUsbListener(new HIDUtils.OnUsbListener() {
            @Override
            public void usbState(final int state, final iDRHIDDev.SecondIDInfo sIDInfo) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (state) {
                            case 0:
                                if (verifyStationNo == 1) {
//                                    ToastUtils.showToast("该设备未激活!");
//                                    tv_lifang.setText("该设备未激活,尝试激活中...");
                                    mHandler.sendEmptyMessageDelayed(11, 1000);
                                    return;
                                }
                                setIDInfos(sIDInfo);
                                mIDInfo = sIDInfo;
                                mHandler.sendEmptyMessage(1);//拍照
                                break;
                            case 3://授权失败
//                                ToastUtils.showToast(mCtx, "USB授权失败");
//                                tv_lifang.setText("USB授权失败,尝试授权中...");
                                break;
                            case 4://未授权
//                                ToastUtils.showToast(mCtx, "USB未授权");
//                                tv_lifang.setText("USB未授权,尝试授权中...");
                                break;
                            case 11://请插入读卡器
//                                ToastUtils.showToast(mCtx, "未插入读卡器,尝试搜索中...");
//                                tv_lifang.setText("未插入读卡器,尝试连接中...");
                                break;
                            case 12://读卡器未准备好
//                                ToastUtils.showToast(mCtx, "读卡器未准备好,尝试搜索中...");
//                                tv_lifang.setText("读卡器未准备好,尝试连接中...");
                                break;
                            case 15://读过一次卡,请重新放啊
                            case 14://hid准备好了
                                mHandler.sendEmptyMessage(0);
                                break;
                        }
                    }
                });
            }
        });
    }

    private void initCamera() {
        mCamera = new CameraUtils(sv_face, mCtx);
        mCamera.setOnCameraListner(new CameraUtils.OnCameraListener() {
            @Override
            public void onPictrue(final byte[] datas) {
                mHandler.sendEmptyMessage(2);//对比验证中
                FaceUtils.comparea(mIDInfo.getIDCardBackPic(mCtx), datas, new FaceUtils.OnFaceResult() {
                    @Override
                    public void onResult(String message, final int confidence) {
                        int result = 2;
                        if (confidence == 0) {//数据错误
                            ToastUtils.showToast(message);
                            mHandler.sendEmptyMessage(0);
                            result = 2;
                        } else {//验证
                            if (confidence >= FACE_RANK)//验证成功//// TODO: 2017/5/22 0022
                                mHandler.sendEmptyMessage(3);
                            else//验证失败
                                mHandler.sendEmptyMessage(4);
                            result = 1;
                        }
                        HttpUtils.uploadface(mIDInfo, result);
                    }
                });
            }
        });
//        mCamera.setFaceListener(new Camera.FaceDetectionListener() {
//            @Override
//            public void onFaceDetection(Camera.Face[] faces, Camera camera) {
//                ToastUtils.showToast("人脸个数" + faces.length);
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        hid.stopReadCard();
        mHandler.removeMessages(10);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hid.startReadCard(0);
        mHandler.sendEmptyMessage(10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hid.close();
    }


}
