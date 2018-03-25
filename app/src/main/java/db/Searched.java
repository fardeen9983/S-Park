package db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by shrey on 24-03-2018.
 */

@Entity
public class Searched {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String locName;

    @NonNull
    public int pincode;

    @NonNull
    public LatLng coordinates;

}
