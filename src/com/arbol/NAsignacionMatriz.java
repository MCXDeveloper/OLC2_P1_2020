package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

public class NAsignacionMatriz extends Nodo implements Instruccion {

    private Nodo valor;
    private Matriz matrix;
    private LinkedList<Dimension> listaDims;

    public NAsignacionMatriz(int linea, int columna, String archivo, LinkedList<Dimension> listaDims, Matriz matrix, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_ASIGNACION_MATRIZ);
        this.valor = valor;
        this.matrix = matrix;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        /*
         * *********************************************************************************************
         * En esta función se deben de realizar 4 validaciones:
         * 1. Validar que la cantidad de dimensiones proporcionada sea solo una.
         * 2. Validar que esa única dimensión no sea de tipo INNER ([[]]).
         * 3. Validar, en base al tipo de acceso, lo siguiente:
         *  3.1. SIMPLE:
         *      3.1.1. El índice proporcionado debe ser de tipo INT. En caso de que se recibiese un
         *      vector, se debe validar que este sea de un solo elemento y que sea de tipo INT.
         *      3.1.2. El índice debe ser mayor a cero.
         *      3.1.3. El índice anterior no debe sobrepasar el tamaño general de la matriz.
         *  3.2. COMPOUND:
         *      3.2.1. Los índices proporcionados deben ser de tipo INT. En caso de que se recibiese un
         *      vector, se debe validar que este sea de un solo elemento y que sea de tipo INT.
         *      3.2.2. El índice debe ser mayor a cero.
         *      3.2.3. Los índices anteriores no deben sobrepasar los valores de filas y columnas que
         *      fueron definidos al crear la matriz.
         *  3.3. ROW:
         *      3.3.1. El índice proporcionado debe ser de tipo INT. En caso de que se recibiese un
         *      vector, se debe validar que este sea de un solo elemento y que sea de tipo INT.
         *      3.3.2. El índice debe ser mayor a cero.
         *      3.3.3. El índice anterior no debe sobrepasar el tamaño de las filas de la matriz.
         *  3.4. COLUMN:
         *      3.4.1. El índice proporcionado debe ser de tipo INT. En caso de que se recibiese un
         *      vector, se debe validar que este sea de un solo elemento y que sea de tipo INT.
         *      3.4.2. El índice debe ser mayor a cero.
         *      3.4.3. El índice anterior no debe sobrepasar el tamaño de las columnas de la matriz.
         *
         * Si las 4 validaciones anteriores se cumplen correctamente, esta función actualizará el valor
         * deseado en las posiciones especificadas.
         * *********************************************************************************************
         */

        /* PRIMERA VALIDACIÓN */
        if (listaDims.size() > 1) {
            msj = "Error. No se puede realizar la modificación de una matriz utilizando accesos combinados.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
        } else {

            /* SEGUNDA Y TERCERA VALIDACIÓN */
            ETipoDimension tipoDim = listaDims.get(0).getTipoDim();

            switch (tipoDim) {

                case ROW: {
                    /* VALIDACIÓN DE LOS PUNTOS 3.3.1 - 3.3.2 */
                    Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                    int posDim = validarPosicionDeDimension(rdim);
                    if (posDim != -1) {
                        /* VALIDACIÓN DEL PUNTO 3.3.3 */
                        if (!matrix.validateRows(posDim)) {
                            msj = "Error. El índice proporcionado sobrepasa los limites de las filas de la matriz.";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                        }
                        /* ACTUALIZO EL VALOR FINAL */
                        Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                        Resultado finalExp = validarExpresionParaFilasYColumnas("fila", matrix.getColumnas(), rexp);
                        if (finalExp != null) {
                            tdr = ETipoDato.NT;
                            rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                            matrix.updateRowValue(posDim, finalExp.getTipoDato(), finalExp.getValor());
                        }
                    }
                }   break;

                case COLUMN: {
                    /* VALIDACIÓN DE LOS PUNTOS 3.4.1 - 3.4.2 */
                    Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                    int posDim = validarPosicionDeDimension(rdim);
                    if (posDim != -1) {
                        /* VALIDACIÓN DEL PUNTO 3.4.3 */
                        if (!matrix.validateColumns(posDim)) {
                            msj = "Error. El índice proporcionado sobrepasa los limites de las columnas de la matriz.";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                        }
                        /* ACTUALIZO EL VALOR FINAL */
                        Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                        Resultado finalExp = validarExpresionParaFilasYColumnas("columna", matrix.getFilas(), rexp);
                        if (finalExp != null) {
                            tdr = ETipoDato.NT;
                            rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                            matrix.updateColumnValue(posDim, finalExp.getTipoDato(), finalExp.getValor());
                        }
                    }
                }   break;

                case SIMPLE: {
                    /* VALIDACIÓN DE LOS PUNTOS 3.1.1 - 3.1.2 */
                    Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                    int posDim = validarPosicionDeDimension(rdim);
                    if (posDim != -1) {
                        /* VALIDACIÓN DEL PUNTO 3.1.3 */
                        if (posDim > matrix.getMatrixSize()) {
                            msj = "Error. El valor proporcionado como posición sobrepasa el tamaño de la matriz.";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                        }
                        /* ACTUALIZO EL VALOR FINAL */
                        Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                        Resultado finalExp = validarExpresion(rexp);
                        if (finalExp != null) {
                            tdr = ETipoDato.NT;
                            rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                            matrix.updateValueByPosition(posDim, finalExp.getTipoDato(), finalExp.getValor());
                        }
                    }
                }   break;

                case COMPOUND: {
                    /* VALIDACIÓN DE LOS PUNTOS 3.2.1 - 3.2.2 */
                    Resultado rdimx = ((Instruccion) listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                    Resultado rdimy = ((Instruccion) listaDims.get(0).getValorDimDer()).Ejecutar(ts);
                    int posX = validarPosicionDeDimension(rdimx);
                    int posY = validarPosicionDeDimension(rdimy);
                    if (posX != -1 && posY != -1) {
                        /* VALIDACIÓN DEL PUNTO 3.2.3 */
                        if (!matrix.validateDimensions(posX, posY)) {
                            msj = "Error. Los indices proporcionados sobrepasan los rangos de la matriz.";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                        }
                        /* ACTUALIZO EL VALOR FINAL */
                        Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                        Resultado finalExp = validarExpresion(rexp);
                        if (finalExp != null) {
                            tdr = ETipoDato.NT;
                            rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                            matrix.updateValueByRowAndCol(posX, posY, rexp.getTipoDato(), rexp.getValor());
                        }
                    }
                }   break;

                default: {
                    msj = "Error. No se puede acceder a una matriz utilizando el tipo de acceso <"+ tipoDim +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                }

            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);
    }

    private int validarPosicionDeDimension(Resultado rdim) {

        String msj;
        int posDim;

        switch (rdim.getTipoDato()) {

            case INT: {
                posDim = (int)rdim.getValor();
            }   break;

            case VECTOR: {

                Vector v = (Vector)rdim.getValor();

                if (v.getInnerType() != ETipoDato.INT) {
                    msj = "Error. No se puede recibir como valor de posicion en la dimensión #1 un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                    return -1;
                }

                if (v.getVectorSize() != 1) {
                    msj = "Error. No se puede recibir como valor de posicion en la dimensión #1 un <VECTOR[INT]> de más de 1 elemento.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                    return -1;
                }

                posDim = (int)v.getElementByPosition(0).getValor();

            }   break;

            default: {
                msj = "Error. El valor propocionado como posición en la dimension es de tipo <"+ rdim.getTipoDato() +">. Se espera un entero.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                return -1;
            }

        }

        if (posDim <= 0) {
            msj = "Error. El indice proporcionado debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

    private Resultado validarExpresion(Resultado rexp) {

        String msj;

        switch (rexp.getTipoDato()) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {
                return rexp;
            }
            case VECTOR: {
                Vector v = (Vector)rexp.getValor();
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede asignar un vector con más de un valor a una posición de una matriz.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                    return null;
                }
                rexp.setTipoDato(v.getInnerType());
                rexp.setValor(v.getElementByPosition(0).getValor());
                return rexp;
            }
            default: {
                msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de una matriz.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                return null;
            }
        }

    }

    private Resultado validarExpresionParaFilasYColumnas(String tipo, int tamano, Resultado rexp) {

        String msj;

        switch (rexp.getTipoDato()) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {
                return rexp;
            }
            case VECTOR: {
                Vector v = (Vector)rexp.getValor();
                if (v.getVectorSize() == 1) {
                    rexp.setTipoDato(v.getInnerType());
                    rexp.setValor(v.getElementByPosition(0).getValor());
                } else {
                    if (v.getVectorSize() != tamano) {
                        msj = "Error. El vector que se desea asignar a la "+ tipo +" no coincide en tamaño con la cantidad de "+ tipo +"s de la matriz.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                        return null;
                    }
                }
                return rexp;
            }
            default: {
                msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una "+ tipo +" de una matriz.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                return null;
            }
        }

    }

}
