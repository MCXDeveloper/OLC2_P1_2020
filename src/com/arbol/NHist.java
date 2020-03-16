package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.graficos.HistogramChart;
import com.main.Main;

import javax.swing.*;
import java.util.LinkedList;

public class NHist extends Nodo implements Instruccion {

    private Nodo xlab;
    private Nodo titulo;
    private Nodo valores;
    private String location = "[N_HIST]";

    public NHist(int linea, int columna, String archivo, Nodo valores, Nodo xlab, Nodo titulo) {
        super(linea, columna, archivo, ETipoNodo.STMT_HIST);
        this.xlab = xlab;
        this.titulo = titulo;
        this.valores = valores;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /* Obtengo los valores de los parámetros */
        String rxlab = validarTextos("xlab", ((Instruccion)xlab).Ejecutar(ts));
        LinkedList<Double> rvalores = validarDatos(((Instruccion)valores).Ejecutar(ts));
        String rtitulo = validarTextos("main", ((Instruccion)titulo).Ejecutar(ts));

        if (rvalores != null && rxlab != null && rtitulo != null) {

            HistogramChart pc = new HistogramChart(getLinea(), getColumna(), getArchivo(), rtitulo, rxlab, rvalores);
            JPanel panel = pc.getHistogramChart();
            panel.setName("Graph[F:"+ getLinea() +",C:"+ getColumna() +"]");
            Main.getGUI().addGraph(panel);

        } else {
            return error;
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private LinkedList<Double> validarDatos(Resultado rvals) {

        String msj;

        if (rvals.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector) rvals.getValor();

            if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. El valor del parámetro 'v' no puede ser de tipo <VECTOR[" + v.getInnerType() + "]>. Se espera un vector de valores numéricos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                return null;
            }

            double d;
            LinkedList<Double> ret = new LinkedList<>();
            for (int i = 0; i < v.getVectorSize(); i++) {
                d = v.getInnerType() == ETipoDato.INT ? (double) (int) v.getElementByPosition(i).getValor() : (double) v.getElementByPosition(i).getValor();
                if (d < 0) {
                    msj = "Error. Los valores a graficar deben de ser mayores a 0.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }
                ret.add(d);
            }

            return ret;

        }

        msj = "Error. El valor del parámetro 'v' no puede ser una expresión de tipo <" + rvals.getTipoDato() + ">.";
        ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
        return null;

    }

    private String validarTextos(String param, Resultado rcadena) {

        String msj;

        switch (rcadena.getTipoDato()) {
            case STRING: {
                return (String)rcadena.getValor();
            }
            case VECTOR: {
                Vector v = (Vector)rcadena.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. El valor del parámetro '"+ param +"' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un STRING.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }
                return (String)v.getElementByPosition(0).getValor();
            }
            default: {
                msj = "Error. El valor del parámetro '"+ param +"' no puede ser una expresión de tipo <"+ rcadena.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                return null;
            }
        }

    }

}
