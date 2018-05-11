class BruteHello {

	public static void main(String[] args) throws InterruptedException {

		String text = "Hello World :)";

		char ch[] = new char[text.length()];
		int i = 0;
		while (ch[text.length() - 1] != text.charAt(text.length() - 1)) {
			ch[i] = 32;
	
			while (ch[i] != text.charAt(i)) {
				System.out.print(ch[i]);
				ch[i]++;
				Thread.sleep(50);
				System.out.print("\b");
			}
			System.out.print(ch[i]);
			i++;

		}

	}

}