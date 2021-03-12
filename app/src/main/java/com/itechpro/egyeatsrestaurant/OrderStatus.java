package com.itechpro.egyeatsrestaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itechpro.egyeatsrestaurant.Adapters.OrderStatusAdapter;
import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Interface.OnRequestClickListener;
import com.itechpro.egyeatsrestaurant.Model.Request;
import com.itechpro.egyeatsrestaurant.Model.Table;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClient;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClientBasicAuth;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatus extends AppCompatActivity {

    Button order_status_button_back;
    Button order_status_refresh;
    Button profile_button;
    Button cart_button;
    RecyclerView order_status_recycler;
    RecyclerView.LayoutManager layoutManager;
    List<Request> orderList;
    OrderStatusAdapter orderStatusAdapter;
    ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        order_status_button_back = findViewById(R.id.order_status_button_back);
        order_status_refresh = findViewById(R.id.order_status_refresh);
        profile_button = findViewById(R.id.profile_button);
        cart_button = findViewById(R.id.cart_button);

        order_status_recycler = findViewById(R.id.order_status_recycler);
        layoutManager = new LinearLayoutManager(this);
        order_status_recycler.setLayoutManager(layoutManager);

        loadOrderStatus();

        order_status_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntentCart = new Intent(OrderStatus.this, MainActivity.class);
                mainIntentCart.putExtra("tabID", "2");
                startActivity(mainIntentCart);
                finish();
            }
        });

        order_status_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadOrderStatus();
                orderStatusAdapter.notifyDataSetChanged();
            }
        });

        cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntentCart = new Intent(OrderStatus.this, MainActivity.class);
                mainIntentCart.putExtra("tabID", "2");
                startActivity(mainIntentCart);
                finish();
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntentProfile = new Intent(OrderStatus.this, MainActivity.class);
                mainIntentProfile.putExtra("tabID", "0");
                startActivity(mainIntentProfile);
                finish();
            }
        });

    }

    private void loadOrderStatus() {

        if (Common.currentUser.getUsername() != null && Common.currentUser.getDisplayName().equals("Guest")) {
            apiService = ApiClient.getClientBasic();
        } else {
            Log.i("Common user:", Common.currentUser.getDisplayName()+ " - "+Common.currentUser.getPassword());
            apiService = ApiClientBasicAuth.createService(ApiInterface.class, Common.currentUser.getUsername(), Common.currentUser.getPassword());
        }

        Call<List<Request>> call = apiService.getOrder(Common.androidId, Long.parseLong(Common.tableId));
        call.enqueue(new Callback<List<Request>>() {
            @Override
            public void onResponse(Call<List<Request>> call, Response<List<Request>> response) {
                if (response.body() != null) {
                    orderList = response.body();
                    for(int i = 0; i< orderList.size(); i++){
                        Log.i("orderList["+i+"] :", orderList.toString());
                    }
                    orderStatusAdapter = new OrderStatusAdapter(orderList, OrderStatus.this);
                    order_status_recycler.setAdapter(orderStatusAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Request>> call, Throwable t) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainIntentCart = new Intent(OrderStatus.this, MainActivity.class);
        mainIntentCart.putExtra("tabID", "2");
        startActivity(mainIntentCart);
        finish();
    }


}