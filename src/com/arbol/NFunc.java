package com.arbol;

import com.abstracto.Fail;
import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NFunc extends Nodo implements Instruccion {

    private String id;
    private LinkedList<Nodo> lstmts;
    private LinkedList<NParam> parametros;

    public NFunc(int linea, int columna, String archivo, String id, Nodo param, LinkedList<Nodo> lstmts) {
        super(linea, columna, archivo, ETipoNodo.STMT_FUNC);
        this.id = id;
        this.lstmts = lstmts;
        this.parametros = new LinkedList<NParam>();

        if (param.getTipoNodo() != ETipoNodo.EXP_ID) {
            String msj = "Error. Se espera que un parametro sea un identificador.";
            ErrorHandler.AddError("Sintáctico", getArchivo(), "[N_FUNC]", msj, getLinea(), getColumna());
        } else {
            this.parametros.add(new NParam(getLinea(), getColumna(), getArchivo(), ((NId)param).getId()));
        }

    }

    public NFunc(int linea, int columna, String archivo, String id, LinkedList<NParam> parametros, LinkedList<Nodo> lstmts) {
        super(linea, columna, archivo, ETipoNodo.STMT_FUNC);
        this.id = id;
        this.lstmts = lstmts;
        this.parametros = parametros;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado r;
        Simbolo ret = ts.getSimbolo("return", false);

        for (Nodo nd : lstmts) {
            try {
                if (nd.getTipoNodo() == ETipoNodo.ERROR)
                    continue;
                r = ((Instruccion)nd).Ejecutar(ts);
                if (r.getFlujo() == EFlujo.RETURN)
                    break;
            } catch (Exception ignored) { }
        }

        return new Resultado(ret.getTipo(), EFlujo.NORMAL, ret.getValor());

    }

    public String getId() {
        return id;
    }

    public LinkedList<NParam> getParametros() {
        return parametros;
    }

}