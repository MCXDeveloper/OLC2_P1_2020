package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;
import java.util.Optional;

public class NAccesoLista extends Nodo implements Instruccion {

    private Lista xlista;
    private LinkedList<Dimension> listaDims;

    public NAccesoLista(int linea, int columna, String archivo, Lista xlista, LinkedList<Dimension> listaDims) {
        super(linea, columna, archivo, ETipoNodo.EXP_ACCESO_LISTA);
        this.xlista = xlista;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        Optional<Dimension> hasAnotherAccess = listaDims.stream().filter(d -> (d.getTipoDim() != ETipoDimension.SIMPLE && d.getTipoDim() != ETipoDimension.INNER)).findAny();

        if (hasAnotherAccess.isPresent()) {
            msj = "Error. No se puede acceder a una lista utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_LISTA]", msj, getLinea(), getColumna());
            return error;
        }

        int cnt = 1;
        Resultado rdim;
        int posicion = 0;
        Object pivote = xlista;

        for (Dimension dim : listaDims) {

            if (pivote instanceof Item) {
                pivote = ((Item) pivote).getValor();
            }

            rdim = ((Instruccion) dim.getValorDimIzq()).Ejecutar(ts);
            posicion = validarPosicionDeDimension(cnt, rdim);

            if (posicion != -1) {
                if (pivote instanceof Lista) {
                    Lista l = ((Lista) pivote);
                    pivote = (dim.getTipoDim() == ETipoDimension.SIMPLE) ? l.getElementoTipo1ParaAsignacion(posicion) : l.getElementoTipo2ParaAsignacion(posicion);
                } else if (pivote instanceof Vector) {
                    if (dim.getTipoDim() != ETipoDimension.SIMPLE) {
                        msj = "Error. La dimension #"+ (cnt-1) +" devuelve un vector y a este no se le puede hacer un acceso de tipo [[]].";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_LISTA]", msj, getLinea(), getColumna());
                        return error;
                    }
                    Vector v = ((Vector)pivote);
                    pivote = v.getValueParaAsignacion(posicion);
                }
                cnt++;
            } else {
                return error;
            }

        }

        if(pivote instanceof Lista) {
            return new Resultado(ETipoDato.LIST, EFlujo.NORMAL, pivote);
        } else if (pivote instanceof Vector) {
            return new Resultado(ETipoDato.VECTOR, EFlujo.NORMAL, pivote);
        } else {
            Item it = (Item)pivote;
            return new Resultado(it.getTipo(), EFlujo.NORMAL, it.getValor());
        }

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private int validarPosicionDeDimension(int numDim, Resultado rdim) {

        String msj;
        int posDim;

        switch (rdim.getTipoDato()) {

            case INT: {
                posDim = (int)rdim.getValor();
            }   break;

            case VECTOR: {

                Vector v = (Vector)rdim.getValor();

                if (v.getInnerType() != ETipoDato.INT) {
                    msj = "Error. No se puede recibir como valor de posicion en la dimensión #"+ numDim +" un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_LISTA]", msj, getLinea(), getColumna());
                    return -1;
                }

                posDim = (int)v.getElementByPosition(0).getValor();

            }   break;

            default: {
                msj = "Error. El valor propocionado como posición en la dimension #"+ numDim +" es de tipo <"+ rdim.getTipoDato() +">. Se espera un entero.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_LISTA]", msj, getLinea(), getColumna());
                return -1;
            }

        }

        if (posDim <= 0) {
            msj = "Error. El indice proporcionado en la dimensión #"+ numDim +" debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_LISTA]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

}
