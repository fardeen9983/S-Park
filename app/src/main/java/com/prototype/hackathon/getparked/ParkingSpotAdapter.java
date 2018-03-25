package com.prototype.hackathon.getparked;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by LUCIFER on 22-03-2018.
 */

public class ParkingSpotAdapter extends ArrayAdapter<ParkingSpot> {

    private ParkingSpot spot;
    private Sensor sensor;

    public ParkingSpotAdapter(@NonNull Context context, List<ParkingSpot> spots) {
        super(context, 0, spots);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        if (root == null) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.activity_few, parent, false);
        }
        TextView address = root.findViewById(R.id.addressView);
        TextView name = root.findViewById(R.id.parkingSpaces);
        TextView distance = root.findViewById(R.id.DdisplayView);
        TextView cost = root.findViewById(R.id.RdisplayView);
        spot = getItem(position);
       if (!(spot.getAddress() == "" || spot.getAddress() == null))
              address.setText(spot.getAddress());
        name.setText(spot.getName());
//        HashMap<Integer, Sensor> map = spot.getSensor();
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < map.size(); i++) {
//            sensor = map.get(i + 1);
//            builder.append("\n" + sensor.toString(i + 1));
//        }
        distance.setText("dfsdf");
        cost.setText(spot.getCost() + "");
        return root;

    }
}
