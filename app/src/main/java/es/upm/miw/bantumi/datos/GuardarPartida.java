package es.upm.miw.bantumi.datos;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import es.upm.miw.bantumi.R;

public class GuardarPartida {

    private static final String TAG = "GuardaPartida";
    private static final String DIRECTORIO = "datos";
    private static final String NOMBRE_ARCHIVO = "partida.txt";

    private final Context context;

    public GuardarPartida(Context context) {
        this.context = context;
    }

    public void guardarPartida(String estadoPartida) {
        File  dir = new File(context.getFilesDir(), DIRECTORIO);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        File archivo = new File(dir, NOMBRE_ARCHIVO);
        try (FileOutputStream fileOutputStream = new FileOutputStream(archivo)) {
            fileOutputStream.write(estadoPartida.getBytes());
            Log.i(TAG, R.string.partidaGuardada + archivo.getAbsolutePath());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            Log.i(TAG, String.valueOf(R.string.erroGuardar), e);
        }
    }

    public String cargarPartido() {
        File dir = new File(context.getFilesDir(), DIRECTORIO);
        File arquivo = new File(dir, NOMBRE_ARCHIVO);

        if (!arquivo.exists()) {
            Log.e(TAG, R.string.archivoNoLocalizado + arquivo.getAbsolutePath());
            return null;
        }

        StringBuilder estadoPartida = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(arquivo)) {
            int byteLido;
            while ((byteLido = fis.read()) != -1) {
                estadoPartida.append((char) byteLido);
            }
            Log.d(TAG, String.valueOf(R.string.juegoCargadoExito));
        } catch (IOException e) {
            Log.e(TAG, String.valueOf(R.string.juegoCargadoError), e);
        }

        return estadoPartida.toString();
    }
}
