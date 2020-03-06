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

public class LineChart {

    private JFreeChart chart;

    public LineChart(String titulo, String xlab, String ylab, String type, Matriz matrix) {

        chart = ChartFactory.createXYLineChart(titulo, xlab, ylab, createDataset(matrix), PlotOrientation.VERTICAL, true, true, false);

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

    private XYDataset createDataset(Matriz matrix) {

        XYSeries series;
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (int col = 1; col <= matrix.getColumnas(); col++) {
            series = new XYSeries("Columna #" + col);
            for (int row = 1; row <= matrix.getFilas(); row++) {
                series.add(row, (matrix.getInnerType() == ETipoDato.INT ? ((int)matrix.getElementByCoordinates(row, col).getValor()) : ((double)matrix.getElementByCoordinates(row, col).getValor())));
            }
            dataset.addSeries(series);
        }

        return dataset;

    }

}