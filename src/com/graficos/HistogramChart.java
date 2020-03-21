package com.graficos;

import com.abstracto.Nodo;
import com.constantes.ETipoNodo;
import com.main.Main;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.IntervalXYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class HistogramChart extends Nodo {

    private JFreeChart chart;

    public HistogramChart(int linea, int columna, String archivo, String titulo, String xlab, LinkedList<Double> valores) {
        super(linea, columna, archivo, ETipoNodo.STMT_HIST);
        ChartFactory.setChartTheme(StandardChartTheme.createDarknessTheme());
        chart = ChartFactory.createHistogram(xlab, titulo, "", createDataset(xlab, valores), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.black);
        XYPlot plot = chart.getXYPlot();
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        ((XYBarRenderer) plot.getRenderer()).setBarPainter(new StandardXYBarPainter());
    }

    private IntervalXYDataset createDataset(String titulo, LinkedList<Double> valores) {

        double[] arr = valores.stream().mapToDouble(Double::doubleValue).toArray();
        double min = valores.stream().min(Double::compare).get();
        double max = valores.stream().max(Double::compare).get();
        int bins = (int)Math.round((max - min) / 5);

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        if (min == max) {
            dataset.addSeries(titulo, arr, (bins == 0 ? 1 : bins), 0, max);
        } else {
            dataset.addSeries(titulo, arr, (bins == 0 ? 1 : bins), min, max);
        }

        return dataset;

    }

    public JPanel getHistogramChart() {
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