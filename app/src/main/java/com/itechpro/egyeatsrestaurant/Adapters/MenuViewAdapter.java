package com.itechpro.egyeatsrestaurant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Interface.OnMenuClickListener;
import com.itechpro.egyeatsrestaurant.Model.Category;
import com.itechpro.egyeatsrestaurant.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuViewAdapter extends RecyclerView.Adapter<MenuViewAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Category> categoryList;
    private OnMenuClickListener listener;
    private Context context;


    public MenuViewAdapter(Context context, List<Category> categoryList, OnMenuClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
        this.context = context;
    }

    public MenuViewAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MenuViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewAdapter.MyViewHolder holder, int position) {
        final Category category = categoryList.get(position);

        if (Common.appLocale.getLanguage().equals("ar")) {
            holder.menu_name.setText(category.getNameArabic());
        }else{
            holder.menu_name.setText(category.getName());
        }
        Picasso.get().load(category.getImageurl())
                .into(holder.menu_image);
        if ((position%2) == 0){
            holder.menu_linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.card_red_bg));
        }else {
            holder.menu_linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.card_green_bg));
        }

        holder.bind(categoryList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView menu_image;
        public TextView menu_name;
        public LinearLayout menu_linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menu_image = itemView.findViewById(R.id.menu_image);
            menu_name = itemView.findViewById(R.id.menu_name);
            menu_linearLayout = itemView.findViewById(R.id.menu_linearLayout);
        }

        public void bind(final Category category, final OnMenuClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(category);
                }
            });
        }
    }
}