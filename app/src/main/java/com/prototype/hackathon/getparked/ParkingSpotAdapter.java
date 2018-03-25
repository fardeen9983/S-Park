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
            root = LayoutInflater.from(getContext()).inflate(R.layout.details_view, parent, false);
        }
        TextView address = root.findViewById(R.id.text2);
        TextView timer = root.findViewById(R.id.timer);
        TextView cost = root.findViewById(R.id.cost);
        spot = getItem(position);
//        if (spot.getAddress() == "" || spot.getAddress() == null)
//            address.setText(spot.getName());
//        else
        address.setText(spot.getName());
        HashMap<Integer, Sensor> map = spot.getSensor();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < map.size(); i++) {
            sensor = map.get(i + 1);
            builder.append("\n" + sensor.toString(i + 1));
        }
        timer.setText(builder.toString());
        cost.setText(spot.getCost() + "");
        return root;

    }
}
