package com.example.user.otp5.MenuApp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.otp5.EncodeAndDecodePassword;
import com.example.user.otp5.Model.Account;
import com.example.user.otp5.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference reference,ref;
    FirebaseUser firebaseUser;
    static final int REQUEST_IMAGE_CAPTURE = 101;
    static final int SELECT_FILE = 1;
    CircleImageView myphoto;
    Button takephoto,save,edit;
    MaterialEditText password,numphone,user;
    Spinner spinner,spinner2;
    TextView province_text,area_text,email;
    String citySelected = "";
    int citySelectIndex;
    String citySelected2 = "";
    int citySelected2Index;
    View view;
    String usernamem = " ";
    String passwordm = " ";
    String addressm = " ";
    String emailm = " ";
    String telephonem = " ";
    String urim = "";
    Uri uriImg;
    int c = 0;
    StorageTask uploadTask;
    StorageReference storageReference;
    EncodeAndDecodePassword encodedeconde;
    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myphoto = findViewById(R.id.myphoto);
        //takephoto = findViewById(R.id.take_a_photo);
        save = findViewById(R.id.button_save);
        password = findViewById(R.id.mypassword);
        //address = findViewById(R.id.myaddress);
        numphone = findViewById(R.id.mynumberphone);
        user = findViewById(R.id.myuser);
        edit = findViewById(R.id.button_edit);
        encodedeconde = new EncodeAndDecodePassword();

        email = findViewById(R.id.myemail);
        spinner = findViewById(R.id.province_select);
        spinner2 = findViewById(R.id.area_select);
        province_text = findViewById(R.id.province_text);
        area_text = findViewById(R.id.area_text);


        spinner.setClickable(false);
        spinner2.setClickable(false);

        user.setClickable(false);
        user.setFocusable(false);
        user.setFocusableInTouchMode(false);

        myphoto.setClickable(false);
        myphoto.setFocusable(false);
        myphoto.setFocusableInTouchMode(false);

        numphone.setClickable(false);
        numphone.setFocusable(false);
        numphone.setFocusableInTouchMode(false);

        password.setClickable(false);
        password.setFocusable(false);
        password.setFocusableInTouchMode(false);




        storageReference = FirebaseStorage.getInstance().getReference("users");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference("Chat");


        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //c = 0;
                Log.i("key1",firebaseUser.getUid());
                account = new Account();
                account = dataSnapshot.getValue(Account.class);
                user.setText(account.getName());
                password.setText(encodedeconde.deCode(account.getPass()));
                numphone.setText(account.getNumber_phone());
                email.setText(account.getEmail());
                province_text.setText(account.getProvince());
                area_text.setText(account.getArea());
                //c = 1;
                if (account.getImage().equals("default")) {
                    //myphoto.setImageResource(R.mipmap.profile);
                } else {
                    Glide.with(getApplicationContext()).load(account.getImage()).into(myphoto);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"You can edit your profile",Toast.LENGTH_SHORT).show();
                c = 0;
                user.setClickable(true);
                user.setFocusable(true);
                user.setFocusableInTouchMode(true);

                myphoto.setClickable(true);
                myphoto.setFocusable(true);
                myphoto.setFocusableInTouchMode(true);

                password.setClickable(true);
                password.setFocusable(true);
                password.setFocusableInTouchMode(true);

                spinner.setClickable(true);
                spinner2.setClickable(true);

                province_text.setText(account.getProvince());
                area_text.setText(account.getArea());

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        //citySelectIndex = parent.getSelectedItemPosition();
                        citySelected = parent.getItemAtPosition(position).toString();
                        if(citySelected.equals("--Province--")){

                        }else {
                            province_text.setText(citySelected);
                            Toast.makeText(ProfileActivity.this,province_text.getText().toString(),Toast.LENGTH_SHORT).show();
                            onCitySelected();
                        }


                        //onCitySelected();




                         // 16->77
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
//                        citySelectIndex = parent.getSelectedItemPosition();
//                        citySelected = parent.getItemAtPosition(citySelectIndex).toString();
//                        province_text.setText(citySelected);

//                        Toast.makeText(ProfileActivity.this,province_text.getText().toString(),Toast.LENGTH_SHORT).show();
//                        onCitySelected();
//                        province_text.setText(account.getProvince());
//                        area_text.setText(account.getArea());
                        //province_text.setText(province_text.getText().toString());
                    }
                });


            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner.setClickable(false);
                spinner2.setClickable(false);

                user.setClickable(false);
                user.setFocusable(false);
                user.setFocusableInTouchMode(false);

                myphoto.setClickable(false);
                myphoto.setFocusable(false);
                myphoto.setFocusableInTouchMode(false);


                password.setClickable(false);
                password.setFocusable(false);
                password.setFocusableInTouchMode(false);

                if (user.getText().toString().length() < 4 || user.getText().toString().length() > 20) {
                    user.setError("Your Username should contain 4-20 characters");
                    user.requestFocus();
                    user.setClickable(true);
                    user.setFocusable(true);
                    user.setFocusableInTouchMode(true);

                    myphoto.setClickable(true);
                    myphoto.setFocusable(true);
                    myphoto.setFocusableInTouchMode(true);

                    password.setClickable(true);
                    password.setFocusable(true);
                    password.setFocusableInTouchMode(true);

                    spinner.setClickable(true);
                    return;
                }
                if (password.getText().toString().length() < 8) {
                    password.setError("Your Password should contain 8-20 characters");
                    password.requestFocus();

                    user.setClickable(true);
                    user.setFocusable(true);
                    user.setFocusableInTouchMode(true);

                    myphoto.setClickable(true);
                    myphoto.setFocusable(true);
                    myphoto.setFocusableInTouchMode(true);

                    password.setClickable(true);
                    password.setFocusable(true);
                    password.setFocusableInTouchMode(true);

                    spinner.setClickable(true);
                    return;


                }
                if(province_text.getText().toString().equals("")) {
                    province_text.setError("Please select Province your left");
                    province_text.requestFocus();

                    user.setClickable(true);
                    user.setFocusable(true);
                    user.setFocusableInTouchMode(true);

                    myphoto.setClickable(true);
                    myphoto.setFocusable(true);
                    myphoto.setFocusableInTouchMode(true);

                    password.setClickable(true);
                    password.setFocusable(true);
                    password.setFocusableInTouchMode(true);

                    spinner.setClickable(true);
                   // spinner2.setClickable(false);

                    return;
                }

                if(area_text.getText().toString().equals("")) {
                    area_text.setError("Please select Area your left");
                    area_text.requestFocus();

                    user.setClickable(true);
                    user.setFocusable(true);
                    user.setFocusableInTouchMode(true);

                    myphoto.setClickable(true);
                    myphoto.setFocusable(true);
                    myphoto.setFocusableInTouchMode(true);

                    password.setClickable(true);
                    password.setFocusable(true);
                    password.setFocusableInTouchMode(true);

                    spinner.setClickable(true);
                    // spinner2.setClickable(false);

                    return;
                }
                else {

//                HashMap<String,Object> hashMap =new HashMap<>();
//                hashMap.put("pass",password.getText()+"");
//                hashMap.put("address",address.getText()+"");
//                hashMap.put("number_phone",numphone.getText()+"");
//                hashMap.put("image",urim);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("key2", firebaseUser.getUid());
                            Account account = dataSnapshot.getValue(Account.class);
                            Log.i("key23", account.getKey());
                            account.setName(user.getText()+"");
                            account.setPass(encodedeconde.enCode(password.getText() + ""));
                           // account.setAddress(address.getText() + "");
                            account.setNumber_phone(numphone.getText() + "");
                            account.setImage(account.getImage());
                            account.setProvince(province_text.getText()+"");
                            account.setArea(area_text.getText()+"");
//                            account.setImage(urim);
                            reference.setValue(account);
                            Toast.makeText(getApplicationContext(), "saved your profile", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    spinner.setClickable(false);
                    spinner2.setClickable(false);

                    user.setClickable(false);
                    user.setFocusable(false);
                    user.setFocusableInTouchMode(false);

                    myphoto.setClickable(false);
                    myphoto.setFocusable(false);
                    myphoto.setFocusableInTouchMode(false);


                    password.setClickable(false);
                    password.setFocusable(false);
                    password.setFocusableInTouchMode(false);
                }


            }
        });


//        if (urim != null) {
//            if (urim.equals("default")) {
//                myphoto.setImageResource(R.mipmap.profile);
//            } else {
//                Glide.with(getApplicationContext()).load(urim).into(myphoto);
//            }
//        }

        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Choose photo from libraries","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Add photo !")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(items[which].equals("Choose photo from libraries")){
                                    openImage();
                                }
                                else if(items[which].equals("Cancel")){
                                    dialog.cancel();
                                }
                            }
                        });
                builder.show();


            }
        });
    }

    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        startActivityForResult(intent.createChooser(intent,"Select File"), SELECT_FILE);
    }



//    public void takemyphoto(){
//        Intent imageTakephoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(imageTakephoto.resolveActivity(getPackageManager())!=null){
//
//            startActivityForResult(imageTakephoto,REQUEST_IMAGE_CAPTURE);
//
//        }
//
//    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadImage(){
        final ProgressDialog progressDialog = ProgressDialog.show(ProfileActivity.this, "Please wait.",
                "Uploading..!", true);


        if (uriImg != null){
            final StorageReference fileRef = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uriImg));

//            fileRef.putFile(uriImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//
//                    progressDialog.dismiss();
//                    //Toast.makeText(getApplicationContext(),"xxx",Toast.LENGTH_SHORT).show();
//                }
//            });

            uploadTask = fileRef.putFile(uriImg);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();

                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        urim = mUri;
                        Glide.with(getApplicationContext()).load(urim).into(myphoto);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Account account = dataSnapshot.getValue(Account.class);
                                account.setImage(urim);
                                reference.setValue(account);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("uriImage",mUri);
//                        reference.updateChildren(map);


                        progressDialog.dismiss();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed ! ",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "No image Selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i("a",requestCode+" "+resultCode);


        if(resultCode == RESULT_OK){
//            if(requestCode == REQUEST_IMAGE_CAPTURE){
////                uriImg = data.getData();
////                uriImg = selectedImageUri;
//                //Glide.with(getApplicationContext()).load(uriImg).into(myphoto);
//                Log.i("uri",uriImg+"");
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap)extras.get("data");
//
//                uploadImage();
//                //urim = selectedImageUri.toString();
//                //Glide.with(getApplicationContext()).load(imageBitmap.toString()).into(myphoto);
////                Uri a = Uri.parse(imageBitmap.toString());
////                urim = a.toString();
//            }
            if(requestCode == SELECT_FILE){
                //uploadImage();
                Uri selectedImageUri = data.getData();
                uriImg = selectedImageUri;
                uploadImage();
                //urim = selectedImageUri.toString();

//                myphoto.setImageURI(Uri.parse(urim));
            }

        }



    }
    private void onCitySelected() {

        Log.d("test", "City = " + citySelected);
        spinner2.setClickable(true);
//        if(citySelected.equals("Select Province")){
//
//        }
        if (citySelected.equals("กรุงเทพมหานคร")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city1, android.R.layout.simple_spinner_item);
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
                        Toast.makeText(ProfileActivity.this, area_text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        area_text.setText(citySelected2);
                        Toast.makeText(ProfileActivity.this, area_text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
//                    province_text.setText(citySelected);
//                    area_text.setText(citySelected2);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
//                    province_text.setText(account.getProvince());
//                    area_text.setText(account.getArea());
                    //province_text.setText(province_text.getText().toString());
                }
            });

        }
        else if (citySelected.equals("กระบี่")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city2, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กาญจนบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city3, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กาฬสินธุ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city4, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("กำแพงเพชร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city5, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ขอนแก่น")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city6, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("จันทบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city7, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ฉะเชิงเทรา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city8, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ชลบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city9, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ชัยนาท")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city10, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }else if (citySelected.equals("ชัยภูมิ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city11, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        else if (citySelected.equals("ชุมพร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city12, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if (citySelected.equals("เชียงราย")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city13, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เชียงใหม่")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city14, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ตรัง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city15, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ตราด")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city16, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ตาก")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city77, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครนายก")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city17, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครปฐม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city18, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครพนม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city19, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครราชสีมา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city20, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นครศรีธรรมราช")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city21, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if (citySelected.equals("นครสวรรค์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city22, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นนทบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city23, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("นราธิวาส")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city24, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("น่าน")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city25, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("บึงกาฬ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city26, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("บุรีรีมย์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city27, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ปทุมธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city28, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ประจวบขีรีขันธ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city29, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ปราจีนบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city30, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ปัตตานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city31, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พระนครศรีอยุธยา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city32, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พะเยา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city33, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พังงา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city34, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พัทลุง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city35, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พิจิตร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city36, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("พิษณุโลก")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city37, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เพชรบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city38, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เพชรบูรณ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city39, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("แพร่")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city40, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ภูเก็ต")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city41, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("มหาสารคาม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city42, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("มุกดาหาร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city43, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("แม่ฮองสอน")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city44, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    citySelected2Index = parent.getSelectedItemPosition();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ยโสธร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city45, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ยะลา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city46, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ร้อยเอ็ด")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city47, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ระนอง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city48, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ระยอง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city49, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ราชบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city50, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ลพบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city51, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ลำปาง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city52, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if (citySelected.equals("ลำพูน")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city53, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("เลย")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city54, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("ศรีสะเกต")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city55, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สกลนคร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city56, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สงขลา")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city57, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สตูล")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city58, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สมุทรปราการ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city59, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สมุทรสงตราม")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city60, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สมุทรสาคร")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city61, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สระแก้ว")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city62, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สระบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city63, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สิงห์บุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city64, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุโขทัย")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city65, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุพรรณบุรี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city66, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุราษฏร์ธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city67, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("สุรินทร์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city68, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("หนองบัวลำภู")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city70, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อ่างทอง")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city71, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อำนาจเจริญ")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city72, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุดรธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city73, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุตรดิตถ์")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city74, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุทัยธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city75, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        else if (citySelected.equals("อุบลราชธานี")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ProfileActivity.this,R.array.city76, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter);
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    citySelected2Index = parent.getSelectedItemPosition();
                    citySelected2 = parent.getItemAtPosition(position).toString();
                    area_text.setText(citySelected2);
                    Toast.makeText(ProfileActivity.this,area_text.getText().toString(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

}
