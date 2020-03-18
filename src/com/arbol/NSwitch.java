package com.arbol;

import com.abstracto.*;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

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

        String msj;
        boolean caseFlag = false;
        boolean breakFlag = false;
        Resultado rexp = ((Instruccion)condicion).Ejecutar(ts);

        switch (rexp.getTipoDato()) {
            case INT:
            case STRING:
            case BOOLEAN:
            case DECIMAL: {
                /* Esta correcto */
            }   break;
            case VECTOR: {
                Vector v = (Vector)rexp.getValor();
                rexp.setTipoDato(v.getInnerType());
                rexp.setValor(v.getElementByPosition(0).getValor());
            }   break;
            default: {
                msj = "Error. La expresión de tipo <"+ rexp.getTipoDato() +"> recibida en el switch no es permitida.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SWITCH]", msj, getLinea(), getColumna());
                return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
            }
        }

        for (NCase caso : lista_casos) {

            Resultado rcasexp = ((Instruccion)caso.getCondicion()).Ejecutar(ts);

            switch (rcasexp.getTipoDato()) {
                case INT:
                case STRING:
                case BOOLEAN:
                case DECIMAL: {
                    /* Esta correcto */
                }   break;
                case VECTOR: {
                    Vector v = (Vector)rcasexp.getValor();
                    rcasexp.setTipoDato(v.getInnerType());
                    rcasexp.setValor(v.getElementByPosition(0).getValor());
                }   break;
                default: {
                    msj = "Error. La expresión de tipo <"+ rexp.getTipoDato() +"> recibida en el switch no es permitida.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SWITCH]", msj, getLinea(), getColumna());
                    return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                }
            }

            if (!caseFlag) {
                if (validarCaseConSwitch(ts, rcasexp.getTipoDato(), rcasexp.getValor(), rexp.getTipoDato(), rexp.getValor())) {
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

    private boolean validarCaseConSwitch(TablaSimbolos ts, ETipoDato tipoCase, Object valorCase, ETipoDato tipoSwitch, Object valorSwitch) {
        NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), valorSwitch, tipoSwitch);
        NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), valorCase, tipoCase);
        NIgualdad ni = new NIgualdad(getLinea(), getColumna(), getArchivo(), op1, op2);
        Resultado rigualdad = ni.Ejecutar(ts);
        if (rigualdad.getTipoDato() == ETipoDato.BOOLEAN) {
            return (boolean)rigualdad.getValor();
        }
        return false;
    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("NODO_SWITCH");
        String tokenswitch = ts.getDeclararNodo("switch");
        String tokenval = ((Instruccion)condicion).GenerarDOT(ts);
        String listason = ts.getDeclararNodo("LISTA_CASOS");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokenswitch);
        ts.enlazarNodos(subson, tokenval);
        ts.enlazarNodos(subson, listason);
        for (NCase casito : lista_casos) {
            son = casito.GenerarDOT(ts);
            ts.enlazarNodos(listason, son);
        }
        if (sentencias_default != null) {
            String ndef = ts.getDeclararNodo("NODO_DEFAULT");
            String def = ts.getDeclararNodo("default");
            String deflist = ts.getDeclararNodo("LISTA_INSTRUCCIONES");
            ts.enlazarNodos(listason, ndef);
            ts.enlazarNodos(ndef, def);
            ts.enlazarNodos(ndef, deflist);
            for (Nodo nodito : sentencias_default) {
                if (nodito.getTipoNodo() != ETipoNodo.ERROR) {
                    son = ((Instruccion)nodito).GenerarDOT(ts);
                    ts.enlazarNodos(deflist, son);
                }
            }
        }
        return parent;
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
