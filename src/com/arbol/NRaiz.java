package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.main.Main;

import java.util.LinkedList;

public class NRaiz extends Nodo implements Instruccion {

    private LinkedList<Nodo> lista_sentencias;

    public NRaiz(int linea, int columna, String archivo, LinkedList<Nodo> lista_sentencias) {
        super(linea, columna, archivo, ETipoNodo.ROOT);
        this.lista_sentencias = lista_sentencias;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        /* Registro todas las funciones. */
        RegistrarFunciones(ts);

        /* Ejecuto todas las demás instrucciones. */
        Resultado r;
        for (Nodo nodito : lista_sentencias) {
            if (nodito.getTipoNodo() != ETipoNodo.STMT_FUNC) {
                r = ((Instruccion)nodito).Ejecutar(ts);
                if (r.getTipoDato() == ETipoDato.ERROR) {
                    return r;
                }
            }
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);

    }

    private void RegistrarFunciones(TablaSimbolos ts) {

        NFunc func;

        if (lista_sentencias.stream().noneMatch(x -> x.getTipoNodo().equals(ETipoNodo.ERROR))) {
            for (Nodo nodito : lista_sentencias) {
                if (nodito.getTipoNodo().equals(ETipoNodo.STMT_FUNC)) {
                    func = (NFunc) nodito;
                    if (!ts.addMetodo(func)) {
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_RAIZ]", "Error. Ya se registro la función {"+ func.getId() +"}", getLinea(), getColumna());
                    }
                }
            }
        } else {
            Main.getGUI().showMessage("Se detectaron errores en la lista de sentencias.  Revisar pestaña de errores.");
        }

    }
}
