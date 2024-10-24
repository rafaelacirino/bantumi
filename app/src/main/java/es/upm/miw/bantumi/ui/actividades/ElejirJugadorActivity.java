package es.upm.miw.bantumi.ui.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import es.upm.miw.bantumi.R;

public class ElejirJugadorActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elejir_jugador);

        radioGroup = findViewById(R.id.radioGroupChoosePlayer);

        Button btnEmpezar = findViewById(R.id.btnEmpezarPartido);
        btnEmpezar.setOnClickListener(v -> {
            int selecionarId = radioGroup.getCheckedRadioButtonId();
            String primeroJugador = String.valueOf(R.string.txtPlayer1);

            if(selecionarId == R.id.radioPlayer2){
                primeroJugador = String.valueOf(R.string.txtPlayer2);
            }

            Intent intent = new Intent(ElejirJugadorActivity.this, MainActivity.class);
            intent.putExtra("primero_jugador", primeroJugador);
            startActivity(intent);
            finish();
        });
    }
}
