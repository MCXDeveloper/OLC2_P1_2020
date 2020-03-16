package com.graficos;

import com.abstracto.Item;
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

    public DispersionChart(int linea, int columna, String archivo, String titulo, String xlab, String ylab, LinkedList<Double> ylim, LinkedList<Double> valores) {

        super(linea, columna, archivo, ETipoNodo.STMT_DISPERSION);

        chart = ChartFactory.createScatterPlot(titulo, xlab, ylab, createDataset(valores, ylim), PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize( new java.awt.Dimension( 100 , 100 ) );
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseLinesVisible(false);
        plot.setRenderer(renderer);

    }

    private XYDataset createDataset(LinkedList<Double> valores, LinkedList<Double> ylim) {

        XYSeries series = new XYSeries("Valores");
        XYSeriesCollection dataset = new XYSeriesCollection();

        String msj;
        double val;
        for (int i = 0; i < valores.size(); i++) {
            val = valores.get(i);
            if (val >= ylim.get(0) && val <= ylim.get(1)) {
                series.add(i, val);
            } else {
                msj = "Error. El valor <"+ val +"> no es permitido en el rango definido por del parámetro 'ylim'.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_DISPERSION_CHART]", msj, getLinea(), getColumna());
            }
        }

        dataset.addSeries(series);
        return dataset;

    }

    public JPanel getDispersionChart() {
        JTabbedPane jtp = Main.getGUI().getGraphContainer();
        BufferedImage graph = chart.createBufferedImage(455, 360);
        JLabel imagen = new JLabel();
        imagen.setSize(jtp.getSize());
        imagen.setIcon(new ImageIcon(graph));
        JPanel panelito = new JPanel();
        panelito.add(imagen);
        return panelito;
    }

}