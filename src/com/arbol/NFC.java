package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Optional;
import java.util.LinkedList;

public class NFC extends Nodo implements Instruccion {

    private LinkedList<Nodo> listaExpresiones;

    public NFC(int linea, int columna, String archivo, LinkedList<Nodo> listaExpresiones) {
        super(linea, columna, archivo, ETipoNodo.EXP_FUNCION_C);
        this.listaExpresiones = listaExpresiones;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        String msj;
        Resultado r;
        boolean flag = false;
        LinkedList<Item> listaItems = new LinkedList<>();

        /* Evaluo todas las expresiones proporcionadas en la función. */
        for (Nodo nodito : listaExpresiones) {
            r = ((Instruccion)nodito).Ejecutar(ts);
            if (r.getTipoDato() == ETipoDato.ERROR) {
                flag = true;
                msj = "Error. No se pudo obtener alguna de las expresiones definidas dentro de la función C.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_FC]", msj, getLinea(), getColumna());
                break;
            } else {
                listaItems.add(new Item(r.getTipoDato(), r.getValor()));
            }
        }

        if (!flag) {

            /* Evaluo si entre los items recopilados existe alguna lista, ya que el retorno de la función C será diferente. */
            Optional<Item> optListaItems = listaItems.stream().filter(i -> i.getTipo() == ETipoDato.LIST).findAny();

            if (optListaItems.isPresent()) {
                tdr = ETipoDato.LIST;
                rvalor = new Lista(listaItems);
            } else {
                Vector v = new Vector(listaItems);
                if (v.rehashing()) {
                    tdr = ETipoDato.VECTOR;
                    rvalor = v;
                }
            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

    }

}
