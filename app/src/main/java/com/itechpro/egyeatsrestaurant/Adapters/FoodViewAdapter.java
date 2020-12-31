package com.itechpro.egyeatsrestaurant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Database.DatabaseHelper;
import com.itechpro.egyeatsrestaurant.Interface.OnFoodClickListener;
import com.itechpro.egyeatsrestaurant.MainActivity;
import com.itechpro.egyeatsrestaurant.Model.Food;
import com.itechpro.egyeatsrestaurant.Model.OrderItem;
import com.itechpro.egyeatsrestaurant.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodViewAdapter extends RecyclerView.Adapter<FoodViewAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Food> foodList;
    private OnFoodClickListener listener;
    private Context context;

    public FoodViewAdapter(Context context, List<Food> foodList, OnFoodClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
        this.context = context;
    }

    public FoodViewAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewAdapter.MyViewHolder holder, int position) {
        final Food food = foodList.get(position);
        holder.food_name.setText(food.getName());
        holder.food_price.setText(food.getPrice());
        holder.food_desc.setText(food.getDescription());
        Picasso.get().load(food.getImageurl())
                .into(holder.food_image);
        holder.bind(foodList.get(position), listener);

        holder.quantity_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                OrderItem orderItem = new OrderItem(
                        (String) (Common.appLocale.getLanguage().equals("ar")?holder.food_name.getText():holder.food_name.getText()),
                        newValue,
                        Double.parseDouble((String) holder.food_price.getText()),
                        holder.discount,
                        "",
                        ""
                );
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                int count = databaseHelper.getProductCount((String) (Common.appLocale.getLanguage().equals("ar")?holder.food_name.getText():holder.food_name.getText()));
                if (count > 0){
                    databaseHelper.updateOrderItem(orderItem);
                }else{
                    boolean b = databaseHelper.addOrder(orderItem);
                }

                Toast.makeText(context, "Item Added to Cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView food_image;
        public TextView food_desc, food_price, food_name;
        public ElegantNumberButton quantity_button;
        public Double discount = Double.valueOf(0);

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_desc = itemView.findViewById(R.id.food_desc);
            food_price = itemView.findViewById(R.id.food_price);
            quantity_button = itemView.findViewById(R.id.quantity_button);
        }

        public void bind(final Food food, final OnFoodClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(food);
                }
            });
        }
    }
}
