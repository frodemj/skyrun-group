package anagram.client;

import anagram.game.Puzzle;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PuzzleDbAsync {

	void size(AsyncCallback<Integer> callback);

	void getPuzzles(int from, int len, AsyncCallback<Puzzle[]> callback);

	void addPuzzles(String[] puzzles, AsyncCallback<Integer> callback);

}
