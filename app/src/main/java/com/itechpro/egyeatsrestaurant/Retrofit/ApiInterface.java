package com.itechpro.egyeatsrestaurant.Retrofit;

import com.itechpro.egyeatsrestaurant.Model.EndUser;
import com.itechpro.egyeatsrestaurant.Model.Food;
import com.itechpro.egyeatsrestaurant.Model.Request;
import com.itechpro.egyeatsrestaurant.Model.TableScanResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("misc/{id}/categories")
    Call<TableScanResponse> getRestaurantCategories(@Path("id") long id);

    @GET("misc/category/{id}/foods")
    Call<List<Food>> getFoods(@Path("id") long id);

    @Headers({"Accept: application/json"})
    @POST("misc/orders")
    Call<Request> placeRequest(@Body Request request);

    @Headers({"Accept: application/json"})
    @POST("misc/register")
    Call<EndUser> signUp(@Body EndUser endUser);

    @Headers({"Accept: application/json"})
    @POST("misc/auth")
    Call<EndUser> signIn(@Body EndUser endUser);

    //Get tableID
    @Headers({"Accept: application/json"})
    @GET("misc/userorders/{deviceId}/{tableId}")
    Call<List<Request>> getOrder(@Path("deviceId") String deviceId, @Path("tableId") long id);

}
