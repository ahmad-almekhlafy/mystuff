public class Sets {
	public static void main(String[] args) {
		char []a = {'0', '1', '2' , '3', 'h', 'g', 'j','b'};
		char []b = {'0', '1' , 'x', '3', 'b'};
		CharSet aSet = new CharSet(a);
		CharSet bSet = new CharSet(b);
		System.out.println("Set a = " + aSet);
		System.out.println("Size of Set a = " + aSet.getSize());
		System.out.println("Set b = " + bSet);
		System.out.println("Set a = Set b ? " + aSet.equals(bSet));
		System.out.println("Durschnitt von a und b = " + aSet.durchschnitt(bSet));
		System.out.println("vereinigung von a und b = "+ aSet.vereinigung(bSet));

	}
}
class CharSet {
	private char [] set ;
	private int size ;
	public CharSet () {
	}
	public CharSet ( char [] set ) {
		setSet(set);
	}
	public char [] getSet () {
		return set;
	}
	public int getSize () {
		return set.length;
	}
	public void setSet ( char [] a) {
		set = new char[a.length];
		for (int i = 0; i < getSize(); i++) {
			set[i] = a[i];
		}
	}
	public boolean equals ( Object x) {
		if (x == null ) return false ;
		if (x.getClass () != getClass ()) return false;
		CharSet other = (CharSet) x;
		if (getSize() != other.getSize()) return false;
		boolean istDa = false;
		for (int i = 0; i != getSize(); i++) {
			for (int k = 0; k != other.getSize(); k++) {
				if (getSet()[i] == other.getSet()[k]) {
					istDa = true;
					break;
				}
			}
			if (istDa == false) return false;
			istDa = false;
		}
		return true ;
	}
	public String toString () {
		String setString = "{ ";
		for (int i = 0; i < getSize(); i++) {
			if (i == getSize() - 1) {
				setString = setString + getSet()[i] + " ";
			} else {
				setString = setString + getSet()[i] + " , ";
			}
		}
		setString = setString + "}";
		return setString;
	}
	public CharSet durchschnitt ( CharSet c) {
		int l = 0;
		for (int i = 0; i < getSize(); i++) {
			for (int k = 0; k < c.getSize(); k++) {
				if (getSet()[i] == c.getSet()[k]) {
					l++;
					break;
				}
			}
		}
		char[] newCharSet = new char[l];
		l = 0;
		for (int g = 0; g < getSize(); g++) {
			for (int h = 0; h < c.getSize(); h++) {
				if (getSet()[g] == c.getSet()[h]) {
					newCharSet[l] = getSet()[g];
					l++;
					break;
				}
			}
		}
		CharSet newSet = new CharSet(newCharSet);
		return newSet;
	}

	public CharSet vereinigung ( CharSet c) {

		char [] test = new char[getSize() + c.getSize()];

		for (int i = 0; i < getSize(); i++) {
			test[i] = getSet()[i];
		}

		for (int g = 0; g < c.getSize(); g++) {
			test[getSize() +  g] = c.getSet()[g];
		}

		for (int k = 0; k < test.length; k++) {
			for (int h = 0; h < test.length; h++) {
				if (test[k] == test[h] && h != k) {
					test[k] = '*';
					break;
				}
			}
		}
		int l = test.length;
		for (int t = 0; t < test.length; t++) {

			if (test[t] == '*' ) l--;
		}

		char [] test1 = new char[l];
		int j = 0;
		for (int b = 0; b < test.length; b++) {

			if (test[b] != '*' ) {
				test1[j] = test[b];
				j++;
			}
		}

		CharSet newSet = new CharSet(test1);
		return newSet;

	}
}