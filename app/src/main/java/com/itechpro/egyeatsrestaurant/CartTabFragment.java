package com.itechpro.egyeatsrestaurant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.egyeatsrestaurant.Adapters.CartViewAdapter;
import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Database.DatabaseHelper;
import com.itechpro.egyeatsrestaurant.Model.OrderItem;
import com.itechpro.egyeatsrestaurant.Model.Request;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClientBasicAuth;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartTabFragment extends Fragment {

    TextView user_name, login_mobile_number;
    Button remove_all_cards;
    RecyclerView card_recycler;
    RecyclerView.LayoutManager layoutManager;
    List<OrderItem> order = new ArrayList<>();
    CartViewAdapter adapter;
    Button place_button, current_orders;
    double total = 0;

    ApiInterface apiInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.cart_tab_fragment, container, false);
        user_name = root.findViewById(R.id.user_name);
        login_mobile_number = root.findViewById(R.id.login_mobile_number);
        place_button = root.findViewById(R.id.place_button);
        current_orders = root.findViewById(R.id.current_orders);
        remove_all_cards = root.findViewById(R.id.remove_all_cards);

        user_name.setText(Common.currentUser.getDisplayName());
        login_mobile_number.setText(Common.currentUser.getUsername());

        card_recycler = root.findViewById(R.id.card_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        card_recycler.setLayoutManager(layoutManager);

        apiInterface = ApiClientBasicAuth.createService(ApiInterface.class, Common.currentUser.getUsername(),Common.currentUser.getPassword());

        loadOrderItems();

        for (OrderItem orderItem : order) {
            total += (orderItem.getPrice()) * (orderItem.getQuantity());
        }

        place_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order.size() >0) {
                    for (OrderItem orderItem : order) {
                        total += (orderItem.getPrice()) * (orderItem.getQuantity());
                    }
                    showCardDialog();


                }
                else
                    Toast.makeText(getContext(), "Your Cart is Empty !!", Toast.LENGTH_SHORT).show();
            }
        });

        current_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent orderStatus = new Intent(getContext(), OrderStatus.class);
                startActivity(orderStatus);
            }
        });

        remove_all_cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                databaseHelper.deleteCarts();
                getActivity().recreate();
            }
        });

        return root;
    }

    private void showCardDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(Common.appLocale.getLanguage().equals("ar")?"خطوة أخيرة":"One More Step !");
        alertDialog.setMessage(Common.appLocale.getLanguage().equals("ar")?"ادخل عدد الأفراد":"Please enter Number of persons to be served ..");

        final EditText edtAddress = new EditText(getContext());
        LinearLayout.LayoutParams oneP = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(oneP);
        alertDialog.setView(edtAddress);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);
        alertDialog.setPositiveButton(Common.appLocale.getLanguage().equals("ar")?"نعم":"YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        total,
                        Integer.parseInt(edtAddress.getText().toString()),
                        Common.table,
                        order,
                        Common.androidId
                );
                apiInterface.placeRequest(request).enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        if (response.isSuccessful()){
                            Log.i("Post is submitted", response.body().toString());
                            Common.request = new Request();
                            Common.request.setId(response.body().getId());
                            Log.i("request ID: ", String.valueOf(response.body().getId()));
                            new DatabaseHelper(getContext()).deleteCarts();
                            Toast.makeText(getContext(), "Order Placed .. Thanks for your order", Toast.LENGTH_SHORT).show();
                            getActivity().recreate();

                        }else {
                            Log.i("response", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Request> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went wrong !!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        alertDialog.setNegativeButton(Common.appLocale.getLanguage().equals("ar")?"لا":"NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void loadOrderItems() {
        order = new DatabaseHelper(getContext()).getCarts();
        adapter = new CartViewAdapter(order, getContext());

        card_recycler.setAdapter(adapter);
    }


}
