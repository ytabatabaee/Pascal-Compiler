package lexical_analyzer;
import java.io.IOException;

%%

%class Scanner
%public
%line
%column
%unicode

%eof{
    return new Symbol("$");
%eof}

%{


%}

/* Patterns */
letter = [A-Za-z]
digit = [0-9]
whitespace = [ \n\t\f\r\v]




%%

<YYINITIAL> {
 "{"
 "}"
 "("
 ")"
 "["
 "]"
 ";"
 ","
 "+"
 "-"
 "*"
 "<"
 ">"
 ">="
 "<="
 "="
 ":="
 "%"
 "|"
 "^"
 "&"
 "/"
 "~"

 "char"
 "integer"
 "boolean"
 "array"
 "assign"
 "break"
 "begin"
 "char"
 "continue"
 "do"
 "else"
 "end"
 "false"
 "function"
 "procedure"
 "if"
 "integer"
 "of"
 "real"
 "return"
 "string"
 "true"
 "while"
 "var"


}
[^]   { throw new Error("Illegal character <" + yytext() + "> at line " + yyline);  }


