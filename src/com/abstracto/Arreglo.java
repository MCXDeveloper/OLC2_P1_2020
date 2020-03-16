package com.abstracto;

import com.constantes.ETipoDato;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

public class Arreglo implements Estructura {

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

    public int getArregloSize() {
        return elementos.size();
    }

    public Item getElementByPosition(int pos) {
        return elementos.get(pos);
    }

    public Item getElementByMultiplePositions(LinkedList<Integer> posiciones) {
        return elementos.get(colAccess(posiciones));
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

    private static String getTabs(int cnt) {
        return "\t".repeat(cnt);
    }

    private int[] tail(int[] arr) {
        return Arrays.copyOfRange(arr, 1, arr.length);
    }

    private void fillWithSomeValues(Object array, String v, LinkedList<Integer> posiciones, int... sizes) {
        for (int i = 0; i < sizes[0]; i++) {
            if (sizes.length == 1) {
                posiciones.add(i+1);
                ((String[]) array)[i] = v + (i+1) + " = <"+ elementos.get(colAccess(posiciones)).getStringItem() +">";
                posiciones.removeLast();
            } else {
                posiciones.add(i+1);
                fillWithSomeValues(Array.get(array, i), v + (i+1) + "-", posiciones, tail(sizes));
                posiciones.removeLast();
            }
        }
    }

    public LinkedList<String> imprimirArreglo() {

        /* Creo un arreglo de int's con la cantidad y valores de los tamaños de las dimensiones. */
        int[] sizes = new int[getCantidadDimensiones()];
        for (int i = 0; i < sizes.length; i++) {
            sizes[i] = listaTamanoDims.get(i);
        }

        /* Obtengo la representación del arreglo en tipo String. */
        Object multiDimArray = Array.newInstance(String.class, sizes);
        fillWithSomeValues(multiDimArray, "pos ", new LinkedList<>(), sizes);
        String array = Arrays.deepToString((Object[]) multiDimArray);

        int cnt = 0;
        boolean flag = false;
        StringBuilder auxiliar = new StringBuilder();
        char[] cadena = array.toCharArray();

        LinkedList<String> l = new LinkedList<>();
        l.add("Imprimiendo arreglo con las siguientes dimensiones: " + Arrays.toString(sizes).replaceAll(", ", "]["));

        /* Se hace un análisis léxico para poder establecer las identaciones del arreglo. */
        for (int i = 0; i < cadena.length; i++) {
            switch(cadena[i]) {
                case '[': {
                    l.add(getTabs(cnt) + "[");
                    cnt++;
                }   break;
                case ']': {
                    if (flag) {
                        l.add(getTabs(cnt) + auxiliar);
                        auxiliar = new StringBuilder();
                        flag = false;
                        i = i - 1;
                    } else {
                        cnt--;
                        l.add(getTabs(cnt) + "]");
                    }
                }   break;
                case ',': {
                    if (flag) {
                        auxiliar.append(", ");
                    } else {
                        l.add(getTabs(cnt) + ",");
                    }
                }   break;
                default: {
                    auxiliar.append(cadena[i]);
                    flag = true;
                }   break;
            }
        }

        return l;
    }

    @Override
    public Object getClone() {
        LinkedList<Item> copia = new LinkedList<>();
        for (Item it : elementos) {
            switch (it.getTipo()) {
                case VECTOR: {
                    Vector v = (Vector) ((Vector)it.getValor()).getClone();
                    copia.add(new Item(ETipoDato.VECTOR, v));
                }   break;
                case LIST: {
                    Lista l = (Lista) ((Lista)it.getValor()).getClone();
                    copia.add(new Item(ETipoDato.LIST, l));
                }   break;
                default: {
                    copia.add(new Item(it.getTipo(), it.getValor()));
                }   break;
            }
        }
        return new Arreglo(tamano, listaTamanoDims, copia);
    }
}