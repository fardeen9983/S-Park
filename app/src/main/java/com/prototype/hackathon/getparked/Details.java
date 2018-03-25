package com.prototype.hackathon.getparked;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Details {

    private double radius;
    private String state;
    private String city;
    private Address address;
    private GoogleMap mGoogleMap;
    private ArrayList<Address> addresses;
    private ListView listView;
    public static SpotDetailsList spotDetailsList;
    private SpotDetails spotDetails;
    private DatabaseReference databaseReference;
    private SpotDetailsAdapter spotDetailsArrayAdapter;
    private Location current;
    private final String MANAGER = "manager";
    private final String TAG = getClass().getSimpleName();
    private Iterable<DataSnapshot> dataSnapshots;
    private ArrayList<Long> valueCount = new ArrayList<>();
    private long locCount;

    private Context context;
    private final String query = "https://get-parked-hackathon-2k18.firebaseio.com/.json?print=pretty&format=export&auth=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1MjA5NzY4ODEsImV4cCI6MTUyMDk4MDQ4MSwidiI6MCwiYWRtaW4iOnRydWV9.XXYoMweAkrrsMge3FDjtONltFZyHIp4iJdjw-ZBUxrk";
    private View root;
    public Details(GoogleMap googleMap, Context context, Location current, double radius) {
        this.mGoogleMap =googleMap;
        this.context = context;
        this.current = current;
        this.radius = radius;
    }
    public void onCreate() {

       // root = LayoutInflater.from(context).inflate(R.layout.activity_details,)
        //getLoaderManager().initLoader(1,null,this);

        //listView = findViewById(R.id.details_list);
        spotDetailsList = new SpotDetailsList();
        //getAddress();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(MANAGER).child("gujarat").child("rajkot");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LoadSpots loadSpots = new LoadSpots();
                loadSpots.execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StringBuilder builder = new StringBuilder();
        for(SpotDetails details : spotDetailsList){
            builder.append(details.toString()+"\n");
        }

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private class LoadSpots extends AsyncTask<DataSnapshot,Void,SpotDetailsList>{

        @Override
        protected SpotDetailsList doInBackground(DataSnapshot... dataSnapshot) {
            dataSnapshots = dataSnapshot[0].getChildren();
            locCount = dataSnapshot[0].getChildrenCount();

            for (DataSnapshot snapshot : dataSnapshots) {
                spotDetails = new SpotDetails();
                spotDetails.setAddress((String) snapshot.child("name").getValue());
                spotDetails.setLatitude((double) snapshot.child("lat").getValue());
                spotDetails.setLongitude((double) snapshot.child("long").getValue());

                DataSnapshot temp = snapshot.child("sensor");
                ArrayList<Boolean> sensorValue = new ArrayList<>();
                Iterable<DataSnapshot> sen = temp.getChildren();
                for (DataSnapshot dataSnapshot1 : sen) {
                    if (dataSnapshot1.getValue() == "true")
                        sensorValue.add((Boolean) dataSnapshot1.getValue());
                    else
                        sensorValue.add((Boolean) dataSnapshot1.getValue());
                }
                spotDetails.setSensors(sensorValue);
                spotDetailsList.add(spotDetails);

            }
            return spotDetailsList;
        }

        @Override
        protected void onPostExecute(SpotDetailsList spotDetails) {
            super.onPostExecute(spotDetails);
            List<String> name = new ArrayList<>();
            for(SpotDetails details : spotDetails){
                LatLng latLng = new LatLng(details.getLatitude(),details.getLongitude());
                if(checkRadius(latLng))
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(details.getAddress()));
            }
        }
    }
    private boolean checkRadius(LatLng latLng){
        float []results ={};
        //ParkingSpot.distanceBetween(current.getLatitude(),current.getLongitude(),latLng.latitude,latLng.longitude,results);
        Log.v(TAG,results.toString()+" sadfsadfsdf");
        return true;
    }
}
