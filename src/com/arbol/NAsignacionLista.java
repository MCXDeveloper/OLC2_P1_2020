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

public class NAsignacionLista extends Nodo implements Instruccion {

    private Nodo valor;
    private Lista listax;
    private LinkedList<Dimension> listaDims;

    public NAsignacionLista(int linea, int columna, String archivo, LinkedList<Dimension> listaDims, Lista listax, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_ASIGNACION_LISTA);
        this.valor = valor;
        this.listax = listax;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        /*
         * *********************************************************************************************
         * En esta función se deben de realizar 3 validaciones:
         * 1. Validar que el tipo de acceso de cada dimensión sea de tipo SIMPLE ([]) o INNER ([[]]).
         * 2. Validar que los indices proporcionados dentro de cada dimensión sean de tipo INT.
         *    En caso de que se recibiese un vector, se debe validar que este sea de un solo elemento
         *    y que sea de tipo INT.
         * 3. El punto anterior va de la mano con el tipo de estructura que se está obteniendo, ya que
         * al tener accesos combinados, algunos retornan vectores y otros listas, es por ello que es
         * importante hacer crecer las estructuras cuando sea necesario sin perder su referencia.
         *
         * Si las 3 validaciones anteriores se cumplen correctamente, esta función devolverá una
         * LinkedList<Integer> con todos los indices a los cuales se quieren acceder de cada dimensión.
         * *********************************************************************************************
         */

        /* PRIMERA VALIDACIÓN */
        Optional<Dimension> hasAnotherAccess = listaDims.stream().filter(d -> (d.getTipoDim() != ETipoDimension.SIMPLE && d.getTipoDim() != ETipoDimension.INNER)).findAny();

        if (hasAnotherAccess.isPresent()) {
            msj = "Error. No se puede acceder a una lista utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
        } else {

            int cnt = 1;
            Resultado rdim;
            int posicion = 0;
            boolean flag = false;
            Object pivote = listax;

            for (Dimension dim : listaDims) {

                if (pivote instanceof Item) {
                    pivote = ((Item) pivote).getValor();
                }

                /* SEGUNDA Y TERCERA VALIDACIÓN */
                rdim = ((Instruccion) dim.getValorDimIzq()).Ejecutar(ts);
                posicion = validarPosicionDeDimension(cnt, rdim);

                if (posicion != -1) {
                    if (pivote instanceof Lista) {
                        Lista l = ((Lista) pivote);
                        pivote = (dim.getTipoDim() == ETipoDimension.SIMPLE) ? l.getElementoTipo1ParaAsignacion(posicion) : l.getElementoTipo2ParaAsignacion(posicion);
                    } else if (pivote instanceof Vector) {
                        if (dim.getTipoDim() != ETipoDimension.SIMPLE) {
                            flag = true;
                            msj = "Error. La dimension #"+ (cnt-1) +" devuelve un vector y a este no se le puede hacer un acceso de tipo [[]].";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                            break;
                        }
                        Vector v = ((Vector)pivote);
                        pivote = v.getValueParaAsignacion(posicion);
                    }
                    cnt++;
                } else {
                    flag = true;
                    break;
                }

            }

            if (!flag) {

                /* ACTUALIZO EL VALOR FINAL */
                Resultado rexp = ((Instruccion)valor).Ejecutar(ts);

                if (pivote instanceof Lista) {
                    if (validarExpresionParaLista(rexp)) {
                        ((Lista)pivote).updateListValue(posicion, rexp.getTipoDato(), rexp.getValor());
                        listax.rehashing();
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                } else if (pivote instanceof Vector) {
                    if (validarExpresionParaVector(rexp)) {
                        ((Vector)pivote).updateVectorValue(posicion, rexp.getTipoDato(), rexp.getValor());
                        listax.rehashing();
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                } else if (pivote instanceof Item) {

                    Item i = ((Item)pivote);

                    if ((i.getTipo() == ETipoDato.LIST && validarExpresionParaLista(rexp)) || (i.getTipo() == ETipoDato.VECTOR && validarExpresionParaVector(rexp))) {
                        if (rexp.getTipoDato() != ETipoDato.LIST && rexp.getTipoDato() != ETipoDato.VECTOR) {
                            i.setTipo(ETipoDato.VECTOR);
                            i.setValor(new Vector(rexp.getTipoDato(), rexp.getValor()));
                        } else {
                            i.setTipo(rexp.getTipoDato());
                            i.setValor(rexp.getValor());
                        }
                        listax.rehashing();
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }

                } else {
                    msj = "Error. Pivote no reconocido en la modificación de listas.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                }

            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);
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
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                    return -1;
                }

                posDim = (int)v.getElementByPosition(0).getValor();

            }   break;

            default: {
                msj = "Error. El valor propocionado como posición en la dimension #"+ numDim +" es de tipo <"+ rdim.getTipoDato() +">. Se espera un entero.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                return -1;
            }

        }

        if (posDim <= 0) {
            msj = "Error. El indice proporcionado en la dimensión #"+ numDim +" debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

    private boolean validarExpresionParaLista(Resultado rexp) {

        String msj;

        switch (rexp.getTipoDato()) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN:
                break;
            case VECTOR: {
                Vector v = ((Vector)rexp.getValor());
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede asignar un vector con más de 1 parámetro a una lista.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                    return false;
                }
            }   break;
            case LIST: {
                Lista l = ((Lista)rexp.getValor());
                if (l.getListSize() > 1) {
                    msj = "Error. No se puede asignar una lista con más de 1 parámetro a otra lista.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                    return false;
                }
            }   break;
            default: {
                msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de una lista.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                return false;
            }
        }

        return true;
    }

    private boolean validarExpresionParaVector(Resultado rexp) {
        switch (rexp.getTipoDato()) {
            case INT:
            case VECTOR:
            case STRING:
            case DECIMAL:
            case BOOLEAN:
                break;
            default: {
                String msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de un vector.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_LISTA]", msj, getLinea(), getColumna());
                return false;
            }
        }
        return true;
    }

}
