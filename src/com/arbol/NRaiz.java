package com.arbol;

import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;
import com.main.Main;

import java.util.Arrays;
import java.util.LinkedList;

public class NRaiz extends Nodo implements Instruccion {

    private LinkedList<Nodo> lista_sentencias;

    public NRaiz(int linea, int columna, String archivo, LinkedList<Nodo> lista_sentencias) {
        super(linea, columna, archivo, ETipoNodo.ROOT);
        this.lista_sentencias = lista_sentencias;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        /* Registro todas las funciones. */
        RegistrarFunciones(ts);

        /* Ejecuto todas las demás instrucciones. */
        Resultado r;
        for (Nodo nodito : lista_sentencias) {
            if (nodito.getTipoNodo() != ETipoNodo.STMT_FUNC) {
                ((Instruccion)nodito).Ejecutar(ts);
            }
        }

        return new Resultado(ETipoDato.NT, EFlujo.NORMAL);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String son;
        String parent = ts.getDeclararNodo("INICIO");
        String listson = ts.getDeclararNodo("LISTA_INSTRUCCIONES");
        ts.enlazarNodos(parent, listson);
        for (Nodo nodito : lista_sentencias) {
            son = ((Instruccion)nodito).GenerarDOT(ts);
            ts.enlazarNodos(listson, son);
        }
        return parent;
    }


    private void RegistrarFunciones(TablaSimbolos ts) {

        NFunc func;

        /* Registro todas las funciones reservadas */
        LinkedList<String> reservadas = new LinkedList<>(Arrays.asList(
                "c",
                "pie",
                "list",
                "ncol",
                "nrow",
                "mean",
                "mode",
                "plot",
                "hist",
                "print",
                "array",
                "trunk",
                "round",
                "matrix",
                "typeof",
                "length",
                "remove",
                "median",
                "barplot",
                "tolowercase",
                "touppercase",
                "stringlength"
        ));

        /* Agrego las funciones reservadas a la tabla de simbolos */
        for (String f : reservadas) {
            ts.addMetodo(new NFunc(getLinea(), getColumna(), getArchivo(), f, new LinkedList<>(), new LinkedList<>()));
        }

        /* Procedo a registrar todas las funciones creadas en el archivo */
        if (lista_sentencias.stream().noneMatch(x -> x.getTipoNodo().equals(ETipoNodo.ERROR))) {
            for (Nodo nodito : lista_sentencias) {
                if (nodito.getTipoNodo().equals(ETipoNodo.STMT_FUNC)) {
                    func = (NFunc) nodito;
                    if (!ts.addMetodo(func)) {
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_RAIZ]", "Error. Ya se registro la función {"+ func.getId() +"}", getLinea(), getColumna());
                    }
                }
            }
        } else {
            Main.getGUI().showMessage("Se detectaron errores en la lista de sentencias.  Revisar pestaña de errores.");
        }

    }
}
