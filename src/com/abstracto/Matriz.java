package com.abstracto;

import com.bethecoder.ascii_table.ASCIITable;
import com.estaticas.ErrorHandler;
import com.estaticas.TError;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Matriz {

    int filas;
    int columnas;
    private LinkedList<Item> elementos;

    public Matriz(int filas, int columnas, LinkedList<Item> elementos) {
        this.filas = filas;
        this.columnas = columnas;
        this.elementos = elementos;
    }

    public LinkedList<Item> getElementos() {
        return elementos;
    }

    public int colAccess(int fila,  int columna) {
        return (columna - 1) * filas + fila;
    }

    private String[] getColsHeader() {
        String[] arr = new String[columnas];
        for (int i = 0; i < columnas; i++) {
            arr[i] = "Columna " + i;
        }
        return arr;
    }

    private List<String[]> getStringArrayElements() {

        int cnt = 0;
        String[] arr = new String[columnas];
        List<String[]> list = new ArrayList<>();

        for (int i = 1; i <= filas; i++) {
            for (int j = 1; j <= columnas; j++) {
                arr[cnt] = elementos.get(colAccess(i, j) - 1).getValor().toString();
                cnt++;
            }
            cnt = 0;
            list.add(arr);
            arr = new String[columnas];
        }

        return list;

    }

    @Override
    public String toString() {
        String[][] data = getStringArrayElements().toArray(new String[0][]);
        return ASCIITable.getInstance().getTable(getColsHeader(), data);
    }
}
