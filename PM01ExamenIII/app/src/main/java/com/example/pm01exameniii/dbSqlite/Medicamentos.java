package com.example.pm01exameniii.dbSqlite;

import java.sql.Blob;
public class Medicamentos {
    private Integer id;
    private String descripcion;
    private Integer cantidad;
    private String tiempo;
    private Integer periocidad;
    private int imagen;

    public Medicamentos() {
    }

    public Medicamentos(Integer id, String descripcion, Integer cantidad, String tiempo, Integer periocidad, int imagen) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.tiempo = tiempo;
        this.periocidad = periocidad;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Integer getPeriocidad() {
        return periocidad;
    }

    public void setPeriocidad(Integer periocidad) {
        this.periocidad = periocidad;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
