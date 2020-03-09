package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;

import java.util.LinkedList;

public class NCase extends Nodo implements Instruccion {

    private Nodo condicion;
    private LinkedList<Nodo> sentencias;

    public NCase(int linea, int columna, String archivo, Nodo condicion, LinkedList<Nodo> sentencias) {
        super(linea, columna, archivo, ETipoNodo.STMT_CASE);
        this.condicion = condicion;
        this.sentencias = sentencias;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado rexp;
        ts.addAmbito(EAmbito.CASE);

        for(Nodo nd: sentencias){
            if(nd.getTipoNodo() == ETipoNodo.ERROR)
                continue;

            rexp = ((Instruccion)nd).Ejecutar(ts);
            if(rexp.getFlujo() == EFlujo.RETURN || rexp.getFlujo() == EFlujo.BREAK) {
                ts.destruirAmbito();
                return rexp;
            }
        }

        ts.destruirAmbito();

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("NODO_CASE");
        String tokencase = ts.getDeclararNodo("case");
        String tokenval = ((Instruccion)condicion).GenerarDOT(ts);
        String listason = ts.getDeclararNodo("LISTA_INSTRUCCIONES");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokencase);
        ts.enlazarNodos(subson, tokenval);
        ts.enlazarNodos(subson, listason);
        for (Nodo nodito : sentencias) {
            son = ((Instruccion)nodito).GenerarDOT(ts);
            ts.enlazarNodos(listason, son);
        }
        return parent;
    }

    public Nodo getCondicion() {
        return condicion;
    }
}
