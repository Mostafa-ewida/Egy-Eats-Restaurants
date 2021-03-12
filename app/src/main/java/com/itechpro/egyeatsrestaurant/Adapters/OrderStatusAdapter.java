package com.itechpro.egyeatsrestaurant.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.egyeatsrestaurant.Interface.OnRequestClickListener;
import com.itechpro.egyeatsrestaurant.Model.Request;
import com.itechpro.egyeatsrestaurant.R;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.RequestViewHolder> {

    private final List<Request> listData;
    private final Context context;

    public OrderStatusAdapter(List<Request> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.order_status_item, parent, false);

        return new RequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.no_of_people.setText("No of People being served: " + listData.get(position).getPersonsCount());
        holder.order_status.setText("Order Status: " + getOrderStatus(listData.get(position).getOrderStatus()));
        holder.order_id.setText("Order ID: " + listData.get(position).getId());
        holder.total_cost.setText("Total Cost: " + listData.get(position).getTotalCost());
    }

    private String getOrderStatus(String status) {
        String returned_status;
        switch (status){
            case "PLACED": returned_status="Placed";
            break;
            case "PENDING": returned_status="Waiting for Restaurant";
            break;
            case "CANCELED": returned_status="Canceld by Restaurant";
            break;
            case "REJECTED": returned_status="Rejected by Restaurant";
            break;
            case "ACCEPTED": returned_status="Accepted by Restaurant";
            break;
            case "UNDER_PREPARATION": returned_status="Under Preparation";
            break;
            case "READY_FOR_DELIVERY": returned_status="Ready for Serving";
            break;
            case "ON_DELIVERY": returned_status="On Its Way to you!";
            break;
            case "SERVED": returned_status="Served";
            break;
            case "PAID": returned_status="Order Paid";
            default: returned_status="waiting";

        }


        return returned_status;
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }


    class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView order_id, order_status, no_of_people, total_cost;

        private OnRequestClickListener itemClickListener;



        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            order_status = itemView.findViewById(R.id.order_status);
            no_of_people = itemView.findViewById(R.id.no_of_people);
            total_cost = itemView.findViewById(R.id.total_cost);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
