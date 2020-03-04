package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.graficos.PieChart;
import com.main.Main;

import javax.swing.*;
import java.util.LinkedList;

public class NPie extends Nodo implements Instruccion {

    private Nodo labels;
    private Nodo titulo;
    private Nodo valores;

    public NPie(int linea, int columna, String archivo, Nodo valores, Nodo labels, Nodo titulo) {
        super(linea, columna, archivo, ETipoNodo.STMT_PIE_CHART);
        this.labels = labels;
        this.titulo = titulo;
        this.valores = valores;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        /* Obtengo los valores de los parámetros */
        String rtitulo = validarTitulo(((Instruccion)titulo).Ejecutar(ts));
        LinkedList<String> rlabels = validarLabels(((Instruccion)labels).Ejecutar(ts));
        LinkedList<Double> rvalores = validarDatos(((Instruccion)valores).Ejecutar(ts));

        if (rtitulo != null && rlabels != null && rvalores != null) {

            if (rlabels.size() != rvalores.size()) {
                msj = "Error. La cantidad de valores del parámetro 'x' no concuerda con la del parámetro 'labels'.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                if (rvalores.size() > rlabels.size()) {
                    for (int i = rlabels.size(); i < rvalores.size(); i++) {
                        rlabels.add("Desconocido #" + i);
                    }
                }
            }

            PieChart pc = new PieChart(rtitulo, rlabels, rvalores);
            JPanel panel = pc.getPieChart();
            panel.setName("Graph[F:"+ getLinea() +",C:"+ getColumna() +"]");
            Main.getGUI().addGraph(panel);

        } else {
            return error;
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL, new NNulo(getLinea(), getColumna(), getArchivo()));

    }

    private String validarTitulo(Resultado rtitulo) {

        String msj;

        switch (rtitulo.getTipoDato()) {
            case STRING: {
                return (String)rtitulo.getValor();
            }
            case VECTOR: {
                Vector v = (Vector)rtitulo.getValor();
                if (v.getInnerType() != ETipoDato.STRING) {
                    msj = "Error. El valor del parámetro 'main' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un STRING.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                    return null;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. El valor del parámetro 'main' no puede ser un <VECTOR[STRING]> con más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                    return null;
                }
                return (String)v.getElementByPosition(0).getValor();
            }
            default: {
                msj = "Error. El valor del parámetro 'main' no puede ser una expresión de tipo <"+ rtitulo.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                return null;
            }
        }

    }

    private LinkedList<String> validarLabels(Resultado rlabels) {

        String msj;

        if (rlabels.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector)rlabels.getValor();

            if (v.getInnerType() != ETipoDato.STRING) {
                msj = "Error. El valor del parámetro 'labels' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un VECTOR[STRING].";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                return null;
            }

            LinkedList<String> ret = new LinkedList<>();
            for (Item it : v.getElementos()) {
                ret.add((String)it.getValor());
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro 'labels' no puede ser una expresión de tipo <"+ rlabels.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
            return null;
        }

    }

    private LinkedList<Double> validarDatos(Resultado rvals) {

        String msj;

        if (rvals.getTipoDato() == ETipoDato.VECTOR) {

            Vector v = (Vector)rvals.getValor();

            if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                msj = "Error. El valor del parámetro 'x' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un vector de valores numéricos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
                return null;
            }

            LinkedList<Double> ret = new LinkedList<>();
            for (Item it : v.getElementos()) {
                double d = (it.getTipo() == ETipoDato.INT) ? (double)(int)it.getValor() : (double)it.getValor();
                ret.add(d);
            }

            return ret;

        } else {
            msj = "Error. El valor del parámetro 'x' no puede ser una expresión de tipo <"+ rvals.getTipoDato() +">.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_PIE]", msj, getLinea(), getColumna());
            return null;
        }

    }

}
