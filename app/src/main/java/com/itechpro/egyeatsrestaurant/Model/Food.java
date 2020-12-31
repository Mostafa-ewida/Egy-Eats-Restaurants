package com.itechpro.egyeatsrestaurant.Model;

import com.itechpro.egyeatsrestaurant.Common.ServerGlobals;

import java.io.Serializable;

public class Food implements Serializable {
    private String imageurl, name, nameArabic, price, discount, description, menuId;
    private long id;

    public Food() {
    }

    public Food(String imageurl, String name, String nameArabic, String price, String discount, String description, String menuId, long id) {
        this.imageurl = imageurl;
        this.name = name;
        this.nameArabic = nameArabic;
        this.price = price;
        this.discount = discount;
        this.description = description;
        this.menuId = menuId;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Food{" +
                "imageurl='" + imageurl + '\'' +
                ", name='" + name + '\'' +
                ", nameArabic='" + nameArabic + '\'' +
                ", price='" + price + '\'' +
                ", discount='" + discount + '\'' +
                ", description='" + description + '\'' +
                ", menuId='" + menuId + '\'' +
                ", id=" + id +
                '}';
    }

    public String getImageurl() {
        return ServerGlobals.BASE_URL+imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public void setNameArabic(String nameArabic) {
        this.nameArabic = nameArabic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
