package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NNegativo extends Nodo implements Instruccion {

    private Nodo opIzq;

    public NNegativo(int linea, int columna, String archivo, Nodo opIzq) {
        super(linea, columna, archivo, ETipoNodo.EXP_NEGATIVO);
        this.opIzq = opIzq;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (opIzq != null) {

            Resultado v1;
            v1 = ((Instruccion) opIzq).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.INT;
                valor = ((int)v1.getValor()) * -1;
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = ((double)v1.getValor()) * -1;
            } else if (v1.getTipoDato() == ETipoDato.VECTOR) {

                Vector vec1 = (Vector)v1.getValor();

                Item it1;
                NPrim op1;
                Resultado r;
                LinkedList<Item> li = new LinkedList<>();

                for (int i = 0; i < vec1.getVectorSize(); i++) {
                    it1 = vec1.getElementByPosition(i);
                    op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                    r = new NNegativo(getLinea(), getColumna(), getArchivo(), op1).Ejecutar(ts);
                    li.add(new Item(r.getTipoDato(), r.getValor()));
                }

                tdr = ETipoDato.VECTOR;
                valor = new Vector(li);

            } else {
                msj = "Error. No hay implementación para la operación NEGATIVO para los tipos <"+ v1.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NEGATIVO]", msj, getLinea(), getColumna());
            }

        }else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NEGATIVO]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
