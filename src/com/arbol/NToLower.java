package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NToLower extends Nodo implements Instruccion {

    private Nodo valor;

    public NToLower(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_TO_LOWER);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
        Resultado rc = ((Instruccion)valor).Ejecutar(ts);

        switch (rc.getTipoDato()) {
            case STRING: {
                return new Resultado(ETipoDato.STRING, EFlujo.NORMAL, ((String)rc.getValor()).toLowerCase());
            }
            case VECTOR: {
                Vector v = (Vector)rc.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. No se puede realizar la función TO_LOWER_CASE a un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_TO_LOWER]", msj, getLinea(), getColumna());
                    return error;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede realizar la función TO_LOWER_CASE a un vector con más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_TO_LOWER]", msj, getLinea(), getColumna());
                    return error;
                }
                return new Resultado(ETipoDato.STRING, EFlujo.NORMAL, ((String)v.getElementByPosition(0).getValor()).toLowerCase());
            }
            default: {
                msj = "Error. No se puede realizar la función TO_LOWER_CASE a una expresión de tipo <"+ rc.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_TO_LOWER]", msj, getLinea(), getColumna());
                return error;
            }
        }

    }
}
