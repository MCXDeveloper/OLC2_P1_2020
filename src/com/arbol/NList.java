package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.LinkedList;

public class NList extends Nodo implements Instruccion {

    private ETipoDato[] tiposNoPermitidos;
    private LinkedList<Nodo> listaExpresiones;

    public NList(int linea, int columna, String archivo, LinkedList<Nodo> listaExpresiones) {
        super(linea, columna, archivo, ETipoNodo.EXP_LIST);
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
                msj = "Error. No se pudo obtener alguna de las expresiones definidas dentro de la función list().";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_LIST]", msj, getLinea(), getColumna());
                break;
            } else {
                if (Arrays.asList(tiposNoPermitidos).contains(r.getTipoDato())) {
                    flag = true;
                    msj = "Error. Dentro de la función list() no están permitidos valores de tipo <"+ r.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_LIST]", msj, getLinea(), getColumna());
                    break;
                } else {
                    listaItems.add(new Item(r.getTipoDato(), r.getValor()));
                }
            }
        }

        if (!flag) {
            tdr = ETipoDato.LIST;
            rvalor = new Lista(listaItems);
        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

    }
}
