package com.example.easytravelapplication.Model;

public class HotelListResponse {

    String address, checkInTime, checkOutTime, city, hotelImages, hotelName, key, location, price,service,state,status;

    public HotelListResponse() {
    }

    public HotelListResponse(String address, String checkInTime, String checkOutTime, String city, String hotelImages, String hotelName, String key, String location, String price, String service, String state, String status) {
        this.address = address;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.city = city;
        this.hotelImages = hotelImages;
        this.hotelName = hotelName;
        this.key = key;
        this.location = location;
        this.price = price;
        this.service = service;
        this.state = state;
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHotelImages() {
        return hotelImages;
    }

    public void setHotelImages(String hotelImages) {
        this.hotelImages = hotelImages;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
