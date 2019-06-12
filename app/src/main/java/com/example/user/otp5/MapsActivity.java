package com.example.user.otp5;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.user.otp5.AboutMap.DirectionFinder;
import com.example.user.otp5.AboutMap.DirectionFinderListener;
import com.example.user.otp5.AboutMap.Route;
import com.example.user.otp5.Model.Room;
import com.example.user.otp5.MenuApp.MessageEventActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;
    private Button btnFindPath, btncreate;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog, progressDialog1;
    private AutocompleteSupportFragment autocompleteFragmentorigin, autocompleteFragmentdestination;
    private String origin, destination, bathstr = "", click, km, name_room = "";
    private int bahtkm, bahttime;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private ArrayList<Room> roomlist_true;
    private boolean check, checkcreate;
    private Geocoder geocoder;
    private List<Address> addressList;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastlocation,currentlocation;
    private Marker marker;
    private static final int Request_User_Location_Code = 99;
    private int setTime, setDate, min = 0, hour = 0, year = 0, month = 0, day = 0;
    private Button mTime, mDate;
    private EditText nameroom;
    private static final int REQUSET_CODE = 101;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        roomlist_true = new ArrayList<>();
        check = false;
        checkcreate = false;
        name_room = "";
        origin = "1";
        destination = "2";
        click = "not click";
        km = "";
        setTime = 3;
        setDate = 4;
        geocoder = new Geocoder(this, Locale.getDefault());
        nameroom = findViewById(R.id.name_room_12);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        Places.initialize(getApplicationContext(), "AIzaSyDEwedjSefPcNoJhADXc8DK2TyooeU19xI");

        PlacesClient placesClient = Places.createClient(this);

        autocompleteFragmentorigin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_origin);
        autocompleteFragmentorigin.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME));

        autocompleteFragmentdestination = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_destination);
        autocompleteFragmentdestination.setPlaceFields(Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME));

        autocompleteFragmentorigin.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),mMap.getMyLocation()+"",Toast.LENGTH_LONG).show();
                origin = place.getName();
                //origin = mMap.getMyLocation()+"";
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        autocompleteFragmentdestination.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull com.google.android.libraries.places.api.model.Place place) {
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_LONG).show();
                destination = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//
//        mapFragment.getMapAsync(this);


        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                km = "";
                click = "not click";
                bahtkm = 0;
                bahttime = 0;
                bathstr = "";
                sendRequest();
            }
        });

        mTime = findViewById(R.id.time);
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime = 0;
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(MapsActivity.this, MapsActivity.this, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);

                timePickerDialog.show();
            }
        });

        mDate = findViewById(R.id.date);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate = 0;
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(MapsActivity.this, MapsActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


        roomlist_true = new ArrayList<>();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Room");

        btncreate = (Button) findViewById(R.id.btn_create);
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click.equals("not click")) {
                    Toast.makeText(getApplicationContext(), "Please find a path!", LENGTH_SHORT).show();
                    return;
                }
                createNewRoom();

            }
        });
        check = false;
    }

    public void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    lastlocation = location;
                    mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);

                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }



    public void createNewRoom(){
        final Room room = new Room(origin, destination, km, bahtkm);
        room.setKey(reference.push().getKey());
        room.setTotal_user(room.getUser1());
        room.setUser1_id(mUser.getUid());
        room.setMinites(min);
        room.setHours(hour);
        room.setDays(day);
        room.setMonths(month);
        room.setYears(year);
        room.setName_room(name_room);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int)dataSnapshot.getChildrenCount()+1;
                room.setRoom_no(count);
                reference.child(room.getKey()).setValue(room);
                Intent intent = new Intent(getApplicationContext(), MessageEventActivity.class);
                ArrayList<Room> rooms = new ArrayList<>();
                rooms.add(room);
                intent.putExtra("room",rooms);
                Toast.makeText(getApplicationContext(), "Create a new room success", LENGTH_SHORT).show();
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendRequest() {

        name_room = nameroom.getText().toString();

        if (origin.equals("1")) {
            Toast.makeText(this, "Please enter origin address!", LENGTH_SHORT).show();
            return;
        }
        if (destination.equals("2")) {
            Toast.makeText(this, "Please enter destination address!", LENGTH_SHORT).show();
            return;
        }
        if(setTime==3){
            Toast.makeText(this, "Please enter time!", LENGTH_SHORT).show();
            return;
        }
        if(setDate==4){
            Toast.makeText(this, "Please enter date!", LENGTH_SHORT).show();
            return;
        }
        if(name_room.equals("")){
            Toast.makeText(this, "Please enter room name!", LENGTH_SHORT).show();
            return;
        }


        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(latLng)
//                .title("I am here");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
//        mMap.addMarker(markerOptions);
//        LatLng hcmus = new LatLng(10.762963, 106.682394);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
//        originMarkers.add(mMap.addMarker(new MarkerOptions()
//                .title("Đại học Khoa học tự nhiên")
//                .position(hcmus)));
//        LatLng hcmus1 = new LatLng(15.762963, 199.682394);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
//        originMarkers.add(mMap.addMarker(new MarkerOptions()
//                .title("Đại học Khoa học tự nhiên")
//                .position(hcmus1)));

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

            //mapFragment.onCreate(savedInstanceState);
//            mapFragment.onResume();
////            mapFragment.getMapAsync(this);


        }



    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {

            String[] bahtarr = route.distance.text.split("");
            for(int i =0;i<bahtarr.length;i++){
                if(bahtarr[i].equals("l") ||bahtarr[i].equals("u") || bahtarr[i].equals("n") || bahtarr[i].equals(",") || bahtarr[i].equals("k") || bahtarr[i].equals("m") || bahtarr[i].equals(" ")){

                }
                else {
                    bathstr += bahtarr[i];
                }
            }
            Log.i("km",bathstr+"");

//            String[] timebaht = route.duration.text.split("");
//            for(int i =0;i<timebaht.length;i++){
//                if(timebaht[i].equals("l") ||timebaht[i].equals("u") || timebaht[i].equals("n") || timebaht[i].equals(",") || timebaht[i].equals("m") || bahtarr[i].equals("i") || bahtarr[i].equals("n") || bahtarr[i].equals("s") || timebaht[i].equals(" ")){
//
//                }
//                else {
//                    timestr += timebaht[i];
//                }
//            }
//            Log.i("time", timestr+"");

            bahtkm = 0;
            bahttime = 0;
            int defau = 35;
            bahtkm = (int) (Double.parseDouble(bathstr)*2+defau); //km
            //bahttime = (int) Double.parseDouble(timestr); //time

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);
            ((TextView) findViewById(R.id.baht)).setText(bahtkm+" baht");
//            Log.i("distance0", baht+"");

            km = route.distance.text;
            Log.i("distance1", route.distance.text+"");
            Log.i("distance2", route.distance.value+"");
            Log.i("starttttttttttt", route.startLocation+"");
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));

            Log.i("enddddddddd", route.endLocation+"");
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.endLocation, 18));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++) {
                Log.i("xxx", String.valueOf(route.points.get(i)));
                polylineOptions.add(route.points.get(i));
            }

            polylinePaths.add(mMap.addPolyline(polylineOptions));

            click = "correct click";
        }
    }
    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_User_Location_Code:
                if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==PackageManager.PERMISSION_GRANTED) {
                        if(googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Permission Denied..",Toast.LENGTH_SHORT).show();
                }
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        lastlocation = location;

        if(marker!=null){
            marker.remove();
        }

        LatLng latLng = new LatLng(lastlocation.getLatitude(), lastlocation.getLongitude());
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            final String address = addressList.get(0).getAddressLine(0);
            String area = addressList.get(0).getLocality();
            String city = addressList.get(0).getAdminArea();
            String country = addressList.get(0).getCountryName();
            String postalcode = addressList.get(0).getPostalCode();

            final String fulladdress = address + ", " + area + ", " + city + ", " + country + ", " + postalcode;

            //mapFragment.onResume();

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(address)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

            marker = mMap.addMarker(markerOptions);


            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(2));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    autocompleteFragmentorigin.setText(fulladdress);
                    origin = address;
                    Toast.makeText(getApplicationContext(),address,LENGTH_SHORT).show();
                }
            });

            if(googleApiClient !=null ){
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            }



        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
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
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendarr = Calendar.getInstance();
       // Log.d("hello","hour  ="+hourTime);
        if (day <= calendarr.get(Calendar.DAY_OF_MONTH) &&
                (month <= calendarr.get(Calendar.MONTH))&&
                (hourOfDay < calendarr.get(Calendar.HOUR))){
            Toast.makeText(this,"cant pick the time",Toast.LENGTH_SHORT).show();
        }
        else if (minute < calendarr.get(Calendar.MINUTE) &&
                (hourOfDay == calendarr.get(Calendar.HOUR))){
            Toast.makeText(this,"cant pick the time",Toast.LENGTH_SHORT).show();
        }
        else {
            hour = hourOfDay;
            min = minute;
            mTime.setText(hour+" : "+min);
            Toast.makeText(getApplicationContext(),hourOfDay+" : "+minute,Toast.LENGTH_SHORT).show();
        }

        //hour = hourOfDay;
//        min = minute;
    }


    @Override
    public void onDateSet(DatePicker view, int years, int monthz, int dayOfMonth) {

        mDate.setText(monthz+"/"+dayOfMonth+"/"+years);
        Toast.makeText(getApplicationContext(),monthz+"/"+dayOfMonth+"/"+years,Toast.LENGTH_SHORT).show();
        month = monthz;
        day = dayOfMonth;
        year = years;
    }
}
