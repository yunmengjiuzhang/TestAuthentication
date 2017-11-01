package com.reformer.authentication.utils;

import android.graphics.Bitmap;

import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class FaceUtils {
    private static final String key = "2ARRAuhtFHM9ClVIIoR2QbWixUUfTos8";//api_key
    private static final String secret = "Y9C22tpPD0zaBMZquvEZc0RP4N4-xcpv";//api_secret
    private static final String URL = "https://api-cn.faceplusplus.com/facepp/v3/compare";

    public static String getFaceToken(final Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return getFaceToken(baos.toByteArray());
    }

    public synchronized static String getFaceToken(byte[] datas) {
        LogUtil.d("__________________________________________");
        String s = null;
        try {
            CommonOperate commonOperate = new CommonOperate(key, secret, false);
            Response response = commonOperate.detectByte(datas, 0, null);
            if (response.getStatus() != 200) {
                return new String(response.getContent());
            }
            JSONObject json = new JSONObject(new String(response.getContent()));
            s = json.optJSONArray("faces").optJSONObject(0).optString("face_token");
            LogUtil.d("__________________________________________" + s);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void comparea(final String token1, final String token2, final Callback or) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("api_key", key)// 构造请求的参数
                        .add("api_secret", secret)// 构造请求的参数
                        .add("face_token1", token1)// 构造请求的参数
                        .add("face_token2", token2)// 构造请求的参数
                        .build();
                Request post_request = new Request.Builder()
                        .url(URL)// 指定请求的地址
                        .post(body)// 指定请求的方式为POST
                        .build();
                client.newCall(post_request).enqueue(or);
            }
        });
    }

    public static void comparea(final Bitmap bitmap, final byte[] base642, final Callback or) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("___________________________");
                final String s = Base64Utils.bitmapToBase64(bitmap);
                Bitmap bitmap1 = BitmapUtils.bytes2Bimap(base642);
                LogUtil.d("___________________________");
                final String s1 = Base64Utils.bitmapToBase64(bitmap1);
                LogUtil.d("___________________________");
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("api_key", key)// 构造请求的参数
                        .add("api_secret", secret)// 构造请求的参数
                        .add("image_base64_1", s)// 构造请求的参数
                        .add("image_base64_2", s1)// 构造请求的参数
                        .build();
                Request post_request = new Request.Builder()
                        .url(URL)// 指定请求的地址
                        .post(body)// 指定请求的方式为POST
                        .build();
                client.newCall(post_request).enqueue(or);
            }
        });
    }

    public static void comparea(final Bitmap bitmap, final byte[] base642, final OnFaceResult onFaceResult) {
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                final String s = Base64Utils.bitmapToBase64(bitmap);
                Bitmap bitmap1 = BitmapUtils.bytes2Bimap(base642);
                final String s1 = Base64Utils.bitmapToBase64(bitmap1);
                bitmap.recycle();
                bitmap1.recycle();
                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("api_key", key)// 构造请求的参数
                        .add("api_secret", secret)// 构造请求的参数
                        .add("image_base64_1", s)// 构造请求的参数
                        .add("image_base64_2", s1)// 构造请求的参数
                        .build();
                Request post_request = new Request.Builder()
                        .url(URL)// 指定请求的地址
                        .post(body)// 指定请求的方式为POST
                        .build();
                client.newCall(post_request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        onFaceResult.onResult("网络不给力,请重新刷卡!", 0);
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        String string = response.body().string();
                        LogUtil.d(string);
                        if (!string.contains("confidence") && !string.contains("thresholds")) {
                            if (string.contains("INTERNAL_ERROR")) {
                                onFaceResult.onResult("服务器错误,请及时联系技术人员!", 0);
                            } else if (string.contains("CONCURRENCY_LIMIT_EXCEEDED")) {
                                onFaceResult.onResult("压力太大,请有序进行验证!", 0);
                            } else {
                                onFaceResult.onResult("人脸模糊,请重新刷卡!", 0);
                            }
                            return;
                        }
                        try {
                            JSONObject jsonObject1 = new JSONObject(string);
                            double confidence = jsonObject1.optDouble("confidence");
                            JSONObject thresholds1 = jsonObject1.optJSONObject("thresholds");
                            double s3 = thresholds1.optDouble("1e-3");
                            double s5 = thresholds1.optDouble("1e-5");
                            double s4 = thresholds1.optDouble("1e-4");
                            LogUtil.d("验证结果:__confidence__" + confidence + "__s3__" + s3 + "__s4__" + s4 + "__s5__" + s5);
                            onFaceResult.onResult("", calculate(confidence, s3, s4, s5));//计算相似度
                        } catch (JSONException e) {
                            onFaceResult.onResult("照片不给力哦!请重新刷卡!", 0);
                        }
                    }
                });
            }
        });
    }

    //计算百分比
    private static int calculate(Double confidence, Double s3, Double s4, Double s5) {
        if (confidence <= s3) {//失败
            return 1;
        } else if (s3 < confidence && confidence <= s4) {//可疑
            return 2;
        } else if (s4 < confidence && confidence <= s5) {//可疑
            return 3;
        } else {//成功
            return 4;
        }
    }

    public interface OnFaceResult {
        void onResult(String message, int confidence);
    }

    public void myHttp(String url, String param1, String param2, Callback or) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("api_key", param1)// 构造请求的参数
                .add("api_secret", param2)// 构造请求的参数
                .build();
        Request post_request = new Request.Builder()
                .url(url)// 指定请求的地址
                .post(body)// 指定请求的方式为POST
                .build();
        client.newCall(post_request).enqueue(or);
    }



}
