package com.prototype.hackathon.getparked;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LUCIFER on 22-03-2018.
 */

public class DatabseRetriever {

    private DatabaseReference dbRef;
    private FirebaseDatabase fireDB;
    public List<ParkingSpot> locList;
    private Iterable<DataSnapshot> pincode;
    private HashMap<Integer, Sensor> sensorHashMap;
    private ParkingSpot parkingSpot;
    private Context context;
    private int pinc;

    public DatabseRetriever(int pin, float rad, Context context) {
        pinc = pin;
        this.context = context;
        createList();
    }

    public void createList() {
        locList = new ArrayList<>();

        fireDB = FirebaseDatabase.getInstance();
        dbRef = fireDB.getReference("manager").child("gujarat").child("" + pinc);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AsyncDBLoader dbLoader = new AsyncDBLoader();
                dbLoader.execute(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class AsyncDBLoader extends AsyncTask<DataSnapshot, Void, ArrayList<ParkingSpot>> {

        @Override
        protected ArrayList<ParkingSpot> doInBackground(DataSnapshot... dataSnapshots) {
            ArrayList<ParkingSpot> list = new ArrayList<>();
            pincode = dataSnapshots[0].getChildren();
            long spotCount = dataSnapshots[0].getChildrenCount();
            for (DataSnapshot temp : pincode) {
                parkingSpot = new ParkingSpot();
                String key = temp.getKey().trim();
                if (key.equals("city"))
                    continue;
                //if (temp.child("name").getValue() != null)
                    parkingSpot.setName((String) temp.child("name").getValue());
                //if (temp.child("address").getValue() != null)
                    parkingSpot.setAddress((String) temp.child("address").getValue());
               // if (temp.child("cost").getValue() != null)
                    parkingSpot.setCost((double) temp.child("cost").getValue());
                //if (temp.child("lat").getValue() != null)
                    parkingSpot.setLatitiude((double) temp.child("lat").getValue());
                //if (temp.child("long").getValue() != null)
                    parkingSpot.setLongitude((double) temp.child("long").getValue());

                    parkingSpot.setPincode(pinc);

                DataSnapshot sensor = temp.child("sensor");
                DataSnapshot timer = temp.child("time");

                Iterable<DataSnapshot> sensorChildren = sensor.getChildren();
                Iterable<DataSnapshot> timeChildren = timer.getChildren();
                parkingSpot.setSensorCount(sensor.getChildrenCount());

                List<Boolean> sensorValue = new ArrayList<>();
                List<Long> timeValue = new ArrayList<>();

                for (DataSnapshot x : sensorChildren) {
                    if (x.getValue().toString().trim() == "true")
                        sensorValue.add(true);
                    else
                        sensorValue.add(false);
                }

                for (DataSnapshot y : timeChildren) {
                    timeValue.add((Long) y.getValue());
                }

                sensorHashMap = new HashMap<>();
                for (int i = 0; i < parkingSpot.getSensorCount(); i++) {
                    sensorHashMap.put(i + 1, new Sensor(sensorValue.get(i), timeValue.get(i)));
                }
                parkingSpot.setSensor(sensorHashMap);
                list.add(parkingSpot);
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<ParkingSpot> parkingSpots) {
            super.onPostExecute(parkingSpots);
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("list", parkingSpots);
            context.startActivity(intent);
        }
    }
}
