package com.constantes;

import com.abstracto.Fail;
import com.arbol.NNulo;

public enum ETipoDato {
    INT(0, "INTEGER", false),
    ANY(null, "ANY", false),
    LIST(null, "LIST", true),
    ERROR(new Fail(), "ERROR", false),
    ARRAY(null, "ARRAY", true),
    VECTOR(null, "VECTOR", true),
    MATRIX(null, "MATRIX", true),
    DECIMAL(0.0, "NUMERIC", false),
    BOOLEAN(false, "BOOLEAN", false),
    NT(new NNulo(0,0,"[NO_FILE]"), "NULL", false),
    STRING(new NNulo(0,0,"[NO_FILE]"), "STRING", false);

    private final Object defecto;
    private final String typeOf;
    private final boolean isEDD;
    ETipoDato(final Object defecto, final String typeOf, final boolean isEDD) { this.defecto = defecto;  this.typeOf = typeOf; this.isEDD = isEDD; }
    public Object getDefecto() { return this.defecto; }
    public String getTypeOf() { return this.typeOf; }
    public boolean isEDD() { return this.isEDD; }

}