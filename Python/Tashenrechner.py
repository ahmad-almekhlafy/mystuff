# April 2018
print ("Bitte geben Sie das erste Zahl ein:")
Zahl1 = int(input()) 
print ("Bitte geben Sie das zweite Zahl ein:")

Zahl2 = int(input()) 
print ("Bitte geben Sie ein Operator ein:")
Operator = input()
if Operator == "+" :
	print(Zahl1 , "+" , Zahl2 , " = " , (Zahl1 + Zahl2))
elif Operator == "-" :
	print(Zahl1 , "-" , Zahl2 , " = " , (Zahl1 - Zahl2))
elif Operator == "*" :
	print(Zahl1 , "*" , Zahl2 , " = " , (Zahl1 * Zahl2))
elif Operator == "/" :
	print(Zahl1 , "/" , Zahl2 , " = " , (Zahl1 / Zahl2))
else: 
	print("Error: Operator nicht erkannt!")            