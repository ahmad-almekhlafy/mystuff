class GameLauncher {

	public static void main(String[] args) {
		GuessGame test = new GuessGame();
		test.startGame();
	}
}

class Player {
	int number;

	void guess () {
		number = (int) (10 * Math.random());
	}
}

class GuessGame {
	Player p1 = new Player();
	Player p2 = new Player();
	Player p3 = new Player();

	void startGame() {
		int winningNumber = (int) (10 * Math.random());
		System.out.println("Number to guess is " + winningNumber);
		p1.guess();
		p2.guess();
		p3.guess();

		System.out.println("Player 1 says " + p1.number);
		System.out.println("Player 2 says " + p2.number);
		System.out.println("Player 3 says " + p3.number);

		if ( p1.number == winningNumber && p2.number == winningNumber && p3.number == winningNumber) {
			System.out.println("All Players have oddly won!");
		} else if (p1.number == winningNumber && p2.number == winningNumber) {
			System.out.println("Player 1 and Player 2 have both won!");
		} else if (p1.number == winningNumber && p3.number == winningNumber) {
			System.out.println("Player 1 and Player 3 have both won!");
		} else if (p2.number == winningNumber && p3.number == winningNumber) {
			System.out.println("Player 2 and Player 3 have both won!");
		} else if (p1.number == winningNumber) {
			System.out.println("Player 1 has won!");
		} else if (p2.number == winningNumber ) {
			System.out.println("Player 2 has won!");
		} else if (p3.number == winningNumber) {
			System.out.println("Player 3 has won!");
		} else {
			System.out.println("No one has won! Play again.");
		}
	}
}