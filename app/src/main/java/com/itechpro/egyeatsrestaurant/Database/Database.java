package com.itechpro.egyeatsrestaurant.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.itechpro.egyeatsrestaurant.Model.OrderItem;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;


//Creating SQLLite DB for Order details
public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "EgyEats.db";
    private static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<OrderItem> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductName", "ProductId", "Quantity", "Price", "Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<OrderItem> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new OrderItem(c.getString(c.getColumnIndex("ProductName")),
                        Integer.parseInt(c.getString(c.getColumnIndex("Quantity"))),
                        Double.parseDouble(c.getString(c.getColumnIndex("Price"))),
                        Double.parseDouble(c.getString(c.getColumnIndex("Discount"))),"",""
                        ));
            }while (c.moveToNext());
        }

        return result;
    }

    public void addToCart (OrderItem orderItem){
        SQLiteDatabase db = getReadableDatabase();
        String query;
        query = String.format("INSERT INTO OrderDetail(ProductName, Quantity, Price, Discount, Discription, Notes) VALUES('$', '$', '$', '$', '$', '$')",
                orderItem.getName(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getDiscount(),
                orderItem.getDescription(),
                orderItem.getNotes());
        db.execSQL(query);
    }

    public void cleanCart (){
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM OrderDetail";
        db.execSQL(query);
    }
}
