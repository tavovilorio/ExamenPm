package com.example.pm01exameniii;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pm01exameniii.Transacciones.Transacciones;
import com.example.pm01exameniii.dbSqlite.Medicamentos;

import java.util.ArrayList;

public class ListaMedicamentos extends AppCompatActivity {

    ListView listViewMedicamentos;
    ArrayList<String> listInformation;
    ArrayList<Medicamentos> listaMedicamentos;
    Integer listrowposition;
    Button btnModificar, btnEliminar;
    ConexionSqlite conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicamentos);

        listViewMedicamentos = findViewById(R.id.listViewMedicamentos);
        btnModificar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        conn = new ConexionSqlite(getApplicationContext(), Transacciones.NameDataBase, null, 1);

        consultarListaMedicamentos();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listInformation);
        listViewMedicamentos.setAdapter(adapter);

        listViewMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listrowposition = listaMedicamentos.get(position).getId();
                Toast.makeText(getApplicationContext(), listrowposition.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listrowposition > 0){
                    finish();
                    enviarValoresPantallaModificar();
                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un contacto para poder actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listrowposition > 0){
                    AlertDialog.Builder alertDelete = new AlertDialog.Builder(ListaMedicamentos.this);
                    alertDelete.setMessage("Esta seguro que desea eliminar")
                            .setCancelable(false)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    borrarMedicamento();
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), ListaMedicamentos.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog tittle = alertDelete.create();
                    tittle.setTitle("ADVERTENCIA");
                    tittle.show();

                } else{
                    Toast.makeText(getApplicationContext(), "Seleccione un contacto para poder eliminar", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void borrarMedicamento(){
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parameterId = {String.valueOf(listrowposition)};

        db.delete(Transacciones.tablamedicamentos, Transacciones.id_medicamento + "=?", parameterId);
        Toast.makeText(getApplicationContext(), "Registro Eliminado", Toast.LENGTH_LONG).show();
    }

    private void enviarValoresPantallaModificar(){
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametroId = {String.valueOf(listrowposition)};
        String[] campos = {Transacciones.id_medicamento,
                Transacciones.descripcion,
                Transacciones.cantidad,
                Transacciones.tiempo,
                Transacciones.periocidad};

        String whereCondition = Transacciones.id_medicamento + "=?";

        try {
            Cursor consultaMedicamentos = db.query(Transacciones.tablamedicamentos, campos, whereCondition, parametroId,
                    null, null, null);

            consultaMedicamentos.moveToFirst();

            Intent pantallaActualizar = new Intent(this, ModificarMedicamentos.class);
            Bundle enviarValoresparaActualizar = new Bundle();
            enviarValoresparaActualizar.putInt("id", consultaMedicamentos.getInt(0));
            enviarValoresparaActualizar.putString("descripcion", consultaMedicamentos.getString(1));
            enviarValoresparaActualizar.putInt("cantidad", consultaMedicamentos.getInt(2));
            enviarValoresparaActualizar.putString("tiempo", consultaMedicamentos.getString(3));
            enviarValoresparaActualizar.putString("periocidad", consultaMedicamentos.getString(4));

            pantallaActualizar.putExtras(enviarValoresparaActualizar);
            startActivity(pantallaActualizar);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Seleccione antes un medicamento", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarListaMedicamentos() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Medicamentos medicamentos = null;
        listaMedicamentos = new ArrayList<Medicamentos>();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablamedicamentos, null);
        while(cursor.moveToNext()) {
            medicamentos = new Medicamentos();

            medicamentos.setId(cursor.getInt(0));
            medicamentos.setDescripcion(cursor.getString(1));
            medicamentos.setCantidad(cursor.getInt(2));
            medicamentos.setTiempo(cursor.getString(3));
            medicamentos.setPeriocidad(cursor.getInt(4));
            listaMedicamentos.add(medicamentos);
        }
        cursor.close();
        obtenerLista();
    }

    private void obtenerLista() {
        listInformation = new ArrayList<>();
        for (int i=0; i<listaMedicamentos.size(); i++) {
            listInformation.add(listaMedicamentos.get(i).getId()+" Descripcion: "+
                    listaMedicamentos.get(i).getDescripcion()+" / No. Pastillas: "+
                    listaMedicamentos.get(i).getCantidad()+" / Dosis: "+
                    listaMedicamentos.get(i).getTiempo()+" / Durante: "+
                    listaMedicamentos.get(i).getPeriocidad()+" Dias");
        }
    }
}