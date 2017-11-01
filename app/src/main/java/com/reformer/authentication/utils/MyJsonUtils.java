package com.reformer.authentication.utils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class MyJsonUtils {

    public static String getJsonStr(String stationNo, String name, String cert_id, String sex, String nation, String birthday, String address, String register_time, String validation_results) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stationNo", stationNo);
            jsonObject.put("name", name);
            jsonObject.put("cert_id", cert_id);
            jsonObject.put("sex", sex);
            jsonObject.put("nation", nation);
            jsonObject.put("birthday", birthday);
            jsonObject.put("address", address);
            jsonObject.put("register_time", register_time);
            jsonObject.put("validation_results", validation_results);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


//    public String getBookDetail() {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("bookId", bookInfo.getBookId());
//            json.put("bookName", bookInfo.getBookName());
//            json.put("description", bookInfo.getDescription());
//
//            JSONArray array = new JSONArray();
//            for (ChapterInfo info : bookInfo.getChapterList()) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("chapterId", info.getChapterId());
//                jsonObject.put("chapterName", info.getChapterName());
//                jsonObject.put("status", info.getDownloadState());
//                array.put(jsonObject);
//            }
//            json.put("chapters", array.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return json.toString();
//    }

}
