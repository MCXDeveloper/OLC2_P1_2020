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
import java.util.Optional;

public class NAsiArr extends Nodo implements Instruccion {

    private String id;
    private Nodo valor;
    private LinkedList<Dimension> listaDims;

    public NAsiArr(int linea, int columna, String archivo, String id, LinkedList<Dimension> listaDims, Nodo valor) {
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
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
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
                    if (validarActualizarLista()) {
                        tdr = ETipoDato.NT;
                        rvalor = new NNulo(getLinea(), getColumna(), getArchivo());
                    }
                }   break;

                // TODO - Pendiente hacer los accesos para matrices y arreglos.
                default: {
                    msj = "Error. No se puede acceder con dimensiones a una variable que no sea de tipo estructura.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
                }

            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

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
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
            return false;
        }

        /*
         * Una vez validado lo anterior, se procede a verificar que el valor proporcionado
         * como posición de la primera dimensión sea de tipo entero y que sea mayor a 0.
        */

        int actualPos = 0;
        Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);

        if (rdim.getTipoDato() != ETipoDato.INT) {
            msj = "Error. Se espera que el valor propocionado como posición en la dimension 1 sea de tipo INTEGER.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
            return false;
        }

        int firstPos = (int)rdim.getValor();

        if (firstPos <= 0) {
            msj = "Error. El indice proporcionado debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
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
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
                    return false;
                } else {
                    actualPos = (int)r.getValor();
                    if (actualPos != firstPos) {
                        msj = "Error. No se puede acceder a una posición inexistente de un vector [Dimension = "+ (i+1) +" | Valor = "+ actualPos +"].";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
                        return false;
                    }
                }
            }
        }

        /* Ejecuto la expresión para obtener su valor y validar que ésta sea de un tipo permitido. */

        Resultado rexp = ((Instruccion)valor).Ejecutar(ts);

        if (rexp.getTipoDato() == ETipoDato.ARRAY || rexp.getTipoDato() == ETipoDato.MATRIX) {
            msj = "Error. No se puede asignar un valor de tipo <"+ rexp.getTipoDato() +"> a una posición de un vector.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
            return false;
        }

        /* Actualizo el valor del arreglo. */
        if (!((Vector)s.getValor()).updateVectorValue(actualPos, rexp.getTipoDato(), rexp.getValor())) {
            msj = "Error. No se pudo actualizar/castear los valores del vector.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASI_ARR]", msj, getLinea(), getColumna());
            return false;
        }

        return true;

    }

    private boolean validarActualizarLista() {
        return true;
    }

}
