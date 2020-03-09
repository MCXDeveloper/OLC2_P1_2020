package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;

public class NReturn extends Nodo implements Instruccion {

    private Nodo valor;

    public NReturn(int linea, int columna, String archivo) {
        super(linea, columna, archivo, ETipoNodo.STMT_RETURN);
        this.valor = null;
    }

    public NReturn(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_RETURN);
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Simbolo s = ts.getSimbolo("return", false);

        if (valor != null) {
            Resultado r = ((Instruccion)valor).Ejecutar(ts);
            s.setTipo(r.getTipoDato());
            s.setValor(r.getValor());
        }

        return new Resultado(s.getTipo(), EFlujo.RETURN, s.getValor());

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("NODO_RETURN");
        String tokenret = ts.getDeclararNodo("return");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokenret);
        if (valor != null) {
            String tokenval = ((Instruccion)valor).GenerarDOT(ts);
            ts.enlazarNodos(subson, tokenval);
        }
        return parent;
    }

}
