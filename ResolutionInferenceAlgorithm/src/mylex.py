'''
Created on 

@author: Danyu Yang
'''

import ply.lex as lex

# List of token names.   This is always required
tokens = (
   'TERM',
   'AND',
   'OR',
   'IMPLY',
   'BICONDITIONAL',
   'NOT',
   'LPAREN',
   'RPAREN',
   'COMA',
)

# Regular expression rules for simple tokens
t_TERM = r'[a-zA-Z0-9]+'
t_AND    = r'\&'
t_OR   = r'\|'
t_NOT  = r'\~'
t_LPAREN  = r'\('
t_RPAREN  = r'\)'
t_COMA = r'\,'
# A regular expression rule with some action code

def t_FUNCNAME(t):
    r'[A-Z]+\('
    t.value = t.value[0:-1]
    return t

def t_IMPLY(t):
    r'[=>]'
    return t

def t_BICONDITIONAL(t):
    r'[<==>]'
    return t

def t_VARIABLE(t):
    r'[a-z]+' 
    return t

def t_CONSTANT(t):
    r'[a-zA-Z0-9]+'
    return t

# Define a rule so we can track line numbers
def t_newline(t):
    r'\n+'
    t.lexer.lineno += len(t.value)

# A string containing ignored characters (spaces and tabs)
t_ignore  = ' \t'

# Error handling rule
def t_error(t):
    print "Illegal character '%s'" % t.value[0]
    t.lexer.skip(1)

# Build the lexer
lexer = lex.lex()

# Test it out
data = "D(John,Alice)"

# Give the lexer some input
lexer.input(data)

# Tokenize
while True:
    tok = lexer.token()
    if not tok: break      # No more input
    print tok

