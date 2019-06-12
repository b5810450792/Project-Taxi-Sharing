package com.example.user.otp5.Model;

public class Chat {
    private String sender;
    private String receiver2,receiver3,receiver4,receiver5;
    private String message;
    private String key_room;
    private String name_user;
    private String image;

    public Chat(String sender, String receiver2, String receiver3, String receiver4, String receiver5, String message) {
        this.sender = sender;
        this.receiver2 = receiver2;
        this.receiver3 = receiver3;
        this.receiver4 = receiver4;
        this.message = message;
        this.key_room = "empty";
        this.name_user = "";
        this.image = "default";
    }

    public Chat(){

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver2() {
        return receiver2;
    }

    public void setReceiver2(String receiver2) {
        this.receiver2 = receiver2;
    }

    public String getReceiver3() {
        return receiver3;
    }

    public void setReceiver3(String receiver3) {
        this.receiver3 = receiver3;
    }

    public String getReceiver4() {
        return receiver4;
    }

    public void setReceiver4(String receiver4) {
        this.receiver4 = receiver4;
    }

    public String getReceiver5() {
        return receiver5;
    }

    public void setReceiver5(String receiver5) {
        this.receiver5 = receiver5;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey_room() {
        return key_room;
    }

    public void setKey_room(String key_room) {
        this.key_room = key_room;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
