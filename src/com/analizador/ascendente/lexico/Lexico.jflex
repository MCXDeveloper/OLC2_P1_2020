/************************* 1RA AREA: CÓDIGO DE USUARIO *************************/

/* PAQUETES E IMPORTACIONES */
package com.analizador.ascendente.lexico;

import com.estaticas.ErrorHandler;
import com.analizador.ascendente.sintactico.CUPSim;
import java_cup.runtime.*;

/********************** 2DA AREA: OPCIONES Y DECLARACIONES *********************/

%%
%{
    String archivo = "";
    StringBuilder cadena = new StringBuilder();

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String nombre) {
        this.archivo = nombre;
    }

%}

/* DIRECTIVAS */
%class Lexico
%cupsym CUPSim
%public
%line
%column
%char
%cup
%unicode
%ignorecase
%state STRING_CAPTURE

%init{
    yychar = 1;
    yyline = 1;
    yycolumn = 1;
%init}

/* EXPRESIONES REGULARES */
Identificador 	= ( ((["."]+[A-Za-z_])|[A-Za-z])([A-Za-z_0-9]|".")* | ["."]+ )
Numero 		    = [0-9]+
Decimal         = [0-9]+\.[0-9]+
LineTerminator 	= \r|\n|\r\n
InputCharacter 	= [^\r\n]

/* COMENTARIOS */
Comentario           = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}
TraditionalComment   = "#*" [^*] ~"*#" | "#*" "*"+ "#"
EndOfLineComment     = "#" {InputCharacter}* {LineTerminator}?
DocumentationComment = "#**" {CommentContent} "*"+ "#"
CommentContent       = ( [^*] | \*+ [^/*] )*

%%

/*************************** 3RA AREA: REGLAS LÉXICAS **************************/

<YYINITIAL> "c"			        { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: c");                return new Symbol(CUPSim.r_c, yycolumn, yyline, yytext()); }
<YYINITIAL> "null"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: r_nulo");           return new Symbol(CUPSim.r_nulo, yycolumn, yyline, yytext()); }
<YYINITIAL> "if"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: if");               return new Symbol(CUPSim.r_if, yycolumn, yyline, yytext()); }
<YYINITIAL> "else"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: else");             return new Symbol(CUPSim.r_else, yycolumn, yyline, yytext()); }
<YYINITIAL> "switch"		    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: switch");           return new Symbol(CUPSim.r_switch, yycolumn, yyline, yytext()); }
<YYINITIAL> "case"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: case");             return new Symbol(CUPSim.r_case, yycolumn, yyline, yytext()); }
<YYINITIAL> "default"		    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: default");          return new Symbol(CUPSim.r_default, yycolumn, yyline, yytext()); }
<YYINITIAL> "break"		        { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: break");            return new Symbol(CUPSim.r_break, yycolumn, yyline, yytext()); }
<YYINITIAL> "continue"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: continue");         return new Symbol(CUPSim.r_continue, yycolumn, yyline, yytext()); }
<YYINITIAL> "return"		    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: return");           return new Symbol(CUPSim.r_return, yycolumn, yyline, yytext()); }
<YYINITIAL> "while"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: while");            return new Symbol(CUPSim.r_while, yycolumn, yyline, yytext()); }
<YYINITIAL> "for"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: for");              return new Symbol(CUPSim.r_for, yycolumn, yyline, yytext()); }
<YYINITIAL> "do"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: do");               return new Symbol(CUPSim.r_do, yycolumn, yyline, yytext()); }
<YYINITIAL> "in"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: in");               return new Symbol(CUPSim.r_in, yycolumn, yyline, yytext()); }
<YYINITIAL> "function"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: function");         return new Symbol(CUPSim.r_function, yycolumn, yyline, yytext()); }
<YYINITIAL> "print"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: print");            return new Symbol(CUPSim.r_print, yycolumn, yyline, yytext()); }
<YYINITIAL> "typeof"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: typeof");           return new Symbol(CUPSim.r_typeof, yycolumn, yyline, yytext()); }
<YYINITIAL> "length"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: length");           return new Symbol(CUPSim.r_length, yycolumn, yyline, yytext()); }
<YYINITIAL> "array"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: array");            return new Symbol(CUPSim.r_array, yycolumn, yyline, yytext()); }
<YYINITIAL> "matrix"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: matrix");           return new Symbol(CUPSim.r_matrix, yycolumn, yyline, yytext()); }
<YYINITIAL> "list"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: list");             return new Symbol(CUPSim.r_list, yycolumn, yyline, yytext()); }
<YYINITIAL> "ncol"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: ncol");             return new Symbol(CUPSim.r_ncol, yycolumn, yyline, yytext()); }
<YYINITIAL> "nrow"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: nrow");             return new Symbol(CUPSim.r_nrow, yycolumn, yyline, yytext()); }
<YYINITIAL> "stringlength"		{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: stringlength");     return new Symbol(CUPSim.r_stringlength, yycolumn, yyline, yytext()); }
<YYINITIAL> "remove"		    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: remove");           return new Symbol(CUPSim.r_remove, yycolumn, yyline, yytext()); }
<YYINITIAL> "tolowercase"		{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: tolowercase");      return new Symbol(CUPSim.r_tolowercase, yycolumn, yyline, yytext()); }
<YYINITIAL> "touppercase"		{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: touppercase");      return new Symbol(CUPSim.r_touppercase, yycolumn, yyline, yytext()); }
<YYINITIAL> "trunk"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: trunk");            return new Symbol(CUPSim.r_trunk, yycolumn, yyline, yytext()); }
<YYINITIAL> "round"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: round");            return new Symbol(CUPSim.r_round, yycolumn, yyline, yytext()); }
<YYINITIAL> "mean"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: mean");             return new Symbol(CUPSim.r_mean, yycolumn, yyline, yytext()); }
<YYINITIAL> "median"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: median");           return new Symbol(CUPSim.r_median, yycolumn, yyline, yytext()); }
<YYINITIAL> "mode"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: mode");             return new Symbol(CUPSim.r_mode, yycolumn, yyline, yytext()); }
<YYINITIAL> "pie"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: pie");              return new Symbol(CUPSim.r_pie, yycolumn, yyline, yytext()); }
<YYINITIAL> "barplot"			{ System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: barplot");          return new Symbol(CUPSim.r_barplot, yycolumn, yyline, yytext()); }
<YYINITIAL> "plot"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: plot");             return new Symbol(CUPSim.r_plot, yycolumn, yyline, yytext()); }
<YYINITIAL> "hist"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: hist");             return new Symbol(CUPSim.r_hist, yycolumn, yyline, yytext()); }
<YYINITIAL> "true"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: true");             return new Symbol(CUPSim.r_true, yycolumn, yyline, yytext()); }
<YYINITIAL> "false"			    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: false");            return new Symbol(CUPSim.r_false, yycolumn, yyline, yytext()); }
<YYINITIAL> "+"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: mas");              return new Symbol(CUPSim.mas, yycolumn, yyline, yytext()); }
<YYINITIAL> "-"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: menos");            return new Symbol(CUPSim.menos, yycolumn, yyline, yytext()); }
<YYINITIAL> "*"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: por");              return new Symbol(CUPSim.por, yycolumn, yyline, yytext()); }
<YYINITIAL> "/"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: div");              return new Symbol(CUPSim.div, yycolumn, yyline, yytext()); }
<YYINITIAL> "^"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: pot");              return new Symbol(CUPSim.pot, yycolumn, yyline, yytext()); }
<YYINITIAL> "%%"                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: mod");              return new Symbol(CUPSim.mod, yycolumn, yyline, yytext()); }
<YYINITIAL> "("                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: par_a");            return new Symbol(CUPSim.par_a, yycolumn, yyline, yytext()); }
<YYINITIAL> ")"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: par_c");            return new Symbol(CUPSim.par_c, yycolumn, yyline, yytext()); }
<YYINITIAL> "["                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: cor_a");            return new Symbol(CUPSim.cor_a, yycolumn, yyline, yytext()); }
<YYINITIAL> "]"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: cor_c");            return new Symbol(CUPSim.cor_c, yycolumn, yyline, yytext()); }
<YYINITIAL> ","                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: coma");             return new Symbol(CUPSim.coma, yycolumn, yyline, yytext()); }
<YYINITIAL> ";"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: puco");             return new Symbol(CUPSim.puco, yycolumn, yyline, yytext()); }
<YYINITIAL> ":"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: dospu");            return new Symbol(CUPSim.dospu, yycolumn, yyline, yytext()); }
<YYINITIAL> "?"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: interrogacion");    return new Symbol(CUPSim.interrogacion, yycolumn, yyline, yytext()); }
<YYINITIAL> "=>"                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: flecha");           return new Symbol(CUPSim.flecha, yycolumn, yyline, yytext()); }
<YYINITIAL> "<"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: menor");            return new Symbol(CUPSim.menor, yycolumn, yyline, yytext()); }
<YYINITIAL> ">"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: mayor");            return new Symbol(CUPSim.mayor, yycolumn, yyline, yytext()); }
<YYINITIAL> "<="                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: menor_igual");      return new Symbol(CUPSim.menor_igual, yycolumn, yyline, yytext()); }
<YYINITIAL> ">="                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: mayor_igual");      return new Symbol(CUPSim.mayor_igual, yycolumn, yyline, yytext()); }
<YYINITIAL> "!="                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: diferente_que");    return new Symbol(CUPSim.diferente_que, yycolumn, yyline, yytext()); }
<YYINITIAL> "=="                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: igualdad");         return new Symbol(CUPSim.igualdad, yycolumn, yyline, yytext()); }
<YYINITIAL> "="                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: igual");            return new Symbol(CUPSim.igual, yycolumn, yyline, yytext()); }
<YYINITIAL> "{"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: llave_a");          return new Symbol(CUPSim.llave_a, yycolumn, yyline, yytext()); }
<YYINITIAL> "}"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: llave_c");          return new Symbol(CUPSim.llave_c, yycolumn, yyline, yytext()); }
<YYINITIAL> "|"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: or");               return new Symbol(CUPSim.or, yycolumn, yyline, yytext()); }
<YYINITIAL> "&"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: and");              return new Symbol(CUPSim.and, yycolumn, yyline, yytext()); }
<YYINITIAL> "!"                 { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: not");              return new Symbol(CUPSim.not, yycolumn, yyline, yytext()); }
<YYINITIAL> "\""                { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: comilla");          cadena.setLength(0); yybegin(STRING_CAPTURE); }

/* CAPTURA DE CADENAS */
<STRING_CAPTURE> {
    \"                          { System.out.println("Encontro el simbolo: " + cadena.toString() + " >>>> Token: cadena"); yybegin(YYINITIAL); return new Symbol(CUPSim.cadena, yycolumn, yyline, cadena.toString()); }
    [^\n\r\"\\]+                { cadena.append(yytext()); }
    \\t                         { cadena.append('\t'); }
    \\n                         { cadena.append('\n'); }
    \\r                         { cadena.append('\r'); }
    \\\"                        { cadena.append('\"'); }
    \\                          { cadena.append('\\'); }
}

/* SIMBOLOS EXPRESIONES REGULARES */
<YYINITIAL> {Comentario}                { /* Comentarios, se ignoran */ }
<YYINITIAL> {Identificador}             { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: identificador");    return new Symbol(CUPSim.identificador, yycolumn, yyline, yytext()); }
<YYINITIAL> {Numero}                    { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: numero_entero");    return new Symbol(CUPSim.numero_entero, yycolumn, yyline, yytext()); }
<YYINITIAL> {Decimal}                   { System.out.println("Encontro el simbolo: " + yytext() + " >>>> Token: numero_decimal");   return new Symbol(CUPSim.numero_decimal, yycolumn, yyline, yytext()); }

/* ESPACIOS */
[ \t\r\n\f]                             { /* Espacios en blanco, se ignoran */ }

/* ERRORES LEXICOS */
.
{
    ErrorHandler.AddError("Léxico", getArchivo(), "[LEXER]", "El caracter '" + yytext() + "' no pertenece al alfabeto del lenguaje.", yyline, yycolumn);
}




























