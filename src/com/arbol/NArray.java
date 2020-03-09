package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;
import java.util.Optional;

public class NArray extends Nodo implements Instruccion {

    private Nodo valores;
    private Nodo dimensiones;

    public NArray(int linea, int columna, String archivo, Nodo valores, Nodo dimensiones) {
        super(linea, columna, archivo, ETipoNodo.EXP_ARRAY);
        this.valores = valores;
        this.dimensiones = dimensiones;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object rvalor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        Resultado rdims = ((Instruccion)dimensiones).Ejecutar(ts);

        if (validateDimensionsValsAndType(rdims)) {

            Vector v = (Vector)rdims.getValor();
            Resultado rvals = ((Instruccion)valores).Ejecutar(ts);

            switch (rvals.getTipoDato()) {

                case INT:
                case DECIMAL:
                case STRING:
                case BOOLEAN: {

                    LinkedList<Integer> lints = getListDimsSizesFromVector(v);
                    int arrSize = getArraySize(lints);

                    LinkedList<Item> li = new LinkedList<>();
                    for (int i = 0; i < arrSize; i++) {
                        li.add(new Item(rvals.getTipoDato(), rvals.getValor()));
                    }

                    tdr = ETipoDato.ARRAY;
                    rvalor = new Arreglo(arrSize, lints, li);

                }   break;

                case LIST:
                case VECTOR: {

                    int cnt = 0;
                    int eddSize = (rvals.getTipoDato() == ETipoDato.VECTOR) ? ((Vector)rvals.getValor()).getVectorSize() : ((Lista)rvals.getValor()).getListSize();
                    LinkedList<Item> itemVals = (rvals.getTipoDato() == ETipoDato.VECTOR) ? ((Vector)rvals.getValor()).getElementos() : ((Lista)rvals.getValor()).getElementos();

                    LinkedList<Integer> lints = getListDimsSizesFromVector(v);
                    int arrSize = getArraySize(lints);

                    Item it;
                    Vector vec;
                    LinkedList<Item> li = new LinkedList<>();
                    for (int i = 0; i < arrSize; i++) {

                        it = itemVals.get(cnt);

                        if (it.getTipo() == ETipoDato.VECTOR) {
                            vec = (Vector)it.getValor();
                            if (vec.getVectorSize() == 1) {
                                li.add(new Item(vec.getInnerType(), vec.getElementByPosition(0).getValor()));
                            } else {
                                li.add(new Item(it.getTipo(), it.getValor()));
                            }
                        } else {
                            li.add(new Item(it.getTipo(), it.getValor()));
                        }

                        cnt++;
                        if (cnt == eddSize) {
                            cnt = 0;
                        }

                    }

                    tdr = ETipoDato.ARRAY;
                    rvalor = new Arreglo(arrSize, lints, li);

                }   break;

                default: {
                    msj = "Error. La expresión recibida como valores del arreglo es de tipo <"+ rdims.getTipoDato() +"> y se espera que sea de tipo [PRIMITIVO|VECTOR|LIST].";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ARRAY]", msj, getLinea(), getColumna());
                }   break;

            }

        }

        return new Resultado(tdr, EFlujo.NORMAL, rvalor);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private boolean validateDimensionsValsAndType(Resultado rdims) {

        String msj;

        /* Valido que la expresión recibida como lista de dimensiones se de tipo vector. */
        if (rdims.getTipoDato() != ETipoDato.VECTOR) {
            msj = "Error. La expresión recibida como dimensiones es de tipo <"+ rdims.getTipoDato() +">. Se espera que sea de tipo <VECTOR>.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ARRAY]", msj, getLinea(), getColumna());
            return false;
        }

        /* Valido que el vector recibido contenga valores de tipo INT */
        Vector v = (Vector)rdims.getValor();
        if (v.getInnerType() != ETipoDato.INT) {
            msj = "Error. La expresión recibida como dimensiones es de tipo <VECTOR["+ rdims.getTipoDato() +"]>. Se espera que sea de tipo <VECTOR[INT]>.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ARRAY]", msj, getLinea(), getColumna());
            return false;
        }

        /* Valido que los valores del vector sean mayor a cero. */
        Optional<Item> opItem =  v.getElementos().stream().filter(it -> ((int)it.getValor()) < 1).findAny();
        if (opItem.isPresent()) {
            msj = "Error. Se espera que los valores que representan el tamaño de las dimensiones sean mayores a cero.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ARRAY]", msj, getLinea(), getColumna());
            return false;
        }

        return true;

    }

    private LinkedList<Integer> getListDimsSizesFromVector(Vector v) {
        LinkedList<Integer> l = new LinkedList<>();
        for (Item it : v.getElementos()) {
            l.add((int)it.getValor());
        }
        return l;
    }

    private int getArraySize(LinkedList<Integer> values) {
        int size = 1;
        for (int i : values) {
            size = size * i;
        }
        return size;
    }

}
