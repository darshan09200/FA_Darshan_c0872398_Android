package com.darshan09200.products.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.darshan09200.products.helper.DateConverter;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String userId;

    private String name;
    private String description;

    private Double price;

    private Double latitude;
    private Double longitude;

    @TypeConverters({DateConverter.class})
    private Date updatedAt;

    public LatLng getCoordinate() {
        if(latitude == null || longitude == null) return null;
        return new LatLng(latitude, longitude);
    }

    public void setCoordinate(LatLng coordinate) {
        latitude = coordinate.latitude;
        longitude = coordinate.longitude;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
