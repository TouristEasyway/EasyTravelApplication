package com.example.easytravelapplication.Model;

import android.os.Parcelable;

import java.io.Serializable;

public class ManageCarResponse implements Serializable {

    String carName, furlType,carType,ratePerKM,city,state,available,key;

    public ManageCarResponse(){

    }

    public ManageCarResponse(String carName, String furlType, String carType, String ratePerKM, String city, String state,String available,String key) {
        this.carName = carName;
        this.furlType = furlType;
        this.carType = carType;
        this.ratePerKM = ratePerKM;
        this.city = city;
        this.state = state;
        this.available = available;
        this.key = key;
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

    public String getFurlType() {
        return furlType;
    }

    public void setFurlType(String furlType) {
        this.furlType = furlType;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getRatePerKM() {
        return ratePerKM + "KM/Hr";
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
}
