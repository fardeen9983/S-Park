package com.prototype.hackathon.getparked;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LUCIFER on 14-03-2018.
 */

public class DetailsLoader extends AsyncTaskLoader<List<SpotDetails>> {
    private String query;

    public DetailsLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public List<SpotDetails> loadInBackground() {
        List<SpotDetails> spotDetails = new ArrayList<>();
        if(query!=null){

        }
        return null;
    }
}
