package com.prototype.hackathon.getparked;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class DetailsActivity extends AppCompatActivity  {

    private String state;
    private String city;
    private Address address;
    private ArrayList<Address> addresses;
    private ListView listView;
    public static SpotDetailsList spotDetailsList;
    private SpotDetails spotDetails;
    private DatabaseReference databaseReference;
    public static void calc(){

    }
    private SpotDetailsAdapter spotDetailsArrayAdapter;
    private Location current;
    private final String MANAGER = "manager";
    private final String TAG = getClass().getSimpleName();
    private Iterable<DataSnapshot> dataSnapshots;
    private ArrayList<Long> valueCount = new ArrayList<>();
    private long locCount;
    private final String query = "https://get-parked-hackathon-2k18.firebaseio.com/.json?print=pretty&format=export&auth=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1MjA5NzY4ODEsImV4cCI6MTUyMDk4MDQ4MSwidiI6MCwiYWRtaW4iOnRydWV9.XXYoMweAkrrsMge3FDjtONltFZyHIp4iJdjw-ZBUxrk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //getLoaderManager().initLoader(1,null,this);
        current = MapActivity.location;
        listView = findViewById(R.id.details_list);
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
    private class LoadSpots extends AsyncTask<DataSnapshot,Void,ArrayList<SpotDetails>>{

        @Override
        protected ArrayList<SpotDetails> doInBackground(DataSnapshot... dataSnapshot) {
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
        protected void onPostExecute(ArrayList<SpotDetails> spotDetails) {
            super.onPostExecute(spotDetails);
            List<String> name = new ArrayList<>();
            for(SpotDetails details : spotDetails){
                name.add(details.getAddress());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,name);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putSerializable("spot",spotDetailsList);
        startActivity(new Intent(getApplicationContext(),MapActivity.class),bundle);
    }
}
