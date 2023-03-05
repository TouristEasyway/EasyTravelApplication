package com.example.easytravelapplication.Model;

public class PackageHistoryListResponse {

    String key, userName, userContact, userEmail, packageName, places, totalDay, totalNight, startingDate, endDate, price, purchaseDate, packageImage, status;

    public PackageHistoryListResponse() {
    }

    public PackageHistoryListResponse(String key, String userName, String userContact, String userEmail, String packageName, String places, String totalDay, String totalNight, String startingDate, String endDate, String price, String purchaseDate, String packageImage, String status) {
        this.key = key;
        this.userName = userName;
        this.userContact = userContact;
        this.userEmail = userEmail;
        this.packageName = packageName;
        this.places = places;
        this.totalDay = totalDay;
        this.totalNight = totalNight;
        this.startingDate = startingDate;
        this.endDate = endDate;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.packageImage = packageImage;
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
