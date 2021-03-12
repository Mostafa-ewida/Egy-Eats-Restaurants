package com.itechpro.egyeatsrestaurant;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupTabFragment extends Fragment {

    EditText signup_mobile_number, signup_password, signup_password_confirm, signup_name;
    TextView errorResponse, error_password;
    Button signup_button, signup_guest;
    ApiInterface apiInterface;
    SharedPreferences encPref;
    SharedPreferences.Editor encEditor;
    String masterKeyAlias;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        signup_mobile_number = root.findViewById(R.id.signup_mobile_number);
        signup_name = root.findViewById(R.id.signup_name);
        signup_password = root.findViewById(R.id.signup_password);
        signup_password_confirm = root.findViewById(R.id.signup_password_confirm);
        signup_guest = root.findViewById(R.id.signup_guest);

        errorResponse = root.findViewById(R.id.error_response);
        error_password = root.findViewById(R.id.error_password);

        signup_button = root.findViewById(R.id.signup_button);

        signup_mobile_number.setTranslationX(800);
        signup_password.setTranslationX(800);
        signup_password_confirm.setTranslationX(800);
        signup_guest.setTranslationX(800);

        signup_mobile_number.setAlpha(v);
        signup_password.setAlpha(v);
        signup_password_confirm.setAlpha(v);
        signup_guest.setAlpha(v);

        signup_mobile_number.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        signup_password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup_password_confirm.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup_guest.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

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

        //Api Backend

        apiInterface = ApiClientBasicAuth.createService(ApiInterface.class);


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getContext())) {
                    if (isValidMobileNumber(signup_mobile_number.getText().toString())) {
                        if (isValidPassword(signup_password.getText().toString())) {
                            if (signup_password.getText().toString().equals(signup_password_confirm.getText().toString())) {
                                signUpAPI();
                            } else
                                Toast.makeText(getContext(), "Password Not matching", Toast.LENGTH_SHORT).show();
                        } else {
                            error_password.setVisibility(View.VISIBLE);
                        }
                    }else {
                        Toast.makeText(getContext(), "Please enter valid mobile number", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please check Your Connection !!", Toast.LENGTH_LONG).show();
                }
            }
        });


        signup_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent qrActivity = new Intent(getContext(), QRActivity.class);
                Common.currentUser = new EndUser("", "Guest", "Guest");
                encEditor.putString("username", "Guest");
                encEditor.apply();

                startActivity(qrActivity);
//                finish();
            }
        });

        return root;
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        // Regex to check valid password.
        String regex = "^01(?=.*[0-9]).{9}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);


        // If the mobile number is empty
        // return false
        if (mobileNumber == null) {
            return false;
        }


        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(mobileNumber);


        // Return if the password
        // matched the ReGex
        return m.matches();
    }

    private boolean isValidPassword(String password) {
        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }

    private void signUpAPI() {
        EndUser endUser = new EndUser(signup_mobile_number.getText().toString(), signup_password.getText().toString(), signup_name.getText().toString());
        apiInterface.signUp(endUser).enqueue(new Callback<EndUser>() {
            @Override
            public void onResponse(Call<EndUser> call, Response<EndUser> response) {
                if (response.isSuccessful()) {
                    Log.i("Sign UP result:", response.body().toString());
                    encEditor.putString("username", endUser.getUsername());
                    encEditor.commit();
                    encEditor.putString("password", endUser.getPassword());
                    encEditor.commit();
                    encEditor.putString("name", endUser.getDisplayName());
                    encEditor.commit();
                    Intent homeIntent = new Intent(getContext(), MainActivity.class);
                    Common.currentUser = new EndUser(endUser.getUsername(), endUser.getDisplayName(), endUser.getPassword());
                    startActivity(homeIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Please Try Again !", Toast.LENGTH_LONG).show();
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
                }

            }

            @Override
            public void onFailure(Call<EndUser> call, Throwable t) {

            }
        });
    }
}
