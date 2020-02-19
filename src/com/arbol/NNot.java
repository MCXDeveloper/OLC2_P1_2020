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

public class NNot extends Nodo implements Instruccion {

    private Nodo op;

    public NNot(int linea, int columna, String archivo, Nodo op) {
        super(linea, columna, archivo, ETipoNodo.EXP_NOT);
        this.op = op;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (op != null) {

            Resultado v1;
            v1 = ((Instruccion)op).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.BOOLEAN) {
                tdr = ETipoDato.BOOLEAN;
                valor = !((boolean)v1.getValor());
            } else {
                msj = "Error. No hay implementación para la operación NOT para los tipos <"+ v1.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NOT]", msj, getLinea(), getColumna());
            }

        } else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NOT]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
