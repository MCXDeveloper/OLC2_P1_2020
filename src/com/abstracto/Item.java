package com.abstracto;

import com.constantes.ETipoDato;

public class Item {

    private Object valor;
    private ETipoDato tipo;

    public Item(ETipoDato tipo, Object valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public ETipoDato getTipo() {
        return tipo;
    }

    public void setTipo(ETipoDato tipo) {
        this.tipo = tipo;
    }

    public String getStringItem() {
        return (tipo == ETipoDato.STRING && !valor.toString().equals("NULL")) ? "\"" + valor.toString() + "\"" : valor.toString();
    }

}