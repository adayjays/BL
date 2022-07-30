package com.example.lendme.models;

import android.graphics.Bitmap;

import com.parse.ParseGeoPoint;

public class Item {

    public String title;
    public String description;
    public String availability;
    public String price;
    public Bitmap image_url;
    public ParseGeoPoint seller_loc;
    public String category;
    public String is_borrowable;
    public String posted_by;
    public String id;

    // constructor
    public Item(String id,String title, String description, String availability, String price, Bitmap image_url,
            ParseGeoPoint seller_loc, String category, String is_borrowable, String posted_by) {
                this.id = id;
        this.title = title;
        this.description = description;
        this.availability = availability;
        this.price = price;
        this.image_url = image_url;
        this.seller_loc = seller_loc;
        this.category = category;
        this.is_borrowable = is_borrowable;
        this.posted_by = posted_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailability() {
        return availability;
    }

    public String getPrice() {
        return price;
    }

    public Bitmap getImage_url() {
        return image_url;
    }

    public ParseGeoPoint getSeller_loc() {
        return seller_loc;
    }

    public String getCategory() {
        return category;
    }

    public String getIs_borrowable() {
        return is_borrowable;
    }

    public String getPosted_by() {
        return posted_by;
    }
}
