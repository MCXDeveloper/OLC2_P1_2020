package com.main;

import com.gui.Ventana;

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
        System.out.println("Saludo inicial!");
    }

}