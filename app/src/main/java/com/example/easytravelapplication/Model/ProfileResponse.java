package com.example.easytravelapplication.Model;

public class ProfileResponse {
    String birth_date, city, contact_no, email, gender, key, name, password, profile_pic, state, status, user_type;

    public ProfileResponse() {

    }

    public ProfileResponse(String birth_date, String city, String contact_no, String email, String gender, String key, String name, String password, String profile_pic, String state, String status, String user_type) {
        this.birth_date = birth_date;
        this.city = city;
        this.contact_no = contact_no;
        this.email = email;
        this.gender = gender;
        this.key = key;
        this.name = name;
        this.password = password;
        this.profile_pic = profile_pic;
        this.state = state;
        this.status = status;
        this.user_type = user_type;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
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

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
