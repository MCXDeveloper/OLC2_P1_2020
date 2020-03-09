package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.LinkedList;

public class NMedian extends Nodo implements Instruccion {

    private Nodo trim;
    private Nodo valor;

    public NMedian(int linea, int columna, String archivo, Nodo valor) {
        super(linea, columna, archivo, ETipoNodo.EXP_MEDIAN);
        this.trim = null;
        this.valor = valor;
    }

    public NMedian(int linea, int columna, String archivo, Nodo valor, Nodo trim) {
        super(linea, columna, archivo, ETipoNodo.EXP_MEDIAN);
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
                msj = "Error. No se puede realizar la función MEDIAN ya que el primer parámetro es un <VECTOR[" + v.getInnerType() + "]>.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MEDIAN]", msj, getLinea(), getColumna());
                return error;
            }

            if (trim != null) {
                Resultado rtrim = validarTrim("[N_MEDIAN]", trim, ts);
                if (rtrim != null) {
                    LinkedList<Double> vals = validarDatosEstadisticos(v.getElementos(), rtrim);
                    return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, getMedian(vals));
                } else {
                    return error;
                }
            } else {
                LinkedList<Double> vals = validarDatosEstadisticos(v.getElementos(), null);
                return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, getMedian(vals));
            }

        }

        msj = "Error. No se puede realizar la función MEDIAN a una expresión de tipo <" + rc.getTipoDato() + ">.";
        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_MEDIAN]", msj, getLinea(), getColumna());
        return error;

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private double getMedian(LinkedList<Double> vals) {
        Double[] numArray = vals.toArray(new Double[0]);
        Arrays.sort(numArray);
        double median;
        if (numArray.length % 2 == 0)
            median = (numArray[numArray.length/2] + numArray[numArray.length/2 - 1])/2;
        else
            median = numArray[numArray.length/2];
        return median;
    }

}
