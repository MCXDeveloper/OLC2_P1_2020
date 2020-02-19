package com.arbol;

import com.abstracto.Fail;
import com.abstracto.Instruccion;
import com.abstracto.Nodo;
import com.abstracto.Resultado;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

public class NAnd extends Nodo implements Instruccion {

    private Nodo opIzq;
    private Nodo opDer;

    public NAnd(int linea, int columna, String archivo, Nodo opIzq, Nodo opDer) {
        super(linea, columna, archivo, ETipoNodo.EXP_AND);
        this.opIzq = opIzq;
        this.opDer = opDer;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (opIzq != null && opDer != null) {

            Resultado v1, v2;
            v1 = ((Instruccion)opIzq).Ejecutar(ts);
            v2 = ((Instruccion)opDer).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.BOOLEAN && v2.getTipoDato() == ETipoDato.BOOLEAN) {
                tdr = ETipoDato.BOOLEAN;
                valor = ((boolean)v1.getValor()) && ((boolean)v2.getValor());
            } else {
                msj = "Error. No hay implementación para la operación AND para los tipos <"+ v1.getTipoDato() +"> y <"+ v2.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_AND]", msj, getLinea(), getColumna());
            }

        } else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_AND]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
