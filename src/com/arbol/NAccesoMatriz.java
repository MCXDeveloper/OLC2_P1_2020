package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

public class NAccesoMatriz extends Nodo implements Instruccion {

    private Matriz matrix;
    private LinkedList<Dimension> listaDims;

    public NAccesoMatriz(int linea, int columna, String archivo, Matriz matrix, LinkedList<Dimension> listaDims) {
        super(linea, columna, archivo, ETipoNodo.EXP_ACCESO_MATRIZ);
        this.matrix = matrix;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        if (listaDims.size() == 1) {
            return getValorDentroDeMatriz(ts);
        } else if (listaDims.size() > 1) {

            LinkedList<Dimension> dimsInternas = Optional.of(listaDims)
                    .stream()
                    .flatMap(Collection::stream)
                    .skip(1)
                    .collect(Collectors.toCollection(LinkedList::new));

            Resultado retMat = getValorDentroDeMatriz(ts);

            switch (retMat.getTipoDato()) {
                case INT:
                case STRING:
                case DECIMAL:
                case BOOLEAN: {
                    Vector v = new Vector(retMat.getTipoDato(), retMat.getValor());
                    v = !ts.enCicloFor() ? (Vector)v.getClone() : v;
                    NAccesoVector nav = new NAccesoVector(getLinea(), getColumna(), getArchivo(), v, dimsInternas);
                    return nav.Ejecutar(ts);
                }
                case VECTOR: {
                    Vector v = (Vector)retMat.getValor();
                    v = !ts.enCicloFor() ? (Vector)v.getClone() : v;
                    NAccesoVector nav = new NAccesoVector(getLinea(), getColumna(), getArchivo(), v, dimsInternas);
                    return nav.Ejecutar(ts);
                }
                default: {
                    msj = "Error. No se puede acceder con dimensiones a una variable que no sea de tipo estructura.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO]", msj, getLinea(), getColumna());
                    return error;
                }
            }

        } else {
            msj = "Error. No se puede realizar el acceso a una matriz sin una lista de dimensiones.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
            return error;
        }

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private Resultado getValorDentroDeMatriz(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        ETipoDimension tipoDim = listaDims.get(0).getTipoDim();

        switch (tipoDim) {

            case SIMPLE: {
                Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                int posDim = validarPosicionDeDimension(rdim);
                if (posDim != -1) {
                    if (posDim > matrix.getMatrixSize()) {
                        msj = "Error. El valor proporcionado como posición sobrepasa el tamaño de la matriz.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
                        return error;
                    }
                    Item it = matrix.getElementByPosition(posDim - 1);
                    return new Resultado(it.getTipo(), EFlujo.NORMAL, it.getValor());
                }
            }

            case COMPOUND: {
                Resultado rdimx = ((Instruccion) listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                Resultado rdimy = ((Instruccion) listaDims.get(0).getValorDimDer()).Ejecutar(ts);
                int posX = validarPosicionDeDimension(rdimx);
                int posY = validarPosicionDeDimension(rdimy);
                if (posX != -1 && posY != -1) {
                    if (!matrix.validateDimensions(posX, posY)) {
                        msj = "Error. Los indices proporcionados sobrepasan los rangos de la matriz.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ASIGNACION_MATRIZ]", msj, getLinea(), getColumna());
                        return error;
                    }
                    Item it = matrix.getElementByCoordinates(posX, posY);
                    return new Resultado(it.getTipo(), EFlujo.NORMAL, it.getValor());
                } else {
                    return error;
                }
            }

            case ROW: {
                Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                int posDim = validarPosicionDeDimension(rdim);
                if (posDim != -1) {
                    if (!matrix.validateRows(posDim)) {
                        msj = "Error. El índice proporcionado sobrepasa los limites de las filas de la matriz.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
                        return error;
                    }
                    /* Construyo el nuevo vector que voy a retornar */
                    Item it;
                    LinkedList<Item> li = new LinkedList<>();
                    for (int i = 1; i <= matrix.getColumnas(); i++) {
                        it = matrix.getElementByCoordinates(posDim, i);
                        li.add(new Item(it.getTipo(), it.getValor()));
                    }
                    return new Resultado(ETipoDato.VECTOR, EFlujo.NORMAL, new Vector(li));
                } else {
                    return error;
                }
            }

            case COLUMN: {
                Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
                int posDim = validarPosicionDeDimension(rdim);
                if (posDim != -1) {
                    if (!matrix.validateColumns(posDim)) {
                        msj = "Error. El índice proporcionado sobrepasa los limites de las columnas de la matriz.";
                        ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
                        return error;
                    }
                    /* Construyo el nuevo vector que voy a retornar */
                    Item it;
                    LinkedList<Item> li = new LinkedList<>();
                    for (int i = 1; i <= matrix.getFilas(); i++) {
                        it = matrix.getElementByCoordinates(i, posDim);
                        li.add(new Item(it.getTipo(), it.getValor()));
                    }
                    return new Resultado(ETipoDato.VECTOR, EFlujo.NORMAL, new Vector(li));
                } else {
                    return error;
                }
            }

            default: {
                msj = "Error. No se puede acceder a una matriz utilizando el tipo de acceso <"+ tipoDim +">.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
                return error;
            }

        }

    }

    private int validarPosicionDeDimension(Resultado rdim) {

        String msj;
        int posDim;

        switch (rdim.getTipoDato()) {

            case INT: {
                posDim = (int)rdim.getValor();
            }   break;

            case VECTOR: {

                Vector v = (Vector)rdim.getValor();

                if (v.getInnerType() != ETipoDato.INT) {
                    msj = "Error. No se puede recibir como valor de posicion en la dimensión #1 un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
                    return -1;
                }

                posDim = (int)v.getElementByPosition(0).getValor();

            }   break;

            default: {
                msj = "Error. El valor propocionado como posición en la dimension es de tipo <"+ rdim.getTipoDato() +">. Se espera un entero.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
                return -1;
            }

        }

        if (posDim <= 0) {
            msj = "Error. El indice proporcionado debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_MATRIZ]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

}
