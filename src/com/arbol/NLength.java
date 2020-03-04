package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NLength extends Nodo implements Instruccion {

    private Nodo valor;

    public NLength(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_LENGTH);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Object rval = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        Resultado rc = ((Instruccion)valor).Ejecutar(ts);

        switch (rc.getTipoDato()) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {
                tdr = ETipoDato.INT;
                rval = 1;
            }   break;
            case VECTOR: {
                tdr = ETipoDato.INT;
                rval = ((Vector)rc.getValor()).getVectorSize();
            }   break;
            case LIST: {
                tdr = ETipoDato.INT;
                rval = ((Lista)rc.getValor()).getListSize();
            }   break;
            case MATRIX: {
                tdr = ETipoDato.INT;
                rval = ((Matriz)rc.getValor()).getMatrixSize();
            }   break;
            case ARRAY: {
                tdr = ETipoDato.INT;
                rval = ((Arreglo)rc.getValor()).getArregloSize();
            }   break;
            default: {
                String msj = "Error. No se puede realizar la función NLENGTH a una expresión de tipo <"+ rc.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_LENGTH]", msj, getLinea(), getColumna());
            }
        }

        return new Resultado(tdr, EFlujo.NORMAL, rval);

    }
}
