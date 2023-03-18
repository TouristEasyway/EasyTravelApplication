package com.example.easytravelapplication.Model;

import java.io.Serializable;

public class PackageListResponse implements Serializable {

    String packageName, places,totalDay,totalNight,startingDate,price,packageImage,key,endDate,hotelName;

    public PackageListResponse() {
    }

    public PackageListResponse(String packageName, String places, String totalDay, String totalNight, String startingDate, String price, String packageImage, String key, String endDate,String hotelName) {
        this.packageName = packageName;
        this.places = places;
        this.totalDay = totalDay;
        this.totalNight = totalNight;
        this.startingDate = startingDate;
        this.price = price;
        this.packageImage = packageImage;
        this.key = key;
        this.endDate = endDate;
        this.hotelName = hotelName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(String totalDay) {
        this.totalDay = totalDay;
    }

    public String getTotalNight() {
        return totalNight;
    }

    public void setTotalNight(String totalNight) {
        this.totalNight = totalNight;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
