package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.main.Main;
import java.awt.Color;
import java.util.LinkedList;

public class NPrint extends Nodo implements Instruccion {

    private Nodo elemento;

    public NPrint(int linea, int columna, String archivo, Nodo elemento) {
        super(linea, columna, archivo, ETipoNodo.STMT_PRINT);
        this.elemento = elemento;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado r = ((Instruccion)elemento).Ejecutar(ts);

        switch (r.getTipoDato()) {
            case ERROR: {
                return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
            }
            case VECTOR: {
                if (((Vector)r.getValor()).getInnerType() == ETipoDato.ERROR) {
                    return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
                }
                Main.getGUI().appendSalida(r.getValor().toString(), Color.CYAN);
            }   break;
            case ARRAY: {
                LinkedList<String> lista = ((Arreglo)r.getValor()).imprimirArreglo();
                for (String s : lista) {
                    Main.getGUI().appendSalida(s, Color.CYAN);
                }
            }   break;
            default: {
                Main.getGUI().appendSalida(r.getValor().toString(), Color.CYAN);
            }   break;
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);
    }

}
