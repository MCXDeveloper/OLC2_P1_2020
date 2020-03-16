package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.graficos.LineChart;
import com.main.Main;

import javax.swing.*;
import java.util.LinkedList;

public class NPlot extends Nodo implements Instruccion {

    private Nodo vatr;
    private Nodo type;
    private Nodo xlab;
    private Nodo ylab;
    private Nodo titulo;

    public NPlot(int linea, int columna, String archivo, Nodo vatr, Nodo type, Nodo xlab, Nodo ylab, Nodo titulo) {
        super(linea, columna, archivo, ETipoNodo.STMT_PLOT);
        this.vatr = vatr;
        this.type = type;
        this.xlab = xlab;
        this.ylab = ylab;
        this.titulo = titulo;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /* Obtengo los valores de los parámetros */
        LinkedList<Double> rvalues = validarDatos(((Instruccion)vatr).Ejecutar(ts));
        String rxlab = validarTextos("xlab", ((Instruccion)xlab).Ejecutar(ts));
        String rylab = validarTextos("ylab", ((Instruccion)ylab).Ejecutar(ts));
        String rtype = validarTextos("type", ((Instruccion)type).Ejecutar(ts));
        String rtitulo = validarTextos("main", ((Instruccion)titulo).Ejecutar(ts));

        if (rxlab != null && rylab != null && rtype != null && rtitulo != null && rvalues != null) {

            rtype = rtype.toUpperCase();

            if (!rtype.equals("P") && !rtype.equals("O") && !rtype.equals("I")) {
                msj = "Error. El valor del parámetro 'type' no acepta el valor <"+ rtype +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PLOT]", msj, getLinea(), getColumna());
                rtype = "O";
            }

            LineChart pc = new LineChart(rtitulo, rxlab, rylab, rtype, rvalues);
            JPanel panel = pc.getLineChart();
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

        switch (rvals.getTipoDato()) {

            case VECTOR: {

                Vector vec = (Vector)rvals.getValor();

                if (vec.getInnerType() != ETipoDato.INT && vec.getInnerType() != ETipoDato.DECIMAL) {
                    msj = "Error. El valor del parámetro 'V' no puede ser de tipo <VECTOR["+ vec.getInnerType() +"]>. Se espera un vector de valores numéricos.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PLOT]", msj, getLinea(), getColumna());
                    return null;
                }

                LinkedList<Double> ret = new LinkedList<>();
                for (Item it : vec.getElementos()) {
                    ret.add(vec.getInnerType() == ETipoDato.INT ? (double)(int)it.getValor() : (double)it.getValor());
                }

                return ret;

            }

            case MATRIX: {

                Matriz mat = (Matriz)rvals.getValor();

                if (mat.getInnerType() != ETipoDato.INT && mat.getInnerType() != ETipoDato.DECIMAL) {
                    msj = "Error. El valor del parámetro 'V' no puede ser de tipo <MATRIX["+ mat.getInnerType() +"]>. Se espera una matriz de valores numéricos.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PLOT]", msj, getLinea(), getColumna());
                    return null;
                }

                LinkedList<Double> ret = new LinkedList<>();
                for (Item it : mat.getElementos()) {
                    ret.add(mat.getInnerType() == ETipoDato.INT ? (double)(int)it.getValor() : (double)it.getValor());
                }

                return ret;

            }

            default: {
                msj = "Error. El valor del parámetro 'V' no puede ser una expresión de tipo <"+ rvals.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PLOT]", msj, getLinea(), getColumna());
                return null;
            }

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
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PLOT]", msj, getLinea(), getColumna());
                    return null;
                }
                return (String)v.getElementByPosition(0).getValor();
            }
            default: {
                msj = "Error. El valor del parámetro '"+ param +"' no puede ser una expresión de tipo <"+ rcadena.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PLOT]", msj, getLinea(), getColumna());
                return null;
            }
        }

    }

}
