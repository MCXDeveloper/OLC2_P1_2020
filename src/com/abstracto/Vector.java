package com.abstracto;

import java.util.Optional;
import java.util.LinkedList;

import com.arbol.NNulo;
import com.constantes.ETipoDato;
import java.util.stream.Collectors;

public class Vector implements Estructura {

    private LinkedList<Item> elementos;

    public Vector(ETipoDato tipo, Object valor) {
        this.elementos = new LinkedList<>();
        this.elementos.add(new Item(tipo, valor));
    }

    public Vector(LinkedList<Item> le) {
        this.elementos = le;
    }

    /**
     * Método utilizado para reorganizar la lista basandose en la prioridad de
     * los tipos.  La prioridad está dada de la siguiente forma:
     *      1. Unificar otros vectores
     *      2. String
     *      3. Decimal
     *      4. Integer
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

    public boolean updateVectorValue(int pos, ETipoDato type, Object value) {

        /* Se le resta 1 a la posición ya que las posiciones son manejadas de 0 en adelante. */
        int finalPos = pos - 1;

        if (finalPos <= getVectorSize()) {
            elementos.set(finalPos, new Item(type, value));
        } else {

            /*
             * Si la posición proporcionada es mayor que el tamaño actual del vector
             * entonces se procede a llenar los espacios entre el espacio actual y el
             * deseado con los valores por defecto del tipo del que sea el vector.
            */

            /* Obtengo el tipo interno del vector. */
            ETipoDato tipoInterno = elementos.get(0).getTipo();

            for (int i = getVectorSize(); i <= finalPos; i++) {
                if (i == finalPos) {
                    elementos.add(new Item(type, value));
                } else {
                    elementos.add(new Item(tipoInterno, tipoInterno.getDefecto()));
                }
            }

        }

        /* Por ultimo, hago un rehash del vector para que se actualicen los tipos. */
        return rehashing();

    }

    public Item getValueParaAsignacion(int pos) {

        int finalPos = pos - 1;

        /*
         * Si la posicion proporcionada para obtener un elemento no existe, se
         * deben de llenar espacios con NULL y luego devolver un valor existente.
         */
        if (finalPos > getVectorSize()) {
            /* Obtengo el valor por defecto en base al primer elemento del vector. */
            ETipoDato tipoInterno = elementos.get(0).getTipo();
            for (int i = getVectorSize(); i <= finalPos; i++) {
                elementos.add(new Item(tipoInterno, tipoInterno.getDefecto()));
            }
        }

        Item it = elementos.get(finalPos);
        if (it.getTipo() != ETipoDato.LIST || it.getTipo() != ETipoDato.VECTOR) {
            it.setTipo(ETipoDato.VECTOR);
            it.setValor(new Vector(it.getTipo(), it.getValor()));
        }

        return it;

    }

    public int getVectorSize() {
        return elementos.size();
    }

    public Item getElementByPosition(int pos) {
        return elementos.get(pos);
    }

    public ETipoDato getInnerType() {
        return elementos.get(0).getTipo();
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

    @Override
    public String toString() {
        return getVectorSize() > 1 ? "[ "+ elementos.stream().map(Item::getStringItem).collect(Collectors.joining(", ")) +" ]" : elementos.get(0).getStringItem();
    }

    public LinkedList<Item> getElementos() {
        return elementos;
    }

    @Override
    public Object getClone() {
        LinkedList<Item> copia = new LinkedList<>();
        for (Item it : elementos) {
            copia.add(new Item(it.getTipo(), it.getValor()));
        }
        return new Vector(copia);
    }
}