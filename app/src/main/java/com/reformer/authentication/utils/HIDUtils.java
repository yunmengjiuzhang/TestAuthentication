package com.reformer.authentication.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;

import com.Routon.iDRHIDLib.iDRHIDDev;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class HIDUtils {
    private static final String ACTION_USB_PERMISSION = "com.Routon.HIDTest.USB_PERMISSION";
    private final UsbManager mUsbManager;
    private UsbDevice mDevice;
    private iDRHIDDev mHIDDev;
    private OnUsbListener mUsbListener;
    private Context mCtx;

    public HIDUtils(Context ctx) {
        mCtx = ctx;
        mUsbManager = (UsbManager) ctx.getSystemService(Context.USB_SERVICE);
        mHIDDev = new iDRHIDDev();
    }

    public void setOnUsbListener(OnUsbListener mul) {// listen for new devices
        mUsbListener = mul;
        //注册usb监听
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mCtx.registerReceiver(mUsbReceiver, filter);
        checkUsb();
    }

    private void checkUsb() {
        // 检查USB设备是否插入
        for (UsbDevice device : mUsbManager.getDeviceList().values()) {
            if (device.getVendorId() == 1061 && device.getProductId() == 33113) {//发现读卡设备
                Intent intent = new Intent(ACTION_USB_PERMISSION);
                PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mCtx, 0, intent, 0);
                mUsbManager.requestPermission(device, mPermissionIntent);//请求权限
                return;
            }
        }
        mHandler.sendEmptyMessageDelayed(2, 1000);
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (mDevice != null) {
                        mHIDDev.closeDevice();
                        mDevice = null;
                    }
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            // call method to set up device communication
                            final int ret = mHIDDev.openDevice(mUsbManager, device);
                            if (ret == 0) {//已授权
                                mDevice = device;
                                startReadCard(0);
                            } else {//授权失败
                                mDevice = null;
                                if (mUsbListener != null)
                                    mUsbListener.usbState(3, null);
                            }
                        }
                    } else {//未授权
                        if (mUsbListener != null)
                            mUsbListener.usbState(4, null);
                    }
                }
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    readIDCard();
                    break;
                case 2:
                    checkUsb();
                    break;
            }
        }
    };

    public interface OnUsbListener {
        public void usbState(int state, iDRHIDDev.SecondIDInfo sIDInfo);
    }

    public void startReadCard(int time) {
        mHandler.removeMessages(1);
        mHandler.sendEmptyMessageDelayed(1, time + 500);
    }

    public void stopReadCard() {
        mHandler.removeMessages(1);
    }

    public void close() {
        stopReadCard();
        mHIDDev.closeDevice();
    }

    private int ret;
    private iDRHIDDev.SecondIDInfo sIDInfo;
    private iDRHIDDev.MoreAddrInfo mAddr;

    private void readIDCard() {
        LogUtil.d("读卡!");
        try {
            if (mDevice == null) {
                if (mUsbListener != null)
                    mUsbListener.usbState(11, null);//请插入读卡器
                mHandler.sendEmptyMessageDelayed(2, 1000);
                return;
            }
            ret = mHIDDev.GetSamStaus();//读安全模块的状态
            if (ret < 0) {
                if (mUsbListener != null)
                    mUsbListener.usbState(12, null);//读卡器未准备好
                mHandler.removeMessages(1);
                mHandler.sendEmptyMessageDelayed(2, 1000);
                return;
            }
//        iDRHIDDev.SamIDInfo samIDInfo = mHIDDev.new SamIDInfo();
//        ret = mHIDDev.GetSamId(samIDInfo);//读安全模块号
            if (mHIDDev.Authenticate() >= 0) {// 找到卡
                //读卡
                sIDInfo = mHIDDev.new SecondIDInfo();
                //            byte[] fingerPrint = new byte[1024];
                //            ret = mHIDDev.ReadBaseFPMsg(sIDInfo, fingerPrint);
                if (mHIDDev.ReadBaseMsg(sIDInfo) < 0) {//读卡失败
                    if (mUsbListener != null)
                        mUsbListener.usbState(13, null);
                    startReadCard(500);
                    return;
                }
                if (mUsbListener != null)
                    mUsbListener.usbState(0, sIDInfo);
                beep(100);
                stopReadCard();
            } else {// 未找到卡
                mAddr = mHIDDev.new MoreAddrInfo();
                // 通过读追加地址来判断卡是否在机具上。
                ret = mHIDDev.GetNewAppMsg(mAddr);
                if (ret < 0) { // 机具上没有放卡  请放卡
                    if (mUsbListener != null)
                        mUsbListener.usbState(14, null);
                } else {// 机具上的卡已读过一次  请重新放卡
                    if (mUsbListener != null)
                        mUsbListener.usbState(15, null);
                }
                startReadCard(500);
            }
//        // 读卡号， 注意不要放在读身份证信息前面，否则会读身份证信息失败
//          byte[] data = new byte[32];
//        int www = mHIDDev.getIDCardCID(data);
//        if (www < 0) {//读卡号失败
//            if (mUsbListener != null)
//                mUsbListener.usbState(16);
//        } else {
//            if (mUsbListener != null)
//                mUsbListener.usbState(17);
//        }
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.d("这个异常很无聊!");
            startReadCard(500);
        }
    }

    public int beep(int time) {
        return mHIDDev.BeepLed(true, true, time);
    }
}
