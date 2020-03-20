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

public class NAsignacionArreglo extends Nodo implements Instruccion {

    private Nodo valor;
    private Arreglo array;
    private LinkedList<Dimension> listaDims;

    public NAsignacionArreglo(int linea, int columna, String archivo, LinkedList<Dimension> listaDims, Arreglo array, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_ASIGNACION_ARREGLO);
        this.array = array;
        this.valor = valor;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;
        LinkedList<Integer> listaValsDims = validarDimensiones(ts);

        if (listaValsDims != null) {
            Resultado rexp = ((Instruccion)valor).Ejecutar(ts);
            if (validarExpresion(rexp)) {

                if (rexp.getTipoDato() == ETipoDato.VECTOR) {
                    Vector v = (Vector)rexp.getValor();
                    array.actualizarValorPorPosiciones(listaValsDims, v.getInnerType(), v.getElementByPosition(0).getValor());
                } else {
                    array.actualizarValorPorPosiciones(listaValsDims, rexp.getTipoDato(), rexp.getValor());
                }

                if (!array.rehashing()) {
                    msj = "Error. No se pudo castear los valores internos del arreglo.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                } else {
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

    private LinkedList<Integer> validarDimensiones(TablaSimbolos ts) {

        String msj;

        /*
         * *********************************************************************************************
         * En esta función se deben de realizar 4 validaciones:
         * 1. Validar que el tipo de acceso de cada dimensión sea de tipo SIMPLE ([]).
         * 2. Validar que la cantidad de dimensiones proporcionada concuerde con la cantidad
         *    de dimensiones establecidas en el arreglo.
         * 3. Validar que los indices proporcionados dentro de cada dimensión sean de tipo INT.
         *    En caso de que se recibiese un vector, se debe validar que este sea de un solo elemento
         *    y que sea de tipo INT.
         * 4. Validar que los indices anteriores no sobrepasen los que ya trae definidos el arreglo.
         *
         * Si las 4 validaciones anteriores se cumplen correctamente, esta función devolverá una
         * LinkedList<Integer> con todos los indices a los cuales se quieren acceder de cada dimensión.
         * *********************************************************************************************
         */

        /* PRIMERA VALIDACIÓN */
        Optional<Dimension> hasAnotherThanSimple = listaDims.stream().filter(d -> d.getTipoDim() != ETipoDimension.SIMPLE).findAny();

        if (hasAnotherThanSimple.isPresent()) {
            msj = "Error. No se puede actualizar el valor en un arreglo utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return null;
        }

        /* SEGUNDA VALIDACIÓN */
        if (array.getCantidadDimensiones() != listaDims.size()) {
            msj = "Error. La cantidad de dimensiones proporcionada <"+ array.getCantidadDimensiones() +"> no concuerda con la definida en el arreglo <"+ listaDims.size() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return null;
        }

        /* TERCERA VALIDACIÓN */
        int cnt = 1;
        Resultado rdim;
        LinkedList<Integer> listaValsDims = new LinkedList<>();
        for (Dimension d : listaDims) {
            rdim = ((Instruccion)d.getValorDimIzq()).Ejecutar(ts);
            switch (rdim.getTipoDato()) {

                case VECTOR: {

                    Vector v = (Vector)rdim.getValor();

                    if (v.getInnerType() != ETipoDato.INT) {
                        msj = "Error. No se puede recibir como valor de posicion en la dimensión #"+ cnt +" un <VECTOR["+ v.getInnerType() +"]>.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                        return null;
                    }

                    if (v.getVectorSize() != 1) {
                        msj = "Error. No se puede recibir como valor de posicion en la dimensión #"+ cnt +" un <VECTOR[INT]> de más de 1 elemento.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                        return null;
                    }

                    Item it = v.getElementByPosition(0);
                    listaValsDims.add((int)it.getValor());

                }   break;

                case INT: {
                    listaValsDims.add((int)rdim.getValor());
                }   break;

                default: {
                    msj = "Error. No se puede recibir como valor de posicion en la dimensión #"+ cnt +" un <"+ rdim.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                    return null;
                }

            }

            cnt++;

        }

        /* CUARTA VALIDACIÓN */
        if (!array.validarIndices(listaValsDims)) {
            msj = "Error. Los indices especificados sobrepasan los límites del arreglo.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return null;
        }

        return listaValsDims;

    }

    private boolean validarExpresion(Resultado rexp) {
        String msj;
        switch (rexp.getTipoDato()) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {
                return true;
            }
            case LIST: {
                Lista l = (Lista)rexp.getValor();
                if (l.getListSize() > 1) {
                    msj = "Error. No se puede asignar una lista con más de 1 valor a una posición de un arreglo.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                    return false;
                }
                return true;
            }
            case VECTOR: {
                Vector v = (Vector)rexp.getValor();
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede asignar un vector con más de 1 valor a una posición de un arreglo.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                    return false;
                }
                return true;
            }
            default: {
                msj = "Error. No se asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de un arreglo.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                return false;
            }
        }
    }

}
