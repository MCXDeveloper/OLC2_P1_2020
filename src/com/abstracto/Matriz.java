package com.abstracto;

import com.bethecoder.ascii_table.ASCIITable;
import com.constantes.ETipoDato;
import com.estaticas.ErrorHandler;
import com.estaticas.TError;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public boolean rehashing() {

        /* Se procede a realizar el casteo en base a prioridades. */
        Optional<Item> priori1 = elementos.stream().filter(i -> (i.getTipo() == ETipoDato.STRING || i.getTipo() == ETipoDato.NT)).findAny();
        if (priori1.isPresent()) {
            convertToString();
        } else {
            Optional<Item> priori2 = elementos.stream().filter(i -> i.getTipo() == ETipoDato.DECIMAL).findAny();
            if (priori2.isPresent()) {
                convertToDecimal();
            } else {
                Optional<Item> priori3 = elementos.stream().filter(i -> i.getTipo() == ETipoDato.INT).findAny();
                if (priori3.isPresent()) {
                    convertToInteger();
                } else {
                    return elementos.stream().allMatch(i -> i.getTipo() == ETipoDato.BOOLEAN);
                }
            }
        }

        return true;

    }

    private void convertToString() {
        Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() != ETipoDato.STRING) {
                elementos.set(i, new Item(ETipoDato.STRING, ((pivot.getTipo() != ETipoDato.NT) ? pivot.getValor().toString() : pivot.getValor())));
            }
        }
    }

    private void convertToDecimal() {
        Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() != ETipoDato.DECIMAL) {
                if (pivot.getTipo() == ETipoDato.BOOLEAN) {
                    elementos.set(i, new Item(ETipoDato.DECIMAL, ((boolean)pivot.getValor() ? Double.valueOf("1") : Double.valueOf("0"))));
                } else {
                    elementos.set(i, new Item(ETipoDato.DECIMAL, Double.valueOf(pivot.getValor().toString())));
                }
            }
        }
    }

    private void convertToInteger() {
        Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() != ETipoDato.INT) {
                elementos.set(i, new Item(ETipoDato.INT, ((boolean)pivot.getValor() ? 1 : 0)));
            }
        }
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
                arr[cnt] = elementos.get(colAccess(i, j) - 1).getStringItem();
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
