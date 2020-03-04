package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NTypeOf extends Nodo implements Instruccion {

    private Nodo valor;

    public NTypeOf(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_TYPE_OF);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Object rval = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        Resultado rvalor = ((Instruccion)valor).Ejecutar(ts);

        switch (rvalor.getTipoDato()) {
            case INT:
            case LIST:
            case ARRAY:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {
                tdr = ETipoDato.STRING;
                rval = rvalor.getTipoDato().getTypeOf();
            }   break;
            case VECTOR: {
                tdr = ETipoDato.STRING;
                rval = ((Vector)rvalor.getValor()).getInnerType().getTypeOf();
            }   break;
            case MATRIX: {
                tdr = ETipoDato.STRING;
                rval = ((Matriz)rvalor.getValor()).getInnerType().getTypeOf();
            }   break;
            default: {
                String msj = "Error. No se puede realizar la función TYPE_OF a una expresión de tipo <"+ rvalor.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_TYPE_OF]", msj, getLinea(), getColumna());
            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rval);

    }
}
