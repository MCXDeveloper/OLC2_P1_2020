package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NStringLength extends Nodo implements Instruccion {

    private Nodo valor;

    public NStringLength(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_STRING_LENGTH);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
        Resultado rc = ((Instruccion)valor).Ejecutar(ts);

        switch (rc.getTipoDato()) {
            case STRING: {
                return new Resultado(ETipoDato.INT, EFlujo.NORMAL, ((String)rc.getValor()).length());
            }
            case VECTOR: {
                Vector v = (Vector)rc.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. No se puede realizar la función STRING_LENGTH a un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_STRING_LENGTH]", msj, getLinea(), getColumna());
                    return error;
                }
                return new Resultado(ETipoDato.INT, EFlujo.NORMAL, ((String)v.getElementByPosition(0).getValor()).length());
            }
            default: {
                msj = "Error. No se puede realizar la función STRING_LENGTH a una expresión de tipo <"+ rc.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_STRING_LENGTH]", msj, getLinea(), getColumna());
                return error;
            }
        }

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

}
