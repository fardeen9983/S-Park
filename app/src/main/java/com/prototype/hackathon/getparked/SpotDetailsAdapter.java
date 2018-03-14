package com.prototype.hackathon.getparked;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LUCIFER on 14-03-2018.
 */

public class SpotDetailsAdapter extends ArrayAdapter<SpotDetails> {
    private SpotDetailsList spotDetailsList;

    public SpotDetailsAdapter(Context context, SpotDetailsList spotDetailsList) {
        super(context, 0, spotDetailsList);
        this.spotDetailsList = spotDetailsList;
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
        TextView counter = root.findViewById(R.id.counter);
        SpotDetails spotDetails = spotDetailsList.get(position);

        address.setText(spotDetails.getAddress());

        return root;
    }
}
