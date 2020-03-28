package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;

public class NPrim extends Nodo implements Instruccion {

    private final Object valor;
    private final ETipoDato tipoDato;

    public NPrim(int linea, int columna, String archivo, Object valor, ETipoDato tipoDato) {
        super(linea, columna, archivo, ETipoNodo.EXP_PRIMITIVE);
        this.valor = valor;
        this.tipoDato = tipoDato;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado r;

        switch (tipoDato) {
            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {
                r = new Resultado(tipoDato, EFlujo.NORMAL, valor);
            }   break;
            default: {
                r = new Resultado(ETipoDato.STRING, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));
            }   break;

        }

        return r;

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("EXPRESION");
        String subson = ts.getDeclararNodo("NODO_PRIMITIVO");
        String son = ts.getDeclararNodo(valor.toString());
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, son);
        return parent;
    }

}
