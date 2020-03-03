package com.abstracto;

import com.bethecoder.ascii_table.ASCIITable;
import com.constantes.ETipoDato;

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

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getSize() {
        return filas*columnas;
    }

    public int getMatrixSize() {
        return elementos.size();
    }

    public ETipoDato getInnerType() {
        return elementos.get(0).getTipo();
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

    public boolean validateDimensions(int posx, int posy) {
        return !(posx > filas || posy > columnas);
    }

    public boolean validateRows(int pos) {
        return !(pos > filas);
    }

    public boolean validateColumns(int pos) {
        return !(pos > columnas);
    }

    public void updateValueByPosition(int pos, ETipoDato type, Object value) {
        elementos.set((pos - 1), new Item(type, value));
        rehashing();
    }

    public void updateRowValue(int rowNum, ETipoDato type, Object value) {

        int pos = 0;

        if (type == ETipoDato.VECTOR) {
            Item it;
            Vector v = (Vector)value;
            for (int i = 0; i < columnas; i++) {
                it = v.getElementByPosition(i);
                pos = colAccess(rowNum, (i + 1)) - 1;
                elementos.set(pos, new Item(it.getTipo(), it.getValor()));
            }
        } else {
            for (int i = 0; i < columnas; i++) {
                pos = colAccess(rowNum, (i + 1)) - 1;
                elementos.set(pos, new Item(type, value));
            }
        }

        rehashing();

    }

    public void updateColumnValue(int colNum, ETipoDato type, Object value) {

        int pos = 0;

        if (type == ETipoDato.VECTOR) {
            Item it;
            Vector v = (Vector)value;
            for (int i = 0; i < filas; i++) {
                it = v.getElementByPosition(i);
                pos = colAccess((i + 1), colNum) - 1;
                elementos.set(pos, new Item(it.getTipo(), it.getValor()));
            }
        } else {
            for (int i = 0; i < filas; i++) {
                pos = colAccess((i + 1), colNum) - 1;
                elementos.set(pos, new Item(type, value));
            }
        }

        rehashing();

    }

    public Item getElementByPosition(int pos) {
        return elementos.get(pos);
    }

    public Item getElementByCoordinates(int row, int column) {
        return elementos.get(colAccess(row, column) - 1);
    }

    public void updateValueByRowAndCol(int posx, int posy, ETipoDato type, Object value) {
        int pos = colAccess(posx, posy);
        elementos.set((pos - 1), new Item(type, value));
        rehashing();
    }

    private int colAccess(int fila,  int columna) {
        return (columna - 1) * filas + fila;
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
            arr[i] = "Columna " + (i + 1);
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

    public Matriz getClone() {
        LinkedList<Item> copia = new LinkedList<>();
        for (Item it : elementos) {
            copia.add(new Item(it.getTipo(), it.getValor()));
        }
        return new Matriz(filas, columnas, copia);
    }

    @Override
    public String toString() {
        String[][] data = getStringArrayElements().toArray(new String[0][]);
        return ASCIITable.getInstance().getTable(getColsHeader(), data);
    }
}
