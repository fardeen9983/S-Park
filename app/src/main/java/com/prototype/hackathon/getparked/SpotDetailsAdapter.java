package com.prototype.hackathon.getparked;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by LUCIFER on 14-03-2018.
 */

public class SpotDetailsAdapter extends ArrayAdapter<SpotDetails> {
   public SpotDetailsAdapter(Context context,List<SpotDetails> spotDetailsList){
       super(context,0,spotDetailsList);
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        if(root==null){
            root = LayoutInflater.from(getContext()).inflate(R.layout.details_view,parent,false);
        }

        return root;
    }
}
