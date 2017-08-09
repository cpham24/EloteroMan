package edu.calstatela.cpham24.eloteroman.NetworkUtils;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by bill on 8/2/17.
 */

public class VendorItem {
    public String id;
    public String owner_name;
    public String cart_name;
    public String days;
    public String hours;
    public boolean in_service;
    public String img_url;
    public VendorLocationItem location;
    public ArrayList<FoodItem> food_list;
    public Bitmap img;
    public String marker_id;

    public VendorItem() {
        // empty constructor
    }

    public VendorItem(String id, String owner_name, String cart_name, String days, String hours, boolean in_service, String img_url, VendorLocationItem location, ArrayList<FoodItem> food_list) {
        this.id = id;
        this.owner_name = owner_name;
        this.cart_name = cart_name;
        this.days = days;
        this.hours = hours;
        this.in_service = in_service;
        this.img_url = img_url;
        this.location = location;
        this.food_list = food_list;
        this.img = null;
        this.marker_id = null;
    }
}
