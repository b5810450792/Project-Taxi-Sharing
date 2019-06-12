package com.example.user.otp5.Model;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Room implements Serializable {
    private int user1,user2,user3,user4,room_no;
    private String user1_id,user2_id,user3_id,user4_id,user5_id,image1,image2,image3,image4,address_current;
    private String origin,destination,key,km,status,room_status,name_room;
    private double baht;
    private int total_user,total_ready,minites,hours,days,months,years;

    public Room(){

    }

    public Room(String origin, String destination, String km, double baht) {
        this.user1 = 1;
        this.user2 = 0;
        this.user3 = 0;
        this.user4 = 0;
        //this.user5 = 0;
        this.room_no = 0;
        this.user1_id = "default";
        this.user2_id = "default";
        this.user3_id = "default";
        this.user4_id = "default";
        //this.user5_id = "default";
        this.origin = origin;
        this.destination = destination;
        this.key = key;
        this.km = km;
        this.baht = baht;
        this.total_user = 0;
        this.status = "available";
        this.room_status = "new";
        this.total_ready = 0;
        this.minites = 0;
        this.hours = 0;
        this.days = 0;
        this.months = 0;
        this.years = 0;
        this.address_current = "0";
        this.name_room = "";


    }

    public int getTotal_user() {
        return total_user;
    }

    public void setTotal_user(int total_user) {
        this.total_user = total_user;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getBaht() {
        return baht;
    }

    public void setBaht(double baht) {
        this.baht = baht;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public int getUser3() {
        return user3;
    }

    public void setUser3(int user3) {
        this.user3 = user3;
    }

    public int getUser4() {
        return user4;
    }

    public void setUser4(int user4) {
        this.user4 = user4;
    }


    public String getUser1_id() {
        return user1_id;
    }

    public void setUser1_id(String user1_id) {
        this.user1_id = user1_id;
    }

    public String getUser2_id() {
        return user2_id;
    }

    public void setUser2_id(String user2_id) {
        this.user2_id = user2_id;
    }

    public String getUser3_id() {
        return user3_id;
    }

    public void setUser3_id(String user3_id) {
        this.user3_id = user3_id;
    }

    public String getUser4_id() {
        return user4_id;
    }

    public void setUser4_id(String user4_id) {
        this.user4_id = user4_id;
    }


    public String getStatus() {
        return status;
    }

    public int getRoom_no() {
        return room_no;
    }

    public void setRoom_no(int room_no) {
        this.room_no = room_no;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom_status() {
        return room_status;
    }

    public void setRoom_status(String room_status) {
        this.room_status = room_status;
    }

    public int getTotal_ready() {
        return total_ready;
    }

    public void setTotal_ready(int total_ready) {
        this.total_ready = total_ready;
    }

    public String getUser5_id() {
        return user5_id;
    }

    public void setUser5_id(String user5_id) {
        this.user5_id = user5_id;
    }

    public int getMinites() {
        return minites;
    }

    public void setMinites(int minites) {
        this.minites = minites;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getName_room() {
        return name_room;
    }

    public void setName_room(String name_room) {
        this.name_room = name_room;
    }

    public String getAddress_current() {
        return address_current;
    }

    public void setAddress_current(String address_current) {
        this.address_current = address_current;
    }
}

