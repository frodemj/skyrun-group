package anagram.client;

import anagram.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Anju implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final SecurityServiceAsync securityService = GWT.create(SecurityService.class);
	private DialogBox loginDialog;

	private InlineHyperlink linkLogin;

	private TextBox txtbxName;

	private PasswordTextBox passwordTextBox;

	private Label lblError;

	private DeckPanel deckPanel;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		loginDialog = createLoginDialog();
//		final Button sendButton = new Button("Send");
//		final TextBox nameField = new TextBox();
//		nameField.setText("GWT User");
//		final Label errorLabel = new Label();
//
//		// We can add style names to widgets
//		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("nameFieldContainer");
		
//		rootPanel.add(nameField, 10, 10);
//		RootPanel.get("sendButtonContainer").add(sendButton, 10, 50);
//		sendButton.setSize("66px", "35px");
//		RootPanel.get("errorLabelContainer").add(errorLabel);
//
//		// Focus the cursor on the name field when the app loads
//		nameField.setFocus(true);
		
		linkLogin = new InlineHyperlink("Login ...", false, "newHistoryToken");
		rootPanel.add(linkLogin, 490, 10);
		linkLogin.setSize("107px", "18px");
		linkLogin.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loginDialog.showRelativeTo(linkLogin);
			}
		});

//		// Create the popup dialog box
//		final DialogBox dialogBox = new DialogBox();
//		dialogBox.setText("Remote Procedure Call");
//		dialogBox.setAnimationEnabled(true);
//		final Button closeButton = new Button("Close");
//		// We can set the id of a widget by accessing its Element
//		closeButton.getElement().setId("closeButton");
//		final Label textToServerLabel = new Label();
//		final HTML serverResponseLabel = new HTML();
//		VerticalPanel dialogVPanel = new VerticalPanel();
//		dialogVPanel.addStyleName("dialogVPanel");
//		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
//		dialogVPanel.add(textToServerLabel);
//		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
//		dialogVPanel.add(serverResponseLabel);
//		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//		dialogVPanel.add(closeButton);
//		dialogBox.setWidget(dialogVPanel);
//
//		// Add a handler to close the DialogBox
//		closeButton.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				dialogBox.hide();
//				sendButton.setEnabled(true);
//				sendButton.setFocus(true);
//			}
//		});

//		// Create a handler for the sendButton and nameField
//		class MyHandler implements ClickHandler, KeyUpHandler {
//			/**
//			 * Fired when the user clicks on the sendButton.
//			 */
//			public void onClick(ClickEvent event) {
//				sendNameToServer();
//			}
//
//			/**
//			 * Fired when the user types in the nameField.
//			 */
//			public void onKeyUp(KeyUpEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					sendNameToServer();
//				}
//			}
//
//			/**
//			 * Send the name from the nameField to the server and wait for a response.
//			 */
//			private void sendNameToServer() {
//				// First, we validate the input.
//				errorLabel.setText("");
//				String textToServer = nameField.getText();
//				if (!FieldVerifier.isValidName(textToServer)) {
//					errorLabel.setText("Please enter at least four characters");
//					return;
//				}
//
//				// Then, we send the input to the server.
//				sendButton.setEnabled(false);
//				textToServerLabel.setText(textToServer);
//				serverResponseLabel.setText("");
//			}
//		}

//		// Add a handler to send the name to the server
//		MyHandler handler = new MyHandler();
//		sendButton.addClickHandler(handler);
//		nameField.addKeyUpHandler(handler);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	private DialogBox createLoginDialog() {
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Login");
		dialogBox.setAnimationEnabled(true);
		
		FlexTable flexTable = new FlexTable();
		dialogBox.add(flexTable);
		flexTable.setSize("281px", "4cm");
		
		Label lblName = new Label("Name");
		flexTable.setWidget(0, 0, lblName);
		
		txtbxName = new TextBox();
		txtbxName.setText("name");
		flexTable.setWidget(0, 1, txtbxName);
		txtbxName.setWidth("100%");
		KeyPressHandler kph = new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				char ch = event.getCharCode();
//				System.out.println("" + event.getUnicodeCharCode() + " " + (int) ch);
				if (event.getCharCode() == 0)
					login();
			}
		};
		txtbxName.addKeyPressHandler(kph);
		
		Label lblPassword = new Label("Password");
		flexTable.setWidget(1, 0, lblPassword);
		
		passwordTextBox = new PasswordTextBox();
		flexTable.setWidget(1, 1, passwordTextBox);
		passwordTextBox.setWidth("100%");
		passwordTextBox.addKeyPressHandler(kph);
		
		lblError = new Label("..");
		Button btnCancel = new Button("Cancel");
		flexTable.setWidget(2, 0, btnCancel);
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				lblError.setText("");
				loginDialog.hide();
			}
		});
		
		deckPanel = new DeckPanel();
		flexTable.setWidget(2, 1, deckPanel);
		Button btnLogin = new Button("Login");
		btnLogin.setFocus(true);
		deckPanel.add(btnLogin);
		
		Button btnLogout = new Button("Logout");
		deckPanel.add(btnLogout);
		deckPanel.showWidget(0);
		btnLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				login();
			}
		});
		btnLogout.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				securityService.logout(new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						linkLogin.setText("Login ...");
						txtbxName.setEnabled(true);
						passwordTextBox.setEnabled(true);
						deckPanel.showWidget(0);
						loginDialog.hide();
					}
					@Override
					public void onFailure(Throwable caught) {
						lblError.setText("Could not logout: " + caught.getClass().getName() + ":" + caught.getMessage());
					}
				});
			}
		});
		flexTable.setWidget(3, 0, lblError);
		flexTable.getFlexCellFormatter().setColSpan(3, 0, 2);
		KeyDownHandler ch = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				lblError.setText("");
			}
		};
		txtbxName.addKeyDownHandler(ch);
		passwordTextBox.addKeyDownHandler(ch);
		return dialogBox;
	}

	private void login() {
		securityService.login(txtbxName.getText(), passwordTextBox.getText(), new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				lblError.setText("Bad Credentials: " + caught.getClass().getName() + ":" + caught.getMessage());
			}
			@Override
			public void onSuccess(String result) {
				lblError.setText("");
				linkLogin.setText(result);
				txtbxName.setEnabled(false);
				passwordTextBox.setEnabled(false);
				deckPanel.showWidget(1);
				loginDialog.hide();
			}
		});
		
	}
}
