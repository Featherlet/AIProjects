'''
Created on 

@author: Danyu Yang
'''
class A(object): pass
a = A()
a.value = 1
def set_a(val):
    a.value = val

print a.value
set_a(5)
print a.value