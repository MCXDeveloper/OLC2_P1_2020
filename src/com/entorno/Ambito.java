package com.entorno;

import java.util.Map;
import java.util.TreeMap;
import com.constantes.EAmbito;
import com.estaticas.Manejador;

public class Ambito {

    private EAmbito tipo;
    private Ambito superior;
    private Map<String, Simbolo> tabla;

    public Ambito(EAmbito tipo){
        this.tipo = tipo;
        this.superior = null;
        this.tabla = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
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

    public boolean addSimbolo(Simbolo sim){
        if(tabla.containsKey(sim.getId())) {
            return false;
        }
        tabla.put(sim.getId(), sim);
        Manejador.TSEstatica.add(new String[] { String.valueOf(tipo), "VARIABLE", String.valueOf(sim.getTipo()), sim.getId() });
        return true;
    }

    public void updateSimbolo(Simbolo sim) {
        Simbolo s;
        Ambito a = this;
        while(a != null){
            s = a.tabla.get(sim.getId());
            if(s != null) {
                a.tabla.put(sim.getId(), sim);
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