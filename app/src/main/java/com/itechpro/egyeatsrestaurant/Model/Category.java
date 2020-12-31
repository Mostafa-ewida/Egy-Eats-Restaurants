package com.itechpro.egyeatsrestaurant.Model;

import com.itechpro.egyeatsrestaurant.Common.ServerGlobals;

import java.io.Serializable;

public class Category implements Serializable {
    private long id;
    private String name, nameArabic, imageurl;

    public Category() {
    }

    public Category(long id, String name, String nameArabic, String imageurl) {
        this.id = id;
        this.name = name;
        this.nameArabic = nameArabic;
        this.imageurl = imageurl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getImageurl() {
        return ServerGlobals.BASE_URL+imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameArabic='" + nameArabic + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }
}
