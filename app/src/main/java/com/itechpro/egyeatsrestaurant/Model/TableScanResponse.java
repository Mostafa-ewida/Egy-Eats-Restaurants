package com.itechpro.egyeatsrestaurant.Model;

import java.util.List;

public class TableScanResponse {
    private String tableName;
    private String restaurantName;
    private String restaurantNameArabic;
    private String restaurantImage;
    private List<Category> foodCategories;

    public TableScanResponse(String tableName, String restaurantName, String restaurantNameArabic, String restaurantImage, List<Category> foodCategories) {
        this.tableName = tableName;
        this.restaurantName = restaurantName;
        this.restaurantNameArabic = restaurantNameArabic;
        this.restaurantImage = restaurantImage;
        this.foodCategories = foodCategories;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantNameArabic() {
        return restaurantNameArabic;
    }

    public void setRestaurantNameArabic(String restaurantNameArabic) {
        this.restaurantNameArabic = restaurantNameArabic;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public void setRestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public List<Category> getFoodCategories() {
        return foodCategories;
    }

    public void setFoodCategories(List<Category> foodCategories) {
        this.foodCategories = foodCategories;
    }

    @Override
    public String toString() {
        return "TableScanResponse{" +
                "tableName='" + tableName + '\'' +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantNameArabic='" + restaurantNameArabic + '\'' +
                ", restaurantImage='" + restaurantImage + '\'' +
                ", foodCategories=" + foodCategories +
                '}';
    }
}
