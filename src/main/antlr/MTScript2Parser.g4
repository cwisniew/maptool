parser grammar MTScript2Parser;


@header {
package net.rptools.maptool.mtscript2.parser;
}

options { tokenVocab=MTScript2Lexer; }

// Top level sentences

chat                        : (OPEN_SCRIPT_MODE script  CLOSE_SCRIPT_MODE | OPEN_ROLL_MODE dice CLOSE_ROLL_MODE | text)* ;

text                        : TEXT+
                            ;

script                      : statement+
                            ;

statement                   : variableDeclarationOrInit SEMI
                            | constantDeclarationAndInit SEMI
                            | expression SEMI
                            | variableAssign SEMI
                            | forStatement
                            | whileStatement
                            | doStatement SEMI
                            | ifStatement
                            | tryStatement
                            | throwStatement SEMI
                            ;

block                       : LBRACE statement+ RBRACE
                            ;

methodCall                  : methodName=IDENTIFIER LPAREN argumentList? RPAREN
                            ;

argumentList                : argument (COMMA argument)* (COMMA namedArgument)*
                            | namedArgument (COMMA namedArgument)*
                            ;

argument                    : expression
                            ;

namedArgument               : variable COLON expression
                            ;

ifStatement                 : KEYWORD_IF LPAREN booleanTest RPAREN block (KEYWORD_ELSE KEYWORD_IF LPAREN booleanTest RPAREN block)* (KEYWORD_ELSE block)?
                            ;

forStatement                : KEYWORD_FOR LPAREN variableDeclarationInit? SEMI booleanTest? SEMI variableAssign? RPAREN block
                            ;

whileStatement              : KEYWORD_WHILE LPAREN booleanTest RPAREN block
                            ;

doStatement                 : KEYWORD_DO block KEYWORD_WHILE LPAREN booleanTest RPAREN
                            ;

tryStatement                : KEYWORD_TRY block catchClause? finallyBlock?
                            ;

catchClause                 : KEYWORD_CATCH LPAREN variable RPAREN block
                            ;

finallyBlock                : KEYWORD_FINALLY block
                            ;

throwStatement              : KEYWORD_THROW expression
                            ;

variableDeclarationOrInit   : KEYWORD_VAR declaration (COMMA declaration )*
                            ;

variableDeclarationInit     : KEYWORD_VAR declarationInit (COMMA declarationInit )*
                            ;

constantDeclarationAndInit  : KEYWORD_CONST declarationInit ( COMMA declarationInit )*
                            ;

variableDefinition          : variable COLON type
                            ;

declaration                 : declarationInit
                            | variableDefinition
                            ;

declarationInit             : variableDefinition OP_ASSIGN expression
                            ;


expression                  : methodCall
                            | variable
                            | expression DOT variable
                            | expression DOT methodCall
                            | LPAREN expression RPAREN
                            | literal
                            | variableDeclarationOrInit
                            | constantDeclarationAndInit
                            | prefix=OP_BANG expression
                            | expression bop=(OP_MUL | OP_DIV | OP_MOD) expression
                            | expression bop=(OP_ADD | OP_SUB) expression
                            | expression bop=(OP_LE | OP_GE | OP_GT | OP_LT) expression
                            | expression bop=(OP_EQUAL | OP_NOTEQUAL) expression
                            | expression bop=OP_BITAND expression
                            | expression bop=OP_CARET expression
                            | expression bop=OP_BITOR expression
                            | expression bop=OP_AND expression
                            | expression bop=OP_OR expression
                            | switchExpression
                            ;


booleanTest                 : expression bop=(OP_EQUAL | OP_NOTEQUAL) expression
                            | expression bop=(OP_LE | OP_GE | OP_GT | OP_LT) expression
                            | expression bop=OP_AND expression
                            | expression bop=OP_OR expression
                            | expression bop=OP_QUESTION expression COLON expression
                            | prefix=OP_BANG expression
                            | expression bop=OP_BITAND expression
                            | expression bop=OP_CARET expression
                            | expression bop=OP_BITOR expression
                            | expression bop=OP_AND expression
                            | expression bop=OP_OR expression
                            | expression bop=OP_QUESTION expression COLON expression
                            | expression postfix=(OP_INC | OP_DEC)
                            ;

switchExpression            : KEYWORD_SWITCH LPAREN expression RPAREN LBRACE switchBlockStatementGroup* switchLabel* RBRACE
                            ;

switchBlockStatementGroup   : (switchLabel+ statement+)+ (KEYWORD_DEFAULT COLON statement+)*
                            ;

switchLabel                 : KEYWORD_CASE literal COLON
                            | KEYWORD_CASE KEYWORD_TYPE KEYWORD_OF type COLON
                            ;

literal                     : DECIMAL_LITERAL
                            | HEX_LITERAL
                            | NUMBER_LITERAL
                            | STRING_LITERAL
                            | BOOL_LITERAL
                            | NULL_LITERAL
                            ;

dice                        : numDice=(ROLL_DECIMAL_LITERAL | EM_ROLL_DECIMAL_LITERAL)? diceName=(ROLL_IDENTIFIER | EM_ROLL_IDENTIFIER)
                            ;

variable                    : IDENTIFIER
                            ;

variableAssign              : variable assignmentOp expression
                            | postfixExpression
                            ;

postfixExpression           : variable postfix=(OP_INC | OP_DEC)
                            ;

assignmentOp                : OP_ASSIGN
                            | OP_ADD_ASSIGN
                            | OP_SUB_ASSIGN
                            | OP_MUL_ASSIGN
                            | OP_DIV_ASSIGN
                            | OP_AND_ASSIGN
                            | OP_OR_ASSIGN
                            | OP_XOR_ASSIGN
                            | OP_MOD_ASSIGN
                            ;

type                        : KEYWORD_BOOLEAN
                            | KEYWORD_NUMBER
                            | KEYWORD_STRING
                            | KEYWORD_LIST
                            | KEYWORD_DICT
                            | KEYWORD_INT
                            | KEYWORD_ROLL
                            | KEYWORD_ANY
                            | KEYWORD_MAP
                            | KEYWORD_TOKEN
                            | KEYWORD_ACTOR
                            | KEYWORD_IMAGE
                            | KEYWORD_SOUND
                            | KEYWORD_TABLE
                            | KEYWORD_LIGHT
                            | KEYWORD_LIGHTSOURCE
                            | KEYWORD_PATH
                            | KEYWORD_ITEM
                            ;

/*
variable                    : varName=IDENTIFIER
                            ;

group                       : LPAREN val=expression RPAREN                          # parenGroup
                            | LBRACE val=expression RBRACE                          # braceGroup
                            ;

diceExpression              : OPEN_EMBEDED_ROLL_MODE dice CLOSE_EMBEDED_ROLL_MODE
                            ;


dice                        :  numDice? diceName  diceArguments?
                            ;

diceName                    : ROLL_IDENTIFIER
                            | EM_ROLL_IDENTIFIER
                            ;

numDice                     : ROLL_DECIMAL_LITERAL
                            | EM_ROLL_DECIMAL_LITERAL
                            | group
                            ;


diceSides                   : integerLiteral
                            | group
                            ;

diceArguments               : diceArgumentList
                            ;

diceArgumentList            : LBRACE diceArgument ( COMMA diceArgument )* RBRACE
                            | LPAREN diceArgument ( COMMA diceArgument )* RPAREN
                            ;


diceArgument                : argName=IDENTIFIER op=( OP_LT | OP_GT | OP_LE | OP_GE | OP_NOTEQUAL | OP_ASSIGN )? val=diceArgumentVal?
                            ;


diceArgumentVal             : IDENTIFIER                                        # dargIdentifier
                            | variable                                          # dargVariable
                            | STRING_LITERAL                                    # dargString
                            | integerLiteral                                    # dargInteger
                            | NUMBER_LITERAL                                    # dargDouble
                            ;
////////////////////////////////////////////////////////////////////////////////



scriptExports               : KEYWORD_EXPORT LBRACE (exported (COMMA exported)*) RBRACE;

exported                    : IDENTIFIER (KEYWORD_AS IDENTIFIER)? (LBRACK exportDest RBRACK)?;

exportDest                  : KEYWORD_INTERNAL
                            | KEYWORD_CHAT (LPAREN perm=(KEYWORD_GM | KEYWORD_TRUSTED) RPAREN)?
                            | KEYWORD_ROLL LPAREN KEYWORD_DEFAULT OP_ASSIGN def=DECIMAL_LITERAL COMMA
                              KEYWORD_ROLL OP_ASSIGN rollName=IDENTIFIER
                            ;


literal                     : integerLiteral    # literalInteger
                            | NUMBER_LITERAL    # literalNumber
                            | STRING_LITERAL    # literalString
                            | BOOL_LITERAL      # literalBool
                            | NULL_LITERAL      # literalNull
                            ;

methodDeclaration           : KEYWORD_FUNCTION IDENTIFIER formalParameters KEYWORD_RETURNS returnType=type block
                            | KEYWORD_PROCEDURE IDENTIFIER formalParameters block
                            ;

formalParameters            : LPAREN formalParameterList? RPAREN ;

formalParameterList         : formalParameter (COMMA formalParameter)* ;

formalParameter             : type variableDeclarator;


blockStatement
                            | doStatement
                            | tryStatement
                            | switchStatement
                            | methodDeclaration
                            ;

statement                   : assertStatement
                            | switchStatement
                            | returnStatement
                            | breakStatement
                            | continueStatement
                            | variableDeclaration
                            | constantDeclaration
                            | statementExpression=expression
                            ;

assertStatement             : KEYWORD_ASSERT expression (COLON expression)?
                            ;


tryStatement                : KEYWORD_TRY block (catchClause+ finallyBlock? | finallyBlock)
                            ;

breakStatement              : KEYWORD_BREAK
                            ;

continueStatement           : KEYWORD_CONTINUE
                            ;

switchStatement             : KEYWORD_SWITCH parExpression LBRACE switchBlockStatementGroup* switchLabel* RBRACE
                            ;

returnStatement             : KEYWORD_RETURN expression?
                            ;


catchClause                 : KEYWORD_CATCH LPAREN IDENTIFIER RPAREN block ;

finallyBlock                : KEYWORD_FINALLY block ;

switchBlockStatementGroup   : switchLabel+ statements ;

switchLabel                 : KEYWORD_CASE constantExpression=expression COLON
                            | KEYWORD_DEFAULT COLON
                            ;


expression                  : methodCall
                            | LPAREN expression RPAREN
                            | diceExpression
                            | literal
                            | variable
                            | expression bop=DOT ( IDENTIFIER | methodCall )
                            | expression LBRACK expression RBRACK
                            | json
                            | prefix=OP_BANG expression
                            | expression bop=(OP_MUL | OP_DIV | OP_MOD) expression
                            | expression bop=(OP_ADD | OP_SUB) expression
                            | expression bop=(OP_LE | OP_GE | OP_GT | OP_LT) expression
                            | expression bop=(TYPE OF) type
                            | expression bop=(OP_EQUAL | OP_NOTEQUAL) expression
                            | expression bop=OP_BITAND expression
                            | expression bop=OP_CARET expression
                            | expression bop=OP_BITOR expression
                            | expression bop=OP_AND expression
                            | expression bop=OP_OR expression
                            | expression bop=OP_QUESTION expression COLON expression
                            | expression postfix=(OP_INC | OP_DEC)
                            | <assoc=right> variable bop=(OP_ASSIGN | OP_ADD_ASSIGN |
                            OP_SUB_ASSIGN | OP_MUL_ASSIGN | OP_DIV_ASSIGN | OP_AND_ASSIGN | OP_OR_ASSIGN | OP_XOR_ASSIGN | OP_MOD_ASSIGN ) expression
                            ;



variableDeclaration         : KEYWORD_VAR variableDeclarationAssign (COMMA variableDeclarationAssign )*
                            ;

variableDeclarationAssign   : variable COLON IDENTIFIER ( OP_ASSIGN expression )?
                            ;

constantDeclaration         : KEYWORD_CONST constantDeclarationAssign ( COMMA constantDeclarationAssign )* SEMI
                            ;

constantDeclarationAssign   : IDENTIFIER COLON IDENTIFIER ( LBRACK RBRACK)* OP_ASSIGN expression
                            ;

variableDeclarator          : KEYWORD_VAR variableDeclaratorId (OP_ASSIGN variableInitializer)? ;


variableDeclaratorId        : IDENTIFIER COLON IDENTIFIER ( LBRACK RBRACK )* ;

variableInitializer         : arrayInitializer
                            | expression
                            ;

arrayInitializer            : LBRACE (variableInitializer ( COMMA variableInitializer )* (COMMA)? )? RBRACE ;

////////

argumentList                : argument (COMMA argument)* ;

argument                    : (namedParameter=IDENTIFIER COLON)? expression
                            ;

listTypeDecl                : LBRACK RBRACK
                            ;

type                        : KEYWORD_INTEGER listTypeDecl?
                            | KEYWORD_NUMBER listTypeDecl?
                            | KEYWORD_STRING listTypeDecl?
                            | KEYWORD_ROLL listTypeDecl?
                            | KEYWORD_BOOLEAN listTypeDecl?
                            | KEYWORD_DICT listTypeDecl?
                            | IDENTIFIER listTypeDecl?
                            ;

json                        : jsonObj
                            | jsonArray
                            ;

jsonValue                   : NUMBER_LITERAL
                            | DECIMAL_LITERAL
                            | HEX_LITERAL
                            | STRING_LITERAL
                            | BOOL_LITERAL
                            | NULL_LITERAL
                            | jsonObj
                            | jsonArray
                            ;

jsonObj                     : LBRACE (jsonPair (COMMA jsonPair)*)? RBRACE
                            | LBRACE RBRACE
                            ;

jsonArray                   : LBRACK (jsonValue (COMMA jsonValue)*)? RBRACK
                            | LBRACK RBRACK
                            ;

jsonPair                    : STRING_LITERAL COLON jsonValue
                            ;



*/