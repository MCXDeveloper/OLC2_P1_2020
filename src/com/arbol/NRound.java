package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.math.BigDecimal;

public class NRound extends Nodo implements Instruccion {

    private Nodo valor;

    public NRound(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_ROUND);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        Resultado rc = ((Instruccion)valor).Ejecutar(ts);

        switch (rc.getTipoDato()) {
            case DECIMAL: {
                return new Resultado(ETipoDato.INT, EFlujo.NORMAL, ((int)Math.round(((double)rc.getValor()))));
            }
            case VECTOR: {
                Vector v = (Vector)rc.getValor();
                if (v.getInnerType() != ETipoDato.DECIMAL) {
                    msj = "Error. No se puede realizar la función ROUND a un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ROUND]", msj, getLinea(), getColumna());
                    return error;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede realizar la función ROUND a un vector con más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ROUND]", msj, getLinea(), getColumna());
                    return error;
                }
                double d = ((double)v.getElementByPosition(0).getValor());
                return new Resultado(ETipoDato.INT, EFlujo.NORMAL, ((int)Math.round(d)));
            }
            default: {
                msj = "Error. No se puede realizar la función ROUND a una expresión de tipo <"+ rc.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ROUND]", msj, getLinea(), getColumna());
                return error;
            }
        }

    }

}
