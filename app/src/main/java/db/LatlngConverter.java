package db;

/**
 * Created by shrey on 24-03-2018.
 */

import android.arch.persistence.room.TypeConverter;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class LatlngConverter {

    @TypeConverter
    public static LatLng toLatLng(String latlng) {
        if(latlng==null)
            return null;
        String pieces[] = latlng.split(",");
        LatLng latLng = new LatLng(Double.parseDouble(pieces[0]),Double.parseDouble(pieces[1]));
        return latLng;
    }

    @TypeConverter
    public static String toString(LatLng latLng) {
        return latLng == null ? null : String.format(Locale.US,"%f %f", latLng.latitude,latLng.longitude);
    }
}
