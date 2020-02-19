/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gui;

import javax.swing.*;

/**
 *
 * @author mcalderon
 */
public class Ventana extends javax.swing.JFrame {

    /**
     * Creates new form Ventana
     */
    public Ventana() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        radioBtnGroup = new javax.swing.ButtonGroup();
        panelAreaTrabajo = new javax.swing.JPanel();
        tabContainer = new javax.swing.JTabbedPane();
        panelConsola = new javax.swing.JPanel();
        jspConsola = new javax.swing.JScrollPane();
        consolaSalida = new javax.swing.JTextPane();
        btnAnalizarAscendente = new javax.swing.JButton();
        btnAnalizarDescendente = new javax.swing.JButton();
        panelGraficos = new javax.swing.JPanel();
        graphContainer = new javax.swing.JTabbedPane();
        barraMenu = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        btnNuevo = new javax.swing.JMenuItem();
        btnAbrir = new javax.swing.JMenuItem();
        btnGuardar = new javax.swing.JMenuItem();
        btnGuardarComo = new javax.swing.JMenuItem();
        menuReportes = new javax.swing.JMenu();
        btnReporteAST = new javax.swing.JMenuItem();
        btnReporteErrores = new javax.swing.JMenuItem();
        btnReporteTS = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelAreaTrabajo.setBorder(javax.swing.BorderFactory.createTitledBorder("Área de trabajo"));

        javax.swing.GroupLayout panelAreaTrabajoLayout = new javax.swing.GroupLayout(panelAreaTrabajo);
        panelAreaTrabajo.setLayout(panelAreaTrabajoLayout);
        panelAreaTrabajoLayout.setHorizontalGroup(
                panelAreaTrabajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAreaTrabajoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tabContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                                .addContainerGap())
        );
        panelAreaTrabajoLayout.setVerticalGroup(
                panelAreaTrabajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelAreaTrabajoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tabContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                                .addContainerGap())
        );

        panelConsola.setBorder(javax.swing.BorderFactory.createTitledBorder("Consola"));

        consolaSalida.setEditable(false);
        consolaSalida.setBackground(new java.awt.Color(0, 0, 0));
        jspConsola.setViewportView(consolaSalida);

        javax.swing.GroupLayout panelConsolaLayout = new javax.swing.GroupLayout(panelConsola);
        panelConsola.setLayout(panelConsolaLayout);
        panelConsolaLayout.setHorizontalGroup(
                panelConsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelConsolaLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jspConsola)
                                .addContainerGap())
        );
        panelConsolaLayout.setVerticalGroup(
                panelConsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelConsolaLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jspConsola, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                                .addContainerGap())
        );

        btnAnalizarAscendente.setText("Analizar Ascendente");
        btnAnalizarAscendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarAscendenteActionPerformed(evt);
            }
        });

        btnAnalizarDescendente.setText("Analizar Descendente");
        btnAnalizarDescendente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarDescendenteActionPerformed(evt);
            }
        });

        panelGraficos.setBorder(javax.swing.BorderFactory.createTitledBorder("Gráficos"));

        javax.swing.GroupLayout panelGraficosLayout = new javax.swing.GroupLayout(panelGraficos);
        panelGraficos.setLayout(panelGraficosLayout);
        panelGraficosLayout.setHorizontalGroup(
                panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelGraficosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(graphContainer)
                                .addContainerGap())
        );
        panelGraficosLayout.setVerticalGroup(
                panelGraficosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelGraficosLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(graphContainer)
                                .addContainerGap())
        );

        menuArchivo.setText("Archivo");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        menuArchivo.add(btnNuevo);

        btnAbrir.setText("Abrir");
        btnAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(btnAbrir);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(btnGuardar);

        btnGuardarComo.setText("Guardar como");
        btnGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarComoActionPerformed(evt);
            }
        });
        menuArchivo.add(btnGuardarComo);

        barraMenu.add(menuArchivo);

        menuReportes.setText("Reportes");

        btnReporteAST.setText("AST");
        btnReporteAST.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteASTActionPerformed(evt);
            }
        });
        menuReportes.add(btnReporteAST);

        btnReporteErrores.setText("Errores");
        btnReporteErrores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteErroresActionPerformed(evt);
            }
        });
        menuReportes.add(btnReporteErrores);

        btnReporteTS.setText("Tabla de simbolos");
        btnReporteTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteTSActionPerformed(evt);
            }
        });
        menuReportes.add(btnReporteTS);

        barraMenu.add(menuReportes);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(panelConsola, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(panelAreaTrabajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 47, Short.MAX_VALUE)
                                                                .addComponent(btnAnalizarAscendente, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(btnAnalizarDescendente, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAnalizarDescendente, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAnalizarAscendente, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(panelAreaTrabajo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(panelGraficos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(panelConsola, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnAbrirActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnReporteASTActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnReporteErroresActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnReporteTSActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnAnalizarAscendenteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnAnalizarDescendenteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventana.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    // Variables declaration - do not modify
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JMenuItem btnAbrir;
    private javax.swing.JButton btnAnalizarAscendente;
    private javax.swing.JButton btnAnalizarDescendente;
    private javax.swing.JMenuItem btnGuardar;
    private javax.swing.JMenuItem btnGuardarComo;
    private javax.swing.JMenuItem btnNuevo;
    private javax.swing.JMenuItem btnReporteAST;
    private javax.swing.JMenuItem btnReporteErrores;
    private javax.swing.JMenuItem btnReporteTS;
    private javax.swing.JTextPane consolaSalida;
    private javax.swing.JTabbedPane graphContainer;
    private javax.swing.JScrollPane jspConsola;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenu menuReportes;
    private javax.swing.JPanel panelAreaTrabajo;
    private javax.swing.JPanel panelConsola;
    private javax.swing.JPanel panelGraficos;
    private javax.swing.ButtonGroup radioBtnGroup;
    private javax.swing.JTabbedPane tabContainer;
    // End of variables declaration
}