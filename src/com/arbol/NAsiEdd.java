package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NAsiEdd extends Nodo implements Instruccion {

    private String id;
    private Nodo valor;
    private LinkedList<Dimension> listaDims;

    public NAsiEdd(int linea, int columna, String archivo, String id, LinkedList<Dimension> listaDims, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_DECASI_ARR);
        this.id = id;
        this.valor = valor;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        /* Verifico que el identificador exista en la TS. */
        Simbolo s = ts.getSimbolo(id, true);

        if (s == null) {
            msj = "Error. No se encontro la variable <"+ id +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
        } else {

            /* Verifico que la variable sea de tipo estructura (vector, lista, matriz o arreglo). */
            switch (s.getTipo()) {

                case INT:
                case STRING:
                case DECIMAL:
                case BOOLEAN: {
                    Vector vec = new Vector(s.getTipo(), s.getValor());
                    s.setTipo(ETipoDato.VECTOR);
                    s.setValor(vec);
                    NAsignacionVector nav = new NAsignacionVector(getLinea(), getColumna(), getArchivo(), listaDims, vec, valor);
                    return nav.Ejecutar(ts);
                }

                case VECTOR: {
                    Vector vec = (Vector)s.getValor();
                    NAsignacionVector nav = new NAsignacionVector(getLinea(), getColumna(), getArchivo(), listaDims, vec, valor);
                    return nav.Ejecutar(ts);
                }

                case LIST: {
                    Lista lis = (Lista)s.getValor();
                    NAsignacionLista nal = new NAsignacionLista(getLinea(), getColumna(), getArchivo(), listaDims, lis, valor);
                    return nal.Ejecutar(ts);
                }

                case MATRIX: {
                    Matriz mat = (Matriz)s.getValor();
                    NAsignacionMatriz nam = new NAsignacionMatriz(getLinea(), getColumna(), getArchivo(), listaDims, mat, valor);
                    return nam.Ejecutar(ts);
                }

                case ARRAY: {
                    Arreglo arr = (Arreglo)s.getValor();
                    NAsignacionArreglo naa = new NAsignacionArreglo(getLinea(), getColumna(), getArchivo(), listaDims, arr, valor);
                    return naa.Ejecutar(ts);
                }

                default: {
                    msj = "Error. No se puede acceder con dimensiones a una variable que no sea de tipo estructura.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                }   break;

            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

    }

}
