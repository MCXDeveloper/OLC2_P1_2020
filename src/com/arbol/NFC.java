package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.Optional;
import java.util.LinkedList;

public class NFC extends Nodo implements Instruccion {

    private ETipoDato[] tiposNoPermitidos;
    private LinkedList<Nodo> listaExpresiones;

    public NFC(int linea, int columna, String archivo, LinkedList<Nodo> listaExpresiones) {
        super(linea, columna, archivo, ETipoNodo.EXP_FUNCION_C);
        this.listaExpresiones = listaExpresiones;
        tiposNoPermitidos = new ETipoDato[]{ ETipoDato.ANY, ETipoDato.ARRAY, ETipoDato.MATRIX };
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        Resultado r;
        boolean flag = false;
        LinkedList<Item> listaItems = new LinkedList<>();

        /* Evaluo todas las expresiones proporcionadas en la función. */
        for (Nodo nodito : listaExpresiones) {
            r = ((Instruccion)nodito).Ejecutar(ts);
            if (r.getTipoDato() == ETipoDato.ERROR) {
                flag = true;
                msj = "Error. No se pudo obtener alguna de las expresiones definidas dentro de la función C().";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_FC]", msj, getLinea(), getColumna());
                break;
            } else {
                if (Arrays.asList(tiposNoPermitidos).contains(r.getTipoDato())) {
                    flag = true;
                    msj = "Error. Dentro de la función C() no están permitidos valores de tipo <"+ r.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_FC]", msj, getLinea(), getColumna());
                    break;
                } else {
                    listaItems.add(new Item(r.getTipoDato(), r.getValor()));
                }
            }
        }

        if (!flag) {

            /* Evaluo si entre los items recopilados existe alguna lista, ya que el retorno de la función C será diferente. */
            Optional<Item> optListaItems = listaItems.stream().filter(i -> i.getTipo() == ETipoDato.LIST).findAny();

            if (optListaItems.isPresent()) {
                Lista l = new Lista(listaItems);
                if (l.rehashing()) {
                    tdr = ETipoDato.LIST;
                    rvalor = l;
                } else {
                    msj = "Error. No se pudo realizar el casteo correspondiente a la lista.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_FC]", msj, getLinea(), getColumna());
                }
            } else {
                Vector v = new Vector(listaItems);
                if (v.rehashing()) {
                    tdr = ETipoDato.VECTOR;
                    rvalor = v;
                } else {
                    msj = "Error. No se pudo realizar el casteo correspondiente al vector.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_FC]", msj, getLinea(), getColumna());
                }
            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

}
