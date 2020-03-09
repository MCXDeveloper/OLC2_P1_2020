package com.abstracto;

import com.entorno.TablaSimbolos;

public interface Instruccion {
    Resultado Ejecutar(TablaSimbolos ts);
    String GenerarDOT(TablaSimbolos ts);
}
