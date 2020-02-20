package com.arbol;

import com.abstracto.Fail;
import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.Ambito;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NCall extends Nodo implements Instruccion {

    private String id;
    private LinkedList<Nodo> params;

    public NCall(int linea, int columna, String archivo, String id, LinkedList<Nodo> params) {
        super(linea, columna, archivo, ETipoNodo.STMT_CALL);
        this.id = id;
        this.params = params;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato rtd = ETipoDato.ERROR;

        NFunc funcion = ts.getMetodo(id);

        if (funcion == null) {
            msj = "Error. No se encontró la función <"+ id +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
        } else {
            Ambito amb = new Ambito(EAmbito.FUNCION);
            Simbolo s = new Simbolo(ETipoDato.NT, "return", new NNulo(getLinea(), getColumna(), getArchivo()));
            amb.addSimbolo(s);
            if (!registrarParametros(amb, funcion.getParametros(), ts)) {
                msj = "Error.  No se pudieron registrar los parametros para la función <"+ id +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
            } else {
                ts.nuevaLLamada(amb);
                Resultado r = funcion.Ejecutar(ts);
                ts.finLLamada();
                return r;
            }
        }

        return new Resultado(rtd, EFlujo.NORMAL, rvalor);

    }

    private boolean registrarParametros(Ambito amb, LinkedList<NParam> parametros_funcion, TablaSimbolos ts) {

        String msj;

        if (parametros_funcion.size() != params.size()) {
            msj = "Error. El número de parámetros indicados no coinciden con los declarados en la función.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
            return false;
        }

        Nodo nodito;
        NParam parametro;
        Resultado paramVal;

        for (int i = 0; i < params.size(); i++) {
            nodito = params.get(i);
            parametro = parametros_funcion.get(i);
            paramVal = (nodito.getTipoNodo() != ETipoNodo.EXP_DEFAULT) ? ((Instruccion)nodito).Ejecutar(ts) : null;
            if (!parametro.registrar(ts, amb, paramVal)) {
                return false;
            }
        }

        return true;

    }

}
