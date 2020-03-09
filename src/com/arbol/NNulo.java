package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;

public class NNulo extends Nodo implements Instruccion {

    public NNulo(int linea, int columna, String archivo) {
        super(linea, columna, archivo, ETipoNodo.EXP_NT);
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {
        return new Resultado(ETipoDato.NT, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));
    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("EXPRESION");
        String subson = ts.getDeclararNodo("NODO_NULL");
        String son = ts.getDeclararNodo("NULL");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, son);
        return parent;
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
