package com.itechpro.egyeatsrestaurant.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.itechpro.egyeatsrestaurant.Model.OrderItem;

import java.util.ArrayList;
import java.util.List;


//Table Characteristics
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "EgyEats.db";
    private static final int DB_VER = 1;
    public static final String ORDER_DETAIL = "OrderDetail";
    public static final String PRODUCT_NAME = "ProductName";
    public static final String QUANTITY = "Quantity";
    public static final String PRICE = "Price";
    public static final String DISCOUNT = "Discount";
    public static final String DESCRIPTION = "Description";
    public static final String NOTES = "Notes";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    // this is called the first time a database is accessed. There should be code to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTabeleStatement = "CREATE TABLE " + ORDER_DETAIL + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCT_NAME + " TEXT, " + QUANTITY + " TEXT, " + PRICE + " TEXT, " + DISCOUNT +" TEXT, " + DESCRIPTION +" TEXT, " + NOTES + " TEXT)";
        db.execSQL(createTabeleStatement);
    }

    //this is called if the database version number changes. It prevents previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOrder(OrderItem orderItem){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRODUCT_NAME, orderItem.getName());
        cv.put(QUANTITY, orderItem.getQuantity());
        cv.put(PRICE, orderItem.getPrice());
        cv.put(DISCOUNT, orderItem.getDiscount());
        cv.put(DESCRIPTION, orderItem.getDescription());
        cv.put(NOTES, orderItem.getNotes());

        long insert = db.insert(ORDER_DETAIL, null, cv);

        if (insert == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<OrderItem> getCarts(){

        List<OrderItem> result = new ArrayList<>();
        String query = "SELECT * FROM "+ORDER_DETAIL;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(QUANTITY)));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(PRICE)));
                double discount = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DISCOUNT)));
                String description = cursor.getString(cursor.getColumnIndex(DESCRIPTION));
                String notes = cursor.getString(cursor.getColumnIndex(NOTES));
                OrderItem orderItem = new OrderItem(productName, quantity, price, discount, description, notes);
                result.add(orderItem);
            }while (cursor.moveToNext());
        }else {

        }

        cursor.close();
        db.close();
        return result;
    }

    public int getProductCount(String sentProductName){

        String query = "SELECT "+QUANTITY+" FROM "+ORDER_DETAIL+" WHERE "+PRODUCT_NAME+" = "+"'"+sentProductName+"'";
        String query2 = "SELECT * FROM "+ORDER_DETAIL;
        Log.i("query is:", query);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int result=0;
        if (cursor.moveToFirst()) {
            result = Integer.parseInt(cursor.getString(0));
            Log.i("inside getCount: ", ""+result);
        }
        cursor.close();
        db.close();
        return result;
    }

    public void deleteCarts(){
        String query = "DELETE FROM "+ORDER_DETAIL;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void updateOrderItem(OrderItem orderItem) {
        int count = getProductCount(orderItem.getName())+orderItem.getQuantity();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ORDER_DETAIL+" SET "+QUANTITY+" = "+count+" WHERE "+PRODUCT_NAME+" = '"+orderItem.getName()+"'";
        db.execSQL(query);
    }
}
