package com.itechpro.egyeatsrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Database.DatabaseHelper;
import com.itechpro.egyeatsrestaurant.Model.Category;
import com.itechpro.egyeatsrestaurant.Model.Food;
import com.itechpro.egyeatsrestaurant.Model.OrderItem;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    ImageView food_detail_image;
    TextView food_detail_name, food_detail_price, food_detail_desc;
    Button food_detail_add_button, food_detail_cancel_button;
    ElegantNumberButton quantity_button;
    EditText food_detail_notes;
    Food food;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        food_detail_image = findViewById(R.id.food_detail_image);
        food_detail_name = findViewById(R.id.food_detail_name);
        food_detail_price = findViewById(R.id.food_detail_price);
        food_detail_desc = findViewById(R.id.food_detail_desc);
        food_detail_add_button = findViewById(R.id.food_detail_add_button);
        food_detail_cancel_button = findViewById(R.id.food_detail_cancel_button);
        quantity_button = findViewById(R.id.quantity_button);
        food_detail_notes = findViewById(R.id.food_detail_notes);

        //Get Intent here
        if (getIntent() != null) {
            food = (Food) getIntent().getSerializableExtra("Food");
            category = (Category) getIntent().getSerializableExtra("Category");
        }

        Picasso.get().load(food.getImageurl())
                .into(food_detail_image);
        food_detail_name.setText(food.getName());
        food_detail_price.setText(food.getPrice());
        food_detail_desc.setText(food.getDescription());

        food_detail_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent foodList = new Intent(FoodDetail.this, FoodList.class);
                foodList.putExtra("Category", category);
                startActivity(foodList);
            }
        });

        food_detail_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderItem orderItem = new OrderItem(
                        (String) (Common.appLocale.getLanguage().equals("ar")?food_detail_name.getText():food_detail_name.getText()),
                        Integer.parseInt(quantity_button.getNumber()),
                        Double.parseDouble((String) food_detail_price.getText()),
                        Double.parseDouble((String) food.getDiscount()),
                        food_detail_desc.getText().toString(),
                        food_detail_notes.getText().toString()
                );
                DatabaseHelper databaseHelper = new DatabaseHelper(FoodDetail.this);
                int count = databaseHelper.getProductCount((String) (Common.appLocale.getLanguage().equals("ar")?food_detail_name.getText():food_detail_name.getText()));
                if (count > 0){
                    databaseHelper.updateOrderItem(orderItem);
                }else{
                    boolean b = databaseHelper.addOrder(orderItem);
                }
                Toast.makeText(FoodDetail.this, "Item Added to Cart!", Toast.LENGTH_SHORT).show();
                Intent foodList = new Intent(FoodDetail.this, FoodList.class);
                foodList.putExtra("Category", category);
                startActivity(foodList);
            }
        });
    }
}