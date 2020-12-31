package com.itechpro.egyeatsrestaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itechpro.egyeatsrestaurant.Adapters.FoodViewAdapter;
import com.itechpro.egyeatsrestaurant.Adapters.MenuViewAdapter;
import com.itechpro.egyeatsrestaurant.Common.Common;
import com.itechpro.egyeatsrestaurant.Interface.OnFoodClickListener;
import com.itechpro.egyeatsrestaurant.Interface.OnMenuClickListener;
import com.itechpro.egyeatsrestaurant.Model.Category;
import com.itechpro.egyeatsrestaurant.Model.Food;
import com.itechpro.egyeatsrestaurant.Model.TableScanResponse;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiClientBasicAuth;
import com.itechpro.egyeatsrestaurant.Retrofit.ApiInterface;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.itechpro.egyeatsrestaurant.Common.Common.tableId;

public class MenuTabFragment extends Fragment {

    RecyclerView menu_recycler;
    RecyclerView.LayoutManager layoutManager;
    long categoryID = 146;
    ApiInterface apiService;
    List<Category> categoryList;
    MenuViewAdapter menuViewAdapter;
    TextView restaurant_name, user_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.menu_tab_fragment, container, false);

        menu_recycler = root.findViewById(R.id.menu_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        menu_recycler.setLayoutManager(layoutManager);
        restaurant_name = root.findViewById(R.id.restaurant_name);
        user_name = root.findViewById(R.id.user_name);
        user_name.setText(Common.currentUser.getDisplayName());
        loadListFood();
        return root;
    }

    private void loadListFood() {
        apiService = ApiClientBasicAuth.createService(ApiInterface.class);
        Call<TableScanResponse> call = apiService.getRestaurantCategories(Long.parseLong(tableId));
        //Call<List<Category>> call = apiService.getCategories();
        call.enqueue(new Callback<TableScanResponse>() {
            @Override
            public void onResponse(Call<TableScanResponse> call, Response<TableScanResponse> response) {
                if (response.body() != null) {
                    categoryList = response.body().getFoodCategories();
                    restaurant_name.setText(response.body().getRestaurantName());
                    Log.i("TableScanResponse", categoryList.get(0).getImageurl());
                    menuViewAdapter = new MenuViewAdapter(getContext(), categoryList, new OnMenuClickListener() {
                        @Override
                        public void onItemClick(Category category) {
                            Intent foodList = new Intent(getContext(), FoodList.class);
                            foodList.putExtra("Category", category);
                            startActivity(foodList);
                        }
                    });
                    menu_recycler.setAdapter(menuViewAdapter);
                    menuViewAdapter.setCategoryList(categoryList);
                }else {
                    Log.i("Category response: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<TableScanResponse> call, Throwable t) {

            }
        });
    }


}
