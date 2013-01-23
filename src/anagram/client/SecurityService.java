package anagram.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("security")
public interface SecurityService extends RemoteService {
	String login(String name);
	String login(String name, String pass);
	void logout();	
}
