package com.abstracto;

import com.constantes.ETipoDato;

import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lista {

    private LinkedList<Item> elementos;

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

    // TODO - Pendiente desarrollar estas funciones para cuando ya estén las listas.
    public Lista getElementoTipo1() {
        return null;
    }

    public Object getElementoTipo2() {
        return null;
    }

    public LinkedList<Item> getElementos() {
        return elementos;
    }

    @Override
    public String toString() {
        return "{"+ elementos.stream().map(Item::getStringItem).collect(Collectors.joining(", ")) +"}";
    }
}