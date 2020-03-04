package com.abstracto;

import com.arbol.NNulo;
import com.constantes.ETipoDato;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lista {

    private LinkedList<Item> elementos;

    public Lista(ETipoDato tipo, Object valor) {
        this.elementos = new LinkedList<>();
        this.elementos.add(new Item(tipo, valor));
    }

    public Lista(LinkedList<Item> le) {
        this.elementos = le;
    }

    public boolean rehashing() {

        /* Primero valido que si vienen listas internas, los unifico. */
        Optional<Item> hasVector = elementos.stream().filter(i -> i.getTipo() == ETipoDato.LIST).findAny();
        if (hasVector.isPresent()) {
            mergeLists();
        }
        
        return true;

    }

    private void mergeLists() {
        Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() == ETipoDato.LIST) {
                elementos.remove(i);
                elementos.addAll(i, ((Lista)pivot.getValor()).getElementos());
            }
        }
    }

    public boolean updateListValue(int pos, ETipoDato type, Object value) {

        /* Se le resta 1 a la posición ya que las posiciones son manejadas de 0 en adelante. */
        int finalPos = pos - 1;

        if (finalPos <= getListSize()) {
            elementos.set(finalPos, new Item(type, value));
        } else {

            /*
             * Si la posición proporcionada es mayor que el tamaño actual del vector
             * entonces se procede a llenar los espacios entre el espacio actual y el
             * deseado con los valores por defecto del tipo del que sea el vector.
             */

            /* Obtengo el tipo interno del vector. */
            for (int i = getListSize(); i <= finalPos; i++) {
                if (i == finalPos) {
                    elementos.add(new Item(type, value));
                } else {
                    elementos.add(new Item(ETipoDato.STRING, new NNulo(0,0,"[NO_FILE]")));
                }
            }

        }

        return true;

    }

    public Item getElementoTipo1ParaAsignacion(int pos) {

        int finalPos = pos - 1;

        /*
         * Si la posicion proporcionada para obtener un acceso de tipo 1 no existe, se
         * deben de llenar espacios con NULL y luego devolver un valor existente.
         */
        if (finalPos > getListSize()) {
            for (int i = getListSize(); i <= finalPos; i++) {
                elementos.add(new Item(ETipoDato.VECTOR, new Vector(ETipoDato.STRING, new NNulo(0,0,"[NO_FILE]"))));
            }
        }

        /* Verifico si el elemento que se desea es diferente de LIST o de VECTOR para poder convertirlo a VECTOR ya que sería un primitivo. */
        Item it = elementos.get(finalPos);
        if (it.getTipo() != ETipoDato.LIST && it.getTipo() != ETipoDato.VECTOR) {
            it.setValor(new Vector(it.getTipo(), it.getValor()));
            it.setTipo(ETipoDato.VECTOR);
        }

        elementos.set(finalPos, new Item(ETipoDato.LIST, new Lista(it.getTipo(), it.getValor())));

        return elementos.get(finalPos);

    }

    public Item getElementoTipo2ParaAsignacion(int pos) {

        int finalPos = pos - 1;

        /*
         * Si la posicion proporcionada para obtener un acceso de tipo 2 no existe, se
         * deben de llenar espacios con NULL y luego devolver un valor existente.
         */
        if (finalPos > getListSize()) {
            for (int i = getListSize(); i <= finalPos; i++) {
                elementos.add(new Item(ETipoDato.VECTOR, new Vector(ETipoDato.STRING, new NNulo(0,0,"[NO_FILE]"))));
            }
        }

        /* Verifico si el elemento que se desea es diferente de LIST o de VECTOR para poder convertirlo a VECTOR ya que sería un primitivo. */
        Item it = elementos.get(finalPos);
        if (it.getTipo() != ETipoDato.LIST && it.getTipo() != ETipoDato.VECTOR) {
            it.setValor(new Vector(it.getTipo(), it.getValor()));
            it.setTipo(ETipoDato.VECTOR);
        }

        return elementos.get(finalPos);

    }

    public Item getElementByPosition(int pos) {
        return elementos.get(pos);
    }

    public int getListSize() {
        return elementos.size();
    }

    public LinkedList<Item> getElementos() {
        return elementos;
    }

    public Lista getClone() {
        LinkedList<Item> copia = new LinkedList<>();
        for (Item it : elementos) {
            switch (it.getTipo()) {
                case VECTOR: {
                    Vector v = ((Vector)it.getValor()).getClone();
                    copia.add(new Item(ETipoDato.VECTOR, v));
                }   break;
                case LIST: {
                    Lista l = ((Lista)it.getValor()).getClone();
                    copia.add(new Item(ETipoDato.LIST, l));
                }   break;
                default: {
                    copia.add(new Item(it.getTipo(), it.getValor()));
                }   break;
            }
        }
        return new Lista(copia);
    }

    @Override
    public String toString() {
        return "{ "+ elementos.stream().map(Item::getStringItem).collect(Collectors.joining(", ")) +" }";
    }
}