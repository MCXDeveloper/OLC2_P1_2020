package com.abstracto;

import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;

public class Nodo {

    private int linea;
    private int columna;
    private String archivo;
    private ETipoNodo tipoNodo;

    public Nodo(int linea, int columna, String archivo, ETipoNodo tipoNodo) {
        this.linea = linea;
        this.columna = columna;
        this.archivo = archivo;
        this.tipoNodo = tipoNodo;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getArchivo() {
        return archivo;
    }

    public ETipoNodo getTipoNodo() {
        return tipoNodo;
    }

    public String getTipoError() {
        return "Semántico";
    }

    public LinkedList<Double> validarDatosEstadisticos(LinkedList<Item> valores, Resultado xtrim) {
        LinkedList<Double> ret = new LinkedList<>();
        if (xtrim != null) {
            for (Item it : valores) {
                double x = (it.getTipo() == ETipoDato.INT) ? (double)(int)it.getValor() : (double)it.getValor();
                if (x >= (double)xtrim.getValor()) {
                    ret.add(x);
                }
            }
            if (ret.size() == 0) {
                ret = addAllValues(valores);
            }
        } else {
            ret = addAllValues(valores);
        }
        return ret;
    }

    private LinkedList<Double> addAllValues(LinkedList<Item> valores) {
        LinkedList<Double> ret = new LinkedList<>();
        for (Item it : valores) {
            ret.add((it.getTipo() == ETipoDato.INT) ? (double)(int)it.getValor() : (double)it.getValor());
        }
        return ret;
    }

    public Resultado validarTrim(String location, Nodo trim, TablaSimbolos ts) {

        String msj;
        Resultado rtrim = ((Instruccion)trim).Ejecutar(ts);

        switch (rtrim.getTipoDato()) {
            case INT: {
                return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, (double)(int)rtrim.getValor());
            }
            case DECIMAL: {
                return rtrim;
            }
            case VECTOR: {
                Vector v = (Vector)rtrim.getValor();
                if (v.getInnerType() != ETipoDato.INT && v.getInnerType() != ETipoDato.DECIMAL) {
                    msj = "Error. El parámetro 'trim' no puede ser de tipo <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }
                if (v.getVectorSize() > 1) {
                    msj = "Error. El parámetro 'trim' no puede ser un vector con más de 1 valor.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                    return null;
                }
                if (v.getInnerType() == ETipoDato.INT) {
                    return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, (double)(int)v.getElementByPosition(0).getValor());
                } else {
                    return new Resultado(ETipoDato.DECIMAL, EFlujo.NORMAL, v.getElementByPosition(0).getValor());
                }
            }
            default: {
                msj = "Error. El parámetro 'trim' no puede ser una expresión de tipo <"+ rtrim.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), location, msj, getLinea(), getColumna());
                return null;
            }
        }

    }

}