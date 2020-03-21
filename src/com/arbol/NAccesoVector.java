package com.arbol;

import com.abstracto.*;
import com.constantes.EFlujo;
import com.constantes.ETipoDato;
import com.constantes.ETipoDimension;
import com.constantes.ETipoNodo;
import com.entorno.TablaSimbolos;
import com.estaticas.ErrorHandler;

import java.util.LinkedList;
import java.util.Optional;

public class NAccesoVector extends Nodo implements Instruccion {

    private Vector vec;
    private LinkedList<Dimension> listaDims;

    public NAccesoVector(int linea, int columna, String archivo, Vector vec, LinkedList<Dimension> listaDims) {
        super(linea, columna, archivo, ETipoNodo.EXP_ACCESO_VECTOR);
        this.vec = vec;
        this.listaDims = listaDims;
    }

    @Override
    public Resultado Ejecutar(TablaSimbolos ts) {

        String msj;
        Resultado error = new Resultado(ETipoDato.ERROR, EFlujo.NORMAL, new Fail());

        Optional<Dimension> hasAnotherThanSimple = listaDims.stream().filter(d -> d.getTipoDim() != ETipoDimension.SIMPLE).findAny();

        if (hasAnotherThanSimple.isPresent()) {
            msj = "Error. No se puede acceder a un vector utilizando la forma de acceso proporcionada.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
            return error;
        }

        int actualPos = 0;
        Resultado rdim = ((Instruccion)listaDims.get(0).getValorDimIzq()).Ejecutar(ts);
        int firstPos = validarPosicionDeDimension(1, rdim);

        if (firstPos != -1) {
            if (listaDims.size() == 1) {
                actualPos = firstPos;
            } else {
                /*
                 * Verifico si el acceso al vector posee más de una dimension. Si eso se cumple,
                 * se tiene que validar que las siguientes dimensiones proporcionen un entero como
                 * valor de posicion y este tiene que ser exactamente igual que el primero, de lo
                 * contrario se debe reportar error.  Éste multiple acceso simula la recursión interna
                 * de la relación entre vectores y tipos primitivos (que también se consideran vectores).
                 *
                 * Una vez finalizado el ciclo y si todas las dimensiones se validaron correctamente
                 * significa que todas las posiciones son iguales, por lo que se procede a tomar la
                 * última (actualPos) y se obtiene su valor.
                 */
                if (firstPos > vec.getVectorSize()) {
                    msj = "Error. No se puede acceder a una posición inexistente de un vector [Dimension = 1 | Valor = "+ firstPos +"].";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
                    return error;
                }

                Resultado r;
                for (int i = 1; i < listaDims.size(); i++) {
                    r = ((Instruccion)listaDims.get(i).getValorDimIzq()).Ejecutar(ts);
                    actualPos = validarPosicionDeDimension((i + 1), r);
                    if (actualPos != -1) {
                        if (actualPos != firstPos) {
                            msj = "Error. No se puede acceder a una posición inexistente de un vector [Dimension = "+ (i + 1) +" | Valor = "+ actualPos +"].";
                            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
                            return error;
                        }
                    } else {
                        return error;
                    }
                }
            }
        } else {
            return error;
        }

        int finalPos = actualPos - 1;

        if (finalPos > vec.getVectorSize() - 1) {
            msj = "Error. No se puede acceder a una posición inexistente de un vector [Valor = "+ actualPos +"].";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
            return error;
        }

        Item it = vec.getElementByPosition(finalPos);

        return new Resultado(it.getTipo(), EFlujo.NORMAL, it.getValor());

    }

    @Override
    public String GenerarDOT(TablaSimbolos ts) {
        return null;
    }

    private int validarPosicionDeDimension(int numDim, Resultado rdim) {

        String msj;
        int posDim;

        switch (rdim.getTipoDato()) {

            case INT: {
                posDim = (int)rdim.getValor();
            }   break;

            case VECTOR: {

                Vector v = (Vector)rdim.getValor();

                if (v.getInnerType() != ETipoDato.INT) {
                    msj = "Error. No se puede recibir como valor de posicion en la dimensión #"+ numDim +" un <VECTOR["+ v.getInnerType() +"]>.";
                    ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
                    return -1;
                }

                posDim = (int)v.getElementByPosition(0).getValor();

            }   break;

            default: {
                msj = "Error. El valor propocionado como posición en la dimension #"+ numDim +" es de tipo <"+ rdim.getTipoDato() +">. Se espera un entero.";
                ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
                return -1;
            }

        }

        if (posDim <= 0) {
            msj = "Error. El indice proporcionado en la dimensión #"+ numDim +" debe ser mayor o igual a 1.";
            ErrorHandler.AddError(getTipoError(), getArchivo(), "[N_ACCESO_VECTOR]", msj, getLinea(), getColumna());
            return -1;
        }

        return posDim;

    }

}
