package com.arbol;

import com.abstracto.*;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NDoWhile extends Nodo implements Instruccion {

    private Nodo condicion;
    private LinkedList<Nodo> sentencias;

    public NDoWhile(int linea, int columna, String archivo, LinkedList<Nodo> sentencias, Nodo condicion) {
        super(linea, columna, archivo, ETipoNodo.STMT_DO_WHILE);
        this.condicion = condicion;
        this.sentencias = sentencias;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        boolean cond;
        Resultado rexp, ev = null;

        do{
            ts.addAmbito(EAmbito.CICLO);
            for( Nodo nd : sentencias){
                ev = ((Instruccion)nd).Ejecutar(ts);
                if(ev.getFlujo() != EFlujo.NORMAL){
                    break;
                }
            }
            ts.destruirAmbito();
            if(ev != null && ev.getFlujo() != EFlujo.NORMAL){
                break;
            }
            rexp = ((Instruccion)condicion).Ejecutar(ts);

            switch (rexp.getTipoDato()) {
                case BOOLEAN: {
                    cond = (boolean)rexp.getValor();
                }   break;
                case VECTOR: {
                    Vector v = (Vector)rexp.getValor();
                    if (v.getInnerType() != ETipoDato.BOOLEAN) {
                        msj = "Error. La condición recibida es de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un valor booleano.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DO_WHILE]", msj, getLinea(), getColumna());
                        return null;
                    }
                    cond = (boolean)v.getElementByPosition(0).getValor();
                }   break;
                default: {
                    msj = "Error. La condición recibida es de tipo <"+ rexp.getTipoDato() +">. Se espera que sea una expresión booleana o un vector de booleanos.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DO_WHILE]", msj, getLinea(), getColumna());
                    return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                }
            }

        } while(cond);

        if(ev != null && ev.getFlujo() == EFlujo.RETURN){
            return ev;
        }else{
            return new Resultado(ETipoDato.NT, EFlujo.NORMAL);
        }

    }
}