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

        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        String msj;
        Simbolo s = ts.getSimbolo(id);

        if (s == null) {
            msj = "Error. No se encontro la variable <"+ id +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
        } else {

            /* Verifico que la variable sea de tipo estructura (vector, lista, matriz o arreglo). */
            switch (s.getTipo()) {

                case VECTOR: {
                    Resultado rvec = accesoVector(ts, s);
                    if (rvec != null) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                }   break;

                case LIST: {
                    if (accesoLista(ts, s)) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                }   break;

                case MATRIX: {
                    /*if (accesoMatriz(ts, s)) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }*/
                }   break;

                // TODO - Pendiente hacer los accesos para arreglos.
                default: {
                    msj = "Error. No se puede acceder con dimensiones a una variable que no sea de tipo estructura.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
                }

            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

    }

    private Resultado accesoVector(TablaSimbolos ts, Simbolo s) {



        return null;
    }

    private boolean accesoLista(TablaSimbolos ts, Simbolo s) {
        return true;
    }

    private Resultado accesoMatriz(TablaSimbolos ts, Simbolo s) {

        String msj;
        Matriz mat = (Matriz)s.getValor();

        if (listaDims.size() > 1) {
            msj = "Error. El acceso a una matriz se puede realizar únicamente por medio de 1 dimensión.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
            return null;
        }

        Dimension dim = listaDims.get(0);

        if (dim.getTipoDim() == ETipoDimension.INNER) {
            msj = "Error. No se puede acceder al valor de una matriz utilizando la forma de acceso [ [ ] ].";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
            return null;
        }

        switch (dim.getTipoDim()) {
            case ROW:
            case SIMPLE:
            case COLUMN: {
                Resultado rdim = ((Instruccion)dim.getValorDimIzq()).Ejecutar(ts);
                int pos = validatePositionOfDim(rdim, mat);
                if (pos != -1) {

                }
            }   break;
            case COMPOUND: {

            }   break;
        }

        return null;
    }

    private int validatePositionOfDim(Resultado rdim, Matriz mat) {

        String msj;

        if (rdim.getTipoDato() != ETipoDato.INT) {
            msj = "Error. Se espera que el valor propocionado como posición en la dimension 1 sea de tipo INTEGER.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
            return -1;
        }


        return -1;
    }

}
