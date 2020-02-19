package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.main.Main;
import java.awt.Color;

public class NPrint extends Nodo implements Instruccion {

    private Nodo elemento;

    public NPrint(int linea, int columna, String archivo, Nodo elemento) {
        super(linea, columna, archivo, ETipoNodo.STMT_PRINT);
        this.elemento = elemento;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {
        Resultado r = ((Instruccion)elemento).Ejecutar(ts);
        Main.getGUI().appendSalida(r.getValor().toString(), Color.CYAN);
        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);
    }

}
