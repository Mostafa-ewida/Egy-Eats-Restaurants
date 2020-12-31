package com.itechpro.egyeatsrestaurant;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itechpro.egyeatsrestaurant.Adapters.FoodViewAdapter;
import com.itechpro.egyeatsrestaurant.Interface.OnFoodClickListener;
import com.itechpro.egyeatsrestaurant.Model.Category;
import com.itechpro.egyeatsrestaurant.Model.Food;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClientBasicAuth;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodList extends AppCompatActivity {

    Button food_list_button_back, food_cart_button, food_profile_button;
    TextView food_list_category_name;
    RecyclerView food_list_recycler;
    RecyclerView.LayoutManager layoutManager;
    Category category;
    ApiInterface apiService;
    FoodViewAdapter foodViewAdapter;
    List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        food_list_button_back = findViewById(R.id.food_list_button_back);
        food_cart_button = findViewById(R.id.food_cart_button);
        food_profile_button = findViewById(R.id.food_profile_button);
        food_list_category_name = findViewById(R.id.food_list_category_name);
        food_list_recycler = findViewById(R.id.food_list_recycler);
        layoutManager = new LinearLayoutManager(this);
        food_list_recycler.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() != null) {
            category = (Category) getIntent().getSerializableExtra("Category");
            loadFoodList(category.getId());
        }


        food_list_category_name.setText(category.getName());
        food_list_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(FoodList.this, MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        food_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntentCart = new Intent(FoodList.this, MainActivity.class);
                mainIntentCart.putExtra("tabID", "2");
                startActivity(mainIntentCart);
                finish();
            }
        });

        food_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntentProfile = new Intent(FoodList.this, MainActivity.class);
                mainIntentProfile.putExtra("tabID", "0");
                startActivity(mainIntentProfile);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainReload = new Intent(FoodList.this, MainActivity.class);
        startActivity(mainReload);
    }

    private void loadFoodList(long categoryID) {
        apiService = ApiClientBasicAuth.createService(ApiInterface.class);
        Call<List<Food>> call = apiService.getFoods(categoryID);
        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                foodList = response.body();
                foodViewAdapter = new FoodViewAdapter(getApplicationContext(), foodList, new OnFoodClickListener() {
                    @Override
                    public void onItemClick(Food food) {
                        Intent foodIntent = new Intent(FoodList.this, FoodDetail.class);
                        foodIntent.putExtra("Food", food);
                        foodIntent.putExtra("Category", category);
                        startActivity(foodIntent);
                    }
                });

                food_list_recycler.setAdapter(foodViewAdapter);

                foodViewAdapter.setFoodList(foodList);
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {

            }
        });
    }


}