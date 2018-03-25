package com.prototype.hackathon.getparked;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ListView listView;
    private ParkingSpot spot;
    private List<ParkingSpot> parkingSpots;
    private Location current;
    private TextView emptyList;
    private ParkingSpotAdapter parkingSpotAdapter;
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (parkingSpots!=null){
            List<MarkerOptions> markers = new ArrayList<>();
            for(ParkingSpot spot : parkingSpots){
                markers.add(new MarkerOptions().position(new LatLng(spot.getLatitiude(),spot.getLongitude()))
                        .title(spot.getName()));
            }
            Intent intent = new Intent(getApplicationContext(),MapActivity.class);
            intent.putExtra("marker", (Serializable) markers);
            startActivity(new Intent(getApplicationContext(),MapActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        parkingSpots = (ArrayList<ParkingSpot>) intent.getSerializableExtra("list");
        current = intent.getParcelableExtra("loc");
        emptyList = findViewById(R.id.list_empty);
        listView = findViewById(R.id.details_list);
        listView.setEmptyView(emptyList);
        if (parkingSpots == null&&current!=null) {
            LatLng latLng = new LatLng(current.getLatitude(),current.getLongitude());
            DatabseRetriever DBr = new DatabseRetriever(geocode(latLng), 0, this);

        }
        if (parkingSpots != null) {
            parkingSpotAdapter = new ParkingSpotAdapter(this, parkingSpots);
            listView.setAdapter(parkingSpotAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }
    }
    private int geocode(LatLng latLng){
        int pincode=0;
        List<Address> addresses = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this);
        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            if (addresses.get(0) != null) {
                Address address = addresses.get(0);
                StringBuilder builder = new StringBuilder();
                for (int i=0;i<address.getMaxAddressLineIndex();i++)
                    builder.append(address.getAddressLine(i));
                Log.v(TAG,builder.toString()+"address!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );
                pincode=388120;
                //if(address.getPostalCode()!=null)
                // pincode=Integer.parseInt(address.getPostalCode().trim());
                //Log.v(TAG,address.getPostalCode())
            }
        }
        return pincode;
    }
}
