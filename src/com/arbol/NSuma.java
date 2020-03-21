package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Arrays;
import java.util.LinkedList;

public class NSuma extends Nodo implements Instruccion {

    private Nodo opIzq;
    private Nodo opDer;

    public NSuma(int linea, int columna, String archivo, Nodo opIzq, Nodo opDer) {
        super(linea, columna, archivo, ETipoNodo.EXP_SUMA);
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
            v1 = ((Instruccion)opIzq).Ejecutar(ts);
            v2 = ((Instruccion)opDer).Ejecutar(ts);

            if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.INT;
                valor = ((int)v1.getValor()) + ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = ((int)v1.getValor()) + ((double)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.DECIMAL;
                valor = ((double)v1.getValor()) + ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.DECIMAL;
                valor = ((double)v1.getValor()) + ((double)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.STRING && v2.getTipoDato() == ETipoDato.INT) {
                tdr = ETipoDato.STRING;
                valor = (v1.getValor().toString()) + ((int)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.STRING && v2.getTipoDato() == ETipoDato.DECIMAL) {
                tdr = ETipoDato.STRING;
                valor = (v1.getValor().toString()) + ((double)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.STRING && v2.getTipoDato() == ETipoDato.BOOLEAN) {
                tdr = ETipoDato.STRING;
                valor = (v1.getValor().toString()) + ((boolean)v2.getValor());
            } else if (v1.getTipoDato() == ETipoDato.STRING && v2.getTipoDato() == ETipoDato.STRING) {
                tdr = ETipoDato.STRING;
                valor = (v1.getValor().toString()) + (v2.getValor().toString());
            } else if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.STRING) {
                tdr = ETipoDato.STRING;
                valor = ((int)v1.getValor()) + (v2.getValor().toString());
            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.STRING) {
                tdr = ETipoDato.STRING;
                valor = ((double)v1.getValor()) + (v2.getValor().toString());
            } else if (v1.getTipoDato() == ETipoDato.BOOLEAN && v2.getTipoDato() == ETipoDato.STRING) {
                tdr = ETipoDato.STRING;
                valor = ((boolean) v1.getValor()) + (v2.getValor().toString());

                /*
                  ___   ___  ___  ___    _    ___  ___  ___   _  _  ___  ___      ___   ___   __   __ ___  ___  _____  ___   ___  ___  ___
                 / _ \ | _ \| __|| _ \  /_\  / __||_ _|/ _ \ | \| || __|/ __|    |   \ | __|  \ \ / /| __|/ __||_   _|/ _ \ | _ \| __|/ __|
                | (_) ||  _/| _| |   / / _ \| (__  | || (_) || .` || _| \__ \    | |) || _|    \ V / | _|| (__   | | | (_) ||   /| _| \__ \
                 \___/ |_|  |___||_|_\/_/ \_\\___||___|\___/ |_|\_||___||___/    |___/ |___|    \_/  |___|\___|  |_|  \___/ |_|_\|___||___/

                */
            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.INT) {

                Vector v = (Vector)v1.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <VECTOR["+ tipoInternoVector +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();

                    if (tipoInternoVector == ETipoDato.INT) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.INT, ((int)i.getValor()) + ((int)v2.getValor())));
                        }
                    } else if (tipoInternoVector == ETipoDato.DECIMAL) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.DECIMAL, ((double)i.getValor()) + ((int)v2.getValor())));
                        }
                    } else {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (i.getValor().toString()) + ((int)v2.getValor())));
                        }
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.DECIMAL) {

                Vector v = (Vector)v1.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <VECTOR["+ tipoInternoVector +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();

                    if (tipoInternoVector == ETipoDato.INT) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.DECIMAL, ((int)i.getValor()) + ((double)v2.getValor())));
                        }
                    } else if (tipoInternoVector == ETipoDato.DECIMAL) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.DECIMAL, ((double)i.getValor()) + ((double)v2.getValor())));
                        }
                    } else {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (i.getValor().toString()) + ((double)v2.getValor())));
                        }
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.BOOLEAN) {

                Vector v = (Vector)v1.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <VECTOR["+ tipoInternoVector +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();
                    for (Item i : v.getElementos()) {
                        li.add(new Item(ETipoDato.STRING, (i.getValor().toString() + ((boolean)v2.getValor()))));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.STRING) {

                Vector v = (Vector)v1.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.BOOLEAN, ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <VECTOR["+ tipoInternoVector +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();

                    if (tipoInternoVector == ETipoDato.INT) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, ((int) i.getValor()) + v2.getValor().toString()));
                        }
                    } else if (tipoInternoVector == ETipoDato.DECIMAL) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, ((double) i.getValor()) + v2.getValor().toString()));
                        }
                    } else if (tipoInternoVector == ETipoDato.BOOLEAN) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, ((boolean) i.getValor()) + v2.getValor().toString()));
                        }
                    } else {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (i.getValor().toString()) + v2.getValor().toString()));
                        }
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.INT && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector v = (Vector)v2.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <"+ v1.getTipoDato() +"> y <VECTOR["+ tipoInternoVector +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();

                    if (tipoInternoVector == ETipoDato.INT) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.INT, ((int)v1.getValor() + (int)i.getValor())));
                        }
                    } else if (tipoInternoVector == ETipoDato.DECIMAL) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.DECIMAL, ((int)v1.getValor() + (double)i.getValor())));
                        }
                    } else {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, ((int)v1.getValor() + i.getValor().toString())));
                        }
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.DECIMAL && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector v = (Vector)v2.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <"+ v1.getTipoDato() +"> y <VECTOR["+ tipoInternoVector +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();

                    if (tipoInternoVector == ETipoDato.INT) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.DECIMAL, ((double)v1.getValor() + (int)i.getValor())));
                        }
                    } else if (tipoInternoVector == ETipoDato.DECIMAL) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.DECIMAL, ((double)v1.getValor() + (double)i.getValor())));
                        }
                    } else {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, ((double)v1.getValor() + i.getValor().toString())));
                        }
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.BOOLEAN && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector v = (Vector)v2.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <"+ v1.getTipoDato() +"> y <VECTOR["+ tipoInternoVector +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();
                    for (Item i : v.getElementos()) {
                        li.add(new Item(ETipoDato.STRING, ((boolean)v1.getValor() + i.getValor().toString())));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.STRING && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector v = (Vector)v2.getValor();
                ETipoDato tipoInternoVector = v.getInnerType();
                ETipoDato[] tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.BOOLEAN, ETipoDato.STRING };

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoVector)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <"+ v1.getTipoDato() +"> y <VECTOR["+ tipoInternoVector +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {

                    LinkedList<Item> li = new LinkedList<>();

                    if (tipoInternoVector == ETipoDato.INT) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (v1.getValor().toString() + (int)i.getValor())));
                        }
                    } else if (tipoInternoVector == ETipoDato.DECIMAL) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (v1.getValor().toString() + (double) i.getValor())));
                        }
                    } else if (tipoInternoVector == ETipoDato.BOOLEAN) {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (v1.getValor().toString() + (boolean) i.getValor())));
                        }
                    } else {
                        for (Item i : v.getElementos()) {
                            li.add(new Item(ETipoDato.STRING, (v1.getValor().toString() + i.getValor().toString())));
                        }
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                }

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector vec1 = (Vector) v1.getValor();
                Vector vec2 = (Vector) v2.getValor();

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
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
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
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
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
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.VECTOR;
                    valor = new Vector(li);

                } else {
                    msj = "Error. Los tamaños de los vectores difieren por lo que no se puede realizar la suma.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                }

            /*
                   ___   ___  ___  ___    _    ___  ___  ___   _  _  ___  ___      ___   ___    __  __    _  _____  ___  ___  ___  ___  ___
                  / _ \ | _ \| __|| _ \  /_\  / __||_ _|/ _ \ | \| || __|/ __|    |   \ | __|  |  \/  |  /_\|_   _|| _ \|_ _|/ __|| __|/ __|
                 | (_) ||  _/| _| |   / / _ \| (__  | || (_) || .` || _| \__ \    | |) || _|   | |\/| | / _ \ | |  |   / | || (__ | _| \__ \
                  \___/ |_|  |___||_|_\/_/ \_\\___||___|\___/ |_|\_||___||___/    |___/ |___|  |_|  |_|/_/ \_\|_|  |_|_\|___|\___||___||___/

            */

            } else if ((v1.getTipoDato() == ETipoDato.INT || v1.getTipoDato() == ETipoDato.DECIMAL || v1.getTipoDato() == ETipoDato.BOOLEAN || v1.getTipoDato() == ETipoDato.STRING) && v2.getTipoDato() == ETipoDato.MATRIX) {

                ETipoDato[] tiposPermitidos;
                Matriz mat = (Matriz)v2.getValor();
                ETipoDato tipoInternoMatriz = mat.getInnerType();

                if (v1.getTipoDato() == ETipoDato.INT || v1.getTipoDato() == ETipoDato.DECIMAL) {
                    tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.STRING };
                } else if (v1.getTipoDato() == ETipoDato.BOOLEAN) {
                    tiposPermitidos = new ETipoDato[] { ETipoDato.STRING };
                } else {
                    tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.BOOLEAN, ETipoDato.STRING };
                }

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoMatriz)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <"+ v1.getTipoDato() +"> y <MATRIX["+ tipoInternoMatriz +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), v1.getValor(), v1.getTipoDato());
                    for (Item i : mat.getElementos()) {
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat.getFilas(), mat.getColumnas(), li);
                }

            } else if (v1.getTipoDato() == ETipoDato.MATRIX && (v2.getTipoDato() == ETipoDato.INT || v2.getTipoDato() == ETipoDato.DECIMAL || v2.getTipoDato() == ETipoDato.BOOLEAN || v2.getTipoDato() == ETipoDato.STRING)) {

                ETipoDato[] tiposPermitidos;
                Matriz mat = (Matriz)v1.getValor();
                ETipoDato tipoInternoMatriz = mat.getInnerType();

                if (v2.getTipoDato() == ETipoDato.INT || v2.getTipoDato() == ETipoDato.DECIMAL) {
                    tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.STRING };
                } else if (v2.getTipoDato() == ETipoDato.BOOLEAN) {
                    tiposPermitidos = new ETipoDato[] { ETipoDato.STRING };
                } else {
                    tiposPermitidos = new ETipoDato[] { ETipoDato.INT, ETipoDato.DECIMAL, ETipoDato.BOOLEAN, ETipoDato.STRING };
                }

                if (!Arrays.asList(tiposPermitidos).contains(tipoInternoMatriz)) {
                    msj = "Error. No hay implementación para la operación SUMA para los tipos <MATRIX["+ tipoInternoMatriz +"]> y <"+ v2.getTipoDato() +">.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {
                    NPrim op1;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), v2.getValor(), v2.getTipoDato());
                    for (Item i : mat.getElementos()) {
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat.getFilas(), mat.getColumnas(), li);
                }

            } else if (v1.getTipoDato() == ETipoDato.VECTOR && v2.getTipoDato() == ETipoDato.MATRIX) {

                Vector v = (Vector)v1.getValor();
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede realizar la suma de un vector de más de 1 elemento con una matriz.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {
                    Matriz mat = (Matriz)v2.getValor();
                    NPrim op2;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op1 = new NPrim(getLinea(), getColumna(), getArchivo(), v.getElementByPosition(0).getValor(), v.getInnerType());
                    for (Item i : mat.getElementos()) {
                        op2 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }
                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat.getFilas(), mat.getColumnas(), li);
                }

            } else if (v1.getTipoDato() == ETipoDato.MATRIX && v2.getTipoDato() == ETipoDato.VECTOR) {

                Vector v = (Vector)v2.getValor();
                if (v.getVectorSize() > 1) {
                    msj = "Error. No se puede realizar la suma de una matriz y un vector de más de 1 elemento.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                } else {
                    Matriz mat = (Matriz)v1.getValor();
                    NPrim op1;
                    Resultado r;
                    LinkedList<Item> li = new LinkedList<>();
                    NPrim op2 = new NPrim(getLinea(), getColumna(), getArchivo(), v.getElementByPosition(0).getValor(), v.getInnerType());
                    for (Item i : mat.getElementos()) {
                        op1 = new NPrim(getLinea(), getColumna(), getArchivo(), i.getValor(), i.getTipo());
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
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
                        r = new NSuma(getLinea(), getColumna(), getArchivo(), op1, op2).Ejecutar(ts);
                        li.add(new Item(r.getTipoDato(), r.getValor()));
                    }

                    tdr = ETipoDato.MATRIX;
                    valor = new Matriz(mat1.getFilas(), mat1.getColumnas(), li);

                } else {
                    msj = "Error. Las dimensiones de la matriz difieren por lo que no se puede realizar la suma.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
                }

            } else {
                msj = "Error. No hay implementación para la operación SUMA para los tipos <"+ v1.getTipoDato() +"> y <"+ v2.getTipoDato() +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
            }

        } else {
            msj = "Error. Uno de los operadores recibidos es NULL.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_SUMA]", msj, getLinea(), getColumna());
        }

        return new Resultado(tdr, EFlujo.NORMAL, valor);

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        String parent = ts.getDeclararNodo("EXPRESION");
        String subson = ts.getDeclararNodo("NODO_SUMA");
        String tokeniz = ((Instruccion)opIzq).GenerarDOT(ts);
        String signo = ts.getDeclararNodo("+");
        String tokende = ((Instruccion)opDer).GenerarDOT(ts);
        ts.enlazarNodos(parent, subson);
        ts.enlazarNodos(subson, tokeniz);
        ts.enlazarNodos(subson, signo);
        ts.enlazarNodos(subson, tokende);
        return parent;
    }

}
