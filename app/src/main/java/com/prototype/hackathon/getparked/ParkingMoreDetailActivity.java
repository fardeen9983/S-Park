package com.prototype.hackathon.getparked;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ParkingMoreDetailActivity extends AppCompatActivity {
    private TextView name;
    private TextView address;
    private TextView cost;
    private TextView distance;
    private TextView counter;
    private TextView openingTime;
    private TextView closingTime;
    private Button mon;
    private Button tue;
    private Button wed;
    private Button thur;
    private Button fri;
    private Button sat;
    private Button sun;
    private Button go;

    private Sensor sensor;
    private int count;
    private HashMap<Integer,Sensor> sensorHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Intent intent = getIntent();
        ParkingSpot parkingSpot = (ParkingSpot) intent.getSerializableExtra("selected_spot");

        initDisplay();

        if (parkingSpot!=null) {
            name.setText(parkingSpot.getName());
            address.setText(parkingSpot.getAddress());
            cost.setText("Rs. " + parkingSpot.getCost() + "");
            distance.setText("10 kms");
            sensorHashMap = parkingSpot.getSensor();
            for (int i = 0; i < sensorHashMap.size(); i++) {
                sensor = sensorHashMap.get(i + 1);
                if (sensor.isFree())
                    count++;
            }
            counter.setText(count + "");

        }
    }

    public void initDisplay(){
        name = findViewById(R.id.parkingSpaceMore);
        address = findViewById(R.id.addressViewMore);
        cost = findViewById(R.id.RdisplayViewMore);
        distance = findViewById(R.id.DdisplayViewMore);
        counter = findViewById(R.id.counter);
        openingTime = findViewById(R.id.displayView);
        closingTime = findViewById(R.id.displayView1);

        mon = findViewById(R.id.buttonMon);
        tue = findViewById(R.id.buttonTue);
        wed = findViewById(R.id.buttonWed);
        thur = findViewById(R.id.buttonThu);
        fri = findViewById(R.id.buttonFri);
        sat = findViewById(R.id.buttonSat);
        sun = findViewById(R.id.buttonSun);
        go = findViewById(R.id.buttonGO);

        sensorHashMap = new HashMap<>();
        count = 0;
    }
}
