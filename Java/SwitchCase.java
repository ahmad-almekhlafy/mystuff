// April 2018
import java.util.Scanner;
class SwitchCase {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.print("Fuer Gegenteil von hoch geben Sie 1 ein, von klein 2, und von stark 3:");
		int cases = sc.nextInt();
		String Gegenteil;

		switch (cases) {
		case 1: Gegenteil = "hoch < - > tief";
			break;
		case 2: Gegenteil = "klein < - > gross";
			break;
		case 3: Gegenteil = "stark < - > schwach";
			break;
		default: Gegenteil = "Error!";
		}
		System.out.print(Gegenteil);
	}
}