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

public class NNegativo extends Nodo implements Instruccion {

    private Nodo opIzq;

    public NNegativo(int linea, int columna, String archivo, Nodo opIzq) {
        super(linea, columna, archivo, ETipoNodo.EXP_NEGATIVO);
        this.opIzq = opIzq;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (opIzq != null) {

            Resultado v1;
            v1 = ((Instruccion) opIzq).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.INT;
                valor = ((int)v1.getValor()) * -1;
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = ((double)v1.getValor()) * -1;
            } else {
                msj = "Error. No hay implementación para la operación NEGATIVO para los tipos <"+ v1.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NEGATIVO]", msj, getLinea(), getColumna());
            }

        }else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NEGATIVO]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
