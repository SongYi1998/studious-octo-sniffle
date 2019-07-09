package com.bytedance.android.lesson.restapi.solution.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author Xavier.S
 * @date 2019.01.17 18:08
 */
public class Cat {
    // TODO-C1 (1) Implement your Cat Bean here according to the response json
    //[{"breeds":[],"id":"b0b","url":"https://cdn2.thecatapi.com/images/b0b.jpg","width":1000,"height":667}]
    @SerializedName("url")
    private String url;
    @SerializedName("width")
    private int width;
    @SerializedName("height")
    private int height;

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "url: " + url + width + "*" + height;
    }
}
