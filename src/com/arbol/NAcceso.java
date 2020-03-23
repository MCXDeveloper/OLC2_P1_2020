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

                case INT:
                case STRING:
                case DECIMAL:
                case BOOLEAN: {
                    Vector v = new Vector(s.getTipo(), s.getValor());
                    v = !ts.enCicloFor() ? (Vector)v.getClone() : v;
                    NAccesoVector nav = new NAccesoVector(getLinea(), getColumna(), getArchivo(), v, listaDims);
                    return nav.Ejecutar(ts);
                }

                case VECTOR: {
                    Vector v = (Vector)s.getValor();
                    v = !ts.enCicloFor() ? (Vector)v.getClone() : v;
                    NAccesoVector nav = new NAccesoVector(getLinea(), getColumna(), getArchivo(), v, listaDims);
                    return nav.Ejecutar(ts);
                }
                case LIST: {
                    Lista l = (Lista)s.getValor();
                    l = !ts.enCicloFor() ? (Lista)l.getClone() : l;
                    NAccesoLista nal = new NAccesoLista(getLinea(), getColumna(), getArchivo(), l, listaDims);
                    return nal.Ejecutar(ts);
                }

                case MATRIX: {
                    Matriz mat = (Matriz)s.getValor();
                    mat = !ts.enCicloFor() ? (Matriz)mat.getClone() : mat;
                    NAccesoMatriz nam = new NAccesoMatriz(getLinea(), getColumna(), getArchivo(), mat, listaDims);
                    return nam.Ejecutar(ts);
                }

                case ARRAY: {
                    Arreglo array = (Arreglo)s.getValor();
                    array = !ts.enCicloFor() ? (Arreglo)array.getClone() : array;
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

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("EXPRESION");
        String subson = ts.getDeclararNodo("NODO_ACCESO");
        String tokenid = ts.getDeclararNodo(id);
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokenid);
        for (Dimension d : listaDims) {
            son = ((Instruccion)d.getValorDimIzq()).GenerarDOT(ts);
            ts.enlazarNodos(subson, son);
            if (d.getTipoDim() == ETipoDimension.COMPOUND) {
                son = ((Instruccion)d.getValorDimDer()).GenerarDOT(ts);
                ts.enlazarNodos(subson, son);
            }
        }
        return parent;
    }

}
