import java.util.Scanner;

/**
 * @author Ahmad Al-Mekhlafy 4934736 Gruppe 99
 * @author Abdullah Nasser 4933504 Gruppe 99
 */
class Drachenkampf {

	// Hier werden die Anfangslebenspunkte, Genauigkeit von waffen und ihre Angriffswerte bestimmt.
	private static int dLebenspunkte = 10;
	private static int hLebenspunkte = 10;
	private static int pfeilBogen = 3;
	private static int pfeilBogenZufall = 70;
	private static int schwert = 2;
	private static int schwertZufall = 85;
	private static int feuer = 4;
	private static int feuerZufall = 50;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Der Kampf beginnt");
		status();

		while (hLebenspunkte > 0 && dLebenspunkte > 0  ) {	// Das Spiel endet, wenn die Lebenspunkte einer Partei 0 erreicht.
			System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem schwert (2) angreifen.");

			/**
			 * Folgendes sind zwei Weisen, um den Nutzer zu zwingen,
			 * entweder 1 oder 2 einzugeben, kein Text und keine andere Zahlen.
			 */

			// Die erste, und einfachere Weise.
			String input = sc.nextLine();
			while ( !input.equals("1") && !input.equals("2")) {
				System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem schwert (2) angreifen.");
				input = sc.nextLine();
			}
			int waffe = Integer.parseInt(input);

			/* Und hier ist die zweite, die eigentlich nicht genutzt wird, da sie ein bisschen komliziert ist.
			int waffe;
			do {
				while (!sc.hasNextInt()) {
					sc.next();
					System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem schwert (2) angreifen.");
				}

				waffe = sc.nextInt();
				if (waffe != 1 && waffe != 2) {
					System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem schwert (2) angreifen.");
				}
			} while ( waffe != 1 && waffe != 2 );
			 */

			if (waffe == 1) { // Wenn der Nützer 1 eingibt, attackiert der Held mit Pfeil und Bogen und reagiert der Drache.
				if (zufallGen(pfeilBogenZufall) >= 35) {
					System.out.println("Der Drache wurde getroffen.");
					dLebenspunkte -= pfeilBogen;
				} else {
					System.out.println("Der Drache wurde verfehlt.");
				}
			} else if (waffe == 2) { // Wenn der Nützer 2 eingibt, attackiert der Held mit schwert und reagiert der Drache.
				if (zufallGen(schwertZufall) >= 35) {
					System.out.println("Der Drache wurde getroffen.");
					dLebenspunkte -= schwert;
				} else {
					System.out.println("Der Drache wurde verfehlt.");
				}
			}
			if (zufallGen(feuerZufall) >= 35) {
						System.out.println("Der Held wurde verletzt.");
						hLebenspunkte -= feuer;
					} else {
						System.out.println("Der Drache hatte eine Fehlz\u00FCndung. Gl\u00FCck gehabt.");
					}
					status();
		}

		// Das Ergibnis des Kampfs.
		if (dLebenspunkte <= 0 && hLebenspunkte <= 0 ) {
			System.out.println("Weder der Drache noch der Held konnte die Schlacht \u00FCberleben.");
		} else if (hLebenspunkte <= 0 ) {
			System.out.println("Der Held wurde get\u00F6tet :-(");
		} else {
			System.out.println("Der Drache wurde besiegt. Hurra.");
		}
	}

	/**
	 * Die Methode status gibt aus,
	 * wie viele Lebenspunkte für den Helden
	 * und den Drachen übrig sind.
	 */
	public static void status() {
		System.out.print("Leben des Helden  : ");
		for (int hZaehler = hLebenspunkte; hZaehler > 0; hZaehler--) {
			System.out.print("O ");
		}

		System.out.print("\nLeben des Drachen : ");
		for (int dZaehler = dLebenspunkte; dZaehler > 0; dZaehler--) {
			System.out.print("X ");
		}
		
		System.out.println("\n");
	}

	/**
	 * Die Funktion zufallGen nutzt die zufallZahl einer
	 * waffe, um eine Trefferwahrscheinlichkeit zu generieren.
	 * @param  zufallZahl Die Genauigkeit einer Waffe
	 * @return            Gibt die generierte Zufallzahl zurück
	 */
	public static int zufallGen(int zufallZahl) {
		return (int) (Math.random() * zufallZahl);
	}
}