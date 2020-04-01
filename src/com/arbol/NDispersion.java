package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.graficos.DispersionChart;
import com.main.Main;

import javax.swing.*;
import java.util.LinkedList;

public class NDispersion extends Nodo implements Instruccion {

    private Nodo xlab;
    private Nodo ylab;
    private Nodo ylim;
    private Nodo titulo;
    private Nodo valores;

    public NDispersion(int linea, int columna, String archivo, Nodo valores, Nodo xlab, Nodo ylab, Nodo titulo, Nodo ylim) {
        super(linea, columna, archivo, ETipoNodo.STMT_DISPERSION);
        this.xlab = xlab;
        this.ylab = ylab;
        this.ylim = ylim;
        this.titulo = titulo;
        this.valores = valores;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /* Obtengo los valores de los parámetros */
        LinkedList<Double> rylim = validarLimites(((Instruccion)ylim).Ejecutar(ts));
        String rxlab = validarTextos("xlab", ((Instruccion)xlab).Ejecutar(ts));
        String rylab = validarTextos("ylab", ((Instruccion)ylab).Ejecutar(ts));
        LinkedList<Double> rvalores = validarDatos(((Instruccion)valores).Ejecutar(ts));
        String rtitulo = validarTextos("main", ((Instruccion)titulo).Ejecutar(ts));

        if (rvalores != null && rxlab != null && rylab != null && rtitulo != null && rylim != null) {
            DispersionChart pc = new DispersionChart(getLinea(), getColumna(), getArchivo(), rtitulo, rxlab, rylab, rylim, rvalores);
            JPanel panel = pc.getDispersionChart();
            panel.setName("Graph[F:"+ getLinea() +",C:"+ getColumna() +"]");
            Main.getGUI().addGraph(panel);
        } else {
            return error;
        }

        return new Resultado(ETipoDato.STRING, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private LinkedList<Double> validarDatos(Resultado rvals) {

        String msj;

        if (rvals.getTipoDato() == ETipoDato.MATRIX) {

            Matriz mat = (Matriz)rvals.getValor();

            if (mat.getInnerType() != ETipoDato.INT && mat.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. El valor del parámetro 'mat' no puede ser de tipo <MATRIX["+ mat.getInnerType() +"]>. Se espera una matriz de valores numéricos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                return null;
            }

            LinkedList<Double> ret = new LinkedList<>();
            for (Item it : mat.getElementos()) {
                double d = (it.getTipo() == ETipoDato.INT) ? (double)(int)it.getValor() : (double)it.getValor();
                if (d < 0) {
                    msj = "Error. Los valores a graficar deben de ser mayores a 0.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                    return null;
                }
                ret.add(d);
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro 'mat' no puede ser una expresión de tipo <"+ rvals.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
            return null;
        }

    }

    private String validarTextos(String param, Resultado rcadena) {

        String msj;

        switch (rcadena.getTipoDato()) {
            case STRING: {
                return rcadena.getValor().toString();
            }
            case VECTOR: {
                Vector v = (Vector)rcadena.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. El valor del parámetro '"+ param +"' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un STRING.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                    return null;
                }
                return (String)v.getElementByPosition(0).getValor();
            }
            default: {
                msj = "Error. El valor del parámetro '"+ param +"' no puede ser una expresión de tipo <"+ rcadena.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                return null;
            }
        }

    }

    private LinkedList<Double> validarLimites(Resultado rlimit) {

        String msj;

        if (rlimit.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector)rlimit.getValor();

            if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. El valor del parámetro 'ylim' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un vector de valores numéricos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                return null;
            }

            if (v.getVectorSize() < 2) {
                msj = "Error. El valor del parámetro 'ylim' no puede ser un vector con menos de 2 elementos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                return null;
            }

            Item it;
            LinkedList<Double> ret = new LinkedList<>();
            for (int i = 0; i < 2; i++) {
                it = v.getElementByPosition(i);
                double d = (it.getTipo() == ETipoDato.INT) ? (double)(int)it.getValor() : (double)it.getValor();
                ret.add(d);
            }

            /*
             * Valido si el valor mínimo es menor que el máximo y viceversa.
             * ret.get(0) = minimo
             * ret.get(1) = maximo
             */
            boolean flag = false;

            /* Minimo > Máximo */
            if (ret.get(0) > ret.get(1)) {
                msj = "Error. El valor del parámetro '"+ "ylim" +"' presenta como mínimo un valor más grande que el valor máximo.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                ret.set(0, Double.NEGATIVE_INFINITY);
                flag = true;
            }

            /* Máximo < Mínimo */
            if ((ret.get(1) < ret.get(0)) && !flag) {
                msj = "Error. El valor del parámetro '"+ "ylim" +"' presenta como máximo un valor más pequeño que el valor mínimo.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
                ret.set(1, Double.POSITIVE_INFINITY);
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro '"+ "ylim" +"' no puede ser una expresión de tipo <"+ rlimit.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION]", msj, getLinea(), getColumna());
            return null;
        }

    }

}
