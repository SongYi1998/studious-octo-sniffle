package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.18 17:53
 */

// response
// {
//    "result": {},
//    "url": "https://lf1-hscdn-tos.pstatp
//    .com/obj/developer-baas/baas/tt7217xbo2wz3cem41/a8efa55c5c22de69_1560563154288.mp4",
//    "success": true
//}

public class PostVideoResponse {

    // TODO-C2 (3) Implement your PostVideoResponse Bean here according to the response json
    @SerializedName("success")
    private boolean success;
    @SerializedName("url")
    private String url;

    public boolean isSuccess() {
        return success;
    }

    public String  getUrl() {
        return url;
    }
}
