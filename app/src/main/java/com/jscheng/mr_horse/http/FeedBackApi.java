package com.jscheng.mr_horse.http;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cheng on 17-2-7.
 */
public interface FeedBackApi {
    @FormUrlEncoded
    @POST("addfeedback")
    Observable<String> addFeedBack(@Field("contact") String contact, @Field("content") String content,
                                   @Field("app_key") String app_key, @Field("app_name") String app_name,
                                   @Field("app_version") String app_version, @Field("version_code") String version_code);
}
