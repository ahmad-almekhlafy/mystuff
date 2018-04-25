import java.util.Scanner;
class  Drachenkampf {

	/**
	 * Hier werden die Anfangslebenspunkte,
	 * Genauigkeit von Waffen und ihre Angriffswerte bestimmt.
	 */
	int dLebenspunkte = 10;
	int hLebenspunkte = 10;
	int PfeilBogen = 3; int PfeilBogenZufall = 70;
	int Schwert = 2; int SchwertZufall = 85;
	int Feuer = 4; int FeuerZufall = 50;

	public static void main(String[] args) {
		Drachenkampf Spiel = new Drachenkampf();
		Scanner sc = new Scanner(System.in);
		System.out.println("Der Kampf beginnt");
		Spiel.Status();

		while (Spiel.hLebenspunkte > 0 && Spiel.dLebenspunkte > 0  ) {	// Das Spiel endet, wenn die Lebenspunkte einer Partei 0 erreicht.

			System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.");

			/**
			 * Folgendes sind zwei Weisen, um den Nutzer zu zwingen,
			 * entweder 1 oder 2 einzugeben, kein Text und keine andere Zahlen.
			 */
			

			// Die erste, und einfachere Weise.
			 
			String Input = sc.nextLine();
			while ( !Input.equals("1") && !Input.equals("2")) {
				System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.");
				Input = sc.nextLine();
			}
			int Waffe = Integer.parseInt(Input);
			
			
			/* Und hier ist die zweite, die eigentlich nicht genutzt wird, da sie ein bisschen komliziert ist.

			int Waffe;
			do {
				while (!sc.hasNextInt()) {
					sc.next();
					System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.");
				}

				Waffe = sc.nextInt();
				if (Waffe != 1 && Waffe != 2) {
					System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.");
				}
			} while ( Waffe != 1 && Waffe != 2 );
			 */
			

			if (Waffe == 1) { // Wenn der Nützer 1 eingibt, attackiert der Held mit Pfeil und Bogen und reagiert der Drache.
				if (Spiel.ZufallGen(Spiel.PfeilBogenZufall) >= 35) {
					System.out.println("Der Drache wurde getroffen.");
					Spiel.dLebenspunkte -= Spiel.PfeilBogen;

					if (Spiel.ZufallGen(Spiel.FeuerZufall) >= 35) {
						System.out.println("Der Held wurde verletzt.");
						Spiel.hLebenspunkte -= Spiel.Feuer;
					} else {
						System.out.println("Der Drache hatte eine Fehlz\u00FCndung. Gl\u00FCck gehabt.");
					}
					Spiel.Status();

				} else {
					System.out.println("Der Drache wurde verfehlt.");

					if (Spiel.ZufallGen(Spiel.FeuerZufall) >= 35) {
						System.out.println("Der Held wurde verletzt.");
						Spiel.hLebenspunkte -= Spiel.Feuer;
					} else {
						System.out.println("Der Drache hatte eine Fehlz\u00FCndung. Gl\u00FCck gehabt.");
					}
					Spiel.Status();
				}

			} else if (Waffe == 2) { // Wenn der Nützer 2 eingibt, attackiert der Held mit Schwert und reagiert der Drache.
				if (Spiel.ZufallGen(Spiel.SchwertZufall) >= 35) {
					System.out.println("Der Drache wurde getroffen.");
					Spiel.dLebenspunkte -= Spiel.Schwert;

					if (Spiel.ZufallGen(Spiel.FeuerZufall) >= 35) {
						System.out.println("Der Held wurde verletzt.");
						Spiel.hLebenspunkte -= Spiel.Feuer;
					} else {
						System.out.println("Der Drache hatte eine Fehlz\u00FCndung. Gl\u00FCck gehabt.");
					}
					Spiel.Status();

				} else {
					System.out.println("Der Drache wurde verfehlt.");
					if (Spiel.ZufallGen(Spiel.FeuerZufall) >= 35) {
						System.out.println("Der Held wurde verletzt.");
						Spiel.hLebenspunkte -= Spiel.Feuer;
					} else {
						System.out.println("Der Drache hatte eine Fehlz\u00FCndung. Gl\u00FCck gehabt.");
					}
					Spiel.Status();
				}
			}

		}

		// Das Ergibnis des Kampfs.
		if (Spiel.dLebenspunkte <= 0 && Spiel.hLebenspunkte <= 0 ) {
			System.out.println("Weder der Drache noch der Held konnte die Schlacht überleben.");
		} else if (Spiel.hLebenspunkte <= 0 ) {
			System.out.println("Der Held wurde get\u00F6tet :-(");
		} else {
			System.out.println("Der Drache wurde besiegt. Hurra.");
		}
	}


	/**
	 * Die Methode Status gibt aus,
	 * wie viele Lebenspunkte für den Helden
	 * und den Drachen übrig sind.
	 */
	public void Status() {
		int hZaehler = hLebenspunkte;
		int dZaehler = dLebenspunkte;

		System.out.print("Leben des Helden  : ");
		while (hZaehler > 0) {
			System.out.print("O ");
			hZaehler--;
		}

		System.out.print("\nLeben des Drachen : ");
		while (dZaehler > 0) {
			System.out.print("X ");
			dZaehler--;
		}

		System.out.println("\n");
	}


	/**
	 * Die Funktion ZufallGen nutzt die ZufallZahl einer
	 * Waffe, um eine Trefferwahrscheinlichkeit zu generieren.
	 */
	public int ZufallGen(int ZufallZahl) {
		return (int) (Math.random() * ZufallZahl);
	}
}