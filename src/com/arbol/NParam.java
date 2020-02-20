package com.arbol;

import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.abstracto.Instruccion;
import com.constantes.ETipoNodo;
import com.entorno.Ambito;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NParam extends Nodo {

    private String id;
    private Nodo valor;

    public NParam(int linea, int columna, String archivo, String id) {
        super(linea, columna, archivo, ETipoNodo.STMT_PARAM);
        this.id = id;
        this.valor = null;
    }

    public NParam(int linea, int columna, String archivo, String id, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.STMT_PARAM);
        this.id = id;
        this.valor = valor;
    }

    public Nodo getValor() {
        return valor;
    }

    public boolean registrar(TablaSimbolos ts, Ambito amb, Resultado paramVal) {

        String msj;

        if (paramVal == null) {
            paramVal = ((Instruccion)valor).Ejecutar(ts);
        }

        Simbolo s = new Simbolo(paramVal.getTipoDato(), id, paramVal.getValor());

        if (!amb.addSimbolo(s)) {
            msj = "Error. Ya existe la variable <"+ id +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PARAM]", msj, getLinea(), getColumna());
            return false;
        }

        return true;

    }

}
