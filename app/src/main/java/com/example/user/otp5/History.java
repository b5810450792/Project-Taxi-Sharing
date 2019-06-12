package com.example.user.otp5;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.otp5.Model.Room;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser mUser;
    ArrayList<Room> roomHis;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listview_history);

        roomHis = new ArrayList<>();
        reference =  FirebaseDatabase.getInstance().getReference("Room");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Room room = dataSnapshot1.getValue(Room.class);
                    if(room.getStatus().equals("busy") && (room.getUser1_id().equals(mUser.getUid())
                            || room.getUser2_id().equals(mUser.getUid())
                            || room.getUser3_id().equals(mUser.getUid())
                            || room.getUser4_id().equals(mUser.getUid()))){
//                    && (room.getUser1_id().equals(mUser.getUid())
//                            || room.getUser2_id().equals(mUser.getUid())
//                            || room.getUser3_id().equals(mUser.getUid())
//                            || room.getUser4_id().equals(mUser.getUid()))
                        roomHis.add(room);
                    }
                }

                CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),roomHis);
                listView.setAdapter(customAdapter);
//                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    roomHis.add(dataSnapshot1.getValue(Room.class));
//                   }
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
            return getCount()-position-1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.history_item,parent,false);

            final ArrayList<Room> room_bill = new ArrayList<>();

            TextView num_room,num_origin,num_destination;
            num_room = convertView.findViewById(R.id.number_room_history);
            num_origin = convertView.findViewById(R.id.origin_history);
            num_destination = convertView.findViewById(R.id.destinatiom_history);

            num_room.setText(custom_room.get((Integer) getItem(i)).getRoom_no()+"");
            num_origin.setText(custom_room.get((Integer) getItem(i)).getOrigin());
            num_destination.setText(custom_room.get((Integer) getItem(i)).getDestination());

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(custom_room.get((Integer) getItem(position)).getUser1_id().equals(mUser.getUid())){
                        Intent intent = new Intent(getApplicationContext(),Bill.class);
                        room_bill.add(custom_room.get((Integer) getItem(position)));
                        intent.putExtra("room_finish",room_bill);
                        startActivity(intent);
                        finish();
                    }
                    else if(custom_room.get((Integer) getItem(position)).getUser2_id().equals(mUser.getUid())){
                        Intent intent = new Intent(getApplicationContext(),Bill.class);
                        room_bill.add(custom_room.get((Integer) getItem(position)));
                        intent.putExtra("room_finish",room_bill);
                        startActivity(intent);
                        finish();
                    }
                    else if(custom_room.get((Integer) getItem(position)).getUser3_id().equals(mUser.getUid())){
                        Intent intent = new Intent(getApplicationContext(),Bill.class);
                        room_bill.add(custom_room.get((Integer) getItem(position)));
                        intent.putExtra("room_finish",room_bill);
                        startActivity(intent);
                        finish();
                    }
                    else if(custom_room.get((Integer) getItem(position)).getUser4_id().equals(mUser.getUid())){
                        Intent intent = new Intent(getApplicationContext(),Bill.class);
                        room_bill.add(custom_room.get((Integer) getItem(position)));
                        intent.putExtra("room_finish",room_bill);
                        startActivity(intent);
                        finish();
                    }


                }
            });



            return convertView;

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MenuAppActivity.class);
        startActivity(intent);
        finish();
    }
}
