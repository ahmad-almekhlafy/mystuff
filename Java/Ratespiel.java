import java.util.Scanner;
class Ratespiel {

	public static void main(String[] args) {
		int zahlVersuche = 0;
		int eingabe = Integer.parseInt(args[0]);
		int randomZahl = (int) (Math.random() * 100);
		Scanner sc = new Scanner(System.in);

		while (eingabe < 0 || eingabe > 100 ) {
			System.out.print("Eingabe muss zwischen 0 und 100. Bitte eine neue Zahl eingeben: ");
			eingabe = sc.nextInt();
		}

		for ( ; randomZahl != eingabe; zahlVersuche++) {

			if (eingabe < 25) {
				randomZahl = (int) (Math.random() * 25);
			} else if ( eingabe >= 25 && eingabe < 50 ) {
				randomZahl = (int) (Math.random() * 25) + 25;
			} else if ( eingabe >= 50 && eingabe < 75) {
				randomZahl = (int) (Math.random() * 25) + 50;
			} else if ( eingabe >= 75 && eingabe < 100) {
				randomZahl = (int) (Math.random() * 25) + 75 ;
			} else {
				randomZahl = (int) (Math.random() * 25) + 100 ;
			}

			System.out.println(randomZahl);

		}

		System.out.println("Zahl der Versuche:" + zahlVersuche);

	}

}