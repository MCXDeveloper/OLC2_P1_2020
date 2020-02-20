package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
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

        Simbolo s = ts.getSimbolo("return");

        if (valor != null) {
            Resultado r = ((Instruccion)valor).Ejecutar(ts);
            s.setTipo(r.getTipoDato());
            s.setValor(r.getValor());
        }

        return new Resultado(s.getTipo(), EFlujo.RETURN, s.getValor());

    }

}
