package com.example.user.otp5;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    Button login,register;

    FirebaseUser firebaseUser;
    boolean status_network;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        status_network = false;
        ConnectivityManager connectMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMan.getActiveNetworkInfo();


        String str = "";
        if (networkInfo != null && networkInfo.isConnected()) {

            str = "Network connected";
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

            if(firebaseUser!=null){
                Log.d("keyStart",firebaseUser.getUid());
                Intent intent = new Intent(StartActivity.this,MenuAppActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else {
            if (status_network != false) {
                str = "You should have connected to network";
                status_network = true;
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                finish();
            }
    }}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,LoginActivityTrue.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,RegisterAcitivityTrue.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
