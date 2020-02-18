package com.estaticas;

public class TError {

    private final int Fila;
    private final int Columna;
    private final String Tipo;
    private final String Archivo;
    private final String Ubicacion;
    private final String Descripcion;

    public TError(String tipo, String archivo, String ubicacion, String descripcion, int fila, int columna) {
        Fila = fila;
        Tipo = tipo;
        Columna = columna;
        Archivo = archivo;
        Ubicacion = ubicacion;
        Descripcion = descripcion;
    }

    public String getFila() {
        return String.valueOf(Fila);
    }

    public String getColumna() {
        return String.valueOf(Columna);
    }

    public String getTipo() {
        return Tipo;
    }

    public String getArchivo() {
        return Archivo;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

}