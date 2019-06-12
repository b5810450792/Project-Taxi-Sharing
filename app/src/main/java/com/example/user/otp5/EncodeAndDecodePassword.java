package com.example.user.otp5;

import android.util.Base64;
import android.util.Log;

public class EncodeAndDecodePassword {
    String en = "";
    String de = "";

    public String enCode(String pass){
        byte[] encodeValue = Base64.encode(pass.getBytes(), Base64.DEFAULT);
        en = new String(encodeValue);

        Log.d("TEST", "defaultValue = " + pass);
        Log.d(  "TEST", "encodeValue = " + en);
        return  en;
    }

    public String deCode(String encode_pass){
        byte[] decodeValue = Base64.decode(encode_pass, Base64.DEFAULT);
        de = new String(decodeValue);
        Log.d("TEST", "decodeValue = " + de);
        return de;
    }

}
