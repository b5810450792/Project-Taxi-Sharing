package com.example.user.otp5.MenuApp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.user.otp5.AboutMap.DirectionFinder;
import com.example.user.otp5.AboutMap.DirectionFinderListener;
import com.example.user.otp5.AboutMap.Route;
import com.example.user.otp5.Model.Account;
import com.example.user.otp5.Model.Room;
import com.example.user.otp5.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class RoomFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener ,LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,DirectionFinderListener{

    TextView orgin,destination,km,price,user,num_room,date,time,currentToOrigin;
    View view1;
    DatabaseReference reference,databaseReference,ref;
    ArrayList<Room> arr,arr_room,arr_room_to_current,arr_search;
    ArrayList<Account> arr_user;
    int count = 0;
    int xx = 0;
    int ii =0;
    int yy =0;
    ProgressDialog progressDialog;
    Button btn_search,btn_create;
    ListView listView;
    CustomAdapter customAdapter;
    SearchView origin_search,destination_search,nameroom_search;
    FirebaseUser mUser123;
    Button bTime,bDate;
    String setTime="",setDate="",nameCurrent="",oneRequest;
    TextView  textViewTime,textViewDate;
    Spinner spinner;
    private String currentAddstring,current_to_origin,key_user;
    private int currentAddint;
    private Geocoder geocoder;
    private Location lastlocation;
    private List<Address> addressList;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static final int Request_User_Location_Code = 99;
    Button km1_10,km11_30,km31_50,km51;
    int intkm = 0;
    ArrayList<Account> accountArrayList;
    int inc = 0;
    TextView nameRoom;
    Account  accountzz;
    EditText origin_srh;


    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.fragment_room, container, false);

        getActivity().setTitle("Room");



        geocoder = new Geocoder(view1.getContext(),Locale.getDefault());
        currentAddstring = "0";
        current_to_origin = "";
        //oneRequest = "0";
        currentAddint = 0;
        count = 0;
        xx = 0;
        intkm = 0;
        inc = 0;
        arr = new ArrayList<>();
        arr_room = new ArrayList<>();
        arr_user = new ArrayList<>();
        arr_search = new ArrayList<>();
        arr_room_to_current = new ArrayList<>();
        accountArrayList = new ArrayList<>();
        key_user = "";

        mUser123 = FirebaseAuth.getInstance().getCurrentUser();
        ref= FirebaseDatabase.getInstance().getReference("users").child(mUser123.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accountzz = new Account();
                accountzz = dataSnapshot.getValue(Account.class);
                key_user = accountzz.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        progressDialog = ProgressDialog.show(view1.getContext(), "Please wait.",
                "Finding room..!", true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }
        //progressDialog = ProgressDialog.show(view1.getContext(), "Please wait.",
        // "Finding room..!", true);

        origin_search = (SearchView)view1.findViewById(R.id.where_are_u);
        origin_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    customAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        destination_search = (SearchView)view1.findViewById(R.id.going_to);
        destination_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    customAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        nameroom_search = view1.findViewById(R.id.name_room_search);
        nameroom_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    customAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });


        textViewTime = view1.findViewById(R.id.show_time);
        textViewDate = view1.findViewById(R.id.show_date);

        bTime = view1.findViewById(R.id.btn_time);
        bTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewTime.setText("");
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(container.getContext(), RoomFragment.this,calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });

        bDate = view1.findViewById(R.id.btn_date);
        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewDate.setText("");
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(container.getContext(),RoomFragment.this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



        listView = view1.findViewById(R.id.listview);

//        ref = FirebaseDatabase.getInstance().getReference("users").child(mUser123.getUid());
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Account account = dataSnapshot.getValue(Account.class);
//                nameCurrent = account.getKey();
//                Log.i("keycur__ent",nameCurrent);
//                Log.i("keycur__ent",nameCurrent);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Room");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                View view;
                Activity activity = getActivity();
                if(activity != null){

                    arr_room_to_current.clear();
                    //ArrayList<Room> rooms = new ArrayList<>();
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        arr_room_to_current.add(dataSnapshot1.getValue(Room.class));
                    }

                   // joinAgain(view1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1);
        //arr.clear();
        //accountArrayList.clear();
        reference = FirebaseDatabase.getInstance().getReference("Room");
        // add list view
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arr.clear();
                accountArrayList.clear();
                arr_search.clear();
                xx = 0;
                yy = 0;
                //buildGoogleApiClient();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    if(room.getStatus().equals("available")){
                        arr.add(room);
                        arr_search.add(room);
                    }
                    if(room.getStatus().equals("destroy")){
                        //reference.child(room.getKey()).removeValue();
                    }
                }
                if(count==0) {
//                    Log.i("your key work",mUser123.getUid());
//                    Log.i("your key work",key_user);
                    Log.i("kuy innnnnnnnnnnnn","zkuy");

                    buildGoogleApiClient();
                    count++;
                    //progressDialog.dismiss();
//                    customAdapter =new CustomAdapter(getContext(),arr,currentAddstring);
//                    listView.setAdapter(customAdapter);
                }

                ArrayList<Account> accounts = new ArrayList<>();
                ArrayList<Room> rooms = new ArrayList<>();
                if(accountArrayList.size()==arr.size()){
                    Log.i("count correct 1",count+"");
//                    Log.i("Distance finish 1",accountArrayList.get(0).getCurrent_address() );
//                    Log.i("Distance finish 2",accountArrayList.get(1).getCurrent_address() );

                    for(int i = 0;i<arr.size();i++){
                        if(Double.parseDouble(accountArrayList.get(i).getCurrent_address())<=10){
                            accounts.add(accountArrayList.get(i));
                            rooms.add(arr.get(i));
                        }
                    }

                    accountArrayList = accounts;
                    arr = rooms;

                    Log.i("ห้องที่น้อยกว่า10km 1",arr.size()+"");
                    Log.i("หระยะที่น้อยกว่า10km 1",accountArrayList.size()+"");

                        customAdapter = new CustomAdapter(getContext(), arr, currentAddstring, accountArrayList,arr_search);
                        customAdapter.notifyDataSetChanged();
                        listView.setAdapter(customAdapter);
                        progressDialog.dismiss();
                        count = 0;
                        xx = 0;
                        ii++;
                        //accountArrayList.clear();




                }
                progressDialog.dismiss();
//                else {
//
//                    if(ii>0){
//                        accountArrayList.clear();
//                        //arr.clear();
//                    }
//                    progressDialog.dismiss();
//                    count=0;
//                    xx = 0;
//                    ii=0;
////
//                }


////                Log.i("Room size",arr.size()+"");
//                progressDialog.dismiss();
//                if(count==0){
//                    buildGoogleApiClient();
//                }0
//                customAdapter = new CustomAdapter(getContext(),arr,currentAddstring,accountArrayList);
//                listView.setAdapter(customAdapter);
//                progressDialog.dismiss();
//                Log.i("cuurent firebase",currentAddstring);
//                if(!currentAddstring.equals("0")) {
//                    //Toast.makeText(view1.getContext(), currentAddstring, Toast.LENGTH_SHORT).show();
//
//                }


            }
////


            @Override
            public void onCancelled(DatabaseError databaseError) {
////
            }
        });



        return view1;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        customAdapter.getFilter().filter("");
        setDate = month+"/"+dayOfMonth+"/"+year;
        textViewDate.setText(setDate);
        customAdapter.getFilter().filter(setDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        customAdapter.getFilter().filter("");
        setTime = hourOfDay+" : "+minute;
        textViewTime.setText(setTime);
        customAdapter.getFilter().filter(setTime);

    }

    public void joinAgain(View view){
        for(int i =0;i<arr_room_to_current.size();i++){
            ArrayList<Room> rooms = new ArrayList<>();
            if((arr_room_to_current.get(i).getUser1_id().equals(mUser123.getUid())
                    || arr_room_to_current.get(i).getUser2_id().equals(mUser123.getUid())
                    || arr_room_to_current.get(i).getUser3_id().equals(mUser123.getUid())
                    || arr_room_to_current.get(i).getUser4_id().equals(mUser123.getUid()))
                    && arr_room_to_current.get(i).getStatus().equals("available")){
                progressDialog.dismiss();
                databaseReference.child(arr_room_to_current.get(i).getKey()).setValue(arr_room_to_current.get(i));
                Intent intent = new Intent(view1.getContext(),MessageEventActivity.class);
                rooms.add(arr_room_to_current.get(i));
                intent.putExtra("room",rooms);
                Toast.makeText(view1.getContext(), "join again..", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_User_Location_Code:
                if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(view1.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED) {
                        if(googleApiClient == null){
                            buildGoogleApiClient();
                            Log.i("permission","permission");

                        }
                        //mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(view1.getContext(),"Permission Denied..",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(view1.getContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, (com.google.android.gms.location.LocationListener) this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;

//        if(marker!=null){
//            marker.remove();
//

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            final String address = addressList.get(0).getAddressLine(0);
            String area = addressList.get(0).getLocality();
            String city = addressList.get(0).getAdminArea();
            String country = addressList.get(0).getCountryName();
            String postalcode = addressList.get(0).getPostalCode();


            currentAddstring = address + ", " + area + ", " + city + ", " + country + ", " + postalcode;




//            customAdapter = new CustomAdapter(getContext(),arr,currentAddstring,accountArrayList);
//            listView.setAdapter(customAdapter);
//            progressDialog.dismiss();
            //Log.i("cuurentAddstring ",currentAddstring);
            if(!currentAddstring.equals("0")){
                for(int i=0;i<arr.size();i++){
                    try {
                        Log.i("i",i+"");
                        new DirectionFinder(this,currentAddstring , arr.get(i).getOrigin()).execute();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                //Log.i("cuurentAddstring 3",currentAddstring);
                if(accountArrayList.size()==arr.size()) {
                    Toast.makeText(view1.getContext(), currentAddstring, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(),"accountArrayList.size()",Toast.LENGTH_SHORT).show();
//                    Log.i("Distance finish 1",accountArrayList.get(0).getCurrent_address() );
//                    Log.i("Distance finish 2",accountArrayList.get(1).getCurrent_address() );
                    ArrayList<Account> accounts = new ArrayList<>();
                    ArrayList<Room> rooms = new ArrayList<>();
                    for (int i = 0; i < arr.size(); i++) {
                        if (Double.parseDouble(accountArrayList.get(i).getCurrent_address()) <= 10) {
                            accounts.add(accountArrayList.get(i));
                            rooms.add(arr.get(i));

                            Log.i("ห้องที่น้อยกว่า10km 3.1", "ห้องที่ "+arr.get(i).getRoom_no() + "");
                            Log.i("ระยะที่น้อยกว่า10km 3.1", "จากจุดเริ่มต้นถึง origin "+accountArrayList.get(i).getCurrent_address() + "");
                        }
                    }

                    accountArrayList = accounts;
                    arr = rooms;

                    Log.i("ห้องที่น้อยกว่า10km 3", arr.size() + "");
                    Log.i("หระยะที่น้อยกว่า10km 3", accountArrayList.size() + "");
                    if(accountArrayList.size()==arr.size()) {
                        customAdapter = new CustomAdapter(getContext(), arr, currentAddstring, accountArrayList,arr_search);
                        customAdapter.notifyDataSetChanged();
                        listView.setAdapter(customAdapter);
                        progressDialog.dismiss();
                        count = 0;
                        xx = 0;
                        //accountArrayList.clear();
                    }
//                    customAdapter = new CustomAdapter(getContext(), arr, currentAddstring, accountArrayList);
//                    customAdapter.notifyDataSetChanged();
//                    listView.setAdapter(customAdapter);
//                    progressDialog.dismiss();
//                    count=0;
//                    xx = 0;
                    // accountArrayList = new ArrayList<>();

                }
//                else {
//                    if(ii>0){
//                        accountArrayList.clear();
//                        arr.clear();
//                    }
//                    progressDialog.dismiss();;
//                    count = 0;
//                    xx = 0;
//                    ii=0;
//                }

            }



//            customAdapter =new CustomAdapter(getContext(),arr,currentAddstring);
//            listView.setAdapter(customAdapter);

//            Log.i("cuurent Address String",currentAddstring);
//            Log.i("Account arr ",accountArrayList.size()+"");
//            Log.i("current Account km 1 ",accountArrayList.get(0).getCurrent_address());
//            Log.i("current Account km 2 ",accountArrayList.get(1).getCurrent_address());

//            MarkerOptions markerOptions = new MarkerOptions()
//                    .position(latLng)
//                    .title(address)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//
//            marker = mMap.addMarker(markerOptions);
//
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomBy(1));
//            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//                    autocompleteFragmentorigin.setText(fulladdress);
//                    origin = address;
//                    Toast.makeText(getApplicationContext(),address,LENGTH_SHORT).show();
//                }
//            });

            if(googleApiClient !=null ){
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void direct(){




    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(view1.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        googleApiClient.connect();

    }

    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(view1.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        //Log.i("cuurentAddstring 2.5",currentAddstring);
        if(!currentAddstring.equals("0")) {
//        ref = FirebaseDatabase.getInstance().getReference("users").child(mUser123.getUid());
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                account = dataSnapshot.getValue(Account.class);
            for (Route route : routes) {
                current_to_origin = "";
                String[] bahtarr = route.distance.text.split("");
                for (int i = 0; i < bahtarr.length; i++) {
                    if (bahtarr[i].equals("l") || bahtarr[i].equals("u") || bahtarr[i].equals("n") || bahtarr[i].equals(",") || bahtarr[i].equals("k") || bahtarr[i].equals("m") || bahtarr[i].equals(" ")) {

                    } else {
                        current_to_origin += bahtarr[i];
                    }
                }

                Log.i("Address_current_value", current_to_origin);
                accountArrayList.add(new Account());
                accountArrayList.get(xx).setCurrent_address(current_to_origin);
                accountArrayList.get(xx).setAddress_origin(currentAddstring);
                Log.i("Address_current_array", accountArrayList.get(xx).getCurrent_address());
                Log.i("current_to_origin",accountArrayList.get(xx).getAddress_origin());
                Log.i("accountArrayList size", accountArrayList.size()+"");
                //Log.i("Address_current_array", accountArrayList.get(xx).getCurrent_address());
                //arr.get(xx).setAddress_current(accountArrayList.get(xx).getCurrent_address());
//                    Log.i("Address_current 2 ", arr.get(xx).getAddress_current());
//                    Log.i("current key ", arr.get(xx).getKey());
                //ref.setValue(account);
                //reference.child(arr.get(xx).getKey()).setValue(arr.get(xx));
                xx++;

            }

            //Log.i("accountArrayList finish", accountArrayList.size()+"");
            //Log.i("list room size", arr.size()+"");
            //Log.i("currentAddstring", currentAddstring);
            //Log.i("accountArrayList finish", accountArrayList.size()+"");


//            if(accountArrayList.size()==arr.size()) {
//                //Toast.makeText(view1.getContext(), currentAddstring, Toast.LENGTH_SHORT).show();
////                Log.i("Distance finish 1",accountArrayList.get(0).getCurrent_address() );
////                Log.i("Distance finish 2",accountArrayList.get(1).getCurrent_address() );
//                customAdapter = new CustomAdapter(getContext(), arr, currentAddstring, accountArrayList);
//                customAdapter.notifyDataSetChanged();
//                listView.setAdapter(customAdapter);
//                progressDialog.dismiss();
//                count=0;
//                xx = 0;
//                //accountArrayList.clear();
//
//            }

        }
        if(accountArrayList.size()==arr.size()) {
            //Toast.makeText(view1.getContext(), currentAddstring, Toast.LENGTH_SHORT).show();
//                Log.i("Distance finish 1",accountArrayList.get(0).getCurrent_address() );
//                Log.i("Distance finish 2",accountArrayList.get(1).getCurrent_address() );
            ArrayList<Account> accounts = new ArrayList<>();
            ArrayList<Room> rooms = new ArrayList<>();
            for(int i = 0;i<arr.size();i++){
                if(Double.parseDouble(accountArrayList.get(i).getCurrent_address())<=10){
                    accounts.add(accountArrayList.get(i));
                    rooms.add(arr.get(i));
                }
            }

            accountArrayList = accounts;
            arr = rooms;

            Log.i("ห้องที่น้อยกว่า10km 2",arr.size()+"");
            Log.i("หระยะที่น้อยกว่า10km 2",accountArrayList.size()+"");
            if(accountArrayList.size()==arr.size()) {

                customAdapter = new CustomAdapter(getContext(), arr, currentAddstring, accountArrayList,arr_search);
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
                progressDialog.dismiss();
                count = 0;
                xx = 0;
                ii++;
            }
        }
           // }


//            customAdapter = new CustomAdapter(getContext(), arr, currentAddstring, accountArrayList);
//            customAdapter.notifyDataSetChanged();
//            listView.setAdapter(customAdapter);
//            progressDialog.dismiss();
//            count=0;
//            xx = 0;
//            ii++;
            //accountArrayList.clear();

        }
////            }
//        Log.i("Address_current zxc ",  accountArrayList.get(0).getCurrent_address());
//        Log.i("Address_current qwe ",  accountArrayList.size()+"");
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        Log.i("Address_current 7 ", accountArrayList.get(xx).getCurrent_address());
//        //arr.get(xx).setAddress_current(accountArrayList.get(xx).getCurrent_address());
//        Log.i("Address_current 8 ", arr.get(xx).getAddress_current());
//        Log.i("current key  9", arr.get(xx).getKey());




    //item
    class CustomAdapter extends BaseAdapter implements Filterable {

        List<Room> array_room;
        Context context;
        LayoutInflater layoutInflater;
        CustomFilter filter;
        List<Room> filter_room;
        String currentAdd;
        ArrayList<Account> accountArray;
//        ArrayList<Account> arr_account;
        //List<String> cur_to_ori;

        public CustomAdapter(Context context, List<Room> arrroom,String currentAdd,ArrayList<Account> accounts,List<Room> totalarrayroom) {
            this.context = context;
            this.array_room = arrroom;
            this.filter_room = totalarrayroom;
            this.currentAdd = currentAdd;
            this.accountArray = accounts;
//            for(int i=0;i<array_room.size();i++){
//                try {
//                    Log.i("count i ",i+"");
//                    new DirectionFinder(this,currentAddstring , array_room.get((Integer) getItem(i)).getOrigin()).execute();
//
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//            }}
            //this.arr_account = accounts;
//            if(count==0){
//                buildGoogleApiClient();
//            }
            //cur_to_ori = new ArrayList<>();
        }


        @Override
        public int getCount() {
            return array_room.size();
        }

        @Override
        public Object getItem(int position) {
            return getCount() - position - 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.room_item, parent, false);

            orgin = convertView.findViewById(R.id.address_origin_);
            destination = convertView.findViewById(R.id.address_destination_);
            km = convertView.findViewById(R.id.km);
            price = convertView.findViewById(R.id.price);
            user = convertView.findViewById(R.id.total_user);
            num_room = convertView.findViewById(R.id.num_room);
            date = convertView.findViewById(R.id.date);
            time = convertView.findViewById(R.id.time);
            currentToOrigin = convertView.findViewById(R.id.current_to_origin);
            nameRoom = convertView.findViewById(R.id.name_room);

            orgin.setText(array_room.get((Integer) getItem(i)).getOrigin());
            destination.setText(array_room.get((Integer) getItem(i)).getDestination());
            km.setText(array_room.get((Integer) getItem(i)).getKm());
            price.setText(array_room.get((Integer) getItem(i)).getBaht() + "");
            user.setText(array_room.get((Integer) getItem(i)).getTotal_user() + "");
            num_room.setText(array_room.get((Integer) getItem(i)).getRoom_no() + "");
            date.setText(array_room.get((Integer) getItem(i)).getMonths() + "/" + array_room.get((Integer) getItem(i)).getDays() + "/" + array_room.get((Integer) getItem(i)).getYears());
            time.setText(array_room.get((Integer) getItem(i)).getHours() + " : " + array_room.get((Integer) getItem(i)).getMinites());
            nameRoom.setText(array_room.get((Integer) getItem(i)).getName_room());
            //currentToOrigin.setText(accountArray.get((Integer) getItem(i)).getCurrent_address());

            Log.i("arraycurrentfinish size",accountArrayList.size()+"");
            Log.i("room finish size",array_room.size()+"");
            if(accountArray.size()==array_room.size() && yy==0) {
                Log.i("setText finish ",accountArray.get((Integer) getItem(i)).getCurrent_address());
                currentToOrigin.setText(accountArray.get((Integer) getItem(i)).getCurrent_address()+" km");
                count = 0;
                inc = 0;
               //accountArrayList.clear();


            }
//            try {}
//                new DirectionFinder(this,this.currentAdd , array_room.get((Integer) getItem(i)).getOrigin()).execute();
//                //currentToOrigin.setText(current_to_origin);
//                Log.i("km",current_to_origin);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }

//            try {
//                new DirectionFinder(this,currentAddstring , array_room.get((Integer) getItem(i)).getOrigin()).execute();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            //currentToOrigin.setText(current_to_origin+"");


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(view.getContext(), MessageEventActivity.class);
                    ArrayList<Room> rooms = new ArrayList<>();

                    //todo ออกมาดูหน้าหลักแล้วเข้าใหม่ได้
                    //todo ออกมาดูหน้าหลักแล้วเข้าใหม่ได้
                    //todo ออกมาดูหน้าหลักแล้วเข้าใหม่ได้
                    if ((array_room.get((Integer) getItem(position)).getUser1_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getUser1()==1)

                            || (array_room.get((Integer) getItem(position)).getUser2_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getUser2()==1)

                            || (array_room.get((Integer) getItem(position)).getUser3_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getUser3()==1)

                            || (array_room.get((Integer) getItem(position)).getUser4_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getUser4()==1)
                            ) {
                       // Toast.makeText(view.getContext(), "Room is full", Toast.LENGTH_SHORT).show();
                        rooms.add(array_room.get((Integer) getItem(position)));
                        Toast.makeText(view.getContext(), "Back to room..", Toast.LENGTH_SHORT).show();
                        intent1.putExtra("room", rooms);
                        startActivityForResult(intent1, 0);

                    } else if (array_room.get((Integer) getItem(position)).getUser1_id().equals("default")
                            && array_room.get((Integer) getItem(position)).getRoom_status().equals(accountzz.getStatus())) {
                        array_room.get((Integer) getItem(position)).setUser1_id(mUser123.getUid());
                        array_room.get((Integer) getItem(position)).setUser1(1);
                        array_room.get((Integer) getItem(position)).setTotal_user(array_room.get((Integer) getItem(position)).getUser1()
                                + array_room.get((Integer) getItem(position)).getUser2()
                                + array_room.get((Integer) getItem(position)).getUser3()
                                + array_room.get((Integer) getItem(position)).getUser4()
                        );
                        reference.child(array_room.get((Integer) getItem(position)).getKey()).setValue(array_room.get((Integer) getItem(position)));
                        rooms.add(array_room.get((Integer) getItem(position)));
                        Toast.makeText(view.getContext(), "Join to room..", Toast.LENGTH_SHORT).show();
                        intent1.putExtra("room", rooms);
                        startActivityForResult(intent1, 0);


                    } else if (array_room.get((Integer) getItem(position)).getUser2_id().equals("default")
                            && !array_room.get((Integer) getItem(position)).getUser1_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getRoom_status().equals(accountzz.getStatus())) {

                        array_room.get((Integer) getItem(position)).setUser2_id(mUser123.getUid());
                        array_room.get((Integer) getItem(position)).setUser2(1);
                        array_room.get((Integer) getItem(position)).setTotal_user(array_room.get((Integer) getItem(position)).getUser1()
                                + array_room.get((Integer) getItem(position)).getUser2()
                                + array_room.get((Integer) getItem(position)).getUser3()
                                + array_room.get((Integer) getItem(position)).getUser4()
                        );
                        reference.child(array_room.get((Integer) getItem(position)).getKey()).setValue(array_room.get((Integer) getItem(position)));
                        rooms.add(array_room.get((Integer) getItem(position)));
                        Toast.makeText(view.getContext(), "Join to room..", Toast.LENGTH_SHORT).show();
                        intent1.putExtra("room", rooms);
                        startActivityForResult(intent1, 0);
                    } else if (array_room.get((Integer) getItem(position)).getUser3_id().equals("default")
                            && !array_room.get((Integer) getItem(position)).getUser2_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getRoom_status().equals(accountzz.getStatus())) {


                        array_room.get((Integer) getItem(position)).setUser3_id(mUser123.getUid());
                        array_room.get((Integer) getItem(position)).setUser3(1);
                        array_room.get((Integer) getItem(position)).setTotal_user(array_room.get((Integer) getItem(position)).getUser1()
                                + array_room.get((Integer) getItem(position)).getUser2()
                                + array_room.get((Integer) getItem(position)).getUser3()
                                + array_room.get((Integer) getItem(position)).getUser4()
                        );
                        reference.child(array_room.get((Integer) getItem(position)).getKey()).setValue(array_room.get((Integer) getItem(position)));
                        rooms.add(array_room.get((Integer) getItem(position)));
                        Toast.makeText(view.getContext(), "Join to room..", Toast.LENGTH_SHORT).show();
                        intent1.putExtra("room", rooms);
                        startActivityForResult(intent1, 0);


                    } else if (array_room.get((Integer) getItem(position)).getUser4_id().equals("default")
                            && !array_room.get((Integer) getItem(position)).getUser2_id().equals(mUser123.getUid())
                            && !array_room.get((Integer) getItem(position)).getUser3_id().equals(mUser123.getUid())
                            && array_room.get((Integer) getItem(position)).getRoom_status().equals(accountzz.getStatus())) {

                        array_room.get((Integer) getItem(position)).setUser4_id(mUser123.getUid());
                        array_room.get((Integer) getItem(position)).setUser4(1);
                        array_room.get((Integer) getItem(position)).setTotal_user(array_room.get((Integer) getItem(position)).getUser1()
                                + array_room.get((Integer) getItem(position)).getUser2()
                                + array_room.get((Integer) getItem(position)).getUser3()
                                + array_room.get((Integer) getItem(position)).getUser4()
                        );
                        reference.child(array_room.get((Integer) getItem(position)).getKey()).setValue(array_room.get((Integer) getItem(position)));
                        rooms.add(array_room.get((Integer) getItem(position)));
                        Toast.makeText(view.getContext(), "Join to room..", Toast.LENGTH_SHORT).show();
                        intent1.putExtra("room", rooms);
                        startActivityForResult(intent1, 0);

                    }
//                    else if(array_room.get((Integer) getItem(position)).getUser5_id().equals("default")
//                            && !array_room.get((Integer) getItem(position)).getUser2_id().equals(mUser123.getUid())
//                            && !array_room.get((Integer) getItem(position)).getUser3_id().equals(mUser123.getUid())
//                            && !array_room.get((Integer) getItem(position)).getUser4_id().equals(mUser123.getUid()))
//                             {
//
//                        array_room.get((Integer) getItem(position)).setUser5_id(mUser123.getUid());
//                        array_room.get((Integer) getItem(position)).setUser5(1);
//                        array_room.get((Integer) getItem(position)).setTotal_user(array_room.get((Integer) getItem(position)).getUser1()
//                                + array_room.get((Integer) getItem(position)).getUser2()
//                                + array_room.get((Integer) getItem(position)).getUser3()
//                                + array_room.get((Integer) getItem(position)).getUser4()
//                                + array_room.get((Integer) getItem(position)).getUser5());
//                        reference.child(array_room.get((Integer) getItem(position)).getKey()).setValue(array_room.get((Integer) getItem(position)));
//                        rooms.add(array_room.get((Integer) getItem(position)));
//                        Toast.makeText(view.getContext(), "Join to room..", Toast.LENGTH_SHORT).show();
//                        intent1.putExtra("room", rooms);
//                        startActivityForResult(intent1, 0);
//                    }
                    else {
                        Toast.makeText(view.getContext(), "can't join to room", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            });

            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = new CustomFilter();
            }
            return filter;
        }

//        @Override
//        public void onDirectionFinderStart() {
//
//        }
//
//        @Override
//        public void onDirectionFinderSuccess(List<Route> routes) {
//            //xx = 0;
//            for (Route route : routes) {
//                current_to_origin = "";
//                String[] bahtarr = route.distance.text.split("");
//                for (int i = 0; i < bahtarr.length; i++) {
//                    if (bahtarr[i].equals("l") || bahtarr[i].equals("u") || bahtarr[i].equals("n") || bahtarr[i].equals(",") || bahtarr[i].equals("k") || bahtarr[i].equals("m") || bahtarr[i].equals(" ")) {
//
//                    } else {
//                        current_to_origin += bahtarr[i];
//                    }
//                }
//
//                accountArrayList.add(new Account());
//                accountArrayList.get(xx).setCurrent_address(current_to_origin);
//                Log.i("Address_current_array", accountArrayList.size()+"");
//                Log.i("Address_current_value", current_to_origin);
//                Log.i("Address_current_true", accountArrayList.get(xx).getCurrent_address());
//                Log.i("count",xx+"");
//                //currentToOrigin.setText(accountArrayList.get(xx).getCurrent_address());
//                xx++;
//
//                //arr.get(xx).setAddress_current(accountArrayList.get(xx).getCurrent_address());
////                    Log.i("Address_current 2 ", arr.get(xx).getAddress_current());
////                    Log.i("current key ", arr.get(xx).getKey());
//                //ref.setValue(account);
//                //reference.child(arr.get(xx).getKey()).setValue(arr.get(xx));
//                ;
//            }
//        }






        class CustomFilter extends Filter{

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if(constraint!=null && constraint.length()>0){

                    String txt = constraint.toString().toLowerCase();

                    List<Room> filters_room_array = new ArrayList<>();

                    for(int i = 0;i<filter_room.size();i++){
                        if(filter_room.get(i).getOrigin().contains(constraint) || filter_room.get(i).getOrigin().toLowerCase().contains(txt)
                                ||constraint.equals(filter_room.get(i).getHours()+" : "+filter_room.get(i).getMinites())
                                ||constraint.equals(filter_room.get(i).getMonths()+"/"+filter_room.get(i).getDays()+"/"+filter_room.get(i).getYears())
                            || filter_room.get(i).getName_room().contains(constraint)){

                            Room r = new Room(filter_room.get(i).getOrigin(),
                                    filter_room.get(i).getDestination(),
                                    filter_room.get(i).getKm(),
                                    filter_room.get(i).getBaht());

                            r.setKey(filter_room.get(i).getKey());
                            r.setTotal_user(filter_room.get(i).getTotal_user());

                            r.setUser1_id(filter_room.get(i).getUser1_id());
                            r.setUser1(filter_room.get(i).getUser1());

                            r.setUser2(filter_room.get(i).getUser2());
                            r.setUser2_id(filter_room.get(i).getUser2_id());

                            r.setUser3(filter_room.get(i).getUser3());
                            r.setUser3_id(filter_room.get(i).getUser3_id());

                            r.setUser4(filter_room.get(i).getUser4());
                            r.setUser4_id(filter_room.get(i).getUser4_id());

//                            r.setUser5(filter_room.get(i).getUser5());
//                            r.setUser5_id(filter_room.get(i).getUser5_id());

                            r.setRoom_no(filter_room.get(i).getRoom_no());
                            r.setRoom_status(filter_room.get(i).getRoom_status());

                            r.setName_room(filter_room.get(i).getName_room());

                            r.setMinites(filter_room.get(i).getMinites());
                            r.setHours(filter_room.get(i).getHours());
                            r.setMonths(filter_room.get(i).getMonths());
                            r.setDays(filter_room.get(i).getDays());
                            r.setYears(filter_room.get(i).getYears());

                            filters_room_array.add(r);
                        }
                        else if(filter_room.get(i).getDestination().contains(constraint) || filter_room.get(i).getDestination().toLowerCase().contains(txt)
                                ||constraint.equals(filter_room.get(i).getHours()+" : "+filter_room.get(i).getMinites())
                                ||constraint.equals(filter_room.get(i).getMonths()+"/"+filter_room.get(i).getDays()+"/"+filter_room.get(i).getYears())
                            || filter_room.get(i).getName_room().contains(constraint)){
                            Room r = new Room(filter_room.get(i).getOrigin(),
                                    filter_room.get(i).getDestination(),
                                    filter_room.get(i).getKm(),
                                    filter_room.get(i).getBaht());

                            r.setKey(filter_room.get(i).getKey());
                            r.setTotal_user(filter_room.get(i).getTotal_user());

                            r.setUser1_id(filter_room.get(i).getUser1_id());
                            r.setUser1(filter_room.get(i).getUser1());

                            r.setUser2(filter_room.get(i).getUser2());
                            r.setUser2_id(filter_room.get(i).getUser2_id());

                            r.setUser3(filter_room.get(i).getUser3());
                            r.setUser3_id(filter_room.get(i).getUser3_id());

                            r.setUser4(filter_room.get(i).getUser4());
                            r.setUser4_id(filter_room.get(i).getUser4_id());

//                            r.setUser5(filter_room.get(i).getUser5());
//                            r.setUser5_id(filter_room.get(i).getUser5_id());

                            r.setRoom_no(filter_room.get(i).getRoom_no());
                            r.setRoom_status(filter_room.get(i).getRoom_status());

                            r.setName_room(filter_room.get(i).getName_room());

                            r.setMinites(filter_room.get(i).getMinites());
                            r.setHours(filter_room.get(i).getHours());
                            r.setMonths(filter_room.get(i).getMonths());
                            r.setDays(filter_room.get(i).getDays());
                            r.setYears(filter_room.get(i).getYears());

                            filters_room_array.add(r);
                        }
                    }
                    results.count = filters_room_array.size();
                    results.values = filters_room_array;

                }
                else {
                    results.count = filter_room.size();
                    results.values = filter_room;
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent1 = new Intent(getContext(),MessageEventActivity.class);
                        ArrayList<Room> rooms = new ArrayList<>();
//                        rooms.add(filter_room.get(filter_room.size()-position-1));
//                        intent1.putExtra("rooms",rooms);
//                        startActivityForResult(intent1,0);
                        if (( filter_room.get(filter_room.size()-position-1).getUser1_id().equals(mUser123.getUid())
                                &&  filter_room.get(filter_room.size()-position-1).getUser1()==1)

                                || ( filter_room.get(filter_room.size()-position-1).getUser2_id().equals(mUser123.getUid())
                                &&  filter_room.get(filter_room.size()-position-1).getUser2()==1)

                                || ( filter_room.get(filter_room.size()-position-1).getUser3_id().equals(mUser123.getUid())
                                && filter_room.get(filter_room.size()-position-1).getUser3()==1)

                                || ( filter_room.get(filter_room.size()-position-1).equals(mUser123.getUid())
                                &&  filter_room.get(filter_room.size()-position-1).getUser4()==1)
                                ) {
                            // Toast.makeText(view.getContext(), "Room is full", Toast.LENGTH_SHORT).show();
                            rooms.add(filter_room.get(filter_room.size()-position-1));
                            Toast.makeText(view.getContext(), "Back to room..", Toast.LENGTH_SHORT).show();
                            intent1.putExtra("room", rooms);
                            startActivityForResult(intent1, 0);
                        }
//                        if(filter_room.get(filter_room.size()-position-1).getUser1_id().equals(mUser123.getUid())){
//                            Toast.makeText(view.getContext(),"Room is full",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        else if(filter_room.get(filter_room.size()-position-1).getUser1_id().equals("default")
                                && filter_room.get(filter_room.size()-position-1).getRoom_status().equals(accountzz.getStatus())){

                            filter_room.get(filter_room.size()-position-1).setUser1_id(mUser123.getUid());
                            filter_room.get(filter_room.size()-position-1).setUser1(1);
                            filter_room.get(filter_room.size()-position-1).setTotal_user(filter_room.get(filter_room.size()-position-1).getUser1()
                                    + filter_room.get(filter_room.size()-position-1).getUser2()
                                    + filter_room.get(filter_room.size()-position-1).getUser3()
                                    + filter_room.get(filter_room.size()-position-1).getUser4()
                            );
                            reference.child(filter_room.get(filter_room.size()-position-1).getKey()).setValue(filter_room.get(filter_room.size()-position-1));
                            rooms.add(filter_room.get(filter_room.size()-position-1));
                            Toast.makeText(view.getContext(),"Join to room..",Toast.LENGTH_SHORT).show();
                            intent1.putExtra("room", rooms);
                            startActivity(intent1);
                        }
                        else if(filter_room.get(filter_room.size()-position-1).getUser2_id().equals("default")
                                && !filter_room.get(filter_room.size()-position-1).getUser1_id().equals(mUser123.getUid())
                                && filter_room.get(filter_room.size()-position-1).getRoom_status().equals(accountzz.getStatus())){

                            filter_room.get(filter_room.size()-position-1).setUser2_id(mUser123.getUid());
                            filter_room.get(filter_room.size()-position-1).setUser2(1);
                            filter_room.get(filter_room.size()-position-1).setTotal_user(filter_room.get(filter_room.size()-position-1).getUser1()
                                    + filter_room.get(filter_room.size()-position-1).getUser2()
                                    + filter_room.get(filter_room.size()-position-1).getUser3()
                                    + filter_room.get(filter_room.size()-position-1).getUser4()
                            );
                            reference.child(filter_room.get(filter_room.size()-position-1).getKey()).setValue(filter_room.get(filter_room.size()-position-1));
                            rooms.add(filter_room.get(filter_room.size()-position-1));
                            Toast.makeText(view.getContext(),"Join to room..",Toast.LENGTH_SHORT).show();
                            intent1.putExtra("room", rooms);
                            startActivity(intent1);
                        }
                        else if(filter_room.get(filter_room.size()-position-1).getUser3_id().equals("default")
                                && !filter_room.get(filter_room.size()-position-1).getUser2_id().equals(mUser123.getUid())
                                && filter_room.get(filter_room.size()-position-1).getRoom_status().equals(accountzz.getStatus())){


                            filter_room.get(filter_room.size()-position-1).setUser3_id(mUser123.getUid());
                            filter_room.get(filter_room.size()-position-1).setUser3(1);
                            filter_room.get(filter_room.size()-position-1).setTotal_user(filter_room.get(filter_room.size()-position-1).getUser1()
                                    + filter_room.get(filter_room.size()-position-1).getUser2()
                                    + filter_room.get(filter_room.size()-position-1).getUser3()
                                    + filter_room.get(filter_room.size()-position-1).getUser4()
                            );
                            reference.child(filter_room.get(filter_room.size()-position-1).getKey()).setValue(filter_room.get(filter_room.size()-position-1));
                            rooms.add(filter_room.get(filter_room.size()-position-1));
                            Toast.makeText(view.getContext(),"Join to room..",Toast.LENGTH_SHORT).show();
                            intent1.putExtra("room", rooms);
                            startActivity(intent1);

                        }
                        else if(filter_room.get(filter_room.size()-position-1).getUser4_id().equals("default")
                                && !filter_room.get(filter_room.size()-position-1).getUser2_id().equals(mUser123.getUid())
                                && !filter_room.get(filter_room.size()-position-1).getUser3_id().equals(mUser123.getUid())
                                && filter_room.get(filter_room.size()-position-1).getRoom_status().equals(accountzz.getStatus())){

                            filter_room.get(filter_room.size()-position-1).setUser4_id(mUser123.getUid());
                            filter_room.get(filter_room.size()-position-1).setUser4(1);
                            filter_room.get(filter_room.size()-position-1).setTotal_user(filter_room.get(filter_room.size()-position-1).getUser1()
                                    + filter_room.get(filter_room.size()-position-1).getUser2()
                                    + filter_room.get(filter_room.size()-position-1).getUser3()
                                    + filter_room.get(filter_room.size()-position-1).getUser4()
                            );
                            reference.child(filter_room.get(filter_room.size()-position-1).getKey()).setValue(filter_room.get(filter_room.size()-position-1));
                            rooms.add(filter_room.get(filter_room.size()-position-1));
                            Toast.makeText(view.getContext(),"Join to room..",Toast.LENGTH_SHORT).show();
                            intent1.putExtra("room", rooms);
                            startActivity(intent1);

                        }
//                        else if(filter_room.get(filter_room.size()-position-1).getUser5_id().equals("default")
//                                && !filter_room.get(filter_room.size()-position-1).getUser4_id().equals(mUser123.getUid())
//                                && !filter_room.get(filter_room.size()-position-1).getUser2_id().equals(mUser123.getUid())
//                                && !filter_room.get(filter_room.size()-position-1).getUser3_id().equals(mUser123.getUid())) {
//
//                            filter_room.get(filter_room.size()-position-1).setUser5_id(mUser123.getUid());
//                            filter_room.get(filter_room.size()-position-1).setUser5(1);
//                            filter_room.get(filter_room.size()-position-1).setTotal_user(filter_room.get(filter_room.size()-position-1).getUser1()
//                                    + filter_room.get(filter_room.size()-position-1).getUser2()
//                                    + filter_room.get(filter_room.size()-position-1).getUser3()
//                                    + filter_room.get(filter_room.size()-position-1).getUser4()
//                                    + filter_room.get(filter_room.size()-position-1).getUser5());
//                            reference.child(filter_room.get(filter_room.size()-position-1).getKey()).setValue(filter_room.get(filter_room.size()-position-1));
//                            rooms.add(filter_room.get(filter_room.size()-position-1));
//                            Toast.makeText(view.getContext(), "Join to room..", Toast.LENGTH_SHORT).show();
//                            intent1.putExtra("room", rooms);
//                            startActivityForResult(intent1, 0);
//                        }
                        else{
                            Toast.makeText(view.getContext(),"can't join to room",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                array_room = (List<Room>) results.values;
                yy = 1;
                notifyDataSetChanged();
            }
        }
    }



}
