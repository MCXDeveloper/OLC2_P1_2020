package com.arbol;

import com.abstracto.Fail;
import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NModulo extends Nodo implements Instruccion {

    private Nodo opIzq;
    private Nodo opDer;

    public NModulo(int linea, int columna, String archivo, Nodo opIzq, Nodo opDer) {
        super(linea, columna, archivo, ETipoNodo.EXP_MODULO);
        this.opIzq = opIzq;
        this.opDer = opDer;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (opIzq != null && opDer != null) {

            Resultado v1, v2;
            v1 = ((Instruccion) opIzq).Ejecutar(ts);
            v2 = ((Instruccion) opDer).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.INT;
                valor = ((int)v1.getValor()) % ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = ((int)v1.getValor()) % ((double)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.DECIMAL;
                valor = ((double)v1.getValor()) % ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = ((double) v1.getValor()) % ((double) v2.getValor());
            } else {
                msj = "Error. No hay implementación para la operación MODULO para los tipos <"+ v1.getTipoDato() +"> y <"+ v2.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MODULO]", msj, getLinea(), getColumna());
            }

        }else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MODULO]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
