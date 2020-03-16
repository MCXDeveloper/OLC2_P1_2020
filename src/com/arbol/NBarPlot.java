package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.graficos.BarChart;
import com.main.Main;

import javax.swing.*;
import java.util.LinkedList;

public class NBarPlot extends Nodo implements Instruccion {

    private Nodo atrh;
    private Nodo xlab;
    private Nodo ylab;
    private Nodo tags;
    private Nodo titulo;

    public NBarPlot(int linea, int columna, String archivo, Nodo atrh, Nodo xlab, Nodo ylab, Nodo titulo, Nodo tags) {
        super(linea, columna, archivo, ETipoNodo.STMT_BAR_PLOT);
        this.atrh = atrh;
        this.xlab = xlab;
        this.ylab = ylab;
        this.tags = tags;
        this.titulo = titulo;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /* Obtengo los valores de los parámetros */
        LinkedList<String> rlabels = validarLabels(((Instruccion)tags).Ejecutar(ts));
        LinkedList<Double> rvalores = validarDatos(((Instruccion)atrh).Ejecutar(ts));
        String rxlab = validarTextos("xlab", ((Instruccion)xlab).Ejecutar(ts));
        String rylab = validarTextos("ylab", ((Instruccion)ylab).Ejecutar(ts));
        String rtitulo = validarTextos("main", ((Instruccion)titulo).Ejecutar(ts));

        if (rlabels != null && rvalores != null && rxlab != null && rylab != null && rtitulo != null) {
            if (rlabels.size() != rvalores.size()) {
                msj = "Error. La cantidad de valores del parámetro 'H' no concuerda con la del parámetro 'names.arg'.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                if (rvalores.size() > rlabels.size()) {
                    for (int i = rlabels.size(); i < rvalores.size(); i++) {
                        rlabels.add("Desconocido #" + i);
                    }
                }
            }

            BarChart pc = new BarChart(rtitulo, rxlab, rylab, rlabels, rvalores);
            JPanel panel = pc.getBarPlotChart();
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

            Vector v = (Vector)rvals.getValor();

            if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. El valor del parámetro 'H' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un vector de valores numéricos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_BAR_PLOT]", msj, getLinea(), getColumna());
                return null;
            }

            LinkedList<Double> ret = new LinkedList<>();
            for (Item it : v.getElementos()) {
                double d = (it.getTipo() == ETipoDato.INT) ? (double)(int)it.getValor() : (double)it.getValor();
                ret.add(d);
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro 'H' no puede ser una expresión de tipo <"+ rvals.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_BAR_PLOT]", msj, getLinea(), getColumna());
            return null;
        }

    }

    private LinkedList<String> validarLabels(Resultado rlabels) {

        String msj;

        if (rlabels.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector)rlabels.getValor();

            if (v.getInnerType() != ETipoDato.STRING) {
                msj = "Error. El valor del parámetro 'names.arg' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un VECTOR[STRING].";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_BAR_PLOT]", msj, getLinea(), getColumna());
                return null;
            }

            LinkedList<String> ret = new LinkedList<>();
            for (Item it : v.getElementos()) {
                ret.add((String)it.getValor());
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro 'names.arg' no puede ser una expresión de tipo <"+ rlabels.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_BAR_PLOT]", msj, getLinea(), getColumna());
            return null;
        }

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
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_BAR_PLOT]", msj, getLinea(), getColumna());
                    return null;
                }
                return (String)v.getElementByPosition(0).getValor();
            }
            default: {
                msj = "Error. El valor del parámetro '"+ param +"' no puede ser una expresión de tipo <"+ rcadena.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_BAR_PLOT]", msj, getLinea(), getColumna());
                return null;
            }
        }

    }

}
