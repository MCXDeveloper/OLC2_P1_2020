package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NMode extends Nodo implements Instruccion {

    private Nodo trim;
    private Nodo valor;

    public NMode(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_MODE);
        this.trim = null;
        this.valor = valor;
    }

    public NMode(int linea, int columna, String archivo, Nodo valor, Nodo trim) {
        super(linea, columna, archivo, ETipoNodo.EXP_MODE);
        this.trim = trim;
        this.valor = valor;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
        Resultado rc = ((Instruccion)valor).Ejecutar(ts);

        if (rc.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector) rc.getValor();

            if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. No se puede realizar la función MODE ya que el primer parámetro es un <VECTOR[" + v.getInnerType() + "]>.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MODE]", msj, getLinea(), getColumna());
                return error;
            }

            if (trim != null) {
                Resultado rtrim = validarTrim("[N_MODE]", trim, ts);
                if (rtrim != null) {
                    LinkedList<Double> vals = validarDatosEstadisticos(v.getElementos(), rtrim);
                    return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, getMode(vals));
                } else {
                    return error;
                }
            } else {
                LinkedList<Double> vals = validarDatosEstadisticos(v.getElementos(), null);
                return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, getMode(vals));
            }

        }

        msj = "Error. No se puede realizar la función MODE a una expresión de tipo <" + rc.getTipoDato() + ">.";
        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MODE]", msj, getLinea(), getColumna());
        return error;

    }

    private Object getMode(LinkedList<Double> vals) {

        double maxValue = 0, maxCount = 0;
        Double[] numArray = vals.toArray(new Double[0]);

        for (Double value : numArray) {
            int count = 0;
            for (Double aDouble : numArray) {
                if (aDouble.equals(value)) ++count;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = value;
            }
        }

        return maxValue;
    }
}
