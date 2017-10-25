package cz.koci.hackathon.service;

import android.util.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by daniel on 25.10.17.
 */

public class RestClient {
    private static final String BASE_URL = "https://api.dropboxapi.com/2/";
    private static ApiInterface apiInterface;

    private static TokenInterceptor tokenInterceptor = new TokenInterceptor();
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    public static ApiInterface getIn() {
        if (apiInterface == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .addInterceptor(tokenInterceptor)
                    .retryOnConnectionFailure(true)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();


            apiInterface = retrofit.create(ApiInterface.class);

        }
        return apiInterface;
    }
}
