package com.abstracto;

import com.constantes.ETipoNodo;

public class Nodo {

    private int linea;
    private int columna;
    private String archivo;
    private ETipoNodo tipoNodo;

    public Nodo(int linea, int columna, String archivo, ETipoNodo tipoNodo) {
        this.linea = linea;
        this.columna = columna;
        this.archivo = archivo;
        this.tipoNodo = tipoNodo;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getArchivo() {
        return archivo;
    }

    public ETipoNodo getTipoNodo() {
        return tipoNodo;
    }

    public String getTipoError() {
        return "Semántico";
    }
}