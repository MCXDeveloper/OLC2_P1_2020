package com.estaticas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ErrorHandler {

    public static List<TError> ListaErrores = new LinkedList<>();

    public static void AddError(String tipo, String archivo, String ubicacion, String descripcion, int fila, int columna) {
        ListaErrores.add(new TError(tipo, archivo, ubicacion, descripcion, fila, columna));
    }

    public static List<String[]> GetStringArrayErrors() {
        List<String[]> list = new ArrayList<>();
        for (TError te : ListaErrores) {
            list.add(new String[] { te.getArchivo(), te.getTipo(), te.getUbicacion(), te.getDescripcion(), te.getFila(), te.getColumna() });
        }
        return list;
    }

}