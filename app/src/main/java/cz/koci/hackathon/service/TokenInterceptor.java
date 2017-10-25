package cz.koci.hackathon.service;

import android.support.annotation.NonNull;

import cz.koci.hackathon.utils.PrefManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by daniel on 25.10.17.
 */
public class TokenInterceptor implements Interceptor {
    private static final String HEADER_AUTHORIZATION = "authorization";
    private static final String HEADER_BEARER = "Bearer %s";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        String token = PrefManager.getToken();
        if (token != null) {
            Request.Builder requestBuilder = original.newBuilder()
                    .header(HEADER_AUTHORIZATION, String.format(HEADER_BEARER, token));

            Request request = requestBuilder.build();
            return chain.proceed(request);
        }

        return chain.proceed(original);
    }
}