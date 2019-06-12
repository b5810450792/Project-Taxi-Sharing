package com.example.user.otp5;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.user.otp5.Model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class OTPActivityTrue extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser mAuth1;
    MaterialEditText editTextPhone;
    String codesent,phonenumber,username,password,key,email,province,area;
    DatabaseReference databaseReference;
    EncodeAndDecodePassword encodedeconde;
    PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otptrue);

        mAuth = FirebaseAuth.getInstance();
        mAuth1 = FirebaseAuth.getInstance().getCurrentUser();

        encodedeconde = new EncodeAndDecodePassword();

        Bundle bundle = getIntent().getExtras();
        phonenumber = bundle.getString("numberphone");
        email = bundle.getString("email");
        username = bundle.getString("name");
        password = bundle.getString("password");
        key = bundle.getString("key");
        province = bundle.getString("province");
        area = bundle.getString("area");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+66"+phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        editTextPhone = (MaterialEditText)findViewById(R.id.editTextverificationcode) ;

        Button buttonSignIn = (Button) findViewById(R.id.buttonregistersuccess);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignIncode();
            }
        });

        Button buttonresend = findViewById(R.id.resend);
        buttonresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSendCodeString(phonenumber,mResendToken);
            }
        });

    }


    private void verifySignIncode() {
        String otp = editTextPhone.getText().toString();
        if (otp.isEmpty()) {
            editTextPhone.setError("Verification code is required");
            editTextPhone.requestFocus();
            return;
        }
        if (otp.length() < 6) {
            editTextPhone.setError("Please enter a valid verification code");
            editTextPhone.requestFocus();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, otp);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            mAuth.signOut();

                            Log.d("keyOTP2",key+"");
                            Account a = new Account();
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("key",key);
                            hashMap.put("email",email);
                            hashMap.put("name",username);
                            hashMap.put("pass",encodedeconde.enCode(password));
                            hashMap.put("number_phone",phonenumber);
                            hashMap.put("status","new");
                            hashMap.put("ready",a.getReady());
                            hashMap.put("image",a.getImage());
                            hashMap.put("province",province);
                            hashMap.put("area",area);


                            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(key);
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    Intent gotoMenu = new Intent(OTPActivityTrue.this,StartActivity.class);
                                    gotoMenu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(gotoMenu);
                                    Toast.makeText(getApplicationContext(), "Register Successfull", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }



                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Incorrect Verication Code", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivityTrue.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent = s;
            mResendToken = forceResendingToken;

        }
    };

    public void  reSendCodeString(String phoneNumber,
                                  PhoneAuthProvider.ForceResendingToken token)

    {
        Toast.makeText(getApplicationContext(), "Resend code again..", Toast.LENGTH_LONG).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+66"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OTPActivityTrue.this);
        builder.setMessage("Are you sure to back to register?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth1.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(getApplicationContext(),RegisterAcitivityTrue.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
