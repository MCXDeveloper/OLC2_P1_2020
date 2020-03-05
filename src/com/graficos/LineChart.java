package com.graficos;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.util.LinkedList;

public class LineChart {

    private JFreeChart chart;

    public LineChart(String titulo, String xlab, String ylab, String type, LinkedList<Double> valores) {
        //chart = ChartFactory.createBarChart(titulo, xlab, ylab, createDataset(valores), PlotOrientation.VERTICAL, true, true, false);
    }

}