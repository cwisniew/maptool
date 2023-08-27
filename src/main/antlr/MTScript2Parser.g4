parser grammar MTScript2Parser;


@header {
package net.rptools.maptool.mtscript2.parser;
}

options { tokenVocab=MTScript2Lexer; }

// Top level sentences

chat                        : (OPEN_SCRIPT_MODE script  CLOSE_SCRIPT_MODE | OPEN_ROLL_MODE simpleRoll CLOSE_ROLL_MODE | text)* ;

text                        : passThroughText
                            ;

passThroughText             : TEXT+
                            ;

script                      : stmt=statement
                            | stmtList=statements;


variable                    : scope=LOCAL_VAR_LEADER varName=IDENTIFIER
                            ;

group                       : LPAREN val=expression RPAREN                          # parenGroup
                            | LBRACE val=expression RBRACE                          # braceGroup
                            ;

diceExpression              : KEYWORD_ROLL dice
                            ;

simpleRoll                  : ROLL_DECIMAL_LITERAL ROLL_IDENTIFIER ROLL_DECIMAL_LITERAL
                            ;

dice                        :  numDice? diceName=IDENTIFIER diceArguments?
                            ;


numDice                     : integerLiteral
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

integerLiteral              : DECIMAL_LITERAL
                            | HEX_LITERAL
                            ;

methodDeclaration           : KEYWORD_FUNCTION IDENTIFIER formalParameters KEYWORD_RETURNS returnType=type block
                            | KEYWORD_PROCEDURE IDENTIFIER formalParameters block
                            ;

formalParameters            : LPAREN formalParameterList? RPAREN ;

formalParameterList         : formalParameter (COMMA formalParameter)* ;

formalParameter             : type variableDeclaratorId;

block                       : LBRACE statements* RBRACE ;


blockStatement              : ifStatement
                            | forStatement
                            | whileStatement
                            | doStatement
                            | tryStatement
                            | switchStatement
                            | methodDeclaration
                            ;

statement                   : assertStatement
                            | switchStatement
                            | returnStatement
                            | throwStatement
                            | breakStatement
                            | continueStatement
                            | variableDeclaration
                            | constantDeclaration
                            | statementExpression=expression
                            ;

assertStatement             : KEYWORD_ASSERT expression (OP_COLON expression)?
                            ;

forStatement                : KEYWORD_FOR LPAREN forControl RPAREN block
                            ;

whileStatement              : KEYWORD_WHILE parExpression block
                            ;

doStatement                 : KEYWORD_DO block KEYWORD_WHILE parExpression
                            ;

tryStatement                : KEYWORD_TRY block (catchClause+ finallyBlock? | finallyBlock)
                            ;

throwStatement              : KEYWORD_THROW expression
                            ;

breakStatement              : KEYWORD_BREAK
                            ;

continueStatement           : KEYWORD_CONTINUE
                            ;

switchStatement             : KEYWORD_SWITCH parExpression LBRACE switchBlockStatementGroup* switchLabel* RBRACE
                            ;

returnStatement             : KEYWORD_RETURN expression?
                            ;


ifStatement                 : KEYWORD_IF parExpression block (KEYWORD_ELSE KEYWORD_IF parExpression block)* (KEYWORD_ELSE block)?
                            ;


statements                  : ( block | statement SEMI | blockStatement | SEMI) ( block | statement SEMI | blockStatement | SEMI )*
                            ;



catchClause                 : KEYWORD_CATCH LPAREN IDENTIFIER RPAREN block ;

finallyBlock                : KEYWORD_FINALLY block ;

switchBlockStatementGroup   : switchLabel+ statements ;

switchLabel                 : KEYWORD_CASE constantExpression=expression OP_COLON
                            | KEYWORD_DEFAULT OP_COLON
                            ;

forControl                  : type variableDeclaratorId OP_COLON expression                  # forControlForeach
                            | forInit? SEMI expression? SEMI forUpdate=expressionList?  # forControlBasic
                            ;

forInit                     : variableDeclaration
                            | expressionList
                            ;

parExpression               : LPAREN expression RPAREN ;

expressionList              : expression (COMMA expression)* ;

methodCall                  : IDENTIFIER LPAREN expressionList? RPAREN  # exprMethodCall
                            ;

expression                  : LPAREN expression RPAREN
                            | diceExpression
                            | literal
                            | variable
                            | expression bop=DOT ( IDENTIFIER | methodCall )
                            | expression LBRACK expression RBRACK
                            | methodCall
                            | json
                            | prefix=OP_BANG expression
                            | expression bop=(OP_MUL | OP_DIV | OP_MOD) expression
                            | expression bop=(OP_ADD | OP_SUB) expression
                            | expression bop=(OP_LE | OP_GE | OP_GT | OP_LT) expression
                            | expression bop=KEYWORD_INSTANCEOF type
                            | expression bop=(OP_EQUAL | OP_NOTEQUAL) expression
                            | expression bop=OP_BITAND expression
                            | expression bop=OP_CARET expression
                            | expression bop=OP_BITOR expression
                            | expression bop=OP_AND expression
                            | expression bop=OP_OR expression
                            | expression bop=OP_QUESTION expression OP_COLON expression
                            | expression postfix=(OP_INC | OP_DEC)
                            | <assoc=right> expression bop=(OP_ASSIGN | OP_ADD_ASSIGN | OP_SUB_ASSIGN | OP_MUL_ASSIGN | OP_DIV_ASSIGN | OP_AND_ASSIGN | OP_OR_ASSIGN | OP_XOR_ASSIGN | OP_MOD_ASSIGN ) expression
                            ;


variableDeclaration         : type variableDeclarationAssign (COMMA variableDeclarationAssign )* SEMI
                            ;

variableDeclarationAssign   : variable ( OP_ASSIGN expression )?
                            ;

constantDeclaration         : KEYWORD_CONST type constantDeclarationAssign ( COMMA constantDeclarationAssign )* SEMI
                            ;

constantDeclarationAssign   : IDENTIFIER OP_ASSIGN expression
                            ;

variableDeclarator          : variableDeclaratorId (OP_ASSIGN variableInitializer)? ;

variableDeclaratorId        : scope=LOCAL_VAR_LEADER IDENTIFIER ( LBRACK RBRACK )* ;

variableInitializer         : arrayInitializer
                            | expression
                            ;

arrayInitializer            : LBRACE (variableInitializer ( COMMA variableInitializer )* (COMMA)? )? RBRACE ;

////////

arguments                   : LPAREN expressionList? RPAREN ;

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

jsonPair                    : STRING_LITERAL OP_COLON jsonValue
                            ;


