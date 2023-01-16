%{
#include <stdio.h>
#include <stdlib.h>

#define YYDEBUG 1
%}

%token INT;
%token CHAR;
%token IF;
%token PRINT;
%token READ;
%token ELSE;
%token WHILE;
%token RETURN;

%token IDENTIFIER;
%token INTCONSTANT;
%token STRINGCONSTANT;

%token PLUS;
%token MINUS;
%token TIMES;
%token DIV;
%token MOD;
%token EQ;
%token BIGGER;
%token BIGGEREQ;
%token LESS;
%token LESSEQ;
%token EQQ;
%token NEQ;
%token AND;
%token OR;

%token SEMICOLON;
%token OPEN;
%token CLOSE;
%token BRACKETOPEN;
%token BRACKETCLOSE;
%token COMMA;

%start program 

%%
program: statement SEMICOLON program { printf("program -> statement ; program\n"); } | statement SEMICOLON {printf("program -> statement ;\n"); } ;

statement: int_statement { printf("statement -> int_statement\n"); }
        | assign_statement { printf("statement -> assign_statement\n"); }
        | while_statement { printf("statement -> while_statement\n"); }
        | function_call_statement { printf("statement -> function_call_statement\n"); }
        ;

int_statement: INT IDENTIFIER { printf("int_statement -> int IDENTIFIER\n"); }
             ;

expression: IDENTIFIER math_operator INTCONSTANT { printf("expression -> IDENTIFIER math_operator INTCONSTANT\n"); }
        | IDENTIFIER math_operator IDENTIFIER { printf("expression -> IDENTIFIER math_operator IDENTIFIER\n"); }
        ;

assign_statement: IDENTIFIER EQ expression { printf("assign_statement -> IDENTIFIER = expression\n"); }
               | IDENTIFIER EQ INTCONSTANT { printf("assign_statement -> IDENTIFIER = INTCONSTANT\n"); }
               ;

condition: IDENTIFIER relational_operator IDENTIFIER { printf("condition -> IDENTIFIER relational_operator IDENTIFIER\n"); }
        ;

while_statement: WHILE OPEN condition CLOSE BRACKETOPEN program BRACKETCLOSE { printf("while_statement -> while ( condition ) { program }\n"); }
              ;

function_call_statement: function_name OPEN IDENTIFIER CLOSE { printf("function_call_statement -> function_name ( IDENTIFIER )\n"); }
                    | function_name OPEN CLOSE { printf("function_call_statement -> function_name ( )\n"); }
                    ;

function_name: PRINT { printf("function_name -> print\n"); }
            | READ { printf("function_name -> read\n"); }
            ;

math_operator: PLUS { printf("math_operator -> +\n"); }
            | MINUS { printf("math_operator -> -\n"); }
            | TIMES { printf("math_operator -> *\n"); }
            | DIV { printf("math_operator -> /\n"); }
            ;

relational_operator: EQQ { printf("relational_operator -> ==\n"); }
                | LESS { printf("relational_operator -> <\n"); }
                | LESSEQ { printf("relational_operator -> <=\n"); }
                | BIGGER { printf("relational_operator -> >\n"); }
				| BIGGEREQ { printf("relational_operator -> >=\n"); }
				;

%%
yyerror(char *s)
{	
	printf("%s\n",s);
}

extern FILE *yyin;

main(int argc, char **argv)
{
	if(argc>1) yyin =  fopen(argv[1],"r");
	if(!yyparse()) fprintf(stderr, "\tOK\n");
} 