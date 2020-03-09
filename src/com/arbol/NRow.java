package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NRow extends Nodo implements Instruccion {

    private Nodo valor;

    public NRow(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_NROW);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado rc = ((Instruccion)valor).Ejecutar(ts);

        if (rc.getTipoDato() == ETipoDato.MATRIX) {
            return new Resultado(ETipoDato.INT, EFlujo.NORMAL, ((Matriz)rc.getValor()).getFilas());
        } else {
            String msj = "Error. No se puede realizar la función NROW a una expresión de tipo <"+ rc.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ROW]", msj, getLinea(), getColumna());
            return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
        }

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

}
