package comedyanalysis;

public class Word {

	private String word;
	private int count;
	
	public Word(String s) {
		word = s;
		count = 0;
	}
	
	public String getWord() {
		return word;
	}
	
	public int getCount() {
		return count;
	}
	
	public void inc() {
		++count;
	}
	
	public boolean equals(Word w) {
		if (w.getWord() == word)
			return true;
		return false;
	}
	
	public String toString() {
		return word + ": " + count;
	}
	
}
