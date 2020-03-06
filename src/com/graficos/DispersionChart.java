package com.graficos;

import com.abstracto.Matriz;
import com.abstracto.Nodo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.estaticas.ErrorHandler;
import com.main.Main;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class DispersionChart extends Nodo {

    private JFreeChart chart;

    public DispersionChart(int linea, int columna, String archivo, String titulo, String xlab, String ylab, LinkedList<Double> xlim, LinkedList<Double> ylim, Matriz matrix, boolean byrow) {

        super(linea, columna, archivo, ETipoNodo.STMT_DISPERSION);

        chart = ChartFactory.createScatterPlot(titulo, xlab, ylab, createDataset(matrix, xlim, ylim, byrow), PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize( new java.awt.Dimension( 100 , 100 ) );
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseLinesVisible(false);
        plot.setRenderer(renderer);

    }

    private XYDataset createDataset(Matriz matrix, LinkedList<Double> xlim, LinkedList<Double> ylim, boolean byrow) {

        XYSeries series;
        XYSeriesCollection dataset = new XYSeriesCollection();

        String msj;
        double val;
        String tag = byrow ? "Fila" : "Columna";
        int klimit = byrow ? matrix.getFilas() : matrix.getColumnas();
        int vlimit = byrow ? matrix.getColumnas() : matrix.getFilas();

        for (int k = 1; k <= klimit; k++) {

            series = new XYSeries(tag + " #" + k);

            for (int v = 1; v <= vlimit; v++) {

                if (matrix.getInnerType() == ETipoDato.INT) {
                    val = byrow ? (double)(int)matrix.getElementByCoordinates(k, v).getValor() : (double)(int)matrix.getElementByCoordinates(v, k).getValor();
                } else {
                    val = byrow ? (double)matrix.getElementByCoordinates(k, v).getValor() : (double)matrix.getElementByCoordinates(v, k).getValor();
                }

                if (v >= xlim.get(0) && v <= xlim.get(1)) {
                    if (val >= ylim.get(0) && val <= ylim.get(1)) {
                        series.add(v, val);
                    } else {
                        msj = "Error. El valor <"+ val +"> no es permitido en el rango definido por del parámetro 'ylim'.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION_CHART]", msj, getLinea(), getColumna());
                    }
                } else {
                    msj = "Error. El valor <"+ val +"> no es permitido en el rango definido por del parámetro 'xlim'.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION_CHART]", msj, getLinea(), getColumna());
                }

            }

            dataset.addSeries(series);

        }

        return dataset;

    }

    public JPanel getDispersionChart() {
        JTabbedPane jtp = Main.getGUI().getGraphContainer();
        BufferedImage graph = chart.createBufferedImage(355, 330);
        JLabel imagen = new JLabel();
        imagen.setSize(jtp.getSize());
        imagen.setIcon(new ImageIcon(graph));
        JPanel panelito = new JPanel();
        panelito.add(imagen);
        return panelito;
    }

}