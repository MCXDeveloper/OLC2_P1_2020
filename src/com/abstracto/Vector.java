package com.abstracto;

import java.util.Optional;
import java.util.LinkedList;
import com.constantes.ETipoDato;
import java.util.stream.Collectors;

public class Vector {

    private LinkedList<Item> elementos;

    public Vector(LinkedList<Item> le) {
        this.elementos = le;
    }

    /**
     * Método utilizado para reorganizar la lista basandose en la prioridad de
     * los tipos.  La prioridad está dada de la siguiente forma:
     *      1. String
     *      2. Numeric/Decimal
     *      3. Integer
     * Si algun elemento no concuerda con alguno de estos tipos, se valida que
     * todos los elementos sean boolean, de lo contrario, se reporta error.
     */
    public boolean rehashing() {

        /* Primero valido que si vienen vectores internos, los unifico. */
        Optional<Item> hasVector = elementos.stream().filter(i -> i.getTipo() == ETipoDato.VECTOR).findAny();
        if (hasVector.isPresent()) {
            mergeVectors();
        }

        /* Una vez unificados todos los valores del vector, se procede a realizar el casteo en base a prioridades. */
        Optional<Item> priori1 = elementos.stream().filter(i -> i.getTipo() == ETipoDato.STRING).findAny();
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

    private void mergeVectors() {
        Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() == ETipoDato.VECTOR) {
                elementos.remove(i);
                elementos.addAll(i, ((Vector)pivot.getValor()).getElementos());
            }
        }
    }

    private void convertToString() {
        Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() != ETipoDato.STRING) {
                elementos.set(i, new Item(ETipoDato.STRING, pivot.getValor().toString()));
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

    @Override
    public String toString() {
        return "Printing vector...\n> ["+ elementos.stream().map(Item::getStringItem).collect(Collectors.joining(", ")) +"]";
    }

    public LinkedList<Item> getElementos() {
        return elementos;
    }
}