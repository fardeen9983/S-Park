package db.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;
import java.util.Date;

import db.AppDatabase;
import db.Favourites;
import db.Searched;
import db.Visited;

/**
 * Created by shrey on 24-03-2018.
 */

public class DatabaseInitializer {

    // Simulate a blocking operation delaying each Loan insertion with a delay:
    private static final int DELAY_MILLIS = 500;

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Searched addSearched(final AppDatabase db, final int id,
                                        final String locName, final int pincode, final double lat,final double lng) {
        Searched searched = new Searched();
        searched.id = id;
        searched.locName = locName;
        searched.pincode = pincode;
        searched.lat = lat;
        searched.lng = lng;
        db.searchedModel().insertSearched(searched);
        return searched;
    }

    private static Favourites addFavourites(final AppDatabase db, final int id,
                                            final String locName, final int pincode,
                                            final double lat,final double lng) {
        Favourites favourites = new Favourites();
        favourites.id = id;
        favourites.locName = locName;
        favourites.pincode = pincode;
        favourites.lat = lat;
        favourites.lng = lng;
        db.favouritesModel().insertFavourites(favourites);
        return  favourites;
    }

    private static Visited addVisited(final AppDatabase db, final int id,
                                      final String locName, final int pincode, final double lat,final double lng,
                                      final double cost, final long duration, final Date date) {
        Visited visited = new Visited();
        visited.id = id;
        visited.locName = locName;
        visited.pincode = pincode;
        visited.lat = lat;
        visited.lng = lng;
        visited.cost = cost;
        visited.duration = duration;
        visited.date = date;
        db.visitedModel().insertVisited(visited);
        return visited;
    }


    private static void populateWithTestData(AppDatabase db) {
        db.searchedModel().deleteAllSearched();
        db.favouritesModel().deleteAllFavourites();
        db.visitedModel().deleteAllVisited();

        Date date = new Date();
        date.setTime(38383838);
        Searched searched = addSearched(db,1,"VVN",388120,27.8838,24.4234);
        Favourites favourites = addFavourites(db, 1, "anand", 370001, 67.93,63.90);
        Visited visited = addVisited(db,1,"VVN",380020,34.9986,56.998,65,35353,date);


    }



    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
