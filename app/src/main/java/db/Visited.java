package db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by shrey on 24-03-2018.
 */

@Entity
public class Visited {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String locName;

    @NonNull
    public int pincode;

    @NonNull
    public double lat;

    @NonNull
    public  double lng;

    @NonNull
    public double cost;

    @NonNull
    public long duration;

    @NonNull
    public Date date;
}
