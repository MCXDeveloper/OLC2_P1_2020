package com.arbol;

import com.abstracto.*;
import com.constantes.EAmbito;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class NWhile extends Nodo implements Instruccion {

    private Nodo condicion;
    private LinkedList<Nodo> sentencias;

    public NWhile(int linea, int columna, String archivo, Nodo condicion, LinkedList<Nodo> sentencias) {
        super(linea, columna, archivo, ETipoNodo.STMT_WHILE);
        this.condicion = condicion;
        this.sentencias = sentencias;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        Resultado rst = null;
        Resultado rc = evaluarCondicion(((Instruccion)condicion).Ejecutar(ts));

        if (rc != null) {

            while((boolean)rc.getValor()) {

                ts.addAmbito(EAmbito.CICLO);

                for(Nodo nd : sentencias) {

                    if(nd.getTipoNodo() == ETipoNodo.ERROR)
                        continue;

                    rst = ((Instruccion)nd).Ejecutar(ts);
                    if(rst.getFlujo() != EFlujo.NORMAL){
                        break;
                    }

                }

                if(rst != null && rst.getFlujo() != EFlujo.NORMAL){
                    ts.destruirAmbito();
                    break;
                }

                ts.destruirAmbito();
                rc = evaluarCondicion(((Instruccion)condicion).Ejecutar(ts));

            }

            if(rst != null && rst.getFlujo() == EFlujo.RETURN){
                return rst;
            }else{
                rst = new Resultado(ETipoDato.NT, EFlujo.NORMAL);
            }

            return rst;

        }

        return new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());
    }

    private Resultado evaluarCondicion(Resultado rc) {
        String msj;
        switch (rc.getTipoDato()) {
            case BOOLEAN: {
                return rc;
            }
            case VECTOR: {
                Vector v = (Vector)rc.getValor();
                if (v.getInnerType() != ETipoDato.BOOLEAN) {
                    msj = "Error. La condición recibida es de tipo <VECTOR["+ v.getInnerType() +"]>. Se espera un valor booleano.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_WHILE]", msj, getLinea(), getColumna());
                    return null;
                }
                rc.setTipoDato(ETipoDato.BOOLEAN);
                rc.setValor(v.getElementByPosition(0).getValor());
                return rc;
            }
            default: {
                msj = "Error. La condición recibida es de tipo <"+ rc.getTipoDato() +">. Se espera que sea una expresión booleana o un vector de booleanos.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_WHILE]", msj, getLinea(), getColumna());
                return null;
            }
        }
    }

}
