package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NContinue extends Nodo implements Instruccion {

    public NContinue(int linea, int columna, String archivo) {
        super(linea, columna, archivo, ETipoNodo.STMT_CONTINUE);
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        ETipoDato rtd = ETipoDato.ERROR;

        if(!ts.enCiclo()){
            String msj = "Error. No se puede utilizar CONTINUE fuera de un ciclo.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CONTINUE]", msj, getLinea(), getColumna());
        } else {
            rtd = ETipoDato.NT;
        }

        return new Resultado(rtd, EFlujo.CONTINUE);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("NODO_CONTINUE");
        String tokenret = ts.getDeclararNodo("continue");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokenret);
        return parent;
    }

}
