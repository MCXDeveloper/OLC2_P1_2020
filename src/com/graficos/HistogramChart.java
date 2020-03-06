package com.graficos;

import com.abstracto.Nodo;
import com.constantes.ETipoNodo;
import com.main.Main;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class HistogramChart extends Nodo {

    private JFreeChart chart;

    public HistogramChart(int linea, int columna, String archivo, String titulo, String xlab, LinkedList<Double> xlim, LinkedList<Double> ylim, LinkedList<Double> valores) {

        super(linea, columna, archivo, ETipoNodo.STMT_HIST);

        chart = ChartFactory.createHistogram(titulo, xlab, "", createDataset(titulo, valores, xlim), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);

    }

    private IntervalXYDataset createDataset(String titulo, LinkedList<Double> valores, LinkedList<Double> xlim) {

        double[] arr = valores.stream().filter(x -> (x >= xlim.get(0) && x <= xlim.get(1))).mapToDouble(Double::doubleValue).toArray();

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries(titulo, arr, arr.length);

        return dataset;

    }

    public JPanel getHistogramChart() {
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