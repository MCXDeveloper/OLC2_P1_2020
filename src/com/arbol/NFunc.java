package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;

public class NFunc extends Nodo implements Instruccion {

    private String id;
    private ETipoDato tipo;

    public NFunc(int linea, int columna, String archivo, ETipoNodo tipoNodo) {
        super(linea, columna, archivo, tipoNodo);
    }

    public String getId() {
        return id;
    }

    public ETipoDato getTipo() {
        return tipo;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {
        return null;
    }
}