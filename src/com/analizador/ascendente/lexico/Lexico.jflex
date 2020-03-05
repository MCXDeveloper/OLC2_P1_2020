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




























