package lexical_analyzer;
import java.io.IOException;

%%

%class Scanner
%public
%type Symbol
%line
%column
%unicode

%{
    private Symbol current = null;
    private Symbol sym(String token) {
        System.out.println(token);
        return new Symbol(token);
    }

    private Symbol sym(String token, String val) {
        System.out.println(token);
        return new Symbol(token, val);
    }

    public int line_number(){
        return yyline;
    }

    public Symbol get_current(){
        return current;
    }

    public String read_token() throws Exception{
        current = yylex();
        return current.getToken();
    }

%}

%eofval{
    return sym("$");
%eofval}

/* Patterns */
letter              = [A-Za-z]
digit               = [0-9]
whitespace          = [ \n\t\f\r\v]
under_score         = [_]
identifier          = {letter}({letter} | {digit} | {under_score})*
int                 = {digit}+
real                = {int}\.{int}
hex                 = "0x" {int}
character           = '[\x20-\x7E]'
string              = \"([^\"\n])+\"
input_char          = [^\r\n]
line_terminator     = \r|\n
dash_comment        = "--" {input_char}* {line_terminator}
regular_comment     = "//" {input_char}* {line_terminator}
comment_content     = ([^])*?
multi_line_comment  = "<--"{comment_content}"-->"
comment             = {dash_comment} | {regular_comment} | {multi_line_comment}


%%

<YYINITIAL> {
 "{"            { return sym("lbrace"); }
 "}"            { return sym("rbrace"); }
 "("            { return sym("l_paren"); }
 ")"            { return sym("r_paren"); }
 "["            { return sym("l_bracket"); }
 "]"            { return sym("r_bracket"); }
 ";"            { return sym("semicolon"); }
 ","            { return sym("comma"); }

 /* Calculation Operators */
 "+"            { return sym("plus"); }
 "-"            { return sym("minus"); }
 "*"            { return sym("mult"); }
 "/"            { return sym("divide"); }
 "%"            { return sym("mod"); }

 /* Comparison Operators */
 "<"            { return sym("lt"); }
 ">"            { return sym("gt"); }
 ">="           { return sym("gte"); }
 "<>"           { return sym("neq"); }
 "<="           { return sym("lte"); }
 "="            { return sym("eq"); }

 ":="           { return sym("colon_eq"); }
 ":"           { return sym("colon"); }


 /* Logical Operators */
 "|"            { return sym("bitwise_or"); }
 "^"            { return sym("exclusive_add"); }
 "&"            { return sym("bitwise_and"); }
 "~"            { return sym("not"); }
 "and"          { return sym("and"); }
 "or"           { return sym("or"); }

 /* Keywords */
 "char"         { return sym("char"); }
 "integer"      { return sym("integer"); }
 "long"      { return sym("long"); }
 "boolean"      { return sym("boolean"); }
 "real"         { return sym("real"); }
 "array"        { return sym("array"); }
 "assign"       { return sym("assign"); }
 "break"        { return sym("break"); }
 "begin"        { return sym("begin"); }
 "continue"     { return sym("continue"); }
 "do"           { return sym("do"); }
 "else"         { return sym("else"); }
 "end"          { return sym("end"); }
 "function"     { return sym("function"); }
 "procedure"    { return sym("procedure"); }
 "if"           { return sym("if"); }
 "then"         { return sym("then"); }
 "of"           { return sym("of"); }
 "return"       { return sym("return"); }
 "string"       { return sym("string"); }
 "while"        { return sym("while"); }
 "var"          { return sym("var"); }
 "true"|"false" { return sym("boolean_const", yytext()); }

 {whitespace}   { /* do nothing */ }
 {comment}      { /* do nothing */ }

 {identifier}   { return sym("id", yytext()); }
 {string}       { return sym("string_const", yytext()); }

 {int}          { return sym("int_const", yytext()); }
 {real}         { return sym("real_const", yytext()); }
 {character}    { return sym("char_const", Character.toString(yytext().charAt(1))); }
 {hex}          { return sym("int_const", yytext()); }


}
[^]   { throw new Error("Illegal character <" + yytext() + "> at line " + yyline);  }