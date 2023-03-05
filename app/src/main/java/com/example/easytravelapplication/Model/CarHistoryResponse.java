package com.example.easytravelapplication.Model;

import java.io.Serializable;

public class CarHistoryResponse implements Serializable {

    public  String  carName,fuelType,carType,ratePerKM,available,fullName,email,contactNo,startDate,endDate,bookDate,price,key,carImage;

    public  CarHistoryResponse(){

    }
    public CarHistoryResponse(String carName, String fuelType, String carType, String ratePerKM, String available, String fullName, String email, String contactNo, String startDate, String endDate, String bookDate, String price,String  key,String carImage) {
        this.carName = carName;
        this.fuelType = fuelType;
        this.carType = carType;
        this.ratePerKM = ratePerKM;
        this.available = available;
        this.fullName = fullName;
        this.email = email;
        this.contactNo = contactNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookDate = bookDate;
        this.price = price;
        this.key = key;
        this.carImage = carImage;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
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

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
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

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
