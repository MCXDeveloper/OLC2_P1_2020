package com.graficos;

import com.main.Main;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class BarChart {

    private JFreeChart chart;

    public BarChart(String titulo, String xlab, String ylab, LinkedList<String> labels, LinkedList<Double> valores) {
        chart = ChartFactory.createBarChart(titulo, xlab, ylab, createDataset(labels, valores), PlotOrientation.VERTICAL, true, true, false);
    }

    public JPanel getBarPlotChart() {
        JTabbedPane jtp = Main.getGUI().getGraphContainer();
        BufferedImage graph = chart.createBufferedImage(455, 360);
        JLabel imagen = new JLabel();
        imagen.setSize(jtp.getSize());
        imagen.setIcon(new ImageIcon(graph));
        JPanel panelito = new JPanel();
        panelito.add(imagen);
        return panelito;
    }

    private CategoryDataset createDataset(LinkedList<String> labels, LinkedList<Double> valores) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < labels.size(); i++) {
            dataset.addValue(valores.get(i), labels.get(i), labels.get(i));
        }
        return dataset;
    }


}
