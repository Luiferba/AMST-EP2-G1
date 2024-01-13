package com.example.amst_2ep_grupo1;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ResultadosActivity extends AppCompatActivity {

    private ListView resultadosListView;
    private TextView cantidadResultadosTextView;  // Agregamos una referencia al TextView
    private int cantidadResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        resultadosListView = findViewById(R.id.resultadosListView);
        cantidadResultadosTextView = findViewById(R.id.cantidadResultadosTextView);  // Referencia al TextView

        // Obtener el criterio de búsqueda de la actividad principal
        String criterioBusqueda = getIntent().getStringExtra("criterioBusqueda");

        // Llamar a la tarea asincrónica para obtener resultados y mostrarlos en la lista
        new BuscarHeroesTask().execute(criterioBusqueda);

        // Agregar un listener de clics a los elementos de la lista
        resultadosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el nombre del héroe seleccionado
                String heroeSeleccionado = (String) parent.getItemAtPosition(position);

                // Lanzar la actividad de perfil del héroe
                Intent intent = new Intent(ResultadosActivity.this, PerfilHeroeActivity.class);
                intent.putExtra("heroeId", heroeSeleccionado);  // Envía el nombre del héroe como ID
                startActivity(intent);
            }
        });

    }

    private class BuscarHeroesTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String criterioBusqueda = params[0];
            ArrayList<String> resultados = new ArrayList<>();

            try {
                // Conectar con la API y obtener la respuesta en formato JSON
                URL url = new URL("https://www.superheroapi.com/api.php/7235463103179052/search/" + criterioBusqueda);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);

                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.next());
                }

                // Parsear la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray heroesArray = jsonResponse.getJSONArray("results");

                cantidadResultados = heroesArray.length();

                for (int i = 0; i < heroesArray.length(); i++) {
                    JSONObject heroObject = heroesArray.getJSONObject(i);
                    String heroName = heroObject.getString("name");
                    resultados.add(heroName);


                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return resultados;
        }

        @Override
        protected void onPostExecute(ArrayList<String> resultados) {
            // Mostrar resultados en la lista usando un ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ResultadosActivity.this, android.R.layout.simple_list_item_1, resultados);
            resultadosListView.setAdapter(adapter);
            // Mostrar la cantidad de resultados en el título de la actividad
            cantidadResultadosTextView.setText("Resultados: " + cantidadResultados);


        }



    }
}