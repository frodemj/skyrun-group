package anagram.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface SecurityServiceAsync {
	void login(String name, AsyncCallback<String> callback);
	void login(String name, String pass, AsyncCallback<String> callback);
	void logout(AsyncCallback<Void> callback);
}
