package com.arbol;

import com.abstracto.*;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.Simbolo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NFor extends Nodo implements Instruccion {

    private String id;
    private Nodo valor;
    private LinkedList<Nodo> sentencias;

    public NFor(int linea, int columna, String archivo, String id, Nodo valor, LinkedList<Nodo> sentencias) {
        super(linea, columna, archivo, ETipoNodo.STMT_FOR);
        this.id = id;
        this.valor = valor;
        this.sentencias = sentencias;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        Resultado rst = null;
        Resultado rv = ((Instruccion)valor).Ejecutar(ts);

        Simbolo s = ts.getSimbolo(id, true);

        switch (rv.getTipoDato()) {

            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {

                ts.addAmbito(EAmbito.CICLO);

                if (s != null) {
                    s.setTipo(rv.getTipoDato());
                    s.setValor(rv.getValor());
                } else {
                    s = new Simbolo(rv.getTipoDato(), id, rv.getValor());
                }

                ts.addSimbolo(s);

                for(Nodo nd : sentencias) {
                    if(nd.getTipoNodo() == ETipoNodo.ERROR) {
                        continue;
                    }
                    rst = ((Instruccion)nd).Ejecutar(ts);
                    if(rst.getFlujo() != EFlujo.NORMAL){
                        break;
                    }
                }

                if(rst != null && rst.getFlujo() != EFlujo.NORMAL){
                    ts.destruirAmbito();
                    break;
                }

                ts.destruirAmbito();

            }   break;

            case LIST:
            case ARRAY:
            case MATRIX:
            case VECTOR: {

                Item it;
                int tamanoEDD = 0;
                LinkedList<Item> listaItems;

                if (rv.getTipoDato() == ETipoDato.LIST) {
                    tamanoEDD = ((Lista)rv.getValor()).getListSize();
                    listaItems = ((Lista)rv.getValor()).getElementos();
                } else if (rv.getTipoDato() == ETipoDato.ARRAY) {
                    tamanoEDD = ((Arreglo)rv.getValor()).getArregloSize();
                    listaItems = ((Arreglo)rv.getValor()).getElementos();
                } else if (rv.getTipoDato() == ETipoDato.MATRIX) {
                    tamanoEDD = ((Matriz)rv.getValor()).getMatrixSize();
                    listaItems = ((Matriz)rv.getValor()).getElementos();
                } else {
                    tamanoEDD = ((Vector)rv.getValor()).getVectorSize();
                    listaItems = ((Vector)rv.getValor()).getElementos();
                }

                for (int i = 0; i < tamanoEDD; i++) {

                    it = listaItems.get(i);

                    ts.addAmbito(EAmbito.CICLO);

                    s = ts.getSimbolo(id, true);

                    if (s != null) {
                        s.setTipo(it.getTipo());
                        s.setValor(it.getValor());
                    } else {
                        s = new Simbolo(it.getTipo(), id, it.getValor());
                        ts.addSimbolo(s);
                    }

                    for(Nodo nd : sentencias) {
                        if(nd.getTipoNodo() == ETipoNodo.ERROR) {
                            continue;
                        }
                        rst = ((Instruccion)nd).Ejecutar(ts);
                        if(rst.getFlujo() != EFlujo.NORMAL){
                            break;
                        }
                    }

                    if(rst != null && rst.getFlujo() != EFlujo.NORMAL){
                        ts.destruirAmbito();
                        break;
                    }

                    ts.destruirAmbito();

                }

            }   break;

            default: {
                msj = "Error. La expresión recibida en el FOR es de tipo <>.  Se espera que sea un primitivo o una estructura.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_FOR]", msj, getLinea(), getColumna());
                return new Resultado(tdr, EFlujo.NORMAL, rvalor);
            }

        }

        if(rst != null && rst.getFlujo() == EFlujo.RETURN){
            return rst;
        }else{
            return new Resultado(ETipoDato.NT, EFlujo.NORMAL);
        }

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("INSTRUCCION");
        String subson = ts.getDeclararNodo("NODO_FOR");
        String tokenfor = ts.getDeclararNodo("for");
        String tokenid = ts.getDeclararNodo(id);
        String tokenin = ts.getDeclararNodo("in");
        String tokenval = ((Instruccion)valor).GenerarDOT(ts);
        String listason = ts.getDeclararNodo("LISTA_INSTRUCCIONES");
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokenfor);
        ts.enlazarNodos(subson, tokenid);
        ts.enlazarNodos(subson, tokenin);
        ts.enlazarNodos(subson, tokenval);
        ts.enlazarNodos(subson, listason);
        for (Nodo nodito : sentencias) {
            son = ((Instruccion)nodito).GenerarDOT(ts);
            ts.enlazarNodos(listason, son);
        }
        return parent;
    }

}
