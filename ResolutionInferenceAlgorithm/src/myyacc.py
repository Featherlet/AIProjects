'''
Created on 

@author: Danyu Yang
'''
# Yacc example

import ply.yacc as yacc

# Get the token map from the lexer.  This is required.
from mylex import tokens

def p_atom_predicate(p):
    'atom : NAME LPAREN VARIABLE RPAREN' 
    p[0]
    
    
# def p_op_imply(p):
#     'expression : term IMPLY term'
#     print type(p)
#     print type(p[1])
#     p[0] = p[1].not self().or with(p[3])    

def p_op_and(p):
    'expression : ' 

# Error rule for syntax errors
def p_error(p):
    print "Syntax error in input!"

# Build the parser
parser = yacc.yacc()

while True:
   try:
       s = raw_input('FOL > ')
   except EOFError:
       break
   if not s: continue
   result = parser.parse(s)
   print result