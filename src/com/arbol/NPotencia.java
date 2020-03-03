package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.LinkedList;

public class NPotencia extends Nodo implements Instruccion {

    private Nodo opIzq;
    private Nodo opDer;

    public NPotencia(int linea, int columna, String archivo, Nodo opIzq, Nodo opDer) {
        super(linea, columna, archivo, ETipoNodo.EXP_POTENCIA);
        this.opIzq = opIzq;
        this.opDer = opDer;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Object valor = new Fail();
        ETipoDato tdr = ETipoDato.ERROR;

        if (opIzq != null && opDer != null) {

            Resultado v1, v2;
            v1 = ((Instruccion) opIzq).Ejecutar(ts);
            v2 = ((Instruccion) opDer).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.DECIMAL;
                valor = Math.pow(((int)v1.getValor()),((int)v2.getValor()));
            } else if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = Math.pow(((int)v1.getValor()),((double)v2.getValor()));
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.DECIMAL;
                valor = Math.pow(((double)v1.getValor()),((int)v2.getValor()));
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = Math.pow(((double) v1.getValor()),((double)v2.getValor()));

                /*
                      ___   ___  ___  ___    _    ___  ___  ___   _  _  ___  ___      ___   ___   __   __ ___  ___  _____  ___   ___  ___  ___
                     / _ \ | _ \| __|| _ \  /_\  / __||_ _|/ _ \ | \| || __|/ __|    |   \ | __|  \ \ / /| __|/ __||_   _|/ _ \ | _ \| __|/ __|
                    | (_) ||  _/| _| |   / / _ \| (__  | || (_) || .` || _| \__ \    | |) || _|    \ V / | _|| (__   | | | (_) ||   /| _| \__ \
                     \___/ |_|  |___||_|_\/_/ \_\\___||___|\___/ |_|\_||___||___/    |___/ |___|    \_/  |___|\___|  |_|  \___/ |_|_\|___||___/

                    */

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && (v2.getTipoDato() == ETipoDato.INT || v2.getTipoDato() == ETipoDato.DECIMAL)) {

                Vector v = (Vector)v1.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación POTENCIA para los tipos <VECTOR["+ tipoInternoVector +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
                } else {
                    NPrim op1;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), v2.getValor(), v2.getTipoDato());
                    for (Item i : v.getElementos()) {
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);
                }

            } else if ((v1.getTipoDato() == ETipoDato.INT || v1.getTipoDato() == ETipoDato.DECIMAL) && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector v = (Vector)v2.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación POTENCIA para los tipos <"+ v1.getTipoDato() +"> y <VECTOR["+ tipoInternoVector +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
                } else {
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), v1.getValor(), v1.getTipoDato());
                    for (Item i : v.getElementos()) {
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);
                }

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector vec1 = (Vector)v1.getValor();
                Vector vec2 = (Vector)v2.getValor();

                if (vec1.getVectorSize() == vec2.getVectorSize()) {

                    Item it1;
                    Item it2;
                    NPrim op1;
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    for (int i = 0; i < vec1.getVectorSize(); i++) {
                        it1 = vec1.getElementByPosition(i);
                        it2 = vec2.getElementByPosition(i);
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else if (vec1.getVectorSize() == 1 && vec2.getVectorSize() > 1) {

                    Item it2;
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    Item it1 = vec1.getElementByPosition(0);
                    NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());

                    for (int i = 0; i < vec2.getVectorSize(); i++) {
                        it2 = vec2.getElementByPosition(i);
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else if (vec1.getVectorSize() > 1 && vec2.getVectorSize() == 1) {

                    Item it1;
                    NPrim op1;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    Item it2 = vec2.getElementByPosition(0);
                    NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());

                    for (int i = 0; i < vec1.getVectorSize(); i++) {
                        it1 = vec1.getElementByPosition(i);
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else {
                    msj = "Error. Los tamaños de los vectores difieren por lo que no se puede realizar la potencia.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
                }

                /*
                   ___   ___  ___  ___    _    ___  ___  ___   _  _  ___  ___      ___   ___    __  __    _  _____  ___  ___  ___  ___  ___
                  / _ \ | _ \| __|| _ \  /_\  / __||_ _|/ _ \ | \| || __|/ __|    |   \ | __|  |  \/  |  /_\|_   _|| _ \|_ _|/ __|| __|/ __|
                 | (_) ||  _/| _| |   / / _ \| (__  | || (_) || .` || _| \__ \    | |) || _|   | |\/| | / _ \ | |  |   / | || (__ | _| \__ \
                  \___/ |_|  |___||_|_\/_/ \_\\___||___|\___/ |_|\_||___||___/    |___/ |___|  |_|  |_|/_/ \_\|_|  |_|_\|___|\___||___||___/

                */

            } else if (v1.getTipoDato() == ETipoDato.MATRIX && (v2.getTipoDato() == ETipoDato.INT || v2.getTipoDato() == ETipoDato.DECIMAL)) {

                Matriz mat = (Matriz)v1.getValor();
                ETipoDato tipoInternoMatriz = mat.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoMatriz)) {
                    msj = "Error. No hay implementación para la operación POTENCIA para los tipos <MATRIX["+ tipoInternoMatriz +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
                } else {
                    NPrim op1;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), v2.getValor(), v2.getTipoDato());
                    for (Item i : mat.getElementos()) {
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat.getFilas(), mat.getColumnas(), li);
                }

            } else if ((v1.getTipoDato() == ETipoDato.INT || v1.getTipoDato() == ETipoDato.DECIMAL) && v2.getTipoDato() == ETipoDato.MATRIX) {

                Matriz mat = (Matriz)v2.getValor();
                ETipoDato tipoInternoMatriz = mat.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoMatriz)) {
                    msj = "Error. No hay implementación para la operación POTENCIA para los tipos <"+ v1.getTipoDato() +"> y <MATRIX["+ tipoInternoMatriz +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
                } else {
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), v1.getValor(), v1.getTipoDato());
                    for (Item i : mat.getElementos()) {
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat.getFilas(), mat.getColumnas(), li);
                }

            } else if (v1.getTipoDato() == ETipoDato.MATRIX && v2.getTipoDato() == ETipoDato.MATRIX) {

                Matriz mat1 = (Matriz)v1.getValor();
                Matriz mat2 = (Matriz)v2.getValor();

                if (mat1.getSize() == mat2.getSize()) {

                    Item it1;
                    Item it2;
                    NPrim op1;
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();

                    for (int i = 0; i < mat1.getMatrixSize(); i++) {
                        it1 = mat1.getElementByPosition(i);
                        it2 = mat2.getElementByPosition(i);
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), it1.getValor(), it1.getTipo());
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), it2.getValor(), it2.getTipo());
                        r = new NPotencia(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat1.getFilas(), mat1.getColumnas(), li);

                } else {
                    msj = "Error. Las dimensiones de la matriz difieren por lo que no se puede realizar la potencia.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
                }

            } else {
                msj = "Error. No hay implementación para la operación POTENCIA para los tipos <"+ v1.getTipoDato() +"> y <"+ v2.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
            }

        }else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_POTENCIA]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

}
