package com.entorno;

import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import com.arbol.NFunc;
import java.util.TreeMap;
import com.constantes.EAmbito;
import com.constantes.ETipoDato;
import com.estaticas.Manejador;

public class TablaSimbolos {

    private int node;
    private LinkedList<String> nodePointers;
    private LinkedList<String> nodeDeclarations;
    private Ambito actual;
    private Ambito global;
    private final Stack<Ambito> llamadas;
    private final Map<String, NFunc> funciones;

    public TablaSimbolos() {
        this.node = 0;
        this.nodePointers = new LinkedList<>();
        this.nodeDeclarations = new LinkedList<>();
        this.actual = new Ambito(EAmbito.GLOBAL);
        this.global = this.actual;
        this.llamadas = new Stack<>();
        this.funciones = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public boolean addMetodo(NFunc fun) {
        if(funciones.containsKey(fun.getId())) {
            return false;
        }
        funciones.put(fun.getId(), fun);
        Manejador.TSEstatica.add(new String[] { String.valueOf(actual.getTipo()), "FUNCION", String.valueOf(ETipoDato.ANY), fun.getId() });
        return true;
    }

    public NFunc getMetodo(String id) {
        return funciones.get(id);
    }

    public boolean addSimbolo(Simbolo sim) {
        return actual.addSimbolo(sim);
    }

    public Simbolo getSimbolo(String id, boolean wantRef) {
        return actual.getSimbolo(id, wantRef);
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

    public void updateSimbolo(Simbolo sim) {
        actual.updateSimbolo(sim);
    }

    private String getIdNodo() {
        return "Nodo" + (node++);
    }

    public String getDeclararNodo(String tag) {
        String id = getIdNodo();
        String nodito = id + "[ label = \""+ tag +"\" ]";
        nodeDeclarations.add(nodito);
        return id;
    }

    public void enlazarNodos(String ptr1, String ptr2) {
         nodePointers.add(ptr1 + " -> " + ptr2);
    }

    public LinkedList<String> getNodePointers() {
        return nodePointers;
    }

    public LinkedList<String> getNodeDeclarations() {
        return nodeDeclarations;
    }
}