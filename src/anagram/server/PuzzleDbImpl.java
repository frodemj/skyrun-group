package anagram.server;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import anagram.client.PuzzleDb;
import anagram.game.Puzzle;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class PuzzleDbImpl extends RemoteServiceServlet implements PuzzleDb {
	private static final long serialVersionUID = 1L;

	@Override
	public Puzzle[] getPuzzles(int from, int len) {
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			throw new SecurityException("Not authenticated");
		}
		if (!currentUser.hasRole("admin")){
			len = 2;
		}
		int to = Math.min(from+len, tab.length);
		return Arrays.copyOfRange(tab, from, to);
	}

	@Override
	public int size() {
		return tab.length;
	}

	private Puzzle[] tab = {
			new Puzzle("SADEBRY","SYREBAD"),
			new Puzzle("TISSFREKKK","STREKKFISK"),
			new Puzzle("LEKKASJ","SJAKKEL"),
			new Puzzle("MUGNESOL","LUNGEMOS"),
			new Puzzle("SØTVANNET","TØNNESTAV"),
			new Puzzle("BØNNÅNDET","TØNNEBÅND"),
			new Puzzle("BREDBÅNDDE","BÅNDBREDDE"),
			new Puzzle("MORMORFE","OMFORMER"),
			
			
			
			
			
	};

	@Override
	public int addPuzzles(String[] puzzles) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		for (String s : puzzles) {
			String[] tab = s.split(",");
			Key puzzleKey = KeyFactory.createKey("puzzlekey", tab[0]);
			Entity puzzle = new Entity("puzzle", puzzleKey);
			puzzle.setProperty("tab", tab);

			datastore.put(puzzle);
		}
		
		// TODO Auto-generated method stub
		return 0;
	}
}
