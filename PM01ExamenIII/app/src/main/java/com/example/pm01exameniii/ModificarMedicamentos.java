package com.example.pm01exameniii;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm01exameniii.Transacciones.Transacciones;

import java.util.ArrayList;

public class ModificarMedicamentos extends AppCompatActivity {

    Integer idRecibido;
    EditText actualizarDescripcion, actualizarCantidad, actualizarPeriocidad;
    Spinner spinnerModificar;
    String tiempo;
    Button btnConfirmar, btnCancelar;
    ConexionSqlite conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_medicamentos);

        conn = new ConexionSqlite(getApplicationContext(), Transacciones.NameDataBase, null, 1);

        actualizarDescripcion = findViewById(R.id.modificardescripcion);
        actualizarCantidad = findViewById(R.id.modificarcantidad);
        actualizarPeriocidad = findViewById(R.id.modificarperiocidad);
        spinnerModificar = (Spinner) findViewById(R.id.spinnerModificar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnConfirmar = findViewById(R.id.btnconfirmar);

        ArrayList<String> elementos = new ArrayList<>();
        elementos.add("");
        elementos.add("4 Horas");
        elementos.add("6 Horas");
        elementos.add("8 Horas");
        elementos.add("12 Horas");

        ArrayAdapter adp = new ArrayAdapter(ModificarMedicamentos.this, android.R.layout.simple_spinner_dropdown_item, elementos);
        spinnerModificar.setAdapter(adp);
        spinnerModificar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle valoresRecuperados = this.getIntent().getExtras();

        idRecibido = valoresRecuperados.getInt("id");
        actualizarDescripcion.setText(valoresRecuperados.getString("descripcion"));
        actualizarCantidad.setText(String.valueOf(valoresRecuperados.getInt("cantidad")));
        actualizarPeriocidad.setText(valoresRecuperados.getString("periocidad"));

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarMedicamento();
                finish();
                Intent intentListView = new Intent(getApplicationContext(), ListaMedicamentos.class);
                startActivity(intentListView);
            }
        });
    }

    private void actualizarMedicamento() {
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] params = {String.valueOf(idRecibido)};

        ContentValues values = new ContentValues();
        tiempo = spinnerModificar.getSelectedItem().toString();
        values.put(Transacciones.descripcion, actualizarDescripcion.getText().toString());
        values.put(Transacciones.cantidad, actualizarCantidad.getText().toString());
        values.put(Transacciones.tiempo, tiempo);
        values.put(Transacciones.periocidad, actualizarPeriocidad.getText().toString());

        db.update(Transacciones.tablamedicamentos, values, Transacciones.id_medicamento + "=?", params);
        Toast.makeText(getApplicationContext(), "Medicamento actualizado", Toast.LENGTH_LONG).show();

    }
}