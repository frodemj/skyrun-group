package anagram.game;

import java.io.Serializable;
import java.util.Arrays;

public class Puzzle implements Serializable {
	private static final long serialVersionUID = 1L;
	String challengeWord;
	String solutionWord;
	public Puzzle() {}
	public String getChallengeWord() {
		return challengeWord;
	}
	public void setChallengeWord(String challengeWord) {
		this.challengeWord = challengeWord;
	}
	public String getSolutionWord() {
		return solutionWord;
	}
	public void setSolutionWord(String solutionWord) {
		this.solutionWord = solutionWord;
	}
	public Puzzle(String challengeWord, String solutionWord) {
		if (bad(challengeWord, solutionWord)) throw new IllegalArgumentException(challengeWord + ":" + solutionWord);
		this.challengeWord = challengeWord;
		this.solutionWord = solutionWord;
	}
	private boolean bad(String a, String b) {
		char[] ta = a.toCharArray();
		Arrays.sort(ta);
		char[] tb = b.toCharArray();
		Arrays.sort(tb);
		return !Arrays.equals(ta, tb);
	}
}
