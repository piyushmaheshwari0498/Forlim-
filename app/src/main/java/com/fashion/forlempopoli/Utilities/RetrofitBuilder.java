package com.fashion.forlempopoli.Utilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static final String BASE_URL = "http://forlimpopoli.in/beta_2mobileapi/";
    public static final String BASE_URL2 = "http://192.168.1.15/beta_2mobileapi_new/";
    private static Retrofit retrofit;
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit2;
    private static OkHttpClient okHttpClient2;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null && okHttpClient == null) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            okHttpClient = new OkHttpClient.Builder()
                     .readTimeout(Defines.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(Defines.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
//                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static Retrofit localgetRetrofitInstance() {

        if (retrofit2 == null && okHttpClient2 == null) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            okHttpClient2 = new OkHttpClient.Builder()
                    .readTimeout(Defines.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                    .connectTimeout(Defines.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
//                    .addNetworkInterceptor(new StethoInterceptor())
                    .addInterceptor(httpLoggingInterceptor)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit2;
    }
}


