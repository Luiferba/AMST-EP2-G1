package com.example.amst_2ep_grupo1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText busquedaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busquedaEditText = findViewById(R.id.Busqueda);
    }

    public void BuscarHeroes(View view) {
        String criterioBusqueda = busquedaEditText.getText().toString().trim();

        if (!criterioBusqueda.isEmpty()) {
            // Lanzar la actividad de resultados
            Intent intent = new Intent(this, ResultadosActivity.class);
            intent.putExtra("criterioBusqueda", criterioBusqueda);
            startActivity(intent);
        }
    }
}