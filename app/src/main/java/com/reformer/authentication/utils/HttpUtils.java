package com.reformer.authentication.utils;

import com.Routon.iDRHIDLib.iDRHIDDev;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class HttpUtils {

    private static final String BASE_URL = "http://114.215.171.48:8090/caballero/reformer/httpstationserver/";
    private static final String DEVICE_ID = "123456789";//设备唯一id

    public static void getAllow(final Callback or) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("stationNo", DEVICE_ID)//设备id
                        .build();
                Request post_request = new Request.Builder()
                        .url(BASE_URL + "verifyStationNo")// 指定请求的地址
                        .post(body)// 指定请求的方式为POST
                        .build();
                client.newCall(post_request).enqueue(or);
            }
        });
    }

    public static void uploadface(final iDRHIDDev.SecondIDInfo mIDInfo, final int result) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                String birthday = mIDInfo.birthday;
//                String param = new Gson().toJson(new Record(DEVICE_ID, mIDInfo.name, mIDInfo.id, mIDInfo.gender, mIDInfo.folk, birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6), mIDInfo.address, DateUtils.getCurrentDate(DateUtils.dateFormatYMDHMS), String.valueOf(result)));
//                String param = new Record().getJsonStr(DEVICE_ID, mIDInfo.name, mIDInfo.id, mIDInfo.gender, mIDInfo.folk, birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6), mIDInfo.address, DateUtils.getCurrentDate(DateUtils.dateFormatYMDHMS), String.valueOf(result));
                String param = MyJsonUtils.getJsonStr(DEVICE_ID, mIDInfo.name, mIDInfo.id, mIDInfo.gender, mIDInfo.folk, birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6), mIDInfo.address, DateUtils.getCurrentDate(DateUtils.dateFormatYMDHMS), String.valueOf(result));
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("param", param)//验证结果 1：成功 ，2：失败
                        .build();
                Request post_request = new Request.Builder()
                        .url(BASE_URL + "registra")// 指定请求的地址
                        .post(body)// 指定请求的方式为POST
                        .build();
                client.newCall(post_request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        try {
                            JSONObject jsonObject1 = new JSONObject(response.body().string());
                            int result = jsonObject1.optInt("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


}
