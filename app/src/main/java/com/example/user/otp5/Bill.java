package com.example.user.otp5;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.otp5.Adapter.Adapter_chat;
import com.example.user.otp5.Model.Account;
import com.example.user.otp5.Model.Chat;
import com.example.user.otp5.Model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Bill extends AppCompatActivity {
    ArrayList<Room> room_finish;
    ListView listView;
    ArrayList<Account> list_user;
    ArrayList<Chat> list_chat;
    Adapter_chat adapter_chat;
    CustomAdapter customAdapter;
    TextView orgin,destination,km,price,user,num_room,success,id,email,numberphone,date,time,area,province,name_room123;
    FirebaseUser mUser;
    DatabaseReference reference,databaseReference,ref,ref1;
    RecyclerView recyclerView;
    EditText text_send;
    ImageButton btn_send;
    String name = "",photo_user="";
//    Button btn_readytogo;
//    ImageView imageView1,imageView2,imageView3,imageView4;
//    int c = 0,count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        reference = FirebaseDatabase.getInstance().getReference("Room");
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat");
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        ref = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                name = account.getName();
                photo_user = account.getImage();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView = findViewById(R.id.list_view_message);

        list_user = new ArrayList<>();
        list_chat = new ArrayList<>();
        room_finish = new ArrayList<>();
        room_finish = (ArrayList<Room>) getIntent().getSerializableExtra("room_finish");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if(mUser.getUid().equals(room_finish.get(0).getUser1_id())){
            readMessages(room_finish.get(0).getKey());
        }
        if(mUser.getUid().equals(room_finish.get(0).getUser2_id())){
            readMessages(room_finish.get(0).getKey());
        }
        if(mUser.getUid().equals(room_finish.get(0).getUser3_id())){
            readMessages(room_finish.get(0).getKey());
        }
        if(mUser.getUid().equals(room_finish.get(0).getUser4_id())){
            readMessages(room_finish.get(0).getKey());
        }


        text_send = findViewById(R.id.text_send);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_send.getText().toString();
                if(!msg.equals("")){
                    //me ,other user ,message
                    if(mUser.getUid().equals(room_finish.get(0).getUser1_id())){
                        sendMessage(mUser.getUid(),room_finish.get(0).getUser2_id(),
                                room_finish.get(0).getUser3_id(),room_finish.get(0).getUser4_id(),
                                msg,room_finish.get(0).getKey());
                    }

                    if(mUser.getUid().equals(room_finish.get(0).getUser2_id())){
                        sendMessage(mUser.getUid(),room_finish.get(0).getUser1_id(),
                                room_finish.get(0).getUser3_id(),room_finish.get(0).getUser4_id(),
                                msg,room_finish.get(0).getKey());
                    }
                    if(mUser.getUid().equals(room_finish.get(0).getUser3_id())){
                        sendMessage(mUser.getUid(),room_finish.get(0).getUser1_id(),
                                room_finish.get(0).getUser2_id(),room_finish.get(0).getUser4_id(),
                                msg,room_finish.get(0).getKey());
                    }
                    if(mUser.getUid().equals(room_finish.get(0).getUser4_id())){
                        sendMessage(mUser.getUid(),room_finish.get(0).getUser1_id(),
                                room_finish.get(0).getUser2_id(),room_finish.get(0).getUser3_id(),
                                msg,room_finish.get(0).getKey());
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"You can't send message",Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
            }
        });





        customAdapter = new CustomAdapter(getApplicationContext(),room_finish);
        listView.setAdapter(customAdapter);


        Button button = findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),History.class);
                startActivity(intent);
                finish();
            }
        });

    }

//    public Runnable  runnable =new Runnable() {
//        @Override
//        public void run() {
//            success.setText("Success");
//            Toast.makeText(getApplicationContext(),"Ready",Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(getApplicationContext(),Bill.class);
//            intent.putExtra("room_finish",list_room);
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
        chat.setImage(photo_user);
        chat.setKey_room(key);
        chat.setMessage(message);

        databaseReference.child(databaseReference.push().getKey()).setValue(chat);
    }

    public void readMessages(final String key){
        list_chat = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_chat.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getKey_room().equals(key)) {
                        list_chat.add(chat);
                    }

                }
                adapter_chat = new Adapter_chat(getApplicationContext(), list_chat);
                recyclerView.setAdapter(adapter_chat);
                ref1 = FirebaseDatabase.getInstance().getReference("users");

                adapter_chat.setOnItemClickListener(new Adapter_chat.OnItemClickListener() {
                    @Override
                    public void onItemClick(final int position) {

                        if (list_chat.get(position).getSender().equals(mUser.getUid())
                                ) {
                            //Log.i("key1",list_chat.get(position).getSender());
                            ref1.child(list_chat.get(position).getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Account account = dataSnapshot.getValue(Account.class);

                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Bill.this);
                                    mBuilder.setCancelable(true);

                                    View mview = getLayoutInflater().inflate(R.layout.daiglog_profile, null);


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

                        } else {
                            ref1.child(list_chat.get(position).getSender()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Account account = dataSnapshot.getValue(Account.class);

                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Bill.this);
                                    mBuilder.setCancelable(true);

                                    View mview = getLayoutInflater().inflate(R.layout.daiglog_profile, null);


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


    class CustomAdapter extends BaseAdapter {

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
            convertView = layoutInflater.inflate(R.layout.room_item_bill,parent,false);

            orgin = convertView.findViewById(R.id.address_origin_);
            destination = convertView.findViewById(R.id.address_destination_);
            km = convertView.findViewById(R.id.km);
            price = convertView.findViewById(R.id.price);
            user = convertView.findViewById(R.id.total_user);
            num_room = convertView.findViewById(R.id.num_room);
            date= convertView.findViewById(R.id.date);
            time = convertView.findViewById(R.id.time);
            name_room123 = convertView.findViewById(R.id.name_room);

            orgin.setText(custom_room.get(i).getOrigin());
            destination.setText(custom_room.get(i).getDestination());
            km.setText(custom_room.get(i).getKm());
            price.setText(custom_room.get(i).getBaht()+"");
            user.setText(custom_room.get(i).getTotal_user()+"");
            num_room.setText(custom_room.get(i).getRoom_no()+"");
            date.setText(custom_room.get(i).getMonths()+"/"+custom_room.get(i).getDays()+"/"+custom_room.get(i).getYears());
            time.setText(custom_room.get(i).getHours()+" : "+custom_room.get(i).getMinites());
            name_room123.setText(custom_room.get(i).getName_room());


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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),History.class);
        startActivity(intent);
        finish();
    }
}
