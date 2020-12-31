package com.itechpro.egyeatsrestaurant.Retrofit;

import android.text.TextUtils;

import com.itechpro.egyeatsrestaurant.Common.ServerGlobals;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClientBasicAuth {

    public static final String API_BASE_URL = ServerGlobals.BASE_URL+"api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

    private static class AuthenticationInterceptor implements Interceptor {
        private String authToken;
        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Request original = chain.request();
            okhttp3.Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);
            okhttp3.Request request = builder.build();
            return chain.proceed(request);
        }
    }
}