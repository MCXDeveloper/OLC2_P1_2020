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

public class NId extends Nodo implements Instruccion {

    private String id;

    public NId(int linea, int columna, String archivo, String id) {
        super(linea, columna, archivo, ETipoNodo.EXP_ID);
        this.id = id;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rval = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        Simbolo s = ts.getSimbolo(id, false);

        if (s != null) {
            tdr = s.getTipo();
            rval = s.getValor();
        } else {
            msj = "Error. No se encontró la variable <"+ id +">";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ID]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, rval);

    }

    public String getId() {
        return id;
    }
}
