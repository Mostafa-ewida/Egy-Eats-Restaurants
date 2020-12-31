package com.itechpro.egyeatsrestaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.itechpro.egyeatsrestaurant.Common.Common;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ProfileTabFragment extends Fragment {

    TextView user_name, user_phone;
    Button sign_out, sign_in;
    SharedPreferences encPref;
    SharedPreferences.Editor encEditor;
    String masterKeyAlias;
    String username, password, name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_tab_fragment, container, false);

        user_name = root.findViewById(R.id.user_name);
        user_phone = root.findViewById(R.id.user_phone);
        sign_out = root.findViewById(R.id.sign_out);
        sign_in = root.findViewById(R.id.sign_in);

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
        username = encPref.getString("username", null);
//        password = encPref.getString("password", null);
        name = encPref.getString("name", null);

        Log.i("username from enc", username);

        if (username.equals("Guest")) {
            user_name.setText(Common.currentUser.getDisplayName());
            sign_in.setVisibility(View.VISIBLE);
            }else {
            user_name.setText(Common.currentUser.getDisplayName());
            user_phone.setText(Common.currentUser.getUsername());
            sign_out.setVisibility(View.VISIBLE);
            user_phone.setVisibility(View.VISIBLE);
        }

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(getContext(), LoginActivity.class);
                encEditor.remove("username");
                encEditor.commit();
                encEditor.remove("password");
                encEditor.commit();
                startActivity(signInIntent);
                getActivity().recreate();
                getActivity().finish();

            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(getContext(), LoginActivity.class);
                startActivity(signInIntent);
                getActivity().recreate();
                getActivity().finish();
            }
        });


        return root;
    }

}
