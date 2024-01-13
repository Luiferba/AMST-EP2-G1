package com.example.amst_2ep_grupo1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Scanner;

public class PerfilHeroeActivity extends AppCompatActivity {

    private TextView nombreHeroeTextView, nombreCompletoTextView;
    private ImageView imagenHeroeImageView;

    private BarChart graficoBarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_heroe);
        graficoBarras = findViewById(R.id.graficoBarras);
        // Obtener el intent que inició esta actividad
        Intent intent = getIntent();
        configureBarChart();

        // Obtener el nombre del héroe del intent
        String heroeId = intent.getStringExtra("heroeId");
        String heroeJsonString = intent.getStringExtra("heroeObject");

        // Buscar el TextView en el diseño por su identificador
        TextView nombreHeroeTextView = findViewById(R.id.nombreHeroeTextView);
        TextView nombreCompletoTextView = findViewById(R.id.nombreCompletoTextView);

        // Configurar el texto del TextView con el nombre del héroe
        nombreHeroeTextView.setText("Nombre de héroe: " + heroeId);



        //String nombreCompletoHeroe = obtenerNombreCompletoHeroeDesdeJSON(heroeObject);
        //nombreCompletoTextView.setText("Nombre completo: " + nombreCompletoHeroe);

        //nombreHeroeTextView = findViewById(R.id.nombreHeroeTextView);
        //nombreCompletoTextView = findViewById(R.id.nombreCompletoTextView);
        //imagenHeroeImageView = findViewById(R.id.imagenHeroeImageView);

        // Obtener el ID del héroe de la actividad anterior
        //String heroeId = getIntent().getStringExtra("heroeId");

        // Llamar a la tarea asincrónica para obtener detalles del héroe y mostrarlos
        new ObtenerDetallesHeroeTask().execute(heroeId);
    }
    private void configureBarChart() {
        // Configurar las estadísticas de poder (puedes obtener estos valores de tu JSON)
        float[] poderes = {75f, 90f, 80f, 70f, 85f, 95f}; // Reemplaza estos valores con los datos reales

        // Configurar las etiquetas para las barras (inteligencia, fuerza, velocidad, etc.)
        String[] etiquetas = {"Inteligencia", "Fuerza", "Velocidad", "Durabilidad", "Poder", "Combate"};

        // Crear una lista de BarEntry para representar los datos en el gráfico
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < poderes.length; i++) {
            entries.add(new BarEntry(i, poderes[i]));
        }

        // Configurar el conjunto de datos y el formato de las barras
        BarDataSet dataSet = new BarDataSet(entries, "Poderes");

        // Configurar el eje X (etiquetas)
        XAxis xAxis = graficoBarras.getXAxis();
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(etiquetas));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Configurar el eje Y (rango de valores)
        YAxis yAxisRight = graficoBarras.getAxisRight();
        yAxisRight.setEnabled(false); // Deshabilitar el eje derecho
        YAxis yAxisLeft = graficoBarras.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);

        // Configurar el gráfico de barras con el conjunto de datos
        BarData data = new BarData(dataSet);
        graficoBarras.setData(data);
        graficoBarras.setFitBars(true);
        graficoBarras.invalidate(); // Actualizar el gráfico
    }
    // Método ficticio para obtener el nombre completo del héroe desde el JSON
    private String obtenerNombreCompletoHeroeDesdeJSON(JSONObject heroeObject) {
        try {
            // Obtener el objeto "biography" del JSON
            JSONObject biographyObject = heroeObject.getJSONObject("biography");

            // Obtener el nombre completo del héroe desde el objeto "biography"
            return biographyObject.getString("full-name");
        } catch (JSONException e) {
            e.printStackTrace();
            return "Nombre Completo No Disponible";  // Manejo de errores, puedes personalizar el mensaje de error.
        }
    }

    private class ObtenerDetallesHeroeTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            String heroeId = params[0];
            JSONObject heroeObject = null;

            try {
                // Conectar con la API y obtener la respuesta en formato JSON
                URL url = new URL("https://www.superheroapi.com/api.php/7235463103179052/" + heroeId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);

                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.next());
                }

                // Parsear la respuesta JSON
                heroeObject = new JSONObject(response.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return heroeObject;

        }

        @Override
        protected void onPostExecute(JSONObject heroeObject) {
            // Mostrar detalles del héroe en la vista
            try {
                Log.d("JSON_RESPONSE", heroeObject.toString());  // Agrega esta línea para imprimir el JSON
                String nombreHeroe = heroeObject.getString("name");
                String nombreCompleto = heroeObject.getString("biography");
                String imageUrl = heroeObject.getString("image");

                nombreHeroeTextView.setText("Nombre de héroe: " + nombreHeroe);
                nombreCompletoTextView.setText("Nombre completo: " + nombreCompleto);

                // Cargar la imagen del héroe utilizando Picasso (asegúrate de agregar la dependencia en el archivo build.gradle)
                Picasso.get().load(imageUrl).into(imagenHeroeImageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}