package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Date;
import java.util.Locale;

import es.upm.miw.bantumi.datos.Bantumi;
import es.upm.miw.bantumi.datos.BantumiRoomDatabase;
import es.upm.miw.bantumi.datos.GuardarPartida;
import es.upm.miw.bantumi.ui.fragmentos.FinalAlertDialog;
import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.viewmodel.BantumiViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    public JuegoBantumi juegoBantumi;
    private BantumiViewModel bantumiVM;
    private GuardarPartida guardarPartida;
    int numInicialSemillas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        numInicialSemillas = getResources().getInteger(R.integer.intNumInicialSemillas);
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, JuegoBantumi.Turno.turnoJ1, numInicialSemillas);
        guardarPartida = new GuardarPartida(this);
        crearObservadores();
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);
        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setTextColor(getColor(R.color.white));
                tvJugador1.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                tvJugador2.setTextColor(getColor(R.color.black));
                tvJugador2.setBackgroundColor(getColor(R.color.white));
                break;
            case turnoJ2:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador1.setBackgroundColor(getColor(R.color.white));
                tvJugador2.setTextColor(getColor(R.color.white));
                tvJugador2.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                break;
            default:
                tvJugador1.setTextColor(getColor(R.color.black));
                tvJugador2.setTextColor(getColor(R.color.black));
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.opcAjustes: // @todo Preferencias
//                startActivity(new Intent(this, BantumiPrefs.class));
//                return true;
            case R.id.opcAcercaDe:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.aboutTitle)
                        .setMessage(R.string.aboutMessage)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;
            case R.id.opcReiniciarPartida:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.reiniciarText)
                        .setMessage(R.string.reiniciarMessage)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            juegoBantumi.inicializar(JuegoBantumi.Turno.turnoJ1);
                            Snackbar.make(
                                    findViewById(android.R.id.content),
                                    getString(R.string.reiniciadoMessage),
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
            case R.id.opcGuardarPartida:
                salvarPartida();
                return true;
            case R.id.opcRecuperarPartida:
                mostrarAlertDialogRecuperarPartida();
                return true;
            case R.id.opcMejoresResultados:
                startActivity(new Intent(this, MejoresResultadosActivity.class));
                return true;
            case R.id.btnBorrarResultados:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.confirmarBorrar)
                        .setMessage(R.string.confirmarBorrarMensaje)
                        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            bantumiVM.borrarTodosResultados();
                            Snackbar.make(findViewById(android.R.id.content),
                                    getString(R.string.resultadosBorrados),
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                return true;
                // @TODO!!! resto opciones


            default:
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtSinImplementar),
                        Snackbar.LENGTH_LONG
                ).show();
        }
        return true;
    }

    private void salvarPartida() {
        String estadoPartida = generarEstadoPartida();
        guardarPartida.guardarPartida(estadoPartida);
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(R.string.partidaGuardada),
                Snackbar.LENGTH_SHORT
        ).show();
    }

    private void mostrarAlertDialogRecuperarPartida() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmarRecuperar)
                .setMessage(R.string.confirmarRecuperarMensage)
                .setPositiveButton(android.R.string.ok, (dialog, wich) -> {
                    recuperarPartida();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void recuperarPartida() {
            String estadoCargado = guardarPartida.cargarPartido();
            if (estadoCargado != null) {
                actualizarEstadoPartida(estadoCargado);
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.paartidaRecuperada),
                        Snackbar.LENGTH_SHORT
                ).show();
            } else {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.erroCargarPartida),
                        Snackbar.LENGTH_LONG
                ).show();
            }
    }

    private String generarEstadoPartida() {
        StringBuilder estado = new StringBuilder();
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            estado.append(juegoBantumi.getSemillas(i)).append(",");
        }
        estado.append(juegoBantumi.turnoActual());
        return estado.toString();
    }

    private void actualizarEstadoPartida(String estado) {
        String[] partes = estado.split(",");
        for(int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int numSemillas = Integer.parseInt(partes[i]);
            juegoBantumi.setSemillas(i, numSemillas);
        }
        JuegoBantumi.Turno turnoActual = JuegoBantumi.Turno.valueOf(partes[JuegoBantumi.NUM_POSICIONES]);
        juegoBantumi.setTurno(turnoActual);
        for(int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            mostrarValor(i, juegoBantumi.getSemillas(i));
        }
        marcarTurno(turnoActual);
    }

    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                Log.i(LOG_TAG, "* Juega Jugador");
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                Log.i(LOG_TAG, "* Juega Computador");
                juegoBantumi.juegaComputador();
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        String texto;
        int semillasJugador1 = juegoBantumi.getSemillas(6);
        int semillasJugador2 = juegoBantumi.getSemillas(13);

        if(semillasJugador1 > 6 * numInicialSemillas) {
            texto = "Gana Jugador 1";
        }
        else if(semillasJugador2 > 6 * numInicialSemillas) {
            texto = "Gana Jugador 2";
        }
        else {
            texto = "¡¡¡ EMPATE !!!";
        }
        guardarPuntuacion(semillasJugador1, semillasJugador2);
        new FinalAlertDialog(texto).show(getSupportFragmentManager(), "ALERT_DIALOG");
    }

    private void guardarPuntuacion(int semillasJugador1, int semillasJugador2) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nombreJugador = sharedPreferences.getString("nombreJugador", "Jugador 1");

        Bantumi partida = new Bantumi(nombreJugador, new Date(), semillasJugador1, semillasJugador2);

        BantumiRoomDatabase.databaseWriteExecutor.execute(() -> {
            BantumiRoomDatabase db = BantumiRoomDatabase.getDatabase(this);
            db.partidaDao().insert(partida);

            runOnUiThread(() -> {
                Snackbar.make(findViewById(android.R.id.content),
                        getString(R.string.partidaGuardada),
                        Snackbar.LENGTH_SHORT).show();
            });
        });
    }
}