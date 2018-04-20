import java.util.Scanner;
class Taschenrechner {

	public static void main (String[] args) {

		Scanner sc = new Scanner(System.in);
		System.out.print("Bitte geben Sie das erste Zahl ein:");
		int Zahl1 = sc.nextInt();
		System.out.print("Bitte geben Sie das zweite Zahl ein:");
		int Zahl2 = sc.nextInt();
		sc.nextLine();
		System.out.print("Bitte geben Sie ein Operator ein:");
		String Operator = sc.nextLine();

		if (Operator.equals("+")) {
			System.out.print(Zahl1 + "+" + Zahl2 + " = " + (Zahl1 + Zahl2));
		} else if (Operator.equals("-")) {
			System.out.print(Zahl1 + "-" + Zahl2 + " = " + (Zahl1 - Zahl2));

		} else if (Operator.equals("*")) {
			System.out.print(Zahl1 + "*" + Zahl2 + " = " + (Zahl1 * Zahl2));
		} else if (Operator.equals("/")) {
			System.out.print(Zahl1 + "/" + Zahl2 + " = " + (Zahl1 / Zahl2));
		} else {
			System.out.print("Error: Operator nicht erkannt!");
		}


	}

}