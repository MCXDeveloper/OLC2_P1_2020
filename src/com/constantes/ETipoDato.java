package com.constantes;

import com.abstracto.Fail;
import com.arbol.NNulo;

public enum ETipoDato {
    INT(0),
    ANY(null),
    LIST(null),
    ERROR(new Fail()),
    ARRAY(null),
    VECTOR(null),
    MATRIX(null),
    DECIMAL(0.0),
    BOOLEAN(false),
    NT(new NNulo(0,0,"[NO_FILE]")),
    STRING(new NNulo(0,0,"[NO_FILE]"));

    private final Object defecto;
    private ETipoDato(final Object defecto) { this.defecto = defecto; }
    public Object getDefecto() { return this.defecto; }

}