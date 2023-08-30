parser grammar MTScript2Parser;


@header {
package net.rptools.maptool.mtscript2.parser;
}

options { tokenVocab=MTScript2Lexer; }

// Top level sentences

chat                        : chatContents* EOF
                            ;

chatContents                : OPEN_SCRIPT_MODE script  CLOSE_SCRIPT_MODE
                            | OPEN_ROLL_MODE dice CLOSE_ROLL_MODE
                            | text
                            | OPEN_MARKDOWN_LINK_MODE MARKDOWN_LINK_TEXT CLOSE_MARKDOWN_LINK_MODE
                            | OPEN_HANDLBARS_RAW_MODE HANDLEBARS_RAW_TEXT CLOSE_HANDLEBARS_RAW_MODE
                            | OPEN_HANDLEBARS_MODE HANDLEBARS_TEXT CLOSE_HANDLEBARS_MODE
                            ;


text                        : TEXT+
                            ;

script                      : statement+
                            ;

statement                   : topLevelStatement
                            | blockStatement
                            ;

topLevelStatement           : exportStatement SEMI
                            | importStatement SEMI
                            ;

blockStatement              : variableDeclarationOrInit SEMI
                            | constantDeclarationAndInit SEMI
                            | switchStatement
                            | expression SEMI
                            | loopLabel? forStatement
                            | loopLabel? whileStatement
                            | loopLabel? doStatement SEMI
                            | ifStatement
                            | tryStatement
                            | throwStatement SEMI
                            | variableAssign SEMI
                            | yieldStatement SEMI
                            | breakStatement SEMI
                            | continueStatement SEMI
                            | assertStatement SEMI
                            | returnStatement SEMI
                            | inlineFunctionDefinition SEMI
                            | functionDefinition
                            | procedureDefinition
                            ;

block                       : LBRACE blockStatement+ RBRACE
                            ;

exportStatement             : KEYWORD_EXPORT name=IDENTIFIER KEYWORD_AS fullyQualifiedName exportDestination?
                            ;

importStatement             : KEYWORD_IMPORT name=fullyQualifiedName KEYWORD_FROM fromPackage=fullyQualifiedName (KEYWORD_AS asName=fullyQualifiedName)?
                            ;

exportDestination           : exportDestKeyword (COMMA exportDestKeyword)*
                            ;

exportDestKeyword           : KEYWORD_TRUSTED
                            | KEYWORD_CHAT
                            | KEYWORD_GM
                            ;

fullyQualifiedName          : IDENTIFIER (DOT IDENTIFIER)*
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

loopLabel                   : IDENTIFIER COLON
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

breakStatement              : KEYWORD_BREAK (label=IDENTIFIER)?
                            ;

continueStatement           : KEYWORD_CONTINUE (label=IDENTIFIER)?
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
                            | postfixExpression
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
                            | dictValue
                            | listValue
                            | embeddedDiceRoll
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
                            | expression postfix=(OP_INC | OP_DEC)
                            ;

switchExpression            : KEYWORD_SWITCH LPAREN expression RPAREN LBRACE switchExpressionLabel+ (KEYWORD_DEFAULT OP_ARROW (block | expression) SEMI)? RBRACE
                            ;

switchExpressionLabel       : KEYWORD_CASE literal (COMMA literal)* OP_ARROW (block | expression SEMI)
                            | KEYWORD_CASE KEYWORD_TYPE KEYWORD_OF type OP_ARROW (block | expression SEMI)
                            ;


yieldStatement              : KEYWORD_YIELD expression
                            ;


switchStatement             : KEYWORD_SWITCH LPAREN expression RPAREN LBRACE switchStatementBlock* RBRACE
                            ;

switchStatementBlock        : (switchStatementLabel blockStatement+)+ (KEYWORD_DEFAULT COLON blockStatement+)?
                            ;

switchStatementLabel        : KEYWORD_CASE literal (COMMA literal)* COLON
                            | KEYWORD_CASE KEYWORD_TYPE KEYWORD_OF type COLON
                            ;

literal                     : DECIMAL_LITERAL
                            | HEX_LITERAL
                            | NUMBER_LITERAL
                            | STRING_LITERAL
                            | BOOL_LITERAL
                            | NULL_LITERAL
                            | NONE_LITERAL
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

assertStatement             : KEYWORD_ASSERT booleanTest (OP_ARROW expression)?
                            ;

returnStatement             : KEYWORD_RETURN expression?
                            ;

inlineFunctionDefinition    : KEYWORD_FUNCTION funcName=IDENTIFIER LPAREN formalParameterList? RPAREN COLON returnType=type OP_ARROW expression
                            ;

functionDefinition          : KEYWORD_FUNCTION funcName=IDENTIFIER LPAREN formalParameterList? RPAREN COLON returnType=type block
                            ;

procedureDefinition         : KEYWORD_PROCEDURE procName=IDENTIFIER LPAREN formalParameterList? RPAREN block
                            ;

formalParameterList         : formalParameter (COMMA formalParameter)*
                            ;

formalParameter             : variable COLON type (OP_ASSIGN expression)?
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


dictValue                   : LBRACE (dictPair (COMMA dictPair)*)? RBRACE
                            | LBRACE RBRACE
                            ;

listValue                   : LBRACK (expression (COMMA expression)*)? RBRACK
                            | LBRACK RBRACK
                            ;

dictPair                    : field=(STRING_LITERAL | IDENTIFIER) COLON expression
                            ;

embeddedDiceRoll            : OPEN_EMBEDED_ROLL_MODE dice CLOSE_EMBEDED_ROLL_MODE
                            ;



////////////////////////////////////////////////////////////////////////////////
/*

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







arrayInitializer            : LBRACE (variableInitializer ( COMMA variableInitializer )* (COMMA)? )? RBRACE ;

////////

*/