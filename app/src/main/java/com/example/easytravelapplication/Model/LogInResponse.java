package com.example.easytravelapplication.Model;

public class LogInResponse {

    public LogInResponse(){

    }
    String user_type,name,email,contact_no,gender,dob,city,state,profileImage,password;

    public LogInResponse(String userType, String name, String email, String contact_no, String gender, String dob, String city, String state, String profileImage, String password) {
        this.user_type = userType;
        this.name = name;
        this.email = email;
        this.contact_no = contact_no;
        this.gender = gender;
        this.dob = dob;
        this.city = city;
        this.state = state;
        this.profileImage = profileImage;
        this.password = password;
    }

    public String getUserType() {
        return user_type;
    }

    public void setUserType(String userType) {
        this.user_type = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getcontact_no() {
        return contact_no;
    }

    public void setcontact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
