package org.adaschool.retrofit.auth;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JWTInterceptor implements Interceptor {

    private final SharedPreferences sharedPreferences;

    public JWTInterceptor(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String token = sharedPreferences.getString("token", null);
        Request.Builder builder = chain.request().newBuilder();
        if (token != null) {
            builder.addHeader("Authorization", "Bearer " + token);
        }
        return chain.proceed(builder.build());
    }
}
