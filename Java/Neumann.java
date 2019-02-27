class Neumann {

	public static void main(String[] args) {
		int anfgangsZahl = Integer.parseInt(args[0]);
		System.out.print (1 + ") " + anfgangsZahl + " -> " );
		String zahlText = "" +  anfgangsZahl * anfgangsZahl;
		int [] array = new int[1001];
		array[0] = anfgangsZahl;
		int a = 1;

		while (zahlText.length() < 8) {
			zahlText = 0 + zahlText;
		}
		System.out.println( zahlText);

		for (int i = 0; i < 1000; i++) {

			System.out.print (i + 2 + ") " + zahlText.substring(2, 6) + " -> " );
			zahlText = "" + Integer.parseInt(zahlText.substring(2, 6)) * Integer.parseInt(zahlText.substring(2, 6));

			while (zahlText.length() < 8) {
				zahlText = 0 + zahlText;
			}

			for (int b = 0; b < array.length ; b++) {
				if (array[b] == Integer.parseInt(zahlText.substring(2, 6))) i = 1000;
			}
			array[a] = Integer.parseInt(zahlText.substring(2, 6));
			a++;

			System.out.println( zahlText);

		}

	}
}