package com.entorno;

import com.arbol.NFunc;
import com.constantes.EAmbito;
import com.estaticas.Manejador;

import java.util.HashMap;
import java.util.Stack;

public class TablaSimbolos {

    private Ambito actual;
    private Ambito global;
    private final Stack<Ambito> llamadas;
    private final HashMap<String, NFunc> funciones;

    public TablaSimbolos() {
        this.actual = new Ambito(EAmbito.GLOBAL);
        this.global = this.actual;
        this.llamadas = new Stack<>();
        this.funciones = new HashMap<>();
    }

    public boolean addMetodo(NFunc fun) {
        if(funciones.containsKey(fun.getId())) {
            return false;
        }
        funciones.put(fun.getId(), fun);
        Manejador.TSEstatica.add(new String[] { String.valueOf(actual.getTipo()), "FUNCION", String.valueOf(fun.getTipo()), fun.getId() });
        return true;
    }

    public NFunc getMetodo(String id) {
        return funciones.get(id);
    }

    public boolean addSimbolo(String id, Simbolo sim) {
        return actual.addSimbolo(id, sim);
    }

    public Simbolo getSimbolo(String id) {
        return actual.getSimbolo(id);
    }

    public void nuevaLLamada(Ambito amb) {
        amb.setSuperior(global);
        llamadas.push(actual);
        actual = amb;
    }

    public void finLLamada(){
        actual = llamadas.pop();
    }

    public boolean enCiclo(){
        return actual.enCiclo();
    }

    public void addAmbito(EAmbito tipo){
        actual = new Ambito(tipo, actual);
    }

    public void destruirAmbito(){
        actual = actual.destruir();
    }

    public void imprimirTS() {
        actual.imprimirSimbolos();
    }

    public void updateSimbolo(String id, Simbolo sim) {
        actual.updateSimbolo(id, sim);
    }

}