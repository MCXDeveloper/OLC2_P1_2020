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

public class NSwitch extends Nodo implements Instruccion {

    private Nodo condicion;
    private LinkedList<NCase> lista_casos;
    private LinkedList<Nodo> sentencias_default;

    public NSwitch(int linea, int columna, String archivo, Nodo condicion, LinkedList<NCase> lista_casos) {
        super(linea, columna, archivo, ETipoNodo.STMT_SWITCH);
        this.condicion = condicion;
        this.lista_casos = lista_casos;
        this.sentencias_default = null;
    }

    public NSwitch(int linea, int columna, String archivo, Nodo condicion, LinkedList<NCase> lista_casos, LinkedList<Nodo> sentencias_default) {
        super(linea, columna, archivo, ETipoNodo.STMT_SWITCH);
        this.condicion = condicion;
        this.lista_casos = lista_casos;
        this.sentencias_default = sentencias_default;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        boolean caseFlag = false;
        boolean breakFlag = false;
        Resultado rexp = ((Instruccion)condicion).Ejecutar(ts);

        for (NCase caso : lista_casos) {

            Resultado rcasexp = ((Instruccion)caso.getCondicion()).Ejecutar(ts);

            if (!caseFlag) {
                if (rcasexp.getValor().equals(rexp.getValor())) {
                    Resultado rcase = caso.Ejecutar(ts);
                    caseFlag = true;
                    if (rcase.getFlujo() == EFlujo.BREAK) {
                        breakFlag = true;
                        break;
                    } else if (rcase.getFlujo() == EFlujo.RETURN) {
                        return rcase;
                    }
                }
            } else {
                Resultado rcase = caso.Ejecutar(ts);
                if (rcase.getFlujo() == EFlujo.BREAK) {
                    breakFlag = true;
                    break;
                } else if (rcase.getFlujo() == EFlujo.RETURN) {
                    return rcase;
                }
            }

        }

        if (!breakFlag) {
            return EjecutarDefault(ts);
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);
        
    }

    private Resultado EjecutarDefault(TablaSimbolos ts) {

        if (sentencias_default != null) {

            Resultado rexp;
            ts.addAmbito(EAmbito.DEFAULT);

            for(Nodo nd: sentencias_default){
                if(nd.getTipoNodo() == ETipoNodo.ERROR)
                    continue;

                rexp = ((Instruccion)nd).Ejecutar(ts);
                if(rexp.getFlujo() == EFlujo.RETURN || rexp.getFlujo() == EFlujo.BREAK) {
                    ts.destruirAmbito();
                    return rexp;
                }
            }

            ts.destruirAmbito();

        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);

    }

}
