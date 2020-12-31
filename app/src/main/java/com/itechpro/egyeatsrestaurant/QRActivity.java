package com.itechpro.egyeatsrestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Model.EndUser;
import com.itechpro.egyeatsrestaurant.Model.Table;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.security.GeneralSecurityException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView scannerView;
    private Button start_scan;
    EndUser user = Common.currentUser;
    SharedPreferences pref;
    SharedPreferences encPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r);

        pref = getApplicationContext().getSharedPreferences("EgyEatsPref", 0);
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            encPref = EncryptedSharedPreferences.create(
                    "EgyEatsPref",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start_scan = findViewById(R.id.start_scan);
        start_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(QRActivity.this)) {
                    QRStart();
                } else {
                    Toast.makeText(QRActivity.this, "Please Check Your Connection !!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void QRStart() {
        scannerView = findViewById(R.id.zxscan);


        //Result Permission
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(QRActivity.this);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(QRActivity.this, "You Must Accept this permission", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    @Override
    protected void onDestroy() {
        if (scannerView != null) {
            scannerView.stopCamera();
        }
        super.onDestroy();
    }

    @Override
    public void handleResult(Result rawResult) {
        processRawResult(rawResult.getText());
        scannerView.stopCamera();
    }

    private void processRawResult(String text) {
        if (text != null) {
            Common.table = new Table(Long.parseLong(text));
            Intent restaurantWelcomeIntent = new Intent(QRActivity.this, MainActivity.class);
            restaurantWelcomeIntent.putExtra("tableId", text);
            Common.tableId = text;
            startActivity(restaurantWelcomeIntent);
            finish();
        }
    }
}