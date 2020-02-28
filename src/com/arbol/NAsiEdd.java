package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        Simbolo s = ts.getSimbolo(id);

        if (s == null) {
            msj = "Error. No se encontro la variable <"+ id +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
        } else {

            /* Verifico que la variable sea de tipo estructura (vector, lista, matriz o arreglo). */
            switch (s.getTipo()) {

                case VECTOR: {
                    if (validarActualizarVector(ts, s)) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                }   break;

                case LIST: {
                    if (validarActualizarLista(ts, s)) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                }   break;

                case MATRIX: {
                    if (validarActualizarMatriz(ts, s)) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                }   break;

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

    private boolean validarActualizarMatriz(TablaSimbolos ts, Simbolo s) {

        String msj;
        Matriz mat = (Matriz)s.getValor();

        /*
         * Valido que el tipo de acceso que se esté realizando sea:
         * 1. SIMPLE
         * 2. COMPOUND
         * 3. ROW
         * 4. COLUMN
         * De lo contrario, se debe mostrar error.
         */

        if (listaDims.size() > 1) {
            msj = "Error. No se puede realizar la modificación de una matriz utilizando accesos combinados.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }

        ETipoDimension tipoDim = listaDims.get(0).getTipoDim();

        if (tipoDim == ETipoDimension.INNER) {
            msj = "Error. No se puede acceder a una matriz utilizando la forma de acceso [ [ ] ].";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }

        if (tipoDim == ETipoDimension.SIMPLE) {

            Resultado rdim = ((Instruccion) listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
            int posDim = validatePositionValueOfDimension(rdim);

            if (posDim != -1) {

                /* Valido que la posición proporcionada no sobrepase el tamaño de la lista que representa la matriz. */
                if (posDim > mat.getMatrixSize()) {
                    msj = "Error. El valor proporcionado como posición sobrepasa los índices de la matriz (IndexOutOfBounds).";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return false;
                }

                /* Valido que el valor de la expresión sea válida para la matriz. */
                Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                Resultado finalExp = validateMatrixExpression(rexp);
                if (finalExp != null) {
                    mat.updateValueByPosition(posDim, finalExp.getTipoDato(), finalExp.getValor());
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } else if (tipoDim == ETipoDimension.COMPOUND) {

            Resultado rdimx = ((Instruccion) listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
            Resultado rdimy = ((Instruccion) listaDims.get(0).getValorDimDer()).Ejecutar(ts);

            int posX = validatePositionValueOfDimension(rdimx);
            int posY = validatePositionValueOfDimension(rdimy);

            if (posX != -1 && posY != -1) {
                if (mat.validateDimensions(posX, posY)) {
                    Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                    Resultado finalExp = validateMatrixExpression(rexp);
                    if (finalExp != null) {
                        mat.updateValueByRowAndCol(posX, posY, rexp.getTipoDato(), rexp.getValor());
                    } else {
                        return false;
                    }
                } else {
                    msj = "Error. Los indices proporcionados sobrepasan los rangos de la matriz.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return false;
                }
            } else {
                return false;
            }

        } else if (tipoDim == ETipoDimension.ROW) {

            Resultado rdim = ((Instruccion) listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
            int posDim = validatePositionValueOfDimension(rdim);

            if (posDim != -1) {

                /* Valido que la posición proporcionada no sobrepase el tamaño de las filas de la matriz. */
                if (!mat.validateRows(posDim)) {
                    msj = "Error. El índice proporcionado sobrepasa los limites de las filas de la matriz.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return false;
                }

                Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                Resultado finalExp = validateMatrixExpressionForRowOrCols("fila", mat.getColumnas(), rexp);

                if (finalExp != null) {
                    mat.updateRowValue(posDim, finalExp.getTipoDato(), finalExp.getValor());
                } else {
                    return false;
                }

            } else {
                return false;
            }

        } else {

            /* El último sería el tipo de acceso por COLUMN */

            Resultado rdim = ((Instruccion) listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
            int posDim = validatePositionValueOfDimension(rdim);

            if (posDim != -1) {

                /* Valido que la posición proporcionada no sobrepase el tamaño de las columnas de la matriz. */
                if (!mat.validateColumns(posDim)) {
                    msj = "Error. El índice proporcionado sobrepasa los limites de las columnas de la matriz.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return false;
                }

                Resultado rexp = ((Instruccion) valor).Ejecutar(ts);
                Resultado finalExp = validateMatrixExpressionForRowOrCols("columna", mat.getFilas(), rexp);

                if (finalExp != null) {
                    mat.updateColumnValue(posDim, finalExp.getTipoDato(), finalExp.getValor());
                } else {
                    return false;
                }

            } else {
                return false;
            }

        }

        return true;
    }

    private boolean validarActualizarVector(TablaSimbolos ts, Simbolo s) {

        String msj;

        /*
         * Valido que el tipo de acceso que se esté realizando sea:
         * 1. SIMPLE
         * De lo contrario, se debe mostrar error.
        */

        Optional<Dimension> hasAnotherThanSimple = listaDims.stream().filter(d -> d.getTipoDim() != ETipoDimension.SIMPLE).findAny();

        if (hasAnotherThanSimple.isPresent()) {
            msj = "Error. No se puede acceder a un vector utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }

        /*
         * Una vez validado lo anterior, se procede a verificar que el valor proporcionado
         * como posición de la primera dimensión sea de tipo entero y que sea mayor a 0.
        */

        int actualPos = 0;
        Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);

        // TODO - Agregar validación aqui de que si la dimensión puede ser un vector de un solo valor.
        if (rdim.getTipoDato() != ETipoDato.INT) {
            msj = "Error. Se espera que el valor propocionado como posición en la dimension 1 sea de tipo INTEGER.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }

        int firstPos = (int)rdim.getValor();

        if (firstPos <= 0) {
            msj = "Error. El indice proporcionado debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }

        /*
         * Una vez validado lo anterior, se procede a verificar si la lista de dimensiones
         * es de una sola dimensión o de varias ya que el procedimiento es distinto.
        */

        if (listaDims.size() == 1) {
            actualPos = firstPos;
        } else {

            /*
             * Verifico si el acceso al vector posee más de una dimension. Si eso se cumple,
             * se tiene que validar que las siguientes dimensiones proporcionen un entero como
             * valor de posicion y este tiene que ser exactamente igual que el primero, de lo
             * contrario se debe reportar error.  Éste multiple acceso simula la recursión interna
             * de la relación entre vectores y tipos primitivos (que también se consideran vectores).
             *
             * Una vez finalizado el ciclo y si todas las dimensiones se validaron correctamente
             * significa que todas las posiciones son iguales, por lo que se procede a tomar la
             * última (actualPos) y se modifica su valor por la expresión proporcionada. Esto se
             * realiza más abajo.
             */

            Resultado r;
            for (int i = 1; i < listaDims.size(); i++) {
                r = ((Instruccion)listaDims.get(i).getValorDimIzq()).Ejecutar(ts);
                if (r.getTipoDato() != ETipoDato.INT) {
                    msj = "Error. Se espera que el valor propocionado como posición en la dimension "+ (i+1) +" sea de tipo INTEGER.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return false;
                } else {
                    actualPos = (int)r.getValor();
                    if (actualPos != firstPos) {
                        msj = "Error. No se puede acceder a una posición inexistente de un vector [Dimension = "+ (i+1) +" | Valor = "+ actualPos +"].";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                        return false;
                    }
                }
            }
        }

        /* Ejecuto la expresión para obtener su valor y validar que ésta sea de un tipo permitido. */
        Resultado rexp = ((Instruccion)valor).Ejecutar(ts);

        if (validateVectorExpression(rexp)) {
            /* Actualizo el valor del vector. */
            if (!((Vector)s.getValor()).updateVectorValue(actualPos, rexp.getTipoDato(), rexp.getValor())) {
                msj = "Error. No se pudo actualizar/castear los valores del vector.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return false;
            }
        } else {
            return false;
        }

        return true;

    }

    private boolean validarActualizarLista(TablaSimbolos ts, Simbolo s) {

        String msj;

        /*
         * Valido que el tipo de acceso que se esté realizando sea:
         * 1. SIMPLE []
         * 2. INNER [[]]
         * De lo contrario, se debe mostrar error.
         */

        Optional<Dimension> hasAnotherAccess = listaDims.stream().filter(d -> (d.getTipoDim() != ETipoDimension.SIMPLE && d.getTipoDim() != ETipoDimension.INNER)).findAny();

        if (hasAnotherAccess.isPresent()) {
            msj = "Error. No se puede acceder a una lista utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }

        /* Procedo a iterar sobre todas las dimensiones proporcionadas. */
        int cnt = 1;
        Resultado rdim;
        int posicion = 0;
        Object pivote = s.getValor();

        for (Dimension dim : listaDims) {

            rdim = ((Instruccion)dim.getValorDimIzq()).Ejecutar(ts);

            if (pivote instanceof Item) {
                pivote = ((Item)pivote).getValor();
            }

            /*
             * Una vez validado lo anterior, se procede a verificar que el valor proporcionado
             * como posición de la dimensión sea de tipo entero y que sea mayor a 0.
             */

            // TODO - Agregar validación aqui de que si la dimensión puede ser un vector de un solo valor.
            if (rdim.getTipoDato() != ETipoDato.INT) {
                msj = "Error. Se espera que el valor propocionado como posición en la dimension #"+ cnt +" sea de tipo INTEGER.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return false;
            }

            posicion = (int)rdim.getValor();

            if (posicion <= 0) {
                msj = "Error. El indice proporcionado en la dimensión #"+ cnt +" debe ser mayor o igual a 1.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return false;
            }

            if (pivote instanceof Lista) {
                Lista l = ((Lista) pivote);
                pivote = (dim.getTipoDim() == ETipoDimension.SIMPLE) ? l.getElementoTipo1ParaAsignacion(posicion) : l.getElementoTipo2ParaAsignacion(posicion);
            } else if (pivote instanceof Vector) {
                if (dim.getTipoDim() != ETipoDimension.SIMPLE) {
                    msj = "Error. La dimension #"+ (cnt-1) +" devuelve un vector y a este no se le puede hacer un acceso de tipo [[]].";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return false;
                }
                Vector v = ((Vector)pivote);
                pivote = v.getValueParaAsignacion(posicion);
            }

            cnt++;

        }

        /* Ejecuto la expresión para obtener su valor y validar que ésta sea de un tipo permitido. */
        Resultado rexp = ((Instruccion)valor).Ejecutar(ts);

        if (pivote instanceof Lista) {
            if (validateListExpression(rexp)) {
                ((Lista)pivote).updateListValue(posicion, rexp.getTipoDato(), rexp.getValor());
            } else {
                return false;
            }
        } else if (pivote instanceof Vector){
            if (validateVectorExpression(rexp)) {
                ((Vector)pivote).updateVectorValue(posicion, rexp.getTipoDato(), rexp.getValor());
            } else {
                return false;
            }
        } else if (pivote instanceof Item) {

            Item i = ((Item)pivote);

            if (i.getTipo() == ETipoDato.LIST) {
                if (!validateListExpression(rexp)) {
                    return false;
                }
            } else if (i.getTipo() == ETipoDato.VECTOR) {
                if (!validateVectorExpression(rexp)) {
                    return false;
                }
            }

            if (rexp.getTipoDato() != ETipoDato.LIST && rexp.getTipoDato() != ETipoDato.VECTOR) {
                i.setTipo(ETipoDato.VECTOR);
                i.setValor(new Vector(rexp.getTipoDato(), rexp.getValor()));
            } else {
                i.setTipo(rexp.getTipoDato());
                i.setValor(rexp.getValor());
            }

        } else {
            System.err.println("Error, pivote no reconocido.");
            return false;
        }

        return true;

    }

    private boolean validateListExpression(Resultado rexp) {
        String msj;
        if (rexp.getTipoDato() == ETipoDato.ARRAY || rexp.getTipoDato() == ETipoDato.MATRIX) {
            msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de una lista.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }
        if (rexp.getTipoDato() == ETipoDato.LIST) {
            Lista l = ((Lista)rexp.getValor());
            if (l.getListSize() > 1) {
                msj = "Error. No se puede asignar una lista con más de 1 parámetro a otra lista.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return false;
            }
        } else if (rexp.getTipoDato() == ETipoDato.VECTOR) {
            Vector v = ((Vector)rexp.getValor());
            if (v.getVectorSize() > 1) {
                msj = "Error. No se puede asignar un vector con más de 1 parámetro a una lista.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return false;
            }
        }
        return true;
    }

    private boolean validateVectorExpression(Resultado rexp) {
        if (rexp.getTipoDato() == ETipoDato.LIST || rexp.getTipoDato() == ETipoDato.ARRAY || rexp.getTipoDato() == ETipoDato.MATRIX) {
            String msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de un vector.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return false;
        }
        return true;
    }

    private Resultado validateMatrixExpression(Resultado rexp) {
        String msj;
        if (rexp.getTipoDato() == ETipoDato.LIST || rexp.getTipoDato() == ETipoDato.ARRAY || rexp.getTipoDato() == ETipoDato.MATRIX) {
            msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de una matriz.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return null;
        } else if (rexp.getTipoDato() == ETipoDato.VECTOR) {
            Vector v = (Vector)rexp.getValor();
            if (v.getVectorSize() > 1) {
                msj = "Error. No se puede asignar un vector con más de un valor a una posición de una matriz.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return null;
            }
            rexp.setTipoDato(v.getInnerType());
            rexp.setValor(v.getElementByPosition(0).getValor());
        }
        return rexp;
    }

    private Resultado validateMatrixExpressionForRowOrCols(String type, int size, Resultado rexp) {
        String msj;
        if (rexp.getTipoDato() == ETipoDato.LIST || rexp.getTipoDato() == ETipoDato.ARRAY || rexp.getTipoDato() == ETipoDato.MATRIX) {
            msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una "+ type +" de una matriz.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return null;
        } else if (rexp.getTipoDato() == ETipoDato.VECTOR) {
            Vector v = (Vector)rexp.getValor();
            if (v.getVectorSize() == 1) {
                rexp.setTipoDato(v.getInnerType());
                rexp.setValor(v.getElementByPosition(0).getValor());
            } else {
                if (v.getVectorSize() != size) {
                    msj = "Error. El vector que se desea asignar a la "+ type +" no coincide en tamaño con la cantidad de "+ type +"s de la matriz.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                    return null;
                }
            }
        }
        return rexp;
    }

    private int validatePositionValueOfDimension(Resultado rdim) {

        String msj;
        ETipoDato tdim = rdim.getTipoDato();

        if (tdim != ETipoDato.VECTOR && tdim != ETipoDato.INT) {
            msj = "Error. Se espera que el valor propocionado como posición en la dimension sea de tipo INTEGER. Tipo recibido: <"+ tdim +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return -1;
        }

        int posDim = 0;

        if (tdim == ETipoDato.VECTOR) {
            Vector v = (Vector)rdim.getValor();
            if (v.getVectorSize() > 1 || v.getInnerType() != ETipoDato.INT) {
                msj = "Error. No se puede utilizar como posición un vector con más de 1 valor y/o que el vector no sea de tipo INTEGER.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
                return -1;
            }
            posDim = (int)v.getElementByPosition(0).getValor();
        } else {
            posDim = (int)rdim.getValor();
        }

        if (posDim == 0) {
            msj = "Error. El indice proporcionado debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_EDD]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

}
