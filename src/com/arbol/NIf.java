package com.arbol;

import com.abstracto.*;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NIf extends Nodo implements Instruccion {

    private Nodo condicion;
    private LinkedList<NIf> secIfs;
    private LinkedList<Nodo> sentencias;

    public NIf(int linea, int columna, String archivo, LinkedList<Nodo> sentencias) {
        super(linea, columna, archivo, ETipoNodo.STMT_ELSE);
        this.secIfs = null;
        this.condicion = null;
        this.sentencias = sentencias;
    }

    public NIf(int linea, int columna, String archivo, Nodo condicion, LinkedList<Nodo> sentencias, ETipoNodo tipo) {
        super(linea, columna, archivo, tipo);
        this.secIfs = null;
        this.condicion = condicion;
        this.sentencias = sentencias;
    }

    public void setSecIf(LinkedList<NIf> secIfs){
        this.secIfs = secIfs;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;

        boolean cond;
        Resultado rexp = ((Instruccion)condicion).Ejecutar(ts);

        switch (rexp.getTipoDato()) {

            case BOOLEAN: {
                cond = (boolean)rexp.getValor();
            }   break;

            case VECTOR: {
                Vector v = (Vector)rexp.getValor();
                if (v.getInnerType() != ETipoDato.BOOLEAN) {
                    msj = "Error. La condición recibida es de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un valor booleano.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_IF]", msj, getLinea(), getColumna());
                    return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                }
                cond = (boolean)v.getElementByPosition(0).getValor();
            }   break;

            default: {
                msj = "Error. La condición recibida es de tipo <"+ rexp.getTipoDato() +">. Se espera que sea una expresión booleana o un vector de booleanos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_IF]", msj, getLinea(), getColumna());
                return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
            }

        }

        if(cond) {
            rexp = runStmts(EAmbito.IF, sentencias, ts);
        } else {
            if(secIfs != null){
                rexp = ejecutarElse(ts);
            } else {
                return new Resultado(ETipoDato.NT, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));
            }
        }

        return rexp;

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("NODO_IF");
        String tokenif = ts.getDeclararNodo("if");
        String tokenval = "";
        if (condicion != null) {
            tokenval = ((Instruccion)condicion).GenerarDOT(ts);
        }
        String listason = ts.getDeclararNodo("LISTA_INSTRUCCIONES");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokenif);
        if (condicion != null) {
            ts.enlazarNodos(subson, tokenval);
        }
        ts.enlazarNodos(subson, listason);
        for (Nodo nodito : sentencias) {
            son = ((Instruccion)nodito).GenerarDOT(ts);
            ts.enlazarNodos(listason, son);
        }
        if (secIfs != null) {
            String subsubson;
            for (NIf n : secIfs) {
                subsubson = ts.getDeclararNodo("NODO_ELSE");
                ts.enlazarNodos(subson, subsubson);
                son = n.GenerarDOT(ts);
                ts.enlazarNodos(subsubson, son);
            }
        }
        return parent;
    }

    private Resultado ejecutarElse(TablaSimbolos ts) {

        String msj;
        boolean cond;
        Resultado rexp;

        for(NIf n : secIfs){

            if(n.getTipoNodo() == ETipoNodo.STMT_ELSE_IF){

                assert n.condicion != null;
                rexp = ((Instruccion)n.condicion).Ejecutar(ts);

                switch (rexp.getTipoDato()) {

                    case BOOLEAN: {
                        cond = (boolean)rexp.getValor();
                    }   break;

                    case VECTOR: {
                        Vector v = (Vector)rexp.getValor();
                        if (v.getInnerType() != ETipoDato.BOOLEAN) {
                            msj = "Error. La condición recibida es de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un valor booleano.";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_IF]", msj, getLinea(), getColumna());
                            return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                        }
                        cond = (boolean)v.getElementByPosition(0).getValor();
                    }   break;

                    default: {
                        msj = "Error. La condición recibida es de tipo <"+ rexp.getTipoDato() +">. Se espera que sea una expresión booleana o un vector de booleanos.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_IF]", msj, getLinea(), getColumna());
                        return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                    }

                }

                if(cond) {
                    return runStmts(EAmbito.ELIF, n.sentencias, ts);
                }

            }
            /* No debe comprobarse ninguna condicion */
            else if(n.getTipoNodo() == ETipoNodo.STMT_ELSE) {
                return runStmts(EAmbito.ELSE, n.sentencias, ts);
            }

        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);

    }

    private Resultado runStmts(EAmbito tipo, LinkedList<Nodo> stmts, TablaSimbolos ts) {

        Resultado rexp;
        ts.addAmbito(tipo);

        for(Nodo nd : stmts){
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

}
