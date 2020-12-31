package com.itechpro.egyeatsrestaurant.Model;

import java.util.List;

public class Request {
    private double totalCost;
    private int personsCount;
    private Table restaurantTable;
    private List<OrderItem> foodOrderItems;
    private String orderStatus;
    private String phone;
    private long id;
    private String deviceId;

    public Request() {
    }

    public Request(double totalCost, int personsCount, Table restaurantTable, List<OrderItem> foodOrderItems, String orderStatus, String phone, long id, String deviceId) {
        this.totalCost = totalCost;
        this.personsCount = personsCount;
        this.restaurantTable = restaurantTable;
        this.foodOrderItems = foodOrderItems;
        this.orderStatus = orderStatus;
        this.phone = phone;
        this.id = id;
        this.deviceId = deviceId;
    }

    public Request(double totalCost, int personsCount, Table restaurantTable, List<OrderItem> foodOrderItems, String deviceId) {
        this.totalCost = totalCost;
        this.personsCount = personsCount;
        this.restaurantTable = restaurantTable;
        this.foodOrderItems = foodOrderItems;
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<OrderItem> getFoodOrderItems() {
        return foodOrderItems;
    }

    public void setFoodOrderItems(List<OrderItem> foodOrderItems) {
        this.foodOrderItems = foodOrderItems;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPersonsCount() {
        return personsCount;
    }

    public void setPersonsCount(int personsCount) {
        this.personsCount = personsCount;
    }

    public Table getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(Table restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Request{" +
                "totalCost=" + totalCost +
                ", personsCount=" + personsCount +
                ", restaurantTable=" + restaurantTable +
                ", foodOrderItems=" + foodOrderItems +
                ", orderStatus='" + orderStatus + '\'' +
                ", phone='" + phone + '\'' +
                ", id=" + id +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
