package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NRemove extends Nodo implements Instruccion {

    private Nodo valorPivote;
    private Nodo valorQuitar;

    public NRemove(int linea, int columna, String archivo, Nodo valorPivote, Nodo valorQuitar) {
        super(linea, columna, archivo, ETipoNodo.EXP_REMOVE);
        this.valorPivote = valorPivote;
        this.valorQuitar = valorQuitar;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        String op1, op2;
        Resultado rcpivote = ((Instruccion)valorPivote).Ejecutar(ts);
        Resultado rcquitar = ((Instruccion)valorQuitar).Ejecutar(ts);

        switch (rcpivote.getTipoDato()) {
            case STRING: {
                op1 = (String)rcpivote.getValor();
            }   break;
            case VECTOR: {
                Vector v = (Vector)rcpivote.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. No se puede realizar la función REMOVE a un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_REMOVE]", msj, getLinea(), getColumna());
                    return error;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede realizar la función REMOVE a un vector con más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_REMOVE]", msj, getLinea(), getColumna());
                    return error;
                }
                op1 = (String)v.getElementByPosition(0).getValor();
            }   break;
            default: {
                msj = "Error. No se puede realizar la función REMOVE ya que el primer parámetro es de tipo <"+ rcpivote.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_REMOVE]", msj, getLinea(), getColumna());
                return error;
            }
        }

        switch (rcquitar.getTipoDato()) {
            case STRING: {
                op2 = (String)rcquitar.getValor();
            }   break;
            case VECTOR: {
                Vector v = (Vector)rcquitar.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. No se puede realizar la función REMOVE a un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_REMOVE]", msj, getLinea(), getColumna());
                    return error;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede realizar la función REMOVE a un vector con más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_REMOVE]", msj, getLinea(), getColumna());
                    return error;
                }
                op2 = (String)v.getElementByPosition(0).getValor();
            }   break;
            default: {
                msj = "Error. No se puede realizar la función REMOVE ya que el segundo parámetro es de tipo <"+ rcpivote.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_REMOVE]", msj, getLinea(), getColumna());
                return error;
            }
        }

        return new Resultado(ETipoDato.STRING, EFlujo.NORMAL, op1.replace(op2,""));
    }

}
