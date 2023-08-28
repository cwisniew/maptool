lexer grammar MTScript2Lexer;

@header {
package net.rptools.mtscript.parser;
}

@lexer::members {
}

// Default Mode
OPEN_SCRIPT_MODE            : '[[' -> pushMode(SCRIPT_MODE);
OPEN_ROLL_MODE              : '[' -> pushMode(ROLL_MODE);
TEXT                        : .+? ;

mode ROLL_MODE;
CLOSE_ROLL_MODE             : ']' -> popMode;
ROLL_DECIMAL_LITERAL        : ( '0' | [1-9] (Digits? | '_' + Digits) ) ;
ROLL_IDENTIFIER             : Letter LetterOrDigit* ;
ROLL_WS                     : [ \t\r\n\u000C]+  -> channel(HIDDEN);

mode EMBEDED_ROLL_MODE;
CLOSE_EMBEDED_ROLL_MODE     : ']]' -> popMode;
EM_ROLL_DECIMAL_LITERAL     : ( '0' | [1-9] (Digits? | '_' + Digits) ) ;
EM_ROLL_IDENTIFIER          : Letter LetterOrDigit* ;
EM_ROLL_WS                  : [ \t\r\n\u000C]+  -> channel(HIDDEN);

mode SCRIPT_MODE;
OPEN_EMBEDED_ROLL_MODE    : '[['  -> pushMode(EMBEDED_ROLL_MODE);
CLOSE_SCRIPT_MODE         : ']]' -> popMode;


// Keywords
KEYWORD_ASSERT     : 'assert';
KEYWORD_BREAK      : 'break';
KEYWORD_CASE       : 'case';
KEYWORD_CONST      : 'const';
KEYWORD_CONTINUE   : 'continue';
KEYWORD_DEFAULT    : 'default';
KEYWORD_DO         : 'do';
KEYWORD_ELSE       : 'else';
KEYWORD_FOR        : 'for';
KEYWORD_FOREACH    : 'foreach';
KEYWORD_FUNCTION   : 'function';
KEYWORD_PROCEDURE  : 'procedure';
KEYWORD_IF         : 'if';
KEYWORD_RETURNS    : 'returns';
KEYWORD_RETURN     : 'return';
KEYWORD_SWITCH     : 'switch';
KEYWORD_WHILE      : 'while';
KEYWORD_TRY        : 'try';
KEYWORD_CATCH      : 'catch';
KEYWORD_FINALLY    : 'finally';
KEYWORD_THROW      : 'throw';
KEYWORD_INSTANCEOF : 'instanceof';

KEYWORD_USE        : 'uses';
KEYWORD_AS         : 'as';
KEYWORD_EXPORT     : 'export';
KEYWORD_INTERNAL   : 'internal';
KEYWORD_CHAT       : 'chat';
KEYWORD_GM         : 'gm';
KEYWORD_TRUSTED    : 'trusted';


KEYWORD_CONSTANT   : 'constant';
KEYWORD_INTEGER    : 'integer';
KEYWORD_NUMBER     : 'number';
KEYWORD_STRING     : 'string';
KEYWORD_TOKEN      : 'token';
KEYWORD_ROLL       : 'roll';
KEYWORD_BOOLEAN    : 'bool';
KEYWORD_DICT       : 'dict';
KEYWORD_LIST       : 'list';
KEYWORD_TYPE       : 'type';

// Literals
DECIMAL_LITERAL : ( '0' | [1-9] (Digits? | '_' + Digits) ) ;
HEX_LITERAL     : '0' [xX] [0-9a-fA-F] ([0-9a-fA-F_]* [0-9a-fA-F])? ;
NUMBER_LITERAL  : (Digits '.' Digits? | '.' Digits) ;
BOOL_LITERAL    : 'true' | 'false' ;
STRING_LITERAL  : ( '\'' (~['] | EscapeSequence)* '\''  )
                | ( '"'  (~["] | EscapeSequence)* '"'   )
                ;
NULL_LITERAL    : 'null';
NONE_LITERAL    : 'none';

// Separators
LPAREN  : '(';
RPAREN  : ')';
LBRACE  : '{';
RBRACE  : '}';
LBRACK  : '[';
RBRACK  : ']';
SEMI    : ';';
COMMA   : ',';
DOT     : '.';

// Operators
OP_ASSIGN  : '=';
OP_GT      : '>';
OP_LT      : '<';
OP_BANG    : '!';
OP_TILDE   : '~';
OP_QUESTION: '?';
OP_COLON   : ':';
OP_EQUAL   : '==';
OP_LE      : '<=';
OP_GE      : '>=';
OP_NOTEQUAL: '!=';
OP_AND     : '&&';
OP_OR      : '||';
OP_INC     : '++';
OP_DEC     : '--';
OP_ADD     : '+';
OP_SUB     : '-';
OP_MUL     : '*';
OP_DIV     : '/';
OP_BITAND  : '&';
OP_BITOR   : '|';
OP_CARET   : '^';
OP_MOD     : '%';

OP_ADD_ASSIGN : '+=';
OP_SUB_ASSIGN : '-=';
OP_MUL_ASSIGN : '*=';
OP_DIV_ASSIGN : '/=';
OP_AND_ASSIGN : '&=';
OP_OR_ASSIGN  : '|=';
OP_XOR_ASSIGN : '^=';
OP_MOD_ASSIGN : '%=';


// Whitespace and comments
WS           : [ \t\r\n\u000C]+  -> channel(HIDDEN);
COMMENT      : '/*' .*? '*/'     -> channel(HIDDEN);
LINE_COMMENT : '//' ~[\r\n]*     -> channel(HIDDEN);

// Identifiers
IDENTIFIER   : Letter LetterOrDigit* ;

LOCAL_VAR_LEADER   : '$';

// Fragment rules
fragment EscapeSequence : '\\' [btnfr"'\\] ;

fragment Digits         : [0-9] ([0-9_]* [0-9])?
                        ;

fragment LetterOrDigit  : Letter
                        | [0-9]
                        ;

fragment Letter         : [a-zA-IZ_] // Java letters below 0x7F
                        | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
                        | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
                        ;
