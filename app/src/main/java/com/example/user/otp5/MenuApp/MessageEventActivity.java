package com.example.user.otp5.MenuApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.otp5.Adapter.Adapter_chat;
import com.example.user.otp5.Bill;
import com.example.user.otp5.MenuAppActivity;
import com.example.user.otp5.Model.Account;
import com.example.user.otp5.Model.Chat;
import com.example.user.otp5.Model.Room;
import com.example.user.otp5.R;
import com.example.user.otp5.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageEventActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Room> list_room,list_room_current;
    ArrayList<Account> list_user;
    ArrayList<Chat> list_chat;
    Adapter_chat adapter_chat;
    CustomAdapter customAdapter;
    TextView orgin,destination,km,price,user,num_room,success,id,email,numberphone,date,time,textView,nameRoom,province,area;
    FirebaseUser mUser;
    DatabaseReference reference,databaseReference,ref,ref1;
    RecyclerView recyclerView;
    EditText text_send;
    ImageButton btn_send;
    String name = "",user_photo="",btn_ready="";
    Button btn_backtomenu;
    ImageView imageView1,imageView2,imageView3,imageView4;
    int c,count;
    private volatile boolean stopThread = false;
    int count_click;
    View view;
    Account account;
    Thread thread1;
    ExampleRunnable runnable;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_event);

//        runnable = new ExampleRunnable(10);
//        thread1 = new Thread(runnable);
//        if (thread1.getState() == Thread.State.NEW)
//        {
//            thread1.start();
//        }
        //thread1.interrupt();
        c = 0;
        count = 0;
        reference = FirebaseDatabase.getInstance().getReference("Room");
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        count_click = 0;
        ref = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               account = new Account();
                account = dataSnapshot.getValue(Account.class);
                name = account.getName();
                user_photo = account.getImage();
                btn_ready = account.getReady();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView = findViewById(R.id.list_view_message);

        list_user = new ArrayList<>();
        list_chat = new ArrayList<>();
        list_room_current = new ArrayList<>();
        list_room = new ArrayList<>();
        list_room = (ArrayList<Room>) getIntent().getSerializableExtra("room");

        reference.child(list_room.get(0).getKey());

        customAdapter = new CustomAdapter(getApplicationContext(),list_room);
        listView.setAdapter(customAdapter);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(mUser.getUid().equals(list_room.get(0).getUser1_id())){
            readMessages(list_room.get(0).getKey());
        }

        if(mUser.getUid().equals(list_room.get(0).getUser2_id())){
            readMessages(list_room.get(0).getKey());
        }
        if(mUser.getUid().equals(list_room.get(0).getUser3_id())){
            readMessages(list_room.get(0).getKey());
        }
        if(mUser.getUid().equals(list_room.get(0).getUser4_id())){
            readMessages(list_room.get(0).getKey());
        }
//        if(mUser.getUid().equals(list_room.get(0).getUser5_id())){
//            readMessages(mUser.getUid(),list_room.get(0).getUser1_id(),
//                    list_room.get(0).getUser2_id(),list_room.get(0).getUser3_id(),
//                    list_room.get(0).getUser4_id(),list_room.get(0).getKey());
//        }

        text_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    //me ,other user ,message
                    if(mUser.getUid().equals(list_room.get(0).getUser1_id())){
                        sendMessage(mUser.getUid(),list_room.get(0).getUser2_id(),
                                list_room.get(0).getUser3_id(),list_room.get(0).getUser4_id(),
                                msg,list_room.get(0).getKey());
                    }

                    if(mUser.getUid().equals(list_room.get(0).getUser2_id())){
                        sendMessage(mUser.getUid(),list_room.get(0).getUser1_id(),
                                list_room.get(0).getUser3_id(),list_room.get(0).getUser4_id(),
                                msg,list_room.get(0).getKey());
                    }
                    if(mUser.getUid().equals(list_room.get(0).getUser3_id())){
                        sendMessage(mUser.getUid(),list_room.get(0).getUser1_id(),
                                list_room.get(0).getUser2_id(),list_room.get(0).getUser4_id(),
                                msg,list_room.get(0).getKey());
                    }
                    if(mUser.getUid().equals(list_room.get(0).getUser4_id())){
                        sendMessage(mUser.getUid(),list_room.get(0).getUser1_id(),
                                list_room.get(0).getUser2_id(),list_room.get(0).getUser3_id(),
                                msg,list_room.get(0).getKey());
                    }
//                    if(mUser.getUid().equals(list_room.get(0).getUser5_id())){
//                        sendMessage(mUser.getUid(),list_room.get(0).getUser1_id(),
//                                list_room.get(0).getUser2_id(),list_room.get(0).getUser3_id(),
//                                list_room.get(0).getUser4_id(),msg,list_room.get(0).getKey());
//                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"You can't send message",Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });
        textView = findViewById(R.id.text_count);
        final TextView total_user_ready = findViewById(R.id.total_user_ready);
        //update value
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_room_current.clear();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    if(dataSnapshot1.getValue(Room.class).getKey().equals(list_room.get(0).getKey())){
                        list_room_current.add(dataSnapshot1.getValue(Room.class));
                        Log.i("key_current",list_room_current.get(0).getKey());

                        customAdapter = new CustomAdapter(getApplicationContext(),list_room_current);
                        listView.setAdapter(customAdapter);
                        break;
                    }
                    //Log.i("total_user_ready",list_room_current.get(0).getTotal_user()+"");

                }


                //Log.i("total_user_ready",list_room_current.get(0).getTotal_ready()+"");
                //Log.i("user_user_not_ready",list_room_current.get(0).getTotal_user()+"");
//                if(dataSnapshot.getValue(Room.class) == null){
//                    Intent intent = new Intent(MessageEventActivity.this, MenuAppActivity.class);
//                    startActivityForResult(intent,0);
//                    finish();
//                    return;
////                    textView.setText(list_room_current.get(0).getTotal_ready()+"");
////                    total_user_ready.setText(list_room_current.get(0).getTotal_user()+"");
////                    return;
//                }
                if(account.getReady().equals("notready") && count_click ==0 && (list_room_current.get(0).getTotal_ready()<list_room_current.get(0).getTotal_user())) {
                    button.setText("ready");
                    button.setBackgroundResource(R.drawable.background_green);
                }
                else {
                    button.setText("not ready");
                    button.setBackgroundResource(R.drawable.background_red);
                }

                if(count==0){
                    textView.setText(list_room_current.get(0).getTotal_ready()+"");
                    total_user_ready.setText(list_room_current.get(0).getTotal_user()+"");
                }
                if(count==0 && c==0){
                    startThread();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        button = findViewById(R.id.count_user);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getReady().equals("notready") && count_click ==0 && (list_room_current.get(0).getTotal_ready()<list_room_current.get(0).getTotal_user())) {
                    button.setText("not ready");
                    button.setBackgroundResource(R.drawable.background_red);
                    list_room_current.get(0).setTotal_ready(list_room_current.get(0).getTotal_ready() + 1);
                    //list_room_current.get(0).setStatus("");
                    textView.setText(list_room_current.get(0).getTotal_ready() + "");
                    count_click++;
                    account.setReady("ready");
                    ref.setValue(account);
                    reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));
                    //startThread();


                }

                else {
                    button.setText("Ready");
                    button.setBackgroundResource(R.drawable.background_green);
                    Toast.makeText(getApplicationContext(),"Not ready",Toast.LENGTH_SHORT).show();
                    count_click = 0;
                    list_room_current.get(0).setTotal_ready(list_room_current.get(0).getTotal_ready() - 1);
                    textView.setText(list_room_current.get(0).getTotal_ready() + "");
                    account.setReady("notready");
                    ref.setValue(account);
                    reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));
                    stopThread(view);
                }



            }
        });
        btn_backtomenu = findViewById(R.id.back_to_menu);
        btn_backtomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account.setStatus("have");
                ref.setValue(account);
                Toast.makeText(MessageEventActivity.this,"back to menu",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MessageEventActivity.this,MenuAppActivity.class);
                startActivityForResult(intent,0);
                finish();
            }
        });


//        btn_readytogo = findViewById(R.id.btn_ready);
//        btn_readytogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(MessageEventActivity.this);
//                builder1.setMessage("Are you ready to go?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Handler handler = new Handler();
//                                if(list_room_current.get(0).getTotal_ready()==4 && list_room_current.get(0).getTotal_user()==4 )
//                                {
//                                    handler.postDelayed(runnable,3000);
//
//                                }else if(list_room_current.get(0).getTotal_ready()==3 && list_room_current.get(0).getTotal_user()==3){
//                                    handler.postDelayed(runnable,3000);
//                                }
//                                else if(list_room_current.get(0).getTotal_ready()==2 && list_room_current.get(0).getTotal_user()==2){
//                                    handler.postDelayed(runnable,3000);
//                                }
//                                else if(list_room_current.get(0).getTotal_ready()==1 && list_room_current.get(0).getTotal_user()==1){
//                                    handler.postDelayed(runnable,3000);
//                                }
////                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
////                                    @Override
////                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                        final Account account = dataSnapshot.getValue(Account.class);
////                                        account.setReady("ready");
//////                                        count = (int)dataSnapshot.getChildrenCount()+1-8;
//////                                        Log.i("count",count+"");
//////                                        account.setImage(count);
//////                                        Log.i("account.getImage()",account.getImage()+"");
////
////                                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MessageEventActivity.this);
////                                        mBuilder.setCancelable(false);
////                                        View mview = getLayoutInflater().inflate(R.layout.daiglog_ready,null);
////                                        imageView1 = mview.findViewById(R.id.image1);
////                                        imageView2 = mview.findViewById(R.id.image2);
////                                        imageView3 = mview.findViewById(R.id.image3);
////                                        imageView4 = mview.findViewById(R.id.image4);
////                                        success = mview.findViewById(R.id.success);
////                                        c = 0;
////
////                                        //todo check จนกว่าจะเท่ากัน 4 คน (database ของ Room)
////
////                                        if(account.getReady().equals("ready") && list_room.get(0).getUser1_id().equals(account.getKey())){
////                                            imageView1.setImageResource(R.mipmap.draw_correct);
////
////                                            //account.setImage(1);
////                                            list_room.get(0).setStatus("busy");
////                                            list_room.get(0).setTotal_ready(1+list_room.get(0).getTotal_ready());
////                                            reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                            ref.setValue(account);
////
////
////                                        }
////                                        if(account.getReady().equals("ready") && list_room.get(0).getUser2_id().equals(account.getKey())){
////                                            imageView2.setImageResource(R.mipmap.draw_correct);
////
////                                            //account.setImage(2);
////                                            list_room.get(0).setStatus("busy");
////                                            list_room.get(0).setTotal_ready(1+list_room.get(0).getTotal_ready());
////                                            reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                            ref.setValue(account);
////
////                                        }
////                                        if(account.getReady().equals("ready") && list_room.get(0).getUser3_id().equals(account.getKey())){
////                                            imageView3.setImageResource(R.mipmap.draw_correct);
////
////                                            //account.setImage(3);
////                                            list_room.get(0).setStatus("busy");
////                                            list_room.get(0).setTotal_ready(1+list_room.get(0).getTotal_ready());
////                                            reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                            ref.setValue(account);
////
////                                        }
////                                        if(account.getReady().equals("ready") && list_room.get(0).getUser4_id().equals(account.getKey())){
////                                            imageView4.setImageResource(R.mipmap.draw_correct);
////
////                                            //account.setImage(4);
////                                            list_room.get(0).setStatus("busy");
////                                            list_room.get(0).setTotal_ready(1+list_room.get(0).getTotal_ready());
////                                            reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                            ref.setValue(account);
////
////                                        }
////
////                                        //todo เมื่อสำเร็จ
////
////                                        Handler handler = new Handler();
////                                        if(list_room.get(0).getTotal_ready()==4 && list_room.get(0).getTotal_user()==4 )
////                                        {
////                                            handler.postDelayed(runnable,3000);
////
////                                        }else if(list_room.get(0).getTotal_ready()==3 && list_room.get(0).getTotal_user()==3){
////                                            handler.postDelayed(runnable,3000);
////                                        }
////                                        else if(list_room.get(0).getTotal_ready()==2 && list_room.get(0).getTotal_user()==2){
////                                            handler.postDelayed(runnable,3000);
////                                        }
////                                        else if(list_room.get(0).getTotal_ready()==1 && list_room.get(0).getTotal_user()==1){
////                                            handler.postDelayed(runnable,3000);
////                                        }
////
////
////                                        Button button = mview.findViewById(R.id.cannel);
////                                        button.setOnClickListener(new View.OnClickListener() {
////                                            @Override
////                                            public void onClick(View v) {
////
////
////                                                if(list_room.get(0).getUser1_id().equals(account.getKey())){
////                                                    list_room.get(0).setTotal_ready(list_room.get(0).getTotal_ready()-1);
////                                                    list_room.get(0).setStatus("available");
////                                                    //reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                                    imageView1.setImageResource(R.mipmap.draw_not_correct);
////                                                }
////                                                if(list_room.get(0).getUser2_id().equals(account.getKey())){
////                                                    list_room.get(0).setTotal_ready(list_room.get(0).getTotal_ready()-1);
////                                                    list_room.get(0).setStatus("available");
////                                                    //reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                                    imageView2.setImageResource(R.mipmap.draw_not_correct);
////                                                }
////                                                if(list_room.get(0).getUser3_id().equals(account.getKey())){
////                                                    list_room.get(0).setTotal_ready(list_room.get(0).getTotal_ready()-1);
////                                                    list_room.get(0).setStatus("available");
////                                                    //reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                                    imageView3.setImageResource(R.mipmap.draw_not_correct);
////                                                }
////                                                if(list_room.get(0).getUser4_id().equals(account.getKey())){
////                                                    list_room.get(0).setTotal_ready(list_room.get(0).getTotal_ready()-1);
////                                                    list_room.get(0).setStatus("available");
////                                                    //reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                                    imageView4.setImageResource(R.mipmap.draw_not_correct);
////                                                }
////                                                reference.child(list_room.get(0).getKey()).setValue(list_room.get(0));
////                                                Intent intent = new Intent(MessageEventActivity.this, MenuAppActivity.class);
////                                                startActivity(intent);
////                                            }
////                                        });
////                                        mBuilder.setView(mview);
////                                        AlertDialog alertDialog = mBuilder.create();
////                                        alertDialog.show();
////
////                                    }
////
////                                    @Override
////                                    public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                                    }
////                                });
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(final DialogInterface dialog, int which) {
//                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                        Account account = dataSnapshot.getValue(Account.class);
////                                        account.setReady("notready");
////
////                                        ref.setValue(account);
//                                        dialog.cancel();
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//
//                            }
//                        });
//
//                AlertDialog alertDial = builder1.create();
//                alertDial.show();
//            }
//
//        });


//        customAdapter = new CustomAdapter(getApplicationContext(),list_room);
//        listView.setAdapter(customAdapter);


    }

//    public Runnable  runnable =new Runnable() {
//        @Override
//        public void run() {
//            //success.setText("Success");
//            //Toast.makeText(getApplicationContext(),"Ready",Toast.LENGTH_SHORT).show();
//            list_room_current.get(0).setStatus("busy");
//            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));
//            //Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(),Bill.class);
//            intent.putExtra("room_finish",list_room_current);
//            startActivity(intent);
//
//        }
//    };

    public void sendMessage(String sender,String receiver2,String receiver3,String receiver4,String message,String key){
        Chat chat = new Chat();
        chat.setName_user(name);
        chat.setSender(sender);
        chat.setReceiver2(receiver2);
        chat.setReceiver3(receiver3);
        chat.setReceiver4(receiver4);
        chat.setKey_room(key);
        chat.setMessage(message);
        chat.setImage(user_photo);

        databaseReference.child(databaseReference.push().getKey()).setValue(chat);
    }

    public void readMessages(final String key){
        list_chat = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_chat.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getKey_room().equals(key)){
                        list_chat.add(chat);
                    }



                }
                adapter_chat = new Adapter_chat(getApplicationContext(),list_chat);
                recyclerView.setAdapter(adapter_chat);
                ref1 = FirebaseDatabase.getInstance().getReference("users");

                adapter_chat.setOnItemClickListener(new Adapter_chat.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int position) {

                        if(list_chat.get(position).getSender().equals(mUser.getUid())
                                ){
                            //Log.i("key1",list_chat.get(position).getSender());
                            ref1.child(list_chat.get(position).getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Account account = dataSnapshot.getValue(Account.class);

                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MessageEventActivity.this);
                                    mBuilder.setCancelable(true);

                                    View mview = getLayoutInflater().inflate(R.layout.daiglog_profile,null);


                                    id = mview.findViewById(R.id.name_id);
                                    email = mview.findViewById(R.id.email_id);
                                    numberphone = mview.findViewById(R.id.numberphone_id);
                                    province = mview.findViewById(R.id.province_text);
                                    area = mview.findViewById(R.id.area_text);

                                    id.setText(account.getName());
                                    email.setText(account.getEmail());
                                    numberphone.setText(account.getNumber_phone());
                                    area.setText("เขต : "+account.getArea());
                                    province.setText("จังหวัด : "+account.getProvince());

                                    CircleImageView myphoto = mview.findViewById(R.id.my_photo);
                                    if (account.getImage().equals("default")) {
                                        //default picture
                                    } else {
                                        Glide.with(getApplicationContext()).load(account.getImage()).into(myphoto);
                                    }


                                    mBuilder.setView(mview);
                                    AlertDialog alertDialog = mBuilder.create();
                                    alertDialog.show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else {
                            ref1.child(list_chat.get(position).getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Account account = dataSnapshot.getValue(Account.class);

                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MessageEventActivity.this);
                                    mBuilder.setCancelable(true);

                                    View mview = getLayoutInflater().inflate(R.layout.daiglog_profile,null);


                                    id = mview.findViewById(R.id.name_id);
                                    email = mview.findViewById(R.id.email_id);
                                    numberphone = mview.findViewById(R.id.numberphone_id);
                                    province = mview.findViewById(R.id.province_text);
                                    area = mview.findViewById(R.id.area_text);

                                    id.setText(account.getName());
                                    email.setText(account.getEmail());
                                    numberphone.setText(account.getNumber_phone());
                                    area.setText("เขต : "+account.getArea());
                                    province.setText("จังหวัด : "+account.getProvince());

                                    CircleImageView myphoto = mview.findViewById(R.id.my_photo);
                                    if (account.getImage().equals("default")) {
                                        //default picture
                                    } else {
                                        Glide.with(getApplicationContext()).load(account.getImage()).into(myphoto);
                                    }

                                    mBuilder.setView(mview);
                                    AlertDialog alertDialog = mBuilder.create();
                                    alertDialog.show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MessageEventActivity.this);
        builder.setMessage("Are you sure to exit this room?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //todo หัวห้องออกต้องพัง user1
                        //todo หัวห้องออกต้องพัง user1
                        //todo หัวห้องออกต้องพัง user1

                        if(list_room_current.get(0).getUser1_id().equals(mUser.getUid())){
                            list_room_current.get(0).setUser1_id("default");
                            list_room_current.get(0).setUser1(0);
                            list_room_current.get(0).setTotal_user(list_room_current.get(0).getTotal_user()-1);

                            if(list_room_current.get(0).getTotal_user()==0){
                                count=1;
                                list_room_current.get(0).setStatus("destroy");
                            }
                            account.setStatus("new");
                            account.setReady("notready");
                            ref.setValue(account);
                            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));

                            Intent intent = new Intent(MessageEventActivity.this,MenuAppActivity.class);
                            startActivityForResult(intent,0);
                            finish();
                        }
                        else if(list_room_current.get(0).getUser2_id().equals(mUser.getUid())) {
                            list_room_current.get(0).setUser2_id("default");
                            list_room_current.get(0).setUser2(0);
                            list_room_current.get(0).setTotal_user(list_room_current.get(0).getTotal_user() - 1);

                            if(list_room_current.get(0).getTotal_user()==0){
                                count=1;
                                list_room_current.get(0).setStatus("destroy");
                            }
                            account.setStatus("new");
                            account.setReady("notready");
                            ref.setValue(account);
                            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));

                            Intent intent = new Intent(MessageEventActivity.this, MenuAppActivity.class);
                            startActivityForResult(intent,0);
                            finish();
                        }
                        else if(list_room_current.get(0).getUser3_id().equals(mUser.getUid())) {
                            list_room_current.get(0).setUser3_id("default");
                            list_room_current.get(0).setUser3(0);
                            list_room_current.get(0).setTotal_user(list_room_current.get(0).getTotal_user() - 1);

                            if(list_room_current.get(0).getTotal_user()==0){
                                count=1;
                                list_room_current.get(0).setStatus("destroy");
                            }
                            account.setStatus("new");
                            account.setReady("notready");
                            ref.setValue(account);
                            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));

                            Intent intent = new Intent(MessageEventActivity.this, MenuAppActivity.class);
                            startActivityForResult(intent,0);
                            finish();

                        }
                        else if(list_room_current.get(0).getUser4_id().equals(mUser.getUid())) {
                            list_room_current.get(0).setUser4_id("default");
                            list_room_current.get(0).setUser4(0);
                            list_room_current.get(0).setTotal_user(list_room_current.get(0).getTotal_user() - 1);

                            if(list_room_current.get(0).getTotal_user()==0){
                                count=1;
                                list_room_current.get(0).setStatus("destroy");
                            }
                            account.setStatus("new");
                            account.setReady("notready");
                            ref.setValue(account);
                            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));

                            Intent intent = new Intent(MessageEventActivity.this, MenuAppActivity.class);
                            startActivityForResult(intent,0);
                            finish();

                        }

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


    class CustomAdapter extends BaseAdapter{

        Context context;
        ArrayList<Room> custom_room;
        LayoutInflater layoutInflater;

        public CustomAdapter(Context context, ArrayList<Room> custom_room) {
            this.context = context;
            this.custom_room = custom_room;
        }

        @Override
        public int getCount() {
            return custom_room.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.room_item,parent,false);

            orgin = convertView.findViewById(R.id.address_origin_);
            destination = convertView.findViewById(R.id.address_destination_);
            km = convertView.findViewById(R.id.km);
            price = convertView.findViewById(R.id.price);
            user = convertView.findViewById(R.id.total_user);
            num_room = convertView.findViewById(R.id.num_room);
            date = convertView.findViewById(R.id.date);
            time = convertView.findViewById(R.id.time);
            nameRoom = convertView.findViewById(R.id.name_room);


            orgin.setText(custom_room.get(i).getOrigin());
            destination.setText(custom_room.get(i).getDestination());
            km.setText(custom_room.get(i).getKm());
            price.setText(custom_room.get(i).getBaht()+"");
            user.setText(custom_room.get(i).getTotal_user()+"");
            num_room.setText(custom_room.get(i).getRoom_no()+"");
            date.setText(custom_room.get(i).getMonths()+"/"+custom_room.get(i).getDays()+"/"+custom_room.get(i).getYears());
            time.setText(custom_room.get(i).getHours()+" : "+custom_room.get(i).getMinites());
            nameRoom.setText(custom_room.get(i).getName_room());



            return convertView;

        }
    }

    public void logout(){
        Intent intent = new Intent(getApplicationContext(),StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout1:
                FirebaseAuth.getInstance().signOut();
                logout();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void startThread() {
        //Handler handler = new Handler();
        if(list_room_current.get(0).getTotal_ready()==list_room_current.get(0).getTotal_user()
                && ((list_room_current.get(0).getUser1_id().equals(mUser.getUid())
                     ||
                        list_room_current.get(0).getUser2_id().equals(mUser.getUid())
                         ||
                 list_room_current.get(0).getUser3_id().equals(mUser.getUid())
        ||
                list_room_current.get(0).getUser4_id().equals(mUser.getUid()))))



        {
            Toast.makeText(getApplicationContext(), "Ready in 5 seconds", Toast.LENGTH_SHORT).show();
//            handler.postDelayed(runnable,5000);
            stopThread = false;
            runnable = new ExampleRunnable(10);
            thread1 = new Thread(runnable);
            if (thread1.getState() == Thread.State.NEW)
            {
                thread1.start();
            }
            //thread1.interrupt();
//            ExampleRunnable runnable = new ExampleRunnable(10);
//            new Thread(runnable).start();
           // new Thread(runnable).stop();


//        }else if(list_room_current.get(0).getTotal_ready()==3 && list_room_current.get(0).getTotal_user()==3){
//            Toast.makeText(getApplicationContext(), "Ready in 5 seconds", Toast.LENGTH_SHORT).show();
//            //handler.postDelayed(runnable,5000);
//            stopThread = false;
//            runnable = new ExampleRunnable(10);
//            thread1 = new Thread(runnable);
//            if (thread1.getState() == Thread.State.NEW)
//            {
//                c=1;
//                thread1.start();
//            }
//            thread1.interrupt();
////            ExampleRunnable runnable = new ExampleRunnable(10);
////            new Thread(runnable).start();
//            //new Thread(runnable).stop();
//
//        }
//        else if(list_room_current.get(0).getTotal_ready()==2 && list_room_current.get(0).getTotal_user()==2){
//            Toast.makeText(getApplicationContext(), "Ready in 5 seconds", Toast.LENGTH_SHORT).show();
//            //handler.postDelayed(runnable,5000);
//            stopThread = false;
//            runnable = new ExampleRunnable(10);
//            thread1 = new Thread(runnable);
//            if (thread1.getState() == Thread.State.NEW)
//            {
//                c=1;
//                thread1.start();
//            }
//            thread1.interrupt();
////            ExampleRunnable runnable = new ExampleRunnable(10);
////            new Thread(runnable).start();
//            //new Thread(runnable).stop();
//        }
//        else if(list_room_current.get(0).getTotal_ready()==1 && list_room_current.get(0).getTotal_user()==1){
//            Toast.makeText(getApplicationContext(), "Ready in 5 seconds", Toast.LENGTH_SHORT).show();
//            //handler.postDelayed(runnable,5000);
//            stopThread = false;
//            runnable = new ExampleRunnable(10);
//            thread1 = new Thread(runnable);
//            if (thread1.getState() == Thread.State.NEW)
//            {
//                c=1;
//                thread1.start();
//                Log.i("thread4",thread1.currentThread()+"");
//                //thread1.currentThread().interrupt();
//            }

//            new Thread(runnable).start();
            //new Thread(runnable).stop();
        }
        else {
            stopThread(view);
        }
//        stopThread = false;
//        ExampleRunnable runnable = new ExampleRunnable(10);
//        new Thread(runnable).start();
    }

    public void stopThread(View view) {
        stopThread = true;
    }

//    class GotoBill extends AsyncTask<String,Void,String>{
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            list_room_current.get(0).setStatus("busy");
//            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));
//            //Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(),Bill.class);
//            intent.putExtra("room_finish",list_room_current);
//            startActivity(intent);
//            finish();
//        }
//    }


    class ExampleRunnable implements Runnable {
        int seconds;

        ExampleRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {

                if (stopThread) {
                    //Toast.makeText(getApplicationContext(), "not ready", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(list_room_current.get(0).getTotal_ready()!=list_room_current.get(0).getTotal_user()
                        && ((list_room_current.get(0).getUser1_id().equals(mUser.getUid())
                        ||
                        list_room_current.get(0).getUser2_id().equals(mUser.getUid())
                        ||
                        list_room_current.get(0).getUser3_id().equals(mUser.getUid())
                        ||
                        list_room_current.get(0).getUser4_id().equals(mUser.getUid())))){
                    c = 0;
                    //stopThread = true;
                    return;
                   // Toast.makeText(getApplicationContext(), "Ready in 5 seconds", Toast.LENGTH_SHORT).show();
//            handler.postDelayed(runnable,5000);


                }
                if (i == 5 ) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            c= 1;
                            account.setStatus("new");
                            account.setReady("notready");
                            ref.setValue(account);
                            list_room_current.get(0).setStatus("busy");
                            reference.child(list_room_current.get(0).getKey()).setValue(list_room_current.get(0));
                            //Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MessageEventActivity.this,Bill.class);
                            intent.putExtra("room_finish",list_room_current);
                            startActivity(intent);
                            thread1.interrupt();
                            //Log.i("thread3",thread1.currentThread()+"");
                            //return;



                        }
                    });


                }

                Log.d("online", "startThread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (IllegalThreadStateException e) {
                    //Log.i("thread1",thread1.currentThread()+"");
                    //thread1.interrupt();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    //Log.i("thread2",thread1.currentThread()+"");
                    //thread1.interrupt();
                    e.printStackTrace();
                }
            }
        }
    }
}
