package es.upm.miw.bantumi.datos;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Bantumi.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BantumiRoomDatabase extends RoomDatabase {

    public static final String BASE_DATOS = Bantumi.TABLA + ".db";

    public abstract BantumiDAO partidaDao();

    private static volatile BantumiRoomDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static BantumiRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BantumiRoomDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BantumiRoomDatabase.class, BASE_DATOS)
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
         new RoomDatabase.Callback() {
             @Override
             public void onOpen(@NonNull SupportSQLiteDatabase db) {
                 super.onOpen(db);

                 databaseWriteExecutor.execute(new Runnable() {
                     @Override
                     public void run() {
                         BantumiDAO dao = INSTANCE.partidaDao();
                     }
                 });
             }
        };
}
