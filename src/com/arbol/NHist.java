package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.graficos.DispersionChart;
import com.graficos.HistogramChart;
import com.main.Main;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import javax.swing.*;
import java.util.LinkedList;

public class NHist extends Nodo implements Instruccion {

    private Nodo xlab;
    private Nodo xlim;
    private Nodo ylim;
    private Nodo titulo;
    private Nodo valores;
    private String location = "[N_HIST]";

    public NHist(int linea, int columna, String archivo, Nodo valores, Nodo titulo, Nodo xlab, Nodo xlim, Nodo ylim) {
        super(linea, columna, archivo, ETipoNodo.STMT_HIST);
        this.xlab = xlab;
        this.xlim = xlim;
        this.ylim = ylim;
        this.titulo = titulo;
        this.valores = valores;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /* Obtengo los valores de los parámetros */
        String rxlab = validarTextos("xlab", ((Instruccion)xlab).Ejecutar(ts));
        String rtitulo = validarTextos("main", ((Instruccion)titulo).Ejecutar(ts));
        LinkedList<Double> rxlim = validarLimites("xlim", ((Instruccion)xlim).Ejecutar(ts));
        LinkedList<Double> rylim = validarLimites("ylim", ((Instruccion)ylim).Ejecutar(ts));
        Object rvalores = validarDatos(((Instruccion)valores).Ejecutar(ts));

        if (rvalores != null && rxlab != null && rtitulo != null && rxlim != null && rylim != null) {

            HistogramChart pc = new HistogramChart(getLinea(), getColumna(), getArchivo(), rtitulo, rxlab, rxlim, rylim, rvalores);
            JPanel panel = pc.getHistogramChart();
            panel.setName("Graph[F:"+ getLinea() +",C:"+ getColumna() +"]");
            Main.getGUI().addGraph(panel);

        } else {
            return error;
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));

    }

    private Object validarDatos(Resultado rvals) {

        String msj;

        switch (rvals.getTipoDato()) {

            case VECTOR: {

                Vector v = (Vector)rvals.getValor();

                if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                    msj = "Error. El valor del parámetro 'v' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un vector de valores numéricos.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }

                MultiValuedMap<Double, Double> ret = new ArrayListValuedHashMap<>();
                for (int i = 0; i < v.getVectorSize(); i++) {
                    ret.put((double)i, (v.getInnerType() == ETipoDato.INT ? (double)(int)v.getElementByPosition(i).getValor() : (double)v.getElementByPosition(i).getValor()));
                }

                return ret;

            }

            case MATRIX: {

                Matriz mat = (Matriz)rvals.getValor();

                if (mat.getInnerType() != ETipoDato.INT && mat.getInnerType() != ETipoDato.DECIMAL) {
                    msj = "Error. El valor del parámetro 'v' no puede ser de tipo <MATRIX["+ mat.getInnerType() +"]>. Se espera una matriz de valores numéricos.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }

                // TODO - Esto posiblemente tenga que cambiarse si se define que se pueden utilizar todas las columnas
                MultiValuedMap<Double, Double> ret = new ArrayListValuedHashMap<>();
                for (int i = 1; i <= mat.getFilas(); i++) {
                    ret.put((double)i, (mat.getInnerType() == ETipoDato.INT ? (double)(int)mat.getElementByCoordinates(i, 1).getValor() : (double)mat.getElementByCoordinates(i, 1).getValor()));
                }

                return mat;

            }

            default: {
                msj = "Error. El valor del parámetro 'v' no puede ser una expresión de tipo <"+ rvals.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
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
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. El valor del parámetro '"+ param +"' no puede ser un <VECTOR[STRING]> con más de 1 valor.";
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

    private LinkedList<Double> validarLimites(String param, Resultado rlimit) {

        String msj;

        if (rlimit.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector)rlimit.getValor();

            if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. El valor del parámetro '"+ param +"' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un vector de valores numéricos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                return null;
            }

            if (v.getVectorSize() != 2) {
                msj = "Error. El valor del parámetro '"+ param +"' no puede ser un vector con más de 2 elementos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                return null;
            }

            LinkedList<Double> ret = new LinkedList<>();
            for (Item it : v.getElementos()) {
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
                msj = "Error. El valor del parámetro '"+ param +"' presenta como mínimo un valor más grande que el valor máximo.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                ret.set(0, Double.NEGATIVE_INFINITY);
                flag = true;
            }

            /* Máximo < Mínimo */
            if ((ret.get(1) < ret.get(0)) && !flag) {
                msj = "Error. El valor del parámetro '"+ param +"' presenta como máximo un valor más pequeño que el valor mínimo.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                ret.set(1, Double.POSITIVE_INFINITY);
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro '"+ param +"' no puede ser una expresión de tipo <"+ rlimit.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
            return null;
        }

    }

}
