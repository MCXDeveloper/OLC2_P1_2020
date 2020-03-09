package com.arbol;

import com.abstracto.*;
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

        switch (id.toUpperCase()) {

            case "C": {
                NFC call = new NFC(getLinea(), getColumna(), getArchivo(), params);
                return call.Ejecutar(ts);
            }

            case "PIE": {
                if (params.size() != 3) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NPie call = new NPie(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1), params.get(2));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "LIST": {
                NList call = new NList(getLinea(), getColumna(), getArchivo(), params);
                return call.Ejecutar(ts);
            }

            case "NCOL": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NCol call = new NCol(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "NROW": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NRow call = new NRow(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "MEAN": {
                if (params.size() > 2) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NMean call = (params.size() == 1) ? new NMean(getLinea(), getColumna(), getArchivo(), params.get(0)) : new NMean(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "MODE": {
                if (params.size() > 2) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NMode call = (params.size() == 1) ? new NMode(getLinea(), getColumna(), getArchivo(), params.get(0)) : new NMode(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "PLOT": {
                if (params.size() != 5 && params.size() != 7) {
                    msj = "Error.  La cantidad de parámetros definida en la función <" + id + "> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    if (params.size() == 5) {
                        NPlot call = new NPlot(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1), params.get(2), params.get(3), params.get(4));
                        return call.Ejecutar(ts);
                    } else {
                        NDispersion call = new NDispersion(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5), params.get(6));
                        return call.Ejecutar(ts);
                    }
                }
            }   break;

            case "HIST": {
                if (params.size() != 5) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NHist call = new NHist(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1), params.get(2), params.get(3), params.get(4));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "PRINT": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NPrint call = new NPrint(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "ARRAY": {
                if (params.size() != 2) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NArray call = new NArray(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "TRUNK": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NTrunk call = new NTrunk(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "ROUND": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NRound call = new NRound(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "MATRIX": {
                if (params.size() != 3) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NMatrix call = new NMatrix(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1), params.get(2));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "TYPEOF": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NTypeOf call = new NTypeOf(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "LENGTH": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NLength call = new NLength(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "REMOVE": {
                if (params.size() != 2) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NRemove call = new NRemove(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "MEDIAN": {
                if (params.size() > 2) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NMedian call = (params.size() == 1) ? new NMedian(getLinea(), getColumna(), getArchivo(), params.get(0)) : new NMedian(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "BARPLOT": {
                if (params.size() != 5) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NBarPlot call = new NBarPlot(getLinea(), getColumna(), getArchivo(), params.get(0), params.get(1), params.get(2), params.get(3), params.get(4));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "TOLOWERCASE": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NToLower call = new NToLower(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "TOUPPERCASE": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NToUpper call = new NToUpper(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            case "STRINGLENGTH": {
                if (params.size() != 1) {
                    msj = "Error.  La cantidad de parámetros definida en la función <"+ id +"> no concuerdan con las establecidas en la declaración.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_CALL]", msj, getLinea(), getColumna());
                } else {
                    NStringLength call = new NStringLength(getLinea(), getColumna(), getArchivo(), params.get(0));
                    return call.Ejecutar(ts);
                }
            }   break;

            default: {

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

            }

        }

        return new Resultado(rtd, EFlujo.NORMAL, rvalor);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String callsen = ts.getDeclararNodo("SENTENCIA_LLAMADA");
        String funcNode = ts.getDeclararNodo(id);
        String paramscall = ts.getDeclararNodo("LISTA_EXPRESIONES");
        ts.enlazarNodos(parent, callsen);
        ts.enlazarNodos(callsen, funcNode);
        ts.enlazarNodos(callsen, paramscall);
        for (Nodo nodito : params) {
            son = ((Instruccion)nodito).GenerarDOT(ts);
            ts.enlazarNodos(paramscall, son);
        }
        return parent;
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
