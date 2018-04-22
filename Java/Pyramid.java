class Pyramid {


	public static void main(String[] args) {
		int a = 0;
		int b = 1;
		int c = 0;
		int d = 5;
		
		while (a < 6) {
			
			while (d > 0) {
				System.out.print(" ");
				d--;
			}

			while (c < b) {
				System.out.print("*");
				c++;
			}

			System.out.println();
			a++;
			b += 2;
			c = 0;
			d = 5-a;
			
		}
	}
}