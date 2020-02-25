package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NMenor extends Nodo implements Instruccion {

    private Nodo opIzq;
    private Nodo opDer;

    public NMenor(int linea, int columna, String archivo, Nodo opIzq, Nodo opDer) {
        super(linea, columna, archivo, ETipoNodo.EXP_MENOR);
        this.opIzq = opIzq;
        this.opDer = opDer;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (opIzq != null && opDer != null) {

            Resultado v1, v2;
            tdr = ETipoDato.BOOLEAN;
            v1 = ((Instruccion) opIzq).Ejecutar(ts);
            v2 = ((Instruccion) opDer).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.INT) {
                valor = ((int)v1.getValor()) < ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.DECIMAL) {
                valor = ((int)v1.getValor()) < ((double)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.INT) {
                valor = ((double)v1.getValor()) < ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.DECIMAL) {
                valor = ((double) v1.getValor()) < ((double) v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.STRING && v2.getTipoDato() == ETipoDato.STRING) {
                valor = v1.getValor().toString().compareTo(v2.getValor().toString()) < 0;
            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector vec1 = (Vector)v1.getValor();
                Vector vec2 = (Vector)v2.getValor();

                if (vec1.getVectorSize() == vec2.getVectorSize()) {

                    Item it1;
                    Item it2;
                    NPrim op1;
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    for (int i = 0; i < vec1.getVectorSize(); i++) {
                        it1 = vec1.getElementByPosition(i);
                        it2 = vec2.getElementByPosition(i);
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());
                        r = new NMenor(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else if (vec1.getVectorSize() == 1 && vec2.getVectorSize() > 1) {

                    Item it2;
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    Item it1 = vec1.getElementByPosition(0);
                    NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());

                    for (int i = 0; i < vec2.getVectorSize(); i++) {
                        it2 = vec2.getElementByPosition(i);
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());
                        r = new NMenor(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else if (vec1.getVectorSize() > 1 && vec2.getVectorSize() == 1) {

                    Item it1;
                    NPrim op1;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    Item it2 = vec2.getElementByPosition(0);
                    NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());

                    for (int i = 0; i < vec1.getVectorSize(); i++) {
                        it1 = vec1.getElementByPosition(i);
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                        r = new NMenor(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else {
                    msj = "Error. Los tamaños de los vectores difieren por lo que no se puede realizar la operación MENOR.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MENOR]", msj, getLinea(), getColumna());
                }

            } else {
                tdr = ETipoDato.ERROR;
                msj = "Error. No hay implementación para la operación MENOR para los tipos <"+ v1.getTipoDato() +"> y <"+ v2.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MENOR]", msj, getLinea(), getColumna());
            }

        }else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MENOR]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }
}
