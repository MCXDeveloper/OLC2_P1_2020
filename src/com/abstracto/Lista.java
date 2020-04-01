package com.abstracto;

import com.arbol.NNulo;
import com.constantes.ETipoDato;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

public class Lista implements Estructura {

    private LinkedList<Item> elementos;

    public Lista(ETipoDato tipo, Object valor) {
        this.elementos = new LinkedList<>();
        this.elementos.add(new Item(tipo, valor));
    }

    public Lista(LinkedList<Item> le) {
        this.elementos = le;
    }

    public boolean rehashing(boolean rehashLists) {

        /* Si dentro de la lista vienen vectores, los rehasheo */
        for (Item it : elementos) {
            if (it.getTipo() == ETipoDato.VECTOR) {
                ((Vector)it.getValor()).rehashing();
            }
        }

        if (rehashLists) {
            Optional<Item> hasLists = elementos.stream().filter(i -> i.getTipo() == ETipoDato.LIST).findAny();
            if (hasLists.isPresent()) {
                mergeLists();
            }
        }

        return true;

    }

    private void mergeLists() {

        Lista l;
        Vector v;
        LinkedList<Item> li = new LinkedList<>();

        for (Item it : elementos) {
            switch (it.getTipo()) {
                case NT:
                case INT:
                case STRING:
                case DECIMAL:
                case BOOLEAN: {
                    li.add(new Item(it.getTipo(), it.getValor()));
                }   break;
                case VECTOR: {
                    v = (Vector)it.getValor();
                    for (Item ivec : v.getElementos()) {
                        li.add(new Item(ivec.getTipo(), ivec.getValor()));
                    }
                }   break;
                case LIST: {
                    l = (Lista)it.getValor();
                    for (Item ilist : l.getElementos()) {
                        li.add(new Item(ilist.getTipo(), ilist.getValor()));
                    }
                }   break;
            }
        }

        elementos = li;

        /*Item pivot;
        for (int i = 0; i < elementos.size(); i++) {
            pivot = elementos.get(i);
            if (pivot.getTipo() == ETipoDato.LIST) {
                elementos.remove(i);
                elementos.addAll(i, ((Lista)pivot.getValor()).getElementos());
            }
        }*/
    }

    public boolean updateListValue(int pos, ETipoDato type, Object value) {

        /* Se le resta 1 a la posición ya que las posiciones son manejadas de 0 en adelante. */
        int finalPos = pos - 1;

        if (pos > getListSize()) {
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
        } else {
            elementos.set(finalPos, new Item(type, value));
        }

        return rehashing(false);

    }

    public Item getElementoTipo1(int pos) {
        int finalPos = pos - 1;
        Item it = elementos.get(finalPos);
        if (it.getTipo() != ETipoDato.LIST && it.getTipo() != ETipoDato.VECTOR) {
            it = new Item(ETipoDato.VECTOR, new Vector(it.getTipo(), it.getValor()));
        }
        return new Item(ETipoDato.LIST, new Lista(it.getTipo(), it.getValor()));
    }

    public Item getElementoTipo2(int pos) {
        int finalPos = pos - 1;
        Item it = elementos.get(finalPos);
        if (it.getTipo() != ETipoDato.LIST && it.getTipo() != ETipoDato.VECTOR) {
            it = new Item(ETipoDato.VECTOR, new Vector(it.getTipo(), it.getValor()));
        }
        return it;
    }

    public Item getElementoTipo1ParaAsignacion(int pos) {

        int finalPos = pos - 1;

        /*
         * Si la posicion proporcionada para obtener un acceso de tipo 1 no existe, se
         * deben de llenar espacios con NULL y luego devolver un valor existente.
         */
        if (pos > getListSize()) {
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
        if (pos > getListSize()) {
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

    @Override
    public String toString() {
        return "⎨ "+ elementos.stream().map(Item::getStringItem).collect(Collectors.joining(", ")) +" ⎬";
    }

    @Override
    public Object getClone() {
        LinkedList<Item> copia = new LinkedList<>();
        for (Item it : elementos) {
            switch (it.getTipo()) {
                case VECTOR: {
                    Vector v = (Vector)((Vector)it.getValor()).getClone();
                    copia.add(new Item(ETipoDato.VECTOR, v));
                }   break;
                case LIST: {
                    Lista l = (Lista)((Lista)it.getValor()).getClone();
                    copia.add(new Item(ETipoDato.LIST, l));
                }   break;
                default: {
                    copia.add(new Item(it.getTipo(), it.getValor()));
                }   break;
            }
        }
        return new Lista(copia);
    }
}