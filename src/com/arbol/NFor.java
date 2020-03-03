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

        switch (rv.getTipoDato()) {

            case INT:
            case STRING:
            case DECIMAL:
            case BOOLEAN: {

                ts.addAmbito(EAmbito.CICLO);

                Simbolo s = new Simbolo(rv.getTipoDato(), id, rv.getValor());
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

            case VECTOR: {

                Item it;
                Simbolo s;
                Vector v = (Vector)rv.getValor();

                for (int i = 0; i < v.getVectorSize(); i++) {

                    it = v.getElementByPosition(i);

                    ts.addAmbito(EAmbito.CICLO);
                    s = new Simbolo(it.getTipo(), id, it.getValor());
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

                }

            }   break;

            case LIST: {

                Item it;
                Simbolo s;
                Lista l = (Lista)rv.getValor();

                for (int i = 0; i < l.getListSize(); i++) {

                    it = l.getElementByPosition(i);

                    ts.addAmbito(EAmbito.CICLO);
                    s = new Simbolo(it.getTipo(), id, it.getValor());
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

                }

            }   break;

            case MATRIX: {

                Item it;
                Simbolo s;
                Matriz mat = (Matriz)rv.getValor();

                for (int i = 0; i < mat.getMatrixSize(); i++) {

                    it = mat.getElementByPosition(i);

                    ts.addAmbito(EAmbito.CICLO);
                    s = new Simbolo(it.getTipo(), id, it.getValor());
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

                }

            }   break;

            case ARRAY: {

                Item it;
                Simbolo s;
                Arreglo arr = (Arreglo)rv.getValor();

                for (int i = 0; i < arr.getArregloSize(); i++) {

                    it = arr.getElementByPosition(i);

                    ts.addAmbito(EAmbito.CICLO);
                    s = new Simbolo(it.getTipo(), id, it.getValor());
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
}
