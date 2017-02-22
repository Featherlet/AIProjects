'''
Created on 

@author: Danyu Yang
'''
import ply.lex as lex
import ply.yacc as yacc

class CNF:
    content = None
    def __init__(self, string):
        content = string
        
