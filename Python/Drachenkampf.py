import random
dLebenspunkte = 10
hLebenspunkte = 10
PfeilBogen = 3 
PfeilBogenZufall = 70
Schwert = 2 
SchwertZufall = 85
Feuer = 4
FeuerZufall = 50

def Status():
	hZaehler = hLebenspunkte
	dZaehler = dLebenspunkte

	print("Leben des Helden  : ",end='')
	while (hZaehler > 0):
		print("O ", end='')
		hZaehler -= 1
	print("\nLeben des Drachen : ",end='')
	while (dZaehler > 0):
		print("X ", end='')
		dZaehler-= 1
	
def ZufallGen(max):
		return (random.randint(0,max)) 
	
print("Der Kampf beginnt")
Status()

while hLebenspunkte > 0 and dLebenspunkte > 0:

	print("\n\nDer Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.")
	Waffe= input()
	
	while Waffe != "1" and Waffe != "2":
		print("Der Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.")
		Waffe= input()	
	Waffe = int(Waffe)
			
	if Waffe == 1: 
		if (ZufallGen(PfeilBogenZufall) >= 25):
			print("Der Drache wurde getroffen.")
			dLebenspunkte -= PfeilBogen

		else:
			print("Der Drache wurde verfehlt.")

	elif Waffe == 2:
			if ZufallGen(SchwertZufall) >= 25:
				print("Der Drache wurde getroffen.")
				dLebenspunkte -= Schwert

			else: 
				print("Der Drache wurde verfehlt.")

	if (ZufallGen(FeuerZufall) >= 25):
		print("Der Held wurde verletzt.")
		hLebenspunkte -= Feuer
	else:
		print("Der Drache hatte eine Fehlz\u00FCndung. Gl\u00FCck gehabt.")
	
	Status()
				
if dLebenspunkte <= 0 and hLebenspunkte <= 0 :
	print("\n\nWeder der Drache noch der Held konnte die Schlacht überleben.")
elif (hLebenspunkte <= 0 ):
	print("\n\nDer Held wurde get\u00F6tet :-(")
else:
	print("\n\nDer Drache wurde besiegt. Hurra.")
		
	