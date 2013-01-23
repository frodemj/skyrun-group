package anagram.client;

import anagram.game.Puzzle;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("puzzledb")
public interface PuzzleDb extends RemoteService {
	Puzzle[] getPuzzles(int from, int len);
	int size();
	int addPuzzles(String[] puzzles);
}
