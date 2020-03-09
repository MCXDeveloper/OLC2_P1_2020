package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NTernario extends Nodo implements Instruccion {

    private Nodo condicion;
    private Nodo valorFalso;
    private Nodo valorVerdadero;

    public NTernario(int linea, int columna, String archivo, Nodo condicion, Nodo valorVerdadero, Nodo valorFalso) {
        super(linea, columna, archivo, ETipoNodo.EXP_TERNARIO);
        this.condicion = condicion;
        this.valorFalso = valorFalso;
        this.valorVerdadero = valorVerdadero;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;

        boolean cond;
        Resultado rc = ((Instruccion)condicion).Ejecutar(ts);

        switch (rc.getTipoDato()) {
            case BOOLEAN: {
                cond = (boolean)rc.getValor();
            }   break;
            case VECTOR: {
                Vector v = (Vector)rc.getValor();
                if (v.getInnerType() != ETipoDato.BOOLEAN) {
                    msj = "Error. La condición recibida es de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un valor booleano.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_TERNARIO]", msj, getLinea(), getColumna());
                    return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                }
                cond = (boolean)v.getElementByPosition(0).getValor();
            }   break;
            default: {
                msj = "Error. La condición recibida es de tipo <"+ rc.getTipoDato() +">. Se espera un valor booleano.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_TERNARIO]", msj, getLinea(), getColumna());
                return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
            }
        }

        return cond ? ((Instruccion)valorVerdadero).Ejecutar(ts) : ((Instruccion)valorFalso).Ejecutar(ts);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("EXPRESION");
        String subson = ts.getDeclararNodo("NODO_TERNARIO");
        String tokencondi = ((Instruccion)condicion).GenerarDOT(ts);
        String signo1 = ts.getDeclararNodo("?");
        String tokenvv = ((Instruccion)valorVerdadero).GenerarDOT(ts);
        String signo2 = ts.getDeclararNodo(":");
        String tokenvf = ((Instruccion)valorFalso).GenerarDOT(ts);
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokencondi);
        ts.enlazarNodos(subson, signo1);
        ts.enlazarNodos(subson, tokenvv);
        ts.enlazarNodos(subson, signo2);
        ts.enlazarNodos(subson, tokenvf);
        return parent;
    }

}
