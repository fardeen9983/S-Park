package com.prototype.hackathon.getparked;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ListView listView;
    private List<CarDetails> carDetailsList;
    private CarDetails carDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
