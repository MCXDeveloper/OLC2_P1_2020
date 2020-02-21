package com.abstracto;

import com.constantes.ETipoDimension;

public class Dimension {

    private Nodo valorDimIzq;
    private Nodo valorDimDer;
    private ETipoDimension tipoDim;

    public Dimension(ETipoDimension tipoDim, Nodo valorDimIzq) {
        this.tipoDim = tipoDim;
        this.valorDimIzq = valorDimIzq;
    }

    public Dimension(ETipoDimension tipoDim, Nodo valorDimIzq, Nodo valorDimDer) {
        this.tipoDim = tipoDim;
        this.valorDimIzq = valorDimIzq;
        this.valorDimDer = valorDimDer;
    }

    public ETipoDimension getTipoDim() {
        return tipoDim;
    }

    public Nodo getValorDimIzq() {
        return valorDimIzq;
    }

    public Nodo getValorDimDer() {
        return valorDimDer;
    }
}