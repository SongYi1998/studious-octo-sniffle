package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Xavier.S
 * @date 2019.01.20 14:17
 */
// response
// {
//    "feeds":[
//        {
//            "student_id":"2220186666",
//            "user_name":"doudou",
//            "image_url":"https://sf6-hscdn-tos.pstatp
//            .com/obj/developer-baas/baas/tt7217xbo2wz3cem41/9c6bbc2aa5355504_1560563154279
//            .jpg",
//            "_id":"5d044dd222e26f0024157401",
//            "video_url":"https://lf1-hscdn-tos.pstatp
//            .com/obj/developer-baas/baas/tt7217xbo2wz3cem41/a8efa55c5c22de69_1560563154288
//            .mp4",
//            "createdAt":"2019-06-15T01:45:54.368Z",
//            "updatedAt":"2019-06-15T01:45:54.368Z",
//        }
//        ...
//    ],
//    "success":true
//}
public class FeedResponse {
    // TODO-C2 (2) Implement your FeedResponse Bean here according to the response json
    @SerializedName("success")
    private boolean success;
    @SerializedName("feeds")
    private List<Feed> feeds;

    public boolean isSuccess() {
        return success;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }
}
