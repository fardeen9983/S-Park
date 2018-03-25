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
public interface SearchedDao {

    @Query("SELECT * FROM Searched")
    List<Searched> getAllSearched();

    @Query("SELECT * FROM Searched where locName LIKE  :locName")
    Searched findSearchedByName(String locName);

    @Insert(onConflict = IGNORE)
    void insertSearched(Searched searched);

    @Delete
    void deleteSearched(Searched searched);

    @Query("DELETE FROM Searched")
    void deleteAllSearched();
}
