package com.example.pm01exameniii.Transacciones;

public class Transacciones {
  /* tablas */
    public  static  final String tablamedicamentos = "medicamentos";
    /* campos */
    public static final String id_medicamento = "id";
    public static final String descripcion = "descripcion";
    public static final String cantidad = "cantidad";
    public static final String tiempo= "tiempo";
    public static final String periocidad = "periocidad";
    public static final String imagen = "imagen";
    /* tablas -CREATE, DROP */
    public static final String CreateTableMedicamentos = "CREATE TABLE medicamentos ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "descripcion TEXT, cantidad INTEGER, tiempo TEXT ," +
            "periocidad INTEGER, imagen BLOB);";

    public static final String DropTableMedicamentos = "DROP TABLE IF EXISTS medicamentos";

    /* Creacion del nombre de la base de datos */
    public static final String NameDataBase = "DBmedicamentos";


}
