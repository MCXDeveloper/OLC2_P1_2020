package com.constantes;

import com.abstracto.Fail;
import com.arbol.NNulo;

public enum ETipoDato {
    INT(0, "INTEGER"),
    ANY(null, "ANY"),
    LIST(null, "LIST"),
    ERROR(new Fail(), "ERROR"),
    ARRAY(null, "ARRAY"),
    VECTOR(null, "VECTOR"),
    MATRIX(null, "MATRIX"),
    DECIMAL(0.0, "NUMERIC"),
    BOOLEAN(false, "BOOLEAN"),
    NT(new NNulo(0,0,"[NO_FILE]"), "NULL"),
    STRING(new NNulo(0,0,"[NO_FILE]"), "STRING");

    private final Object defecto;
    private final String typeOf;
    ETipoDato(final Object defecto, final String typeOf) { this.defecto = defecto;  this.typeOf = typeOf; }
    public Object getDefecto() { return this.defecto; }
    public String getTypeOf() { return this.typeOf; }

}