package com.prototype.hackathon.getparked;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private ImageView qr;
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
        //return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        qr = findViewById(R.id.imageQR);
        GetQR getQR = new GetQR();
        auth = FirebaseAuth.getInstance();
        getQR.execute();
    }

    private class GetQR extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void...voids) {
            Uri uri = auth.getCurrentUser().getPhotoUrl();
            URL url=null;
            Bitmap bitmap = null;
            if (uri != null) {
                try {
                    url = new URL(uri.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap!=null)
            {
                //BitmapDrawable drawable = new BitmapDrawable()
                qr.setImageBitmap(bitmap);
            }
        }
    }
}
