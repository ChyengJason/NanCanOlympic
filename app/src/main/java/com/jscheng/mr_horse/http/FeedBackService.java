package com.jscheng.mr_horse.http;

import com.jscheng.mr_horse.utils.Constants;
import com.jscheng.mr_horse.utils.ToStringConverterFactory;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by cheng on 17-2-7.
 */
public class FeedBackService {
    private static FeedBackApi feedbackApi;
    private static ToStringConverterFactory stringConverterFactory =new ToStringConverterFactory();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static FeedBackApi getFeedBackApi(){
        if(feedbackApi==null){
            Retrofit retrofit = new Retrofit.Builder()
                    .client(genericClient())
                    .baseUrl(Constants.FEEDBACK_URL)
                    .addConverterFactory(stringConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();

            feedbackApi = retrofit.create(FeedBackApi.class);
        }
        return feedbackApi;
    }

    public static OkHttpClient genericClient() {
        final StringBuffer cookieBuffer = new StringBuffer();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Response originalResponse = chain.proceed(chain.request());
                        //这里获取请求返回的cookie
                        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                            cookieBuffer.setLength(0);
                            Observable.from(originalResponse.headers("Set-Cookie"))
                                    .map(new Func1<String, String>() {
                                        @Override
                                        public String call(String s) {
                                            String[] cookieArray = s.split(";");
                                            return cookieArray[0];
                                        }
                                    })
                                    .subscribe(new Action1<String>() {
                                        @Override
                                        public void call(String cookie) {
                                            cookieBuffer.append(cookie);
                                        }
                                    });
                            Logger.e("cookie",cookieBuffer.toString());
                        }
                        return originalResponse;
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request.Builder builder = chain.request().newBuilder();
                        builder .addHeader("user-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:45.0) Gecko/20100101 Firefox/45.0")
                                .addHeader("accept'","application/json, text/javascript, */*; q=0.01")
                                .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("HOST", Constants.FEEDBACK_HOST)
                                .addHeader("Origin", Constants.FEEDBACK_ORIGIN)
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("content-length","167")
                                .addHeader("Referer", Constants.FEEDBACK_REFERER)
                                .addHeader("X-Requested-With", "XMLHttpRequest")
                                .addHeader("cookie",cookieBuffer.toString())
                                .build();
                        return chain.proceed(builder.build());
                    }
                })
                .build();
        return httpClient;
    }
}

