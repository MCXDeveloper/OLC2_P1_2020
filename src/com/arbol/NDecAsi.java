package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NDecAsi extends Nodo implements Instruccion {

    private String id;
    private Nodo valor;

    public NDecAsi(int linea, int columna, String archivo, String id, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_DECASI);
        this.id = id;
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rval = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (valor != null) {

            tdr = ETipoDato.NT;
            rval = new NNulo(getLinea(), getColumna(), getArchivo());

            Simbolo s = ts.getSimbolo(id);
            Resultado r = ((Instruccion)valor).Ejecutar(ts);

            if (s != null) {
                ts.updateSimbolo(new Simbolo(r.getTipoDato(), id, r.getValor()));
            } else {
                ts.addSimbolo(new Simbolo(r.getTipoDato(), id, r.getValor()));
            }

        } else {
            msj = "Error. La expresión recibida es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DECASI]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, rval);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("SENTENCIA_DECASI");
        ts.enlazarNodos(parent, subson);
        String tokenid = ts.getDeclararNodo(id);
        ts.enlazarNodos(subson, tokenid);
        String tokenigual = ts.getDeclararNodo(" = ");
        ts.enlazarNodos(subson, tokenigual);
        String tokenval = ((Instruccion)valor).GenerarDOT(ts);
        ts.enlazarNodos(subson, tokenval);
        return parent;
    }

}
