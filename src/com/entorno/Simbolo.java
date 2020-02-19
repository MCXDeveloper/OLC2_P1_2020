package com.entorno;

import com.constantes.ETipoDato;

public class Simbolo {

    private String id;
    private Object valor;
    private ETipoDato tipo;

    public Simbolo(ETipoDato tipo){
        this.tipo = tipo;
    }

    public Simbolo(ETipoDato tipo, Object valor){
        this.tipo = tipo;
        this.valor = valor;
    }

    public Simbolo(ETipoDato tipo, String id) {
        this.id = id;
        this.tipo = tipo;
    }

    public Simbolo(ETipoDato tipo, String id, Object valor) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}