package es.upm.miw.bantumi.datos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BantumiDAO {

    @Query("SELECT * FROM " + Bantumi.TABLA)
    LiveData<List<Bantumi>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Bantumi bantumi);

    @Query("DELETE FROM " + Bantumi.TABLA)
    void deletAll();

    @Delete
    void deletePartida(Bantumi bantumi);
}
