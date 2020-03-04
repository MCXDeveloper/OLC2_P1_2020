package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NAcceso extends Nodo implements Instruccion {

    private String id;
    private LinkedList<Dimension> listaDims;

    public NAcceso(int linea, int columna, String archivo, String id, LinkedList<Dimension> listaDims) {
        super(linea, columna, archivo, ETipoNodo.EXP_ACCESO);
        this.id = id;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        Simbolo s = ts.getSimbolo(id);

        if (s == null) {
            msj = "Error. No se encontro la variable <"+ id +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
            return error;
        } else {

            /* Verifico que la variable sea de tipo estructura (vector, lista, matriz o arreglo). */
            switch (s.getTipo()) {
                case VECTOR: {
                    Vector v = (Vector)s.getValor();
                    NAccesoVector nav = new NAccesoVector(getLinea(), getColumna(), getArchivo(), v, listaDims);
                    return nav.Ejecutar(ts);
                }
                case LIST: {
                    Lista l = (Lista)s.getValor();
                    NAccesoLista nal = new NAccesoLista(getLinea(), getColumna(), getArchivo(), l, listaDims);
                    return nal.Ejecutar(ts);
                }

                case MATRIX: {
                    Matriz mat = (Matriz)s.getValor();
                    NAccesoMatriz nam = new NAccesoMatriz(getLinea(), getColumna(), getArchivo(), mat, listaDims);
                    return nam.Ejecutar(ts);
                }

                case ARRAY: {
                    Arreglo array = (Arreglo)s.getValor();
                    NAccesoArreglo naa = new NAccesoArreglo(getLinea(), getColumna(), getArchivo(), array, listaDims);
                    return naa.Ejecutar(ts);
                }

                default: {
                    msj = "Error. No se puede acceder con dimensiones a una variable que no sea de tipo estructura.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
                    return error;
                }

            }

        }

    }

}
