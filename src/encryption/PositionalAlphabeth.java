package encryption;

public class PositionalAlphabeth {
	final int SIZE;
	char [] lettersArr={'à','á','â','ã','ä','å','¸','æ',
			'ç','è','é','ê','ë','ì','í','î','ï','ð','ñ',
			'ò','ó','ô','õ','ö','÷','ø','ù','ú','û',
			'ü','ý','þ','ÿ'};							// '0','1','2','3','4','5','6','7','8','9'};

	PositionalAlphabeth() { // cyrillic only dummy
		SIZE = lettersArr.length;
	}	
	
	public int getInt(char c) {
		char inC = Character.toLowerCase(c);
		int ind;
		for (ind=0; ind<SIZE && (lettersArr[ind] != inC); ind++);
		if (ind >= SIZE) {
			return 0;
		} else {
			return ind+1;
		}
	}

	public char getChar(int i) {
		int ind = (i < 1) ? i+SIZE : i;
		return lettersArr[(ind-1)%SIZE];
	} 
}
