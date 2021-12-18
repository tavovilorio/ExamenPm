package com.example.pm01exameniii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm01exameniii.Transacciones.Transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private android.widget.Spinner spinner;
    ConexionSqlite DB;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAM = 100;
    ImageView ObjImagen;
    EditText txtdescripcion, txtcantidad, txtperiocidad;
    Spinner tiempospinner;
    Button btnGuardar;
    FloatingActionButton fabmodificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtdescripcion = (EditText) findViewById(R.id.txtdescripcion);
        txtcantidad = (EditText) findViewById(R.id.txtcantidad);
        txtperiocidad = (EditText) findViewById(R.id.txtperiocidad);
        spinner = (android.widget.Spinner) findViewById(R.id.spinnerModificar);
        fabmodificar = findViewById(R.id.fabmodificar);

        fabmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModificarMedicamentos.class);
                startActivity(intent);

            }
        });


        DB = new ConexionSqlite(this, Transacciones.NameDataBase, null, 1);
        ObjImagen = (ImageView) findViewById(R.id.ObjImagen);

        btnGuardar = findViewById(R.id.btnguardar);
        Button btnLista = findViewById(R.id.btnLista);

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListaMedicamentos.class);
                startActivity(intent);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRegistro();
            }
        });


        ObjImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });

        ArrayList<String> elementos = new ArrayList<>();
        elementos.add("");
        elementos.add("4 Horas");
        elementos.add("6 Horas");
        elementos.add("8 Horas");
        elementos.add("12 Horas");

        ArrayAdapter adp = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, elementos);
        spinner.setAdapter(adp);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imgBitmap = (Bitmap) extras.get("data");
            ObjImagen.setImageBitmap(imgBitmap);
        }
    }
    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAM);
        } else {
            tomarfoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarfoto();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Se necesitan permisos de acceso", Toast.LENGTH_LONG).show();
        }
    }

    private void tomarfoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    public void guardarRegistro(){

        Bitmap photo = ((BitmapDrawable)ObjImagen.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();

        ConexionSqlite conexion = new ConexionSqlite(this, Transacciones.NameDataBase,  null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.descripcion, txtdescripcion.getText().toString());
        valores.put(Transacciones.cantidad, Integer.parseInt(txtcantidad.getText().toString()));
        valores.put(Transacciones.tiempo, spinner.getSelectedItem().toString());
        valores.put(Transacciones.periocidad, Integer.parseInt(txtperiocidad.getText().toString()));
        valores.put(Transacciones.imagen, bArray);

        Long resultado = db.insert(Transacciones.tablamedicamentos, Transacciones.id_medicamento, valores);

        Toast.makeText(getApplicationContext(), "Registro Ingresado : " + resultado.toString(), Toast.LENGTH_LONG).show();

        db.close();
    }

    public void limpiarCampos() {
        txtdescripcion.setText("");
        txtcantidad.setText("");
        spinner.setSelection(-1);
        txtperiocidad.setText("");
    }
}