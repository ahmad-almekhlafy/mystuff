import java.util.Scanner;

/**
 * @author Ahmad Al-Mekhlafy 4934736 Gruppe 1b
 */
class Drachenkampf2 {

	// Hier werden die Anfangslebenspunkte, Genauigkeit von waffen und ihre Angriffswerte bestimmt.
	private static int dLebenspunkte = 10;
	private static int hLebenspunkte = 10;
	private static int pfeilBogen = 3;
	private static int pfeilBogenZufall = 70;
	private static int schwert = 2;
	private static int schwertZufall = 85;
	private static int feuer = 4;
	private static int feuerZufall = 50;
	private static char[][] arena;
	private static int arenaBreite;
	private static int arenaLaenge;
	private static int[] dPos = new int[2];
	private static int[] hPos = new int[2];

	public static void main(String[] args) throws InterruptedException {
		arenaLaenge = Integer.parseInt(args[0]);
		arenaBreite = Integer.parseInt(args[1]);

		// Terminert, wenn die Arena kleiner als 8*8 ist.
		if (arenaBreite < 8 || arenaLaenge < 8) {
			System.out.println("** Die Arena muss mindestens 8*8 gro\u00df sein. Drachenkampf wird terminiert. **");
			Thread.sleep(3000);
			System.exit(0);
		}

		Scanner sc = new Scanner(System.in);
		System.out.println("Der Kampf beginnt");

		//Positionen von Drachen und Helden am Anfang.
		dPos[0] = 1;
		dPos[1] = arenaBreite / 2;
		hPos[0] = arenaLaenge - 2;
		hPos[1] = arenaBreite / 2;
		zeichneArena();

		while (hLebenspunkte > 0 && dLebenspunkte > 0  ) {	// Das Spiel endet, wenn die Lebenspunkte einer Partei 0 erreicht.
			System.out.println("w,a,s,d,q zur bewegung nutzen. Bei q bleibt der Spieler, wo er ist.");

			// Die folgende while-Schleife zwingt den Nutzer, w,a,s,d oder q einzugeben.
			String bewegung;
			while (true) {
				bewegung = sc.nextLine();
				if (bewegung.equals("w") || bewegung.equals("a") || bewegung.equals("s") || bewegung.equals("d") || bewegung.equals("q") ) {
					if (bewege(bewegung)) {
						break;	// Falls gewünschtes Feld frei und Bewegeung möglich, while-Scleife abbrechen.
					}
				} else {
					System.out.println("** w,a,s,d,q zur bewegung nutzen. Keine andere Eingabe ist erlaubt. **");
				}
			}

			zeichneArena();
			status();
			System.out.println("Der Held kann mit Pfeil und Bogen (1) oder mit dem Schwert (2) angreifen.");

			// Die folgende while-Schleife zwingen den Nutzer, entweder 1 oder 2 einzugeben, kein Text und keine andere Zahlen.
			String input = sc.nextLine();
			while ( !input.equals("1") && !input.equals("2")) {
				System.out.println("** Eingabe nicht erlaubt. Bitte entweder (1) f\u00FCr Pfeil und Bogen oder (2) f\u00FCr Schwert eingeben. **");
				input = sc.nextLine();
			}
			int waffe = Integer.parseInt(input);

			// Schwert kann aus der Ferne durch dieser if-Bedingung nicht eingesetzt werden.
			if (waffe == 2 && abstand() > 1 ) {
				System.out.println("** Schwert kann aus der Ferne nicht eingesetzt werden. Der Held wird mit Pfeil und Bogen attakieren. **");
				waffe = 1;
			}

			if (waffe == 1) { // Wenn der Nützer 1 eingibt, attackiert der Held mit Pfeil und Bogen und reagiert der Drache.
				if (zufallGen(pfeilBogenZufall) - abstand() >= 25) { // Größe Ferne = Weniger Treffwahrschenlichkeit
					System.out.println("Der Drache wurde getroffen.");
					dLebenspunkte -= pfeilBogen;
				} else {
					System.out.println("Der Drache wurde verfehlt.");
				}
			} else if (waffe == 2) { // Wenn der Nützer 2 eingibt, attackiert der Held mit schwert und reagiert der Drache.
				if (zufallGen(schwertZufall) >= 25) {
					System.out.println("Der Drache wurde getroffen.");
					dLebenspunkte -= schwert;
				} else {
					System.out.println("Der Drache wurde verfehlt.");
				}
			}

			if (zufallGen(feuerZufall) >= 25) {
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
	 * Die Methode status gibt aus, wie viele Lebenspunkte für den Helden und den Drachen übrig sind.
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
	 * Wie es vom Namen zu verstehen.
	 */
	public static void zeichneArena() {
		arena = new char[arenaLaenge][arenaBreite];
		arena[dPos[0]][dPos[1]] = 'D';
		arena[hPos[0]][hPos[1]] = 'H';

		for (int i = 0; i < arena.length; i++) {
			arena[i][0] = '#';
			arena[i][arena[0].length - 1] = '#';
			for (int z = 0; z < arena[0].length; z++) {
				arena[0][i] = '#';
				arena[arena.length - 1][i] = '#';
			}
		}
		for (int i = 0; i < arena.length; i++) {
			for (int z = 0; z < arena[0].length; z++) {
				System.out.print(arena[i][z]);
			}
			System.out.print("\n");
		}
	}

	/**
	 * Pruft, ob ein Feld besetzt oder nicht.
	 * @param  y Reihe des Feldes
	 * @param  x Spalte des Feldes
	 * @return   True, wenn frei. False, wenn nicht.
	 */
	public static boolean begehbar( int y, int x) {
		if (arena[y][x] == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Bewegt den Held zu einer gewünschten Feld, wenn dieses Feld frei ist.
	 * @param  richtung richtung der bewegung
	 * @return          Wenn das Bewegen erfolgt, ist bewege True, wenn nicht, dann False.
	 */
	public static boolean bewege(String richtung) {
		if (richtung.equals("w") && begehbar(hPos[0] - 1, hPos[1])) {
			hPos[0]--;
			return true;
		} else if (richtung.equals("a") && begehbar(hPos[0], hPos[1] - 1)) {
			hPos[1]--;
			return true;
		} else if (richtung.equals("s") && begehbar(hPos[0] + 1, hPos[1])) {
			hPos[0]++;
			return true;
		} else if (richtung.equals("d") && begehbar(hPos[0], hPos[1] + 1)) {
			hPos[1]++;
			return true;
		} else if (richtung.equals("q")) {
			return true;
		} else {
			System.out.println("** Das gew\u00FCnschte Feld ist besetzt. Bitte ein freies Feld ausw\u00e4hlen. **");
			return false;
		}
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

	/**
	 * Berechnet Abstand des Helden vom Drachen
	 * @return Gibt berechneteen Abstand zurück
	 */
	public static int abstand() {
		return Math.abs((hPos[0] - dPos[0])) + Math.abs((hPos[1] - dPos[1]));
	}
}