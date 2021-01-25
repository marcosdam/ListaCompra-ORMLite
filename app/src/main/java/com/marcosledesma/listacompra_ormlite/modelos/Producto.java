package com.marcosledesma.listacompra_ormlite.modelos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// Después de añadir la dependencia (como librería) ormlite-android, comenzar a mapear
@DatabaseTable(tableName = "Productos")
public class Producto {

    // Primary Key
    @DatabaseField(generatedId = true, columnName = "id_producto")  // Autoincremental
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Resto de campos
    @DatabaseField(canBeNull = false)   // Campo obligatorio
    private String nombre;
    @DatabaseField(canBeNull = false, defaultValue = "0")
    private float precio;
    @DatabaseField(columnName = "cantidad")   // Puede ser nulo
    private int cantidad;
    @DatabaseField()
    private float importeTotal;


    // Necesario const vacío para ORM Lite
    public Producto() {
    }

    public Producto(int id, String nombre, float precio, int cantidad, float importeTotal) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.importeTotal = importeTotal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(float importeTotal) {
        this.importeTotal = precio * cantidad;
    }
}
