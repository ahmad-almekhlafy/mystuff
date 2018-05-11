class BeerSong {

	public static void main(String[] args) {
		int x = 99;

		while (x > 0) {
			if (x == 1) {
				System.out.println(x + " bottles of beer on the wall, " + x + " bottles of beer.");
				System.out.println("Take one down and pass it around, no more bottles of beer on the wall.\n");
			} else {
				System.out.println(x + " bottles of beer on the wall, " + x + " bottles of beer.");
				System.out.println("Take one down and pass it around, " + (x - 1)  + " bottles of beer on the wall.\n");
			}

			x--;
		}

		System.out.println("No more bottles of beer on the wall, no more bottles of beer." +
		                   "\nGo to the store and buy some more, 99 bottles of beer on the wall.");
	}

}