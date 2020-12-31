package com.itechpro.egyeatsrestaurant.Model;

public class OrderItem {
    private String name;
    private int quantity;
    private double price;
    private double discount;
    private String description;
    private String notes;


    public OrderItem() {
    }

    public OrderItem(String name, int quantity, double price, double discount, String description, String notes) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                ", description='" + description + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }


}
