package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.Bantumi;
import es.upm.miw.bantumi.ui.viewmodel.BantumiViewModel;

public class MejoresResultadosActivity extends AppCompatActivity {

    private BantumiViewModel bantumiVM;
    private ListView listaResultados;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mejores_resultados);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        listaResultados = findViewById(R.id.listaMejoresResultados);
        button = findViewById(R.id.btnBorrarResultados);

        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);

        bantumiVM.getTop10Resultados().observe(this, new Observer<List<Bantumi>>() {
            @Override
            public void onChanged(List<Bantumi> bantumiList) {
                if (bantumiList != null) {
                    ArrayAdapter<Bantumi> adapter = new ArrayAdapter<>(
                            MejoresResultadosActivity.this,
                            android.R.layout.simple_list_item_1, bantumiList);
                    listaResultados.setAdapter(adapter);
                } else {
                    Log.d("MejoresResultados", "A lista de melhores resultados está vazia");
                    Toast.makeText(MejoresResultadosActivity.this, "Nenhum resultado encontrado.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(view -> {
            new AlertDialog.Builder(MejoresResultadosActivity.this)
                    .setTitle(R.string.confirmarBorrar)
                    .setMessage(R.string.confirmarBorrarMensaje)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        bantumiVM.borrarTodosResultados();
                        Toast.makeText(MejoresResultadosActivity.this,
                                R.string.resultadosBorrados,
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
