package encryption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.IntBuffer;

public class BookCode {
	final static int MAX_SIZE = 2048; // 
	
	PositionalAlphabeth abc = new PositionalAlphabeth(); // dummy cyrillic
	
	private IntBuffer keyBuf;
	
	public BookCode(File file) {		
		try {
			FileReader fr = new FileReader(file);
			
			 keyBuf = IntBuffer.allocate(MAX_SIZE);
			 
			 CharBuffer inputKeys = CharBuffer.allocate(MAX_SIZE*2);
			 
			 int keySize = fr.read(inputKeys);
			 int codeCap = MAX_SIZE;
					 
			 inputKeys.flip();
			 for (int count=0; 
					 count < keySize && codeCap > 0; 
					 count++) {
				 
				 int num = abc.getInt(inputKeys.get());
				 if (num > 0) {
					 keyBuf.put(num);
					 codeCap--;
				 }
			 }
			 fr.close();
			 keyBuf.flip();
						
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}

	public String encrypt(String inputText, boolean encryptFlag) {
		int coeff = encryptFlag ? 1 : -1; 
		StringBuilder sb = new StringBuilder();
		
		keyBuf.rewind();
		int size = inputText.length();
		for (int i=0; i < size; i++) { // for now remain with punctuation and spaces
			char ch = inputText.charAt(i);
			int curInd = abc.getInt(ch);
			if (curInd > 0) {
				int shift = keyBuf.get();
				if (!keyBuf.hasRemaining()) {
					keyBuf.rewind();
				}
				sb.append(abc.getChar(curInd+coeff*shift));
			} else {
				sb.append(ch);
			}
		}
		
		return sb.toString();
	}
}
