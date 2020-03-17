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

public class NAsignacionVector extends Nodo implements Instruccion {

    private Nodo valor;
    private Vector vec;
    private LinkedList<Dimension> listaDims;

    public NAsignacionVector(int linea, int columna, String archivo, LinkedList<Dimension> listaDims, Vector vec, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_ASIGNACION_VECTOR);
        this.vec = vec;
        this.valor = valor;
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
         * 1. Validar que el tipo de acceso de cada dimensión sea de tipo SIMPLE ([]).
         * 2. Validar que los indices proporcionados dentro de cada dimensión sean de tipo INT.
         *    En caso de que se recibiese un vector, se debe validar que este sea de un solo elemento
         *    y que sea de tipo INT.
         * 3. Validar si la lista de dimensiones es de una sola dimensión o de varias ya que el
         *    procedimiento es distinto.
         *
         * Si las 3 validaciones anteriores se cumplen correctamente, esta función actualizará en las
         * posiciones indicadas, el vector.
         * *********************************************************************************************
         */

        /* PRIMERA VALIDACIÓN */
        Optional<Dimension> hasAnotherThanSimple = listaDims.stream().filter(d -> d.getTipoDim() != ETipoDimension.SIMPLE).findAny();

        if (hasAnotherThanSimple.isPresent()) {
            msj = "Error. No se puede acceder a un vector utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
        } else {

            /* SEGUNDA VALIDACIÓN */
            int actualPos = 0;
            boolean flag = false;
            Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
            int firstPos = validarPosicionDeDimension(1, rdim);

            if (firstPos != -1) {

                if (listaDims.size() == 1) {
                    actualPos = firstPos;
                } else {

                    /*
                     * Verifico si el acceso al vector posee más de una dimension. Si eso se cumple,
                     * se tiene que validar que las siguientes dimensiones proporcionen un entero como
                     * valor de posicion y este tiene que ser exactamente igual a 1, de lo contrario se
                     * debe reportar error.  Éste multiple acceso simula la recursión interna de la
                     * relación entre vectores y tipos primitivos (que también se consideran vectores).
                     *
                     * Una vez finalizado el ciclo y si todas las dimensiones se validaron correctamente
                     * significa que todas las posiciones son iguales, por lo que se procede a tomar la
                     * última (actualPos) y se modifica su valor por la expresión proporcionada. Esto se
                     * realiza más abajo.
                     */

                    Resultado r;
                    for (int i = 1; i < listaDims.size(); i++) {
                        r = ((Instruccion)listaDims.get(i).getValorDimIzq()).Ejecutar(ts);
                        actualPos = validarPosicionDeDimension((i+1), r);
                        if (actualPos != -1) {
                            if (actualPos != 1) {
                                flag = true;
                                msj = "Error. No se puede acceder a una posición inexistente de un vector [Dimension = "+ (i+1) +" | Valor = "+ actualPos +"].";
                                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
                                break;
                            }
                        } else {
                            flag = true;
                            break;
                        }
                    }

                }

            } else {
                flag = true;
            }

            if (!flag) {
                Resultado rexp = ((Instruccion)valor).Ejecutar(ts);
                if (validarExpresionParaVector(rexp)) {
                    vec.updateVectorValue(actualPos, rexp.getTipoDato(), rexp.getValor());
                    tdr = ETipoDato.NT;
                    rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
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
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
                    return -1;
                }

                posDim = (int)v.getElementByPosition(0).getValor();

            }   break;

            default: {
                msj = "Error. El valor propocionado como posición en la dimension #"+ numDim +" es de tipo <"+ rdim.getTipoDato() +">. Se espera un entero.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
                return -1;
            }

        }

        if (posDim <= 0) {
            msj = "Error. El indice proporcionado en la dimensión #"+ numDim +" debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

    private boolean validarExpresionParaVector(Resultado rexp) {
        String msj;
        switch (rexp.getTipoDato()) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN:
                break;
            case VECTOR: {
                Vector v = (Vector)rexp.getValor();
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede asignar un vector de más de 1 valor a una posición de un vector.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
                    return false;
                }
            }   break;
            default: {
                msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de un vector.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_VECTOR]", msj, getLinea(), getColumna());
                return false;
            }
        }
        return true;
    }

}
