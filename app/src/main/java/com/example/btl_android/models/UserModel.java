package com.example.btl_android.models;

public class UserModel {
    String name, email, password;
    String profileImg;
    String number,address;

    public UserModel() {
    }

    public UserModel(String name, String email, String password, String number, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
        this.address = address;
    }

    public UserModel(String name, String email, String password, String profileImg, String number, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.number = number;
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
