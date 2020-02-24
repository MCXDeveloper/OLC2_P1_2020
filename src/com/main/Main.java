package com.main;

import com.estaticas.ErrorHandler;
import com.estaticas.Manejador;
import com.gui.Ventana;

import java.util.LinkedList;

public class Main {

    private static Ventana window;

    public static Ventana getGUI() {
        return window;
    }

    public static void main(String[] args) {
        cleaner();
        window = new Ventana();
        window.setVisible(true);
    }

    public static void cleaner() {
        Manejador.TSEstatica = new LinkedList<>();
        ErrorHandler.ListaErrores = new LinkedList<>();
    }

}