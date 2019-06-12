package com.example.user.otp5;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.otp5.MenuApp.MessageEventActivity;
import com.example.user.otp5.Model.Account;
import com.example.user.otp5.Model.Room;
import com.example.user.otp5.MenuApp.ProfileActivity;
import com.example.user.otp5.MenuApp.RoomFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.PendingIntent.getActivity;


public class MenuAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ArrayList<Room> roomarr;
    private long backPressTime;
    FirebaseUser mUser;
    Account account;
    DrawerLayout drawer;
    DatabaseReference reference,databaseReference;
    TextView name_email,name;
    Fragment fragment = null;
    int c = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_menu_true);
        roomarr = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        roomarr = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Room> rooms = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Room");

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    c = 0;
                     roomarr.clear();
                     for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                         Room room = dataSnapshot1.getValue(Room.class);
                         if (((room.getUser1_id().equals(mUser.getUid())
                                 && room.getUser1()==1)

                                 || (room.getUser2_id().equals(mUser.getUid())
                                 && room.getUser2()==1)

                                 || (room.getUser3_id().equals(mUser.getUid())
                                 && room.getUser3()==1)

                                 || (room.getUser4_id().equals(mUser.getUid())
                                 && room.getUser4()==1)

                                 )
                                 && room.getStatus().equals("available")) {

                             // Toast.makeText(view.getContext(), "Room is full", Toast.LENGTH_SHORT).show();
                             c = 1;
                             roomarr.add(room);
                             Log.i("เคยอยู่ในห้อง", roomarr.size()+"");
                             break;
//                             Toast.makeText(MenuAppActivity.this, "Back to room..", Toast.LENGTH_SHORT).show();
//                             Intent intent1 = new Intent(MenuAppActivity.this,MessageEventActivity.class);
//                             intent1.putExtra("room", roomarr);
//                             startActivityForResult(intent1, 0);
//                             break;
                     }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RoomFragment()).addToBackStack(null).commit();}
            //navigationView.setCheckedItem(R.id.room);}





        Log.d("key",mUser.getUid()+"");
        reference = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);

                View head = navigationView.getHeaderView(0);
                name_email = (TextView) head.findViewById(R.id.name_email);
                name_email.setText(account.getEmail());

                name = (TextView) head.findViewById(R.id.name_app);
                name.setText(account.getName());

                CircleImageView myphoto = head.findViewById(R.id.image_1);

                if (account.getImage().equals("default")) {
                    //default picture
                } else {
                    Glide.with(getApplicationContext()).load(account.getImage()).into(myphoto);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        showHome();

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Log.i("d","d");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragment instanceof RoomFragment){
                Log.i("break","breakkkkk");

                super.onBackPressed();
            }else {
                Log.i("showhome","showhome");
                showHome();
            }

        }
//        if(backPressTime +2000 > System.currentTimeMillis()) {
////
////            if(fragment instanceof RoomFragment){
////                super.onBackPressed();
////            }
////            else {
////                showHome();
////            }
////        }
////            else {
////                Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
////            }
////            backPressTime = System.currentTimeMillis();
//////        }
    }

    public void logout(){
        Intent intent = new Intent(MenuAppActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
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

//    private void status(String status){
//
//        reference = FirebaseDatabase.getInstance().getReference("users").child(mUser.getUid());
//        HashMap<String , Object> hashMap = new HashMap<>();
//        hashMap.put("status",status);
//        reference.updateChildren(hashMap);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        status("online");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        status("offline");
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.find_path:
                if(c==0) {
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivityForResult(intent, 0);
                    break;
                }else {
                    Toast.makeText(MenuAppActivity.this, "can't not click", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.room:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RoomFragment()).commit();
                break;
            case R.id.checkevent:
                Intent intent1 = new Intent(this,History.class);
                startActivityForResult(intent1,0);
                break;
            case R.id.im_going:
                if(roomarr.size()==0){
                    Toast.makeText(MenuAppActivity.this, "You don't have a current room!", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    Toast.makeText(MenuAppActivity.this, "Back to room..", Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(MenuAppActivity.this, MessageEventActivity.class);
                    intent3.putExtra("room", roomarr);
                    startActivityForResult(intent3, 0);
                    break;
                }
            case R.id.profile:
                Intent intent2 = new Intent(this,ProfileActivity.class);
                startActivityForResult(intent2,0);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showHome(){
        fragment = new RoomFragment();
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    fragment).commit();
        }
    }







}
