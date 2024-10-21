package es.upm.miw.bantumi.datos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BantumiRepositorio {

    private BantumiDAO mBantumiDAO;
    private LiveData<List<Bantumi>> mPartidas;

    public BantumiRepositorio(Application application) {
        BantumiRoomDatabase db = BantumiRoomDatabase.getDatabase(application);
        mBantumiDAO = db.partidaDao();
        mPartidas = mBantumiDAO.getAll();
    }

    public LiveData<List<Bantumi>> getAllPartidas() {
        return mPartidas;
    }

    public long insert(Bantumi bantumi) {
        return mBantumiDAO.insert(bantumi);
    }

    public void deleteAll() {
        mBantumiDAO.deletAll();
    }

    public void deletePartida(Bantumi bantumi) {
        mBantumiDAO.deletePartida(bantumi);
    }
}
