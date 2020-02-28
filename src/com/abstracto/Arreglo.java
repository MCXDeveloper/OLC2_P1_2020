package com.abstracto;

import com.constantes.ETipoDato;

import java.util.LinkedList;

public class Arreglo {

    private int tamano;
    private LinkedList<Item> elementos;
    private LinkedList<Integer> listaTamanoDims;

    public Arreglo(int tamano, LinkedList<Integer> listaTamanoDims, LinkedList<Item> elementos) {
        this.tamano = tamano;
        this.elementos = elementos;
        this.listaTamanoDims = listaTamanoDims;
    }

    public int getTamano() {
        return tamano;
    }

    public LinkedList<Item> getElementos() {
        return elementos;
    }

    public LinkedList<Integer> getListaTamanoDims() {
        return listaTamanoDims;
    }

    public int getCantidadDimensiones() {
        return listaTamanoDims.size();
    }

    public void actualizarValorPorPosiciones(LinkedList<Integer> posiciones, ETipoDato tipo, Object valor) {
        int pos = colAccess(posiciones);
        elementos.set(pos, new Item(tipo, valor));
    }

    public boolean validarIndices(LinkedList<Integer> posiciones) {
        for (int i = 0; i < listaTamanoDims.size(); i++) {
            if (posiciones.get(i) > listaTamanoDims.get(i)) {
                return false;
            }
        }
        return true;
    }

    private int colAccess(LinkedList<Integer> posiciones) {
        int ret = 1;
        for (int i = posiciones.size(); i > 0; i--) {
            if (i == posiciones.size()) {
                ret = posiciones.get(i - 1) - 1;
            } else {
                ret = ((ret * listaTamanoDims.get(i - 1)) + posiciones.get(i - 1)) - 1;
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "";
    }
}