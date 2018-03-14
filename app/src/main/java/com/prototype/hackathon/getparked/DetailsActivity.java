package com.prototype.hackathon.getparked;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private ListView listView;
    private SpotDetails spotDetails;
    private SpotDetailsList spotDetailsList;
    private SpotDetailsAdapter spotDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        listView = findViewById(R.id.details_list);
        spotDetailsList = (SpotDetailsList)intent.getSerializableExtra("spot");
        spotDetailsAdapter = new SpotDetailsAdapter(this,spotDetailsList);
        listView.setAdapter(spotDetailsAdapter);

    }
}
