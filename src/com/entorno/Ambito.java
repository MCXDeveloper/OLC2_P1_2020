package com.entorno;

import com.constantes.EAmbito;
import com.estaticas.Manejador;

import java.util.HashMap;

public class Ambito {

    private EAmbito tipo;
    private Ambito superior;
    private HashMap<String, Simbolo> tabla;

    public Ambito(EAmbito tipo){
        this.tipo = tipo;
        this.superior = null;
        this.tabla = new HashMap<>();
    }

    public Ambito(EAmbito tipo, Ambito superior){
        this(tipo);
        this.superior = superior;
    }

    public Simbolo getSimbolo(String id) {

        Simbolo s = null;
        Ambito a = this;

        while(a != null){
            s = a.tabla.get(id);
            if(s != null)
                break;

            a = a.superior;
        }

        return s;
    }

    public boolean addSimbolo(String id, Simbolo sim){
        if(tabla.containsKey(id)) {
            return false;
        }
        tabla.put(id, sim);
        Manejador.TSEstatica.add(new String[] { String.valueOf(tipo), "VARIABLE", String.valueOf(sim.getTipo()), id });
        return true;
    }

    public void updateSimbolo(String id, Simbolo sim) {
        Simbolo s;
        Ambito a = this;
        while(a != null){
            s = a.tabla.get(id);
            if(s != null) {
                a.tabla.put(id, sim);
                break;
            } else {
                a = a.superior;
            }
        }
    }

    public boolean enCiclo(){
        Ambito a = this;
        while(a != null){
            if(a.tipo == EAmbito.CICLO) {
                return true;
            }
            a = a.superior;
        }
        return false;
    }

    public Ambito destruir(){
        Ambito sup = superior;
        superior = null;
        tabla = null;
        return sup;
    }

    public void imprimirSimbolos() {
        System.out.println("*********************************************");
        for (Simbolo s : tabla.values()) {
            System.out.println("Nombre: " + s.getId() + " | Tipo: " + s.getTipo() + " | Valor: " + s.getValor().toString());
        }
        System.out.println("*********************************************");
    }

    public void setSuperior(Ambito superior) {
        this.superior = superior;
    }

    public EAmbito getTipo() {
        return tipo;
    }
}