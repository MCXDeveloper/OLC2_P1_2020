package com.constantes;

import com.abstracto.Fail;
import com.arbol.NNulo;

public enum ETipoDato {
    INT(0, "integer", false, true),
    ANY(null, "any", false, false),
    LIST(null, "list", true, false),
    ERROR(new Fail(), "error", false, false),
    ARRAY(null, "array", true, false),
    VECTOR(null, "vector", true, false),
    MATRIX(null, "matrix", true, false),
    DECIMAL(0.0, "numeric", false, true),
    BOOLEAN(false, "boolean", false, true),
    NT(new NNulo(0,0,"[NO_FILE]"), "string", false, true),
    STRING(new NNulo(0,0,"[NO_FILE]"), "string", false, true);

    private final Object defecto;
    private final String typeOf;
    private final boolean isEDD;
    private final boolean isPrim;
    ETipoDato(final Object defecto, final String typeOf, final boolean isEDD, final boolean isPrim) { this.defecto = defecto;  this.typeOf = typeOf; this.isEDD = isEDD; this.isPrim = isPrim; }
    public Object getDefecto() { return this.defecto; }
    public String getTypeOf() { return this.typeOf; }
    public boolean isEDD() { return this.isEDD; }
    public boolean isPrimitive() { return this.isPrim; }

}