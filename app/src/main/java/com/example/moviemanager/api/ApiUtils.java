package com.example.moviemanager.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "897234d6f42a98253dbba30d2bd09f12";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500";

    private ApiUtils() {
    }

    private static OkHttpClient instance;

    public static OkHttpClient getInstance() {
        if (instance == null) {
            synchronized (ApiUtils.class) {
                instance = createClient();
            }
        }
        return instance;
    }

    private static OkHttpClient createClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        return new OkHttpClient.Builder()
                .followRedirects(false)
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();
    }

    public static <T> T buildApi(String url, Class<T> apiClass) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getInstance())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(apiClass);
    }
}
