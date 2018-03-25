package db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by shrey on 24-03-2018.
 */

@Dao
@TypeConverters(LatlngConverter.class)
public interface FavouritesDao {

    @Query("SELECT * FROM Favourites")
    List<Favourites> getAllFavourites();

    @Query("SELECT * FROM Favourites where locName LIKE  :locName")
    Favourites findFavouritesByName(String locName);

    @Insert(onConflict = IGNORE)
    void insertFavourites(Favourites favourites);

    @Delete
    void deleteFavourites(Favourites favourites);

    @Query("DELETE FROM Favourites")
    void deleteAllFavourites();
}
