'''
Created on 

@author: Danyu Yang
'''
import ply.lex as lex
import ply.yacc as yacc
import re

class A: pass


class Atom:
    name = None
    argu = []
    negated = False
    arguType = "var"
    def __init__(self, n, argStr, neg):
        self.name = n
        self.argu = argStr.split(",")
        if(re.findall(r'[A-Z]', self.argu[0]) != None):
            self.arguType = "const"
        self.negated = neg

    def getName(self):
        return self.name
    
    def getArgu(self):
        return self.argu
    
    def getType(self):
        return self.arguType
    
    def negate(self):
        if(self.negated == True):
            self.negated = False
        else:
            self.negated = True
            
    def show(self):
        argustr = ""
        for i in range(len(self.argu) - 1):
            argustr = argustr + self.argu[i] + ","
        argustr = argustr + self.argu[len(self.argu) - 1]
        if(self.negated == True):
            string = "~" + self.name + "(" + argustr + ")"    
        else:
            string = self.name + "(" + argustr + ")"    
        return string
            
class Clause:
    #connector is or
    atoms = []
    def __init__(self, a):
        self.atoms = []
        self.atoms.append(a)
        
    def negate(self):
        clauses = []
        if(self.isAtom()):
            a = self.atoms[0]
            a.negate()
            #print a.show()
            b = Clause(a)
            clauses.append(b)      
        else:      
            for a in self.atoms:
                #print a.show()
                a = a.negate()
                b = Clause(a)
                clauses.append(b)
        return clauses

    def isAtom(self):
        if(len(self.atoms) == 1):
            return True
        else:
            return False
    
    def orWith(self, b):
        if(b.isAtom()):            
            self.atoms.append(b.getAtoms()[0])
        else:
            for atom in b.getAtoms():
                self.atoms.append(atom)
        return self
            
    def getAtoms(self):
        return self.atoms
    
    def show(self):
        string = self.atoms[0].show()
        for i in range(1, len(self.atoms)):
            string += " | " + self.atoms[i].show()
        return string
            

class CNF:
    clauses = []
    def __init__(self):
        self.clauses = []
#         a = Atom(name, argu)
#         b = Clause(a)
#         self.clauses.append(b)
    
    def addClause(self, clause):
        self.clauses.append(clause)
        
    def negate(self):
        #self.show()
        if(self.isClause()):
            temp = self.clauses[0].negate() #return clauses each of which is a negated atom sentence
#             for clause in temp:
#                 print clause.show()
            self.clauses = temp
            return self
        else:
            nagetedcnf = CNF()
            for clause in self.clauses:
                temp = clause.negate()
                thecnf = CNF()
                for cl in temp:
                    thecnf.addClause(cl)
                nagetedcnf.andWith(thecnf)
            return nagetedcnf
    
    def isClause(self):
        if(len(self.clauses)):
            return True
        else:
            return False
    
    def andWith(self, cnf):
        if(cnf.isClause()):
            self.clauses.append(cnf.getClauses()[0])
        else:
            for clause in cnf.getClauses():
                self.clauses.append(clause)
        return self
    
    def orWith(self, cnf):
        if(cnf.isClause()):
            for clause in self.clauses:
                clause.orWith(cnf.getClauses()[0])
        else:
            for c1 in self.clause:
                for c2 in cnf.getClauses():
                    c1.orWtih(c2)
        return self
    
    def getClauses(self):
        return self.clauses
    
    def show(self):
        string = ""
        for clause in self.clauses:
            string += clause.show() + "\n"
        print string
        

tokens = (
       #'TERM',
       'NAME',
       'VARIABLE',
       'CONSTANT',
       'AND',
       'OR',
       'IMPLY',
       'NOT',
       'LPAREN',
       'RPAREN',
       'COMA',
    )

def myLexer():
    # Regular expression rules for simple tokens
    #t_TERM = r'[a-zA-Z0-9]+'
    t_AND = r'\&'
    t_OR = r'\|'
    t_NOT = r'\~'
    t_LPAREN = r'\('
    t_RPAREN = r'\)'
    t_COMA = r'\,'
    # A regular expression rule with some action code
        
    def t_IMPLY(t):
        r'=>'
        return t
        
    def t_VARIABLE(t):
        r'[a-z]+' 
        return t
        
    def t_CONSTANT(t):
        r'[a-zA-Z0-9]+'
        return t
    
    # A string containing ignored characters (spaces and tabs)
    t_ignore  = ' \t'
     
    # Error handling rule
    def t_error(t):
        print "Illegal character '%s'" % t.value[0]
        t.lexer.skip(1)
            
    return lex.lex()
    
def Parse(data):
    #YACC=======================================
#     def p_cnf_expr(p):
#         'cnf : LPAREN cnf RPAREN' 
#         p[0] = p[2]
    
    def p_cnf_not(p):
        'cnf : NOT LPAREN cnf RPAREN' 
        #print type(p[3])
        p[0] = p[3].negate()
    
    def p_cnf_imply(p):
        'cnf : cnf IMPLY clause' 
        cnf = CNF()
        cnf.addClause(p[3])  
        p[0] = p[1].negate().orWith(cnf)
    
    def p_cnf_or(p):
        'cnf : cnf OR clause' 
        p[0] = p[1].orWith(p[3])
    
    def p_cnf_and(p):
        'cnf : cnf AND clause' 
        cnf = CNF()
        cnf.addClause(p[3])        
        temp = p[1].andWith(cnf)
        p[0] = temp
    
    def p_cnf_clause(p):
        'cnf : clause' 
        p[0] = CNF()
        p[0].addClause(p[1])
    
    def p_clause_or(p):
        'clause : clause OR atom'
        b = Clause(p[3])
        p[0] = p[1].orWith(b)
    
    def p_clause_atom(p):
        'clause : atom'
        b = Clause(p[1]) 
        p[0] = b
    
    def p_atom_withnot(p):
        'atom : NOT CONSTANT LPAREN argus RPAREN'       
        a = Atom(p[2], p[4], True)
#         b = Clause(a) 
#         p[0] = CNF()
#         p[0].addClause(b)
        p[0] = a
        
    def p_atom_withoutnot(p):
        'atom : CONSTANT LPAREN argus RPAREN'       
        a = Atom(p[1], p[3], False)
        p[0] = a        
    
    def p_var(p):
        'var : VARIABLE' 
        p[0] = p[1]
    
    def p_const(p):
        'con : CONSTANT' 
        p[0] = p[1]
        
    def p_arugs_var(p):
        'argu : var'
        p[0] = p[1]
        
    def p_arugs_con(p):
        'argu : con'
        p[0] = p[1]
        
    def p_arugs_vars(p):
        'argus : argus COMA argu'
        p[0] = p[1] + ',' + p[3]
        
    def p_arugs_single(p):
        'argus : argu'
        p[0] = p[1]
            
    parser = yacc.yacc()
    res = parser.parse(data)
    return res
        
lexer = myLexer()
lexer.input("A(x, y) => B(John)")
# while True:
#     tok = lexer.token()
#     if not tok: break      # No more input
#     print tok
 
res = Parse("B(John) | A(x)")
res.show()