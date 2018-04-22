class Pyramid {


	public static void main(String[] args) {
		int b = 1;
		int c = 0;
		
		
		for (int a = 0;  a < 6; a++) {
			
			for (int d = 5; d > 0; d--) {
				System.out.print(" ");
			}

			while (c < b) {
				System.out.print("*");
				c++;
			}

			System.out.println();
			
			b += 2;
			c = 0;
			d = 4-a;
			
		}
	}
}