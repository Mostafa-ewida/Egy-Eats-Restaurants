package com.itechpro.egyeatsrestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Model.EndUser;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClientBasicAuth;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiInterface;

import static com.itechpro.egyeatsrestaurant.Common.Common.androidId;
import static java.lang.Thread.sleep;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    ImageView logo, splash_logo;
    TextView made_by;
    float v = 0;

    SharedPreferences encPref;
    SharedPreferences.Editor encEditor;
    String masterKeyAlias;
    String username;
    String password;
    String name;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logo);
        splash_logo = findViewById(R.id.splash_logo);
        made_by = findViewById(R.id.made_by);

        logo.setTranslationY(-1600);
        splash_logo.setTranslationX(800);
        made_by.setTranslationY(1600);

        logo.setAlpha(v);
        splash_logo.setAlpha(v);
        made_by.setAlpha(v);

        logo.animate().translationY(0).alpha(1).setDuration(1000);
        splash_logo.animate().translationX(0).alpha(1).setDuration(1000);
        made_by.animate().translationY(0).alpha(1).setDuration(1000);

        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("Device ID: ", androidId);

        Common.setAppLocale(Locale.getDefault());
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            encPref = EncryptedSharedPreferences.create(
                    "EgyEatsPref",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(() -> {
            try {
                sleep(2000);
                if (Common.isConnectedToInternet(SplashScreen.this)) {
                    username = encPref.getString("username", null);
                    password = encPref.getString("password", null);
                    name = encPref.getString("name", null);
                    if (username != null) {
                        if (!username.equals("Guest")) {
                            Log.i("username recorded", username);
//                            Log.i("password", password);
                            apiInterface = ApiClientBasicAuth.createService(ApiInterface.class);

                            EndUser user = new EndUser(username, password, name);
                            apiInterface.signIn(user).enqueue(new Callback<EndUser>() {
                                @Override
                                public void onResponse(Call<EndUser> call, Response<EndUser> response) {
                                    if (response.isSuccessful()) {

                                        EndUser returnedUser = response.body();
                                        if (user.getUsername().equals(returnedUser.getUsername())) {
                                            Intent homeIntent = new Intent(SplashScreen.this, QRActivity.class);
                                            Common.currentUser = new EndUser(user.getUsername(), user.getPassword(), user.getDisplayName());
                                            Log.i("Common User after", Common.currentUser.toString());
                                            startActivity(homeIntent);
                                            finish();
                                        }
                                    } else {
                                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                                        startActivity(intent);
                                        Log.i("SignIn Response:", response.code() + "");
                                        finish();

                                    }

                                }

                                @Override
                                public void onFailure(Call<EndUser> call, Throwable t) {

                                }
                            });
                        } else {
                            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });
        thread.start();

    }
}