package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /*
        * 1. Valido que la cantidad de dimensiones:
        *   1.1 Sea exactamente igual a la cantidad de dimensiones proporcionadas por el arreglo.
        *   1.2 Sea mayor a la cantidad de dimensiones proporcionadas por el arreglo (en caso de que se
        *   quiera modificar un elemento interno del arreglo).
        * */

        if (listaDims.size() == array.getCantidadDimensiones()) {

            LinkedList<Integer> listaValsDims = validarDimensiones(ts, listaDims);

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
                        return error;
                    }
                } else {
                    return error;
                }
            } else {
                return error;
            }

        } else if (listaDims.size() > array.getCantidadDimensiones()) {

            /*
             * 2. Obtengo las dimensiones que le corresponden al arreglo, ya que las demás hacen referencia
             * a las estructuras internas del arreglo.
             * */
            LinkedList<Dimension> dimsArray = new LinkedList<>();
            for (int i = 0; i < array.getCantidadDimensiones(); i++) {
                dimsArray.add(listaDims.get(i));
            }

            LinkedList<Integer> listaValsDims = validarDimensiones(ts, dimsArray);

            if (listaValsDims != null) {

                /*
                 * 3. Creo una nueva lista de dimensiones a partir del tamaño de las dimensiones del arreglo.
                 * Es decir, si el arreglo tiene un tamaño de 3 dimensiones, quito los primeros 3 elementos
                 * de 'listaDims' y las demás dimensiones pertenecen a el objeto interno dentro del arreglo.
                 * */
                LinkedList<Dimension> dimsInternas = Optional.ofNullable(listaDims)
                    .stream()
                    .flatMap(Collection::stream)
                    .skip(array.getCantidadDimensiones())
                    .collect(Collectors.toCollection(LinkedList::new));

                /*
                * 4. Obtengo el elemento interno del arreglo ya que quedan dimensiones pendientes de operar
                * pero para eso ya se harán cargo las respectivas clases de asignación de lista/vector.
                * */
                Resultado ret;
                Item it = array.getElementByMultiplePositions(listaValsDims);

                switch (it.getTipo()) {
                    case INT:
                    case STRING:
                    case DECIMAL:
                    case BOOLEAN: {
                        Vector vec = new Vector(it.getTipo(), it.getValor());
                        array.actualizarValorPorPosiciones(listaValsDims, ETipoDato.VECTOR, vec);
                        it = array.getElementByMultiplePositions(listaValsDims);
                        NAsignacionVector nav = new NAsignacionVector(getLinea(), getColumna(), getArchivo(), it, dimsInternas, vec, valor);
                        ret = nav.Ejecutar(ts);
                        if (ret.getTipoDato() != ETipoDato.ERROR) {
                            array.rehashing();
                        }
                        return ret;
                    }
                    case VECTOR: {
                        Vector vec = (Vector)it.getValor();
                        NAsignacionVector nav = new NAsignacionVector(getLinea(), getColumna(), getArchivo(), it, dimsInternas, vec, valor);
                        ret = nav.Ejecutar(ts);
                        if (ret.getTipoDato() != ETipoDato.ERROR) {
                            array.rehashing();
                        }
                        return ret;
                    }
                    case LIST: {
                        Lista lis = (Lista)it.getValor();
                        NAsignacionLista nal = new NAsignacionLista(getLinea(), getColumna(), getArchivo(), dimsInternas, lis, valor);
                        ret = nal.Ejecutar(ts);
                        if (ret.getTipoDato() != ETipoDato.ERROR) {
                            array.rehashing();
                        }
                        return ret;
                    }
                    default: {
                        msj = "Error. No se puede modificar por medio de accesos un elemento interno dentro un arreglo de tipo <"+ it.getTipo() +">.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
                        return error;
                    }
                }

            }

            return error;

        } else {
            msj = "Error. La cantidad de dimensiones proporcionada <"+ listaDims.size() +"> no concuerda con la definida en el arreglo <"+ array.getCantidadDimensiones() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return error;
        }

        return new Resultado(ETipoDato.STRING, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private LinkedList<Integer> validarDimensiones(TablaSimbolos ts, LinkedList<Dimension> lista_de_dimensiones) {

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
         * 4. Validar que los indices proporcionados sean mayor o igual a 1.
         * 5. Validar que los indices anteriores no sobrepasen los que ya trae definidos el arreglo.
         *
         * Si las 5 validaciones anteriores se cumplen correctamente, esta función devolverá una
         * LinkedList<Integer> con todos los indices a los cuales se quieren acceder de cada dimensión.
         * *********************************************************************************************
         */

        /* PRIMERA VALIDACIÓN */
        Optional<Dimension> hasAnotherThanSimple = lista_de_dimensiones.stream().filter(d -> d.getTipoDim() != ETipoDimension.SIMPLE).findAny();

        if (hasAnotherThanSimple.isPresent()) {
            msj = "Error. No se puede actualizar el valor en un arreglo utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return null;
        }

        /* SEGUNDA VALIDACIÓN */
        if (array.getCantidadDimensiones() != lista_de_dimensiones.size()) {
            msj = "Error. La cantidad de dimensiones proporcionada <"+ lista_de_dimensiones.size() +"> no concuerda con la definida en el arreglo <"+ array.getCantidadDimensiones() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return null;
        }

        /* TERCERA VALIDACIÓN */
        int cnt = 1;
        Resultado rdim;
        LinkedList<Integer> listaValsDims = new LinkedList<>();
        for (Dimension d : lista_de_dimensiones) {
            rdim = ((Instruccion)d.getValorDimIzq()).Ejecutar(ts);
            switch (rdim.getTipoDato()) {
                case VECTOR: {
                    Vector v = (Vector)rdim.getValor();
                    if (v.getInnerType() != ETipoDato.INT) {
                        msj = "Error. No se puede recibir como valor de posicion en la dimensión #"+ cnt +" un <VECTOR["+ v.getInnerType() +"]>.";
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
        Optional<Integer> hasLessThanZero = listaValsDims.stream().filter(x -> x <= 0).findAny();
        if (hasLessThanZero.isPresent()) {
            msj = "Error. Alguno de los indices especificados no cumple con el criterio de ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_ARREGLO]", msj, getLinea(), getColumna());
            return null;
        }

        /* QUINTA VALIDACIÓN */
        if (array.validarIndices(listaValsDims)) {
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
