package com.itechpro.egyeatsrestaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Model.EndUser;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClientBasicAuth;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiInterface;

import java.io.IOException;
import java.security.GeneralSecurityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTabFragment extends Fragment {

    EditText login_mobile_number, login_password;
    TextView errorResponse;
    Button login_button, login_forget_password, continue_guest;
    float v = 0;
    ApiInterface apiInterface;
    SharedPreferences encPref;
    SharedPreferences.Editor encEditor;
    String masterKeyAlias;
    EndUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        login_mobile_number = root.findViewById(R.id.login_mobile_number);
        login_password = root.findViewById(R.id.login_password);
        login_forget_password = root.findViewById(R.id.login_forget_password);
        continue_guest = root.findViewById(R.id.continue_guest);
        errorResponse = root.findViewById(R.id.error_response);

        login_button = root.findViewById(R.id.login_button);

        login_mobile_number.setTranslationX(800);
        login_password.setTranslationX(800);
        login_forget_password.setTranslationX(800);
        continue_guest.setTranslationX(800);
        login_button.setTranslationX(800);

        login_mobile_number.setAlpha(v);
        login_password.setAlpha(v);
        login_forget_password.setAlpha(v);
        continue_guest.setAlpha(v);
        login_button.setAlpha(v);

        login_mobile_number.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_forget_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        continue_guest.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        login_button.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();


        //API Backend
        apiInterface = ApiClientBasicAuth.createService(ApiInterface.class);

        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            encPref = EncryptedSharedPreferences.create(
                    "EgyEatsPref",
                    masterKeyAlias,
                    getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        encEditor = encPref.edit();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(root.getContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(root.getContext());
                    mDialog.setMessage("Processing ...");
                    mDialog.show();

                    user = new EndUser(login_mobile_number.getText().toString(), login_password.getText().toString(), null);

                    apiInterface.signIn(user).enqueue(new Callback<EndUser>() {
                        @Override
                        public void onResponse(Call<EndUser> call, Response<EndUser> response) {
                            if (response.isSuccessful()) {
                                mDialog.cancel();
                                EndUser returnedUser = response.body();
                                if (user.getUsername().equals(returnedUser.getUsername())) {
                                    encEditor.putString("username", returnedUser.getUsername());
                                    encEditor.commit();
                                    encEditor.putString("password", user.getPassword());
                                    encEditor.commit();
                                    encEditor.putString("name", returnedUser.getDisplayName());
                                    encEditor.commit();
                                    Intent homeIntent = new Intent(getActivity(), QRActivity.class);
                                    Common.currentUser = new EndUser(returnedUser.getUsername(), user.getPassword(), returnedUser.getDisplayName());
                                    startActivity(homeIntent);
                                    getActivity().finish();
                                }
                            } else {
                                Toast.makeText(root.getContext(), "Please Try Again !", Toast.LENGTH_LONG).show();
                                Log.i("SignIn Response:", response.code() + "");
                                switch (response.code()) {
                                    case 401:
                                        errorResponse.setText("Invalid Username or Password !!");
                                        break;
                                    case 409:
                                        errorResponse.setText("Username already Exists !!");
                                        break;
                                    default:
                                        errorResponse.setText("Something went wrong .. Please try again Later !!");
                                }

                                mDialog.cancel();
                            }
                        }

                        @Override
                        public void onFailure(Call<EndUser> call, Throwable t) {
                            mDialog.cancel();
                        }
                    });

                }else{
                    Toast.makeText(root.getContext(), "Please check Your Connection !!", Toast.LENGTH_LONG).show();
                }
            }
        });

        login_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(root.getContext(), "feature will be available later !!", Toast.LENGTH_LONG).show();
            }
        });

        continue_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrActivity = new Intent(getContext(), QRActivity.class);
                Common.currentUser = new EndUser("", "Guest", "Guest");
                encEditor.putString("username", "Guest");
                encEditor.apply();

                startActivity(qrActivity);
            }
        });

        return root;

    }
}
