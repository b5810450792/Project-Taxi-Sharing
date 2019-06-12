package com.example.user.otp5.Model;

public class Account {
    private String key,email,pass,number_phone,status,name,ready;
    private String image,address,current_address,address_origin,province,area;

    public Account(){
        this.ready = "notready";
        this.image = "default" ;
        this.status = "new";
        //this.address = "";
//        this.current_address = "0";
//        this.address_origin = "";
        this.province = "";
        this.area = "";
    }

    public Account(String key,String email,String name, String pass,String number_phone) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.number_phone = number_phone;
        this.status = "new";
        this.ready = "notready";
        this.image = "default" ;
        //this.address = "";
       // this.current_address = "0";
       // this.address_origin = "";
        this.province = "";
        this.area= "";
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNumber_phone() {
        return number_phone;
    }

    public void setNumber_phone(String number_phone) {
        this.number_phone = number_phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getReady() {
        return ready;
    }

    public void setReady(String ready) {
        this.ready = ready;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrent_address() {
        return current_address;
    }

    public void setCurrent_address(String current_address) {
        this.current_address = current_address;
    }

    public String getAddress_origin() {
        return address_origin;
    }

    public void setAddress_origin(String address_origin) {
        this.address_origin = address_origin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
