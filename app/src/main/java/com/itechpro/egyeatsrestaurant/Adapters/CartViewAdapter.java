package com.itechpro.egyeatsrestaurant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Database.DatabaseHelper;
import com.itechpro.egyeatsrestaurant.Model.OrderItem;
import com.itechpro.egyeatsrestaurant.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder>{

    private List<OrderItem> listData = new ArrayList<>();
    private Context context;

    public CartViewAdapter(List<OrderItem> listData, Context context) {

        this.listData = listData;
        this.context = context;

    }

    @NonNull
    @Override
    public CartViewAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewAdapter.CartViewHolder holder, int position) {
        holder.food_name.setText(listData.get(position).getName());
        double price = listData.get(position).getPrice()* listData.get(position).getQuantity();
        NumberFormat fmt = NumberFormat.getCurrencyInstance(Common.appLocale);
        holder.food_price.setText(fmt.format(price));
        holder.food_name.setText(listData.get(position).getName());
        holder.delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listData.remove(position);
                notifyDataSetChanged();
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                databaseHelper.deleteCarts();
                for (OrderItem item: listData){
                    databaseHelper.addOrder(item);
                }
                Toast.makeText(context, "Item deleted from Cart: ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class CartViewHolder<ItemClickListener> extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView food_name, food_price, food_desc;
        public Button delete_card;

        private ItemClickListener itemClickListener;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.food_name);
            food_price = itemView.findViewById(R.id.food_price);
            food_desc = itemView.findViewById(R.id.food_desc);
            delete_card = itemView.findViewById(R.id.delete_card);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
