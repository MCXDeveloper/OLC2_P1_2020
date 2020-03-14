package com.graficos;

import com.abstracto.Matriz;
import com.constantes.ETipoDato;
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

public class LineChart {

    private JFreeChart chart;

    public LineChart(String titulo, String xlab, String ylab, String type, LinkedList<Double> valores) {

        chart = ChartFactory.createXYLineChart(titulo, xlab, ylab, createDataset(valores), PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize( new java.awt.Dimension( 100 , 100 ) );
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );

        switch (type) {
            case "P":
                renderer.setBaseShapesVisible(true);
                renderer.setBaseLinesVisible(false);
                break;
            case "I":
                renderer.setBaseShapesVisible(false);
                renderer.setBaseLinesVisible(true);
                break;
            default:
                renderer.setBaseShapesVisible(true);
                renderer.setBaseLinesVisible(true);
                break;
        }

        plot.setRenderer(renderer);

    }

    public JPanel getLineChart() {
        JTabbedPane jtp = Main.getGUI().getGraphContainer();
        BufferedImage graph = chart.createBufferedImage(355, 330);
        JLabel imagen = new JLabel();
        imagen.setSize(jtp.getSize());
        imagen.setIcon(new ImageIcon(graph));
        JPanel panelito = new JPanel();
        panelito.add(imagen);
        return panelito;
    }

    private XYDataset createDataset(LinkedList<Double> valores) {

        XYSeries series;
        XYSeriesCollection dataset = new XYSeriesCollection();

        series = new XYSeries("Linea");
        for (int i = 0; i < valores.size(); i++) {
            series.add(i+1, valores.get(i));
        }
        dataset.addSeries(series);

        return dataset;

    }

}