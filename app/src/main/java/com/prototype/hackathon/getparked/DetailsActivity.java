package com.prototype.hackathon.getparked;

import android.app.LoaderManager;
import android.content.Loader;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<SpotDetails>> {

    private String state;
    private String city;
    private Address address;
    private List<Address> addresses;
    private ListView listView;
    private List<SpotDetails> spotDetailsList;
    private SpotDetails spotDetails;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private SpotDetailsAdapter spotDetailsArrayAdapter;
    private Location current;
    private final String MANAGER = "manager";
    private String key;
    private String child;
    private final String TAG = getClass().getSimpleName();
    private Iterable<DataSnapshot> dataSnapshots;
    private List<String> loc = new ArrayList<>();
    private List<Long> valueCount = new ArrayList<>();
    private long locCount;
    private final String query = "https://get-parked-hackathon-2k18.firebaseio.com/.json?print=pretty&format=export&auth=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1MjA5NzY4ODEsImV4cCI6MTUyMDk4MDQ4MSwidiI6MCwiYWRtaW4iOnRydWV9.XXYoMweAkrrsMge3FDjtONltFZyHIp4iJdjw-ZBUxrk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //getLoaderManager().initLoader(1,null,this);
        current = MapActivity.location;
        listView = findViewById(R.id.details_list);
        spotDetailsList = new ArrayList<>();
        spotDetailsArrayAdapter = new SpotDetailsAdapter(this, spotDetailsList);
        listView.setAdapter(spotDetailsArrayAdapter);
        //getAddress();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(MANAGER).child("gujarat").child("rajkot");
        key = databaseReference.getKey();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshots = dataSnapshot.getChildren();
                locCount = dataSnapshot.getChildrenCount();

                for (DataSnapshot snapshot : dataSnapshots) {
                    spotDetails = new SpotDetails();
                    spotDetails.setLatitude((double)snapshot.child("lat").getValue());
                    spotDetails.setLongitude((double)snapshot.child("long").getValue());
                    spotDetails.setAddress((String)snapshot.child("name").getValue());
                    DataSnapshot temp = snapshot.child("sensor");
                    List<Boolean> sensorValue = new ArrayList<>();
                    Iterable<DataSnapshot> sen = temp.getChildren();
                    int i=0;
                    for(DataSnapshot dataSnapshot1 : sen){
                        if(i==0)
                            continue;
                        sensorValue.add((Boolean)dataSnapshot.getValue());
                    }
                    spotDetails.setSensors(sensorValue);
                    spotDetailsList.add(spotDetails);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        List<String> names = new ArrayList<>();
        for(SpotDetails spotDetails : spotDetailsList){
            names.add(spotDetails.getAddress());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);
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

    @Override
    public Loader<List<SpotDetails>> onCreateLoader(int id, Bundle args) {
        return new DetailsLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<SpotDetails>> loader, List<SpotDetails> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<SpotDetails>> loader) {

    }


    private void getAddress() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(current.getLatitude(), current.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = addresses.get(0);
        city = address.getAddressLine(0);
        state = address.getAddressLine(1);
    }
}
