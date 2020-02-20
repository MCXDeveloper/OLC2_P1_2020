package com.abstracto;

import java.util.LinkedList;

public class Lista {

    private LinkedList<Item> elementos;

    public Lista(LinkedList<Item> le) {
        this.elementos = le;
    }

    public void addItem(Item i) {
        elementos.add(i);
    }

    // TODO - Pendiente desarrollar estas funciones para cuando ya estén las listas.
    public Lista getElementoTipo1() {
        return null;
    }

    public Object getElementoTipo2() {
        return null;
    }

    // TODO - Pendiente desarrollar la función toString para poder imprimir los valores de la lista.
    @Override
    public String toString() {
        return "";
    }
}