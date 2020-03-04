package com.graficos;

import com.main.Main;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class PieChart {

    private JFreeChart chart;

    public PieChart(String titulo, LinkedList<String> labels, LinkedList<Double> valores) {
        PieDataset dataset = createDataset(labels, valores);
        chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{1}");
        ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);
    }

    public JPanel getPieChart() {
        JTabbedPane jtp = Main.getGUI().getGraphContainer();
        BufferedImage graph = chart.createBufferedImage(jtp.getWidth(), jtp.getHeight());
        JLabel imagen = new JLabel();
        imagen.setSize(jtp.getSize());
        imagen.setIcon(new ImageIcon(graph));
        JPanel panelito = new JPanel();
        panelito.add(imagen);
        return panelito;
    }

    private PieDataset createDataset(LinkedList<String> labels, LinkedList<Double> valores) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < labels.size(); i++) {
            dataset.setValue(labels.get(i), valores.get(i));
        }
        return dataset;
    }



}