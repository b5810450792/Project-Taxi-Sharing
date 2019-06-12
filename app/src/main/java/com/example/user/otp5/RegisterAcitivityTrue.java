package com.example.user.otp5;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.user.otp5.MenuApp.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterAcitivityTrue extends AppCompatActivity {
    FirebaseAuth mAuth;
    MaterialEditText editTextPhone,editTextUsername,editTextPassword,editTextEmail;
    FirebaseUser firebaseUser;
    String codesent;
    Spinner spinner,spinner2;
    TextView province_text,area_text;
    String citySelected = "";
    int citySelectIndex;
    String citySelected2 = "";
    int citySelected2Index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity_true);
        mAuth = FirebaseAuth.getInstance();

        editTextPhone =  findViewById(R.id.phonenumber);
        editTextUsername =  findViewById(R.id.username);
        editTextPassword =  findViewById(R.id.password);
        editTextEmail = findViewById(R.id.email);

        spinner = findViewById(R.id.province_select);
        spinner2 = findViewById(R.id.area_select);
        province_text = findViewById(R.id.province_text);
        area_text = findViewById(R.id.area_text);


        Button buttonGetVerificationCode = (Button) findViewById(R.id.btn_register);
        buttonGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RegisterAcitivityTrue.this,R.array.city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySelectIndex = parent.getSelectedItemPosition();
                citySelected = parent.getItemAtPosition(position).toString();
                if(citySelected.equals("--Province--")){

                }else {
                    province_text.setText(citySelected);
                    Toast.makeText(RegisterAcitivityTrue.this,province_text.getText().toString(),Toast.LENGTH_SHORT).show();
                    onCitySelected();
                }
//                province_text.setText(citySelected);
//                Toast.makeText(RegisterAcitivityTrue.this,province_text.getText().toString(),Toast.LENGTH_SHORT).show();
//                onCitySelected(); // 16->77
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void sendVerificationCode() {
        final String phonenumber = editTextPhone.getText().toString();
        final String textuser = editTextUsername.getText().toString();
        final String textpass = editTextPassword.getText().toString();
        final String textemail = editTextEmail.getText().toString();
        if (textuser.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }
        if (textuser.length() < 4 || textuser.length() > 20) {
            editTextUsername.setError("Your Username should contain 4-20 characters");
            editTextUsername.requestFocus();
            return;
        }
        if (!textemail.contains("@gmail.com") && !textemail.contains("@hotmail.com")) {
            editTextEmail.setError("Email or Gmail only!");
            editTextEmail.requestFocus();
            return;
        }

        if (textpass.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (textpass.length() < 8) {
            editTextPassword.setError("Your Password should contain 8-20 characters");
            editTextPassword.requestFocus();
            return;
        }

        if (phonenumber.isEmpty()) {
            editTextPhone.setError("Phone number is required");
            editTextPhone.requestFocus();
            return;
        }
        if (phonenumber.length() < 10) {
            editTextPhone.setError("Please enter a valid phone");
            editTextPhone.requestFocus();
            return;
        }
        if(province_text.getText().toString().equals("")){
            province_text.setError("Province is required");
            province_text.requestFocus();
            return;
        }
        if(area_text.getText().toString().equals("")){
            area_text.setError("Area is required");
            area_text.requestFocus();
            return;
        }
        AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterAcitivityTrue.this);
        builder1.setMessage("Are you sure to register?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.createUserWithEmailAndPassword(textemail, textpass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            firebaseUser = mAuth.getCurrentUser();
                                            String key = firebaseUser.getUid();
                                            Intent intent = new Intent(RegisterAcitivityTrue.this, OTPActivityTrue.class);
                                            Toast.makeText(getApplicationContext(), "Message has sent", Toast.LENGTH_LONG).show();
                                            intent.putExtra("name", textuser);
                                            intent.putExtra("email", textemail);
                                            intent.putExtra("password", textpass);
                                            intent.putExtra("numberphone", phonenumber);
                                            intent.putExtra("key", key);
                                            intent.putExtra("province",province_text.getText().toString());
                                            intent.putExtra("area",area_text.getText().toString());
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "email has been use", Toast.LENGTH_LONG).show();
                                        }
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
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();

//        mAuth.createUserWithEmailAndPassword(textemail, textpass)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            firebaseUser = mAuth.getCurrentUser();
//                            String key = firebaseUser.getUid();
//                            Intent intent = new Intent(RegisterAcitivityTrue.this, OTPActivityTrue.class);
//                            Toast.makeText(getApplicationContext(), "Message has sent", Toast.LENGTH_LONG).show();
//                            intent.putExtra("name", textuser);
//                            intent.putExtra("email", textemail);
//                            intent.putExtra("password", textpass);
//                            intent.putExtra("numberphone", phonenumber);
//                            intent.putExtra("key", key);
//                            startActivity(intent);
//                        } else {
//                                Toast.makeText(getApplicationContext(), "email has been use", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterAcitivityTrue.this,LoginActivityTrue.class);
        startActivity(intent);
        finish();
    }
    private void onCitySelected() {
        spinner2.setClickable(true);
        Log.d("test", "City = " + citySelected);

        if (citySelected.equals("กรุงเทพมหานคร")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.city1, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    if(citySelected2.equals("--Area--")){
                        citySelected2 = parent.getItemAtPosition(position+1).toString();
                        area_text.setText(citySelected2);
                        Toast.makeText(RegisterAcitivityTrue.this, area_text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        area_text.setText(citySelected2);
                        Toast.makeText(RegisterAcitivityTrue.this, area_text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
//                    area_text.setText(citySelected2);
//                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กระบี่")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city2, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กาญจนบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city3, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กาฬสินธุ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city4, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กำแพงเพชร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city5, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ขอนแก่น")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city6, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("จันทบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city7, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ฉะเชิงเทรา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city8, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ชลบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city9, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ชัยนาท")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city10, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else if (citySelected.equals("ชัยภูมิ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city11, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        else if (citySelected.equals("ชุมพร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city12, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if (citySelected.equals("เชียงราย")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city13, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เชียงใหม่")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city14, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ตรัง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city15, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ตราด")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city16, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ตาก")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city77, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครนายก")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city17, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครปฐม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city18, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครพนม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city19, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครราชสีมา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city20, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครศรีธรรมราช")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city21, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if (citySelected.equals("นครสวรรค์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city22, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นนทบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city23, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นราธิวาส")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city24, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("น่าน")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city25, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("บึงกาฬ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city26, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("บุรีรีมย์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city27, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ปทุมธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city28, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ประจวบขีรีขันธ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city29, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ปราจีนบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city30, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ปัตตานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city31, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พระนครศรีอยุธยา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city32, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พะเยา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city33, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พังงา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city34, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พัทลุง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city35, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พิจิตร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city36, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พิษณุโลก")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city37, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เพชรบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city38, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    //Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เพชรบูรณ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city39, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("แพร่")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city40, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ภูเก็ต")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city41, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("มหาสารคาม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city42, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("มุกดาหาร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city43, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("แม่ฮองสอน")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city44, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ยโสธร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city45, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ยะลา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city46, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ร้อยเอ็ด")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city47, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ระนอง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city48, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ระยอง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city49, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ราชบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city50, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ลพบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city51, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ลำปาง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city52, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if (citySelected.equals("ลำพูน")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city53, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เลย")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city54, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ศรีสะเกต")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city55, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สกลนคร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city56, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สงขลา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city57, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สตูล")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city58, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สมุทรปราการ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city59, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สมุทรสงตราม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city60, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สมุทรสาคร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city61, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สระแก้ว")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city62, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สระบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city63, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สิงห์บุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city64, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุโขทัย")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city65, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุพรรณบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city66, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุราษฏร์ธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city67, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุรินทร์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city68, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("หนองบัวลำภู")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city70, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อ่างทอง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city71, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อำนาจเจริญ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city72, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุดรธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city73, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุตรดิตถ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city74, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุทัยธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city75, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุบลราชธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.city76, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(getApplicationContext(),area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
}}
