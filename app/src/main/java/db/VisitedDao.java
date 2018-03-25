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
@TypeConverters({DateConverter.class,LatlngConverter.class})
public interface VisitedDao {

    @Query("SELECT * FROM Visited")
    List<Visited> getAllVisited();

    @Query("SELECT * FROM Visited where locName LIKE  :locName")
    Visited findVisitedByName(String locName);

    /*@Query("SELECT * FROM Visited where cost = min(cost)")
    Visited findVisitedByCost(double cost);

    @Query("SELECT * FROM Visited where duration = max(duration)")
    Visited findVisitedByDuration(long duration);*/

    @Insert(onConflict = IGNORE)
    void insertVisited(Visited visited);

    @Delete
    void deleteVisited(Visited visited);

    @Query("DELETE FROM  Visited")
    void deleteAllVisited();
}
