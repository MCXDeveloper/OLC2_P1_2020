package com.graficos;

import com.abstracto.Item;
import com.abstracto.Matriz;
import com.abstracto.Nodo;
import com.constantes.ETipoNodo;
import com.main.Main;
import org.apache.commons.collections4.MultiValuedMap;
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

    public HistogramChart(int linea, int columna, String archivo, String titulo, String xlab, LinkedList<Double> xlim, LinkedList<Double> ylim, Object valores) {

        super(linea, columna, archivo, ETipoNodo.STMT_HIST);

        chart = ChartFactory.createHistogram(titulo, xlab, "", createDataset(valores, xlim, ylim), PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(Color.white);

    }

    private IntervalXYDataset createDataset(Object valores, LinkedList<Double> xlim, LinkedList<Double> ylim) {

        Matriz mat = (Matriz)valores;
        LinkedList<Double> ld = new LinkedList<>();
        for (Item it : mat.getElementos()) {
            ld.add((double)it.getValor());
        }

        double[] arr = ld.stream().mapToDouble(Double::doubleValue).toArray();

        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("Hist", arr, arr.length);

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