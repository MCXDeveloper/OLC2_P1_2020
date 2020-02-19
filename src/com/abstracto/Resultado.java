package com.abstracto;

import com.constantes.EFlujo;
import com.constantes.ETipoDato;

public class Resultado {

    private Object valor;
    private EFlujo flujo;
    private ETipoDato tipoDato;

    public Resultado(ETipoDato tipoDato, EFlujo flujo){
        this.valor = null;
        this.flujo = flujo;
        this.tipoDato = tipoDato;
    }

    public Resultado(ETipoDato tipoDato, EFlujo flujo, Object valor){
        this.valor = valor;
        this.flujo = flujo;
        this.tipoDato = tipoDato;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public EFlujo getFlujo() {
        return flujo;
    }

    public void setFlujo(EFlujo flujo) {
        this.flujo = flujo;
    }

    public ETipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(ETipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }
}
