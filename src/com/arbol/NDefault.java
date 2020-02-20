package com.arbol;

import com.abstracto.Nodo;
import com.constantes.ETipoNodo;

public class NDefault extends Nodo {

    public NDefault(int linea, int columna, String archivo) {
        super(linea, columna, archivo, ETipoNodo.EXP_DEFAULT);
    }

}
