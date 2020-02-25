package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NNot extends Nodo implements Instruccion {

    private Nodo op;

    public NNot(int linea, int columna, String archivo, Nodo op) {
        super(linea, columna, archivo, ETipoNodo.EXP_NOT);
        this.op = op;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (op != null) {

            Resultado v1;
            v1 = ((Instruccion)op).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.BOOLEAN) {
                tdr = ETipoDato.BOOLEAN;
                valor = !((boolean)v1.getValor());
            } else if (v1.getTipoDato() == ETipoDato.VECTOR) {

                Vector vec1 = (Vector)v1.getValor();

                Item it1;
                NPrim op1;
                Resultado r;
                LinkedList<Item> li = new LinkedList<>();

                for (int i = 0; i < vec1.getVectorSize(); i++) {
                    it1 = vec1.getElementByPosition(i);
                    op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                    r = new NNot(getLinea(), getColumna(), getArchivo(), op1).Ejecutar(ts);
                    li.add(new Item(r.getTipoDato(), r.getValor()));
                }

                tdr = ETipoDato.VECTOR;
                valor = new Vector(li);

            } else {
                msj = "Error. No hay implementación para la operación NOT para los tipos <"+ v1.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NOT]", msj, getLinea(), getColumna());
            }

        } else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_NOT]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
