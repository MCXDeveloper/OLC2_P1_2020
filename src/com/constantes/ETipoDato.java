package com.constantes;

import com.abstracto.Fail;
import com.arbol.NNulo;

public enum ETipoDato {
    INT(0, "integer", false),
    ANY(null, "any", false),
    LIST(null, "list", true),
    ERROR(new Fail(), "error", false),
    ARRAY(null, "array", true),
    VECTOR(null, "vector", true),
    MATRIX(null, "matrix", true),
    DECIMAL(0.0, "numeric", false),
    BOOLEAN(false, "boolean", false),
    NT(new NNulo(0,0,"[NO_FILE]"), "string", false),
    STRING(new NNulo(0,0,"[NO_FILE]"), "string", false);

    private final Object defecto;
    private final String typeOf;
    private final boolean isEDD;
    ETipoDato(final Object defecto, final String typeOf, final boolean isEDD) { this.defecto = defecto;  this.typeOf = typeOf; this.isEDD = isEDD; }
    public Object getDefecto() { return this.defecto; }
    public String getTypeOf() { return this.typeOf; }
    public boolean isEDD() { return this.isEDD; }

}