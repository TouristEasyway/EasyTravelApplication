package com.example.easytravelapplication.Model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class ManageCarResponse implements Serializable {

    String carName, fuelType,carType,ratePerKM,city,state,available,bookedDate,key,carImage;

    public ManageCarResponse(){

    }

    public ManageCarResponse(String carName, String fuelType, String carType, String ratePerKM, String city, String state,String available,String bookedDate,String key,String carImage) {
        this.carName = carName;
        this.fuelType = fuelType;
        this.carType = carType;
        this.ratePerKM = ratePerKM;
        this.city = city;
        this.state = state;
        this.available = available;
        this.bookedDate = bookedDate;
        this.key = key;
        this.carImage = carImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getfuelType() {
        return fuelType;
    }

    public void setfuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRatePerKM() {
        return ratePerKM;
    }

    public void setRatePerKM(String ratePerKM) {
        this.ratePerKM = ratePerKM;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }
}
