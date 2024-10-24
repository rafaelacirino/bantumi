package es.upm.miw.bantumi.datos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = Bantumi.TABLA)
public class Bantumi {

    static public final String TABLA = "partidas";

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String nombreJugador;
    public Date fechaPartida;
    public int semillasJugador1;
    public int semillasJugador2;

    public Bantumi(String nombreJugador, Date fechaPartida, int semillasJugador1, int semillasJugador2) {
        this.nombreJugador = nombreJugador;
        this.fechaPartida = fechaPartida;
        this.semillasJugador1 = semillasJugador1;
        this.semillasJugador2 = semillasJugador2;
    }

    @Override
    public String toString() {
        return "Bantumi{" +
                " nombreJugador='" + nombreJugador + '\'' +
                ", fechaPartida=" + fechaPartida +
                ", semillasJugador1=" + semillasJugador1 +
                ", semillasJugador2=" + semillasJugador2 +
                '}';
    }
}
