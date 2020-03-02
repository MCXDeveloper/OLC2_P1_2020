package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.LinkedList;

public class NMatrix extends Nodo implements Instruccion {

    private Nodo nrow;
    private Nodo ncolumn;
    private Nodo valores;

    public NMatrix(int linea, int columna, String archivo, Nodo valores, Nodo nrow, Nodo ncolumn) {
        super(linea, columna, archivo, ETipoNodo.EXP_MATRIX);
        this.nrow = nrow;
        this.valores = valores;
        this.ncolumn = ncolumn;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato rtd = ETipoDato.ERROR;

        /*
         * Para crear una matriz se tienen que realizar las siguientes validaciones:
         * 1.   Tanto los nodos 'nrow' como 'ncolumn' al ejecutarlos ambos deben de retornar
         *      un valor de tipo INT.  Se debe validar que si se recibe un vector, éste tiene que
         *      ser de tamaño 1 y el valor interno debe ser un INT.
         * 2.   El nodo 'valores' solo puede ser de tipo PRIMITIVO o VECTOR.  Si es algún
         *      otro se debe reportar error.
         */

        Resultado rrow = ((Instruccion)nrow).Ejecutar(ts);
        if (validateRow(rrow)) {
            Resultado rcol = ((Instruccion)ncolumn).Ejecutar(ts);
            if (validateColumn(rcol)) {
                Resultado rvalores = ((Instruccion)valores).Ejecutar(ts);
                if (validateValues(rvalores)) {

                    int filas = (int)rrow.getValor();
                    int columnas = (int)rcol.getValor();
                    LinkedList<Item> li = new LinkedList<>();

                    if (rvalores.getTipoDato() == ETipoDato.VECTOR) {

                        Item it;
                        int cnt = 0;
                        Vector v = ((Vector)rvalores.getValor());

                        for (int i = 0; i < columnas; i++) {
                            for (int j = 0; j < filas; j++) {
                                it = v.getElementByPosition(cnt);
                                li.add(new Item(it.getTipo(), it.getValor()));
                                cnt++;
                                if (cnt == v.getVectorSize()) {
                                    cnt = 0;
                                }
                            }
                        }

                    } else {
                        for (int i = 0; i < columnas; i++) {
                            for (int j = 0; j < filas; j++) {
                                li.add(new Item(rvalores.getTipoDato(), rvalores.getValor()));
                            }
                        }
                    }

                    Matriz m = new Matriz(filas, columnas, li);
                    if (m.rehashing()) {
                        rtd = ETipoDato.MATRIX;
                        rvalor = m;
                    }

                }
            }
        }

        return new Resultado(rtd, EFlujo.NORMAL, rvalor);

    }

    private boolean validateValues(Resultado rval) {
        ETipoDato[] tiposNoPermitidos = new ETipoDato[]{ ETipoDato.LIST, ETipoDato.MATRIX, ETipoDato.ARRAY };
        if (Arrays.asList(tiposNoPermitidos).contains(rval.getTipoDato())) {
            String msj = "Error. Una matriz no puede alojar valores de tipo <"+ rval.getTipoDato() +">, únicamente valores primitivos.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
            return false;
        }
        return true;
    }

    private boolean validateRow(Resultado rrow) {

        String msj;

        if (rrow.getTipoDato() == ETipoDato.VECTOR) {
            Vector vrow = ((Vector)rrow.getValor());
            if (vrow.getInnerType() == ETipoDato.INT) {
                if (vrow.getVectorSize() > 1) {
                    msj = "Error. El valor del parámetro 'nrow' no puede ser un vector de enteros de más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
                    return false;
                }
            } else {
                msj = "Error. El valor del parámetro 'nrow' no puede ser de tipo <VECTOR["+ vrow.getInnerType() +"]>.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
                return false;
            }
        } else if (rrow.getTipoDato() != ETipoDato.INT) {
            msj = "Error. El valor del parámetro 'nrow' no puede ser de tipo <"+ rrow.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
            return false;
        }

        return true;

    }

    private boolean validateColumn(Resultado rcol) {

        String msj;

        if (rcol.getTipoDato() == ETipoDato.VECTOR) {
            Vector vcol = ((Vector)rcol.getValor());
            if (vcol.getInnerType() == ETipoDato.INT) {
                if (vcol.getVectorSize() > 1) {
                    msj = "Error. El valor del parámetro 'ncol' no puede ser un vector de enteros de más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
                    return false;
                }
            } else {
                msj = "Error. El valor del parámetro 'ncol' no puede ser de tipo <VECTOR["+ vcol.getInnerType() +"]>.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
                return false;
            }
        } else if (rcol.getTipoDato() != ETipoDato.INT) {
            msj = "Error. El valor del parámetro 'ncol' no puede ser de tipo <"+ rcol.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MATRIX]", msj, getLinea(), getColumna());
            return false;
        }

        return true;

    }

}
