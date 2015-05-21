package il.ac.technion.cs.sd.app.mail;

import il.ac.technion.cs.sd.msg.Messenger;
import il.ac.technion.cs.sd.msg.MessengerException;
import il.ac.technion.cs.sd.msg.MessengerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The client side of the TMail application.
 * Allows sending and getting mail to and from other clients using a server.
 * <br>
 * You should implement all the methods in this class 
 */
public class ClientMailApplication {
	
	private String username;
	private String serverAddress;
	private Messenger messenger;
	
	/**
	 * Creates a new application, tied to a single user
	 * @param serverAddress The address of the server to connect to for sending and requesting mail
	 * @param username The user that will be sending and accepting the mail using this object
	 */
	public ClientMailApplication(String serverAddress, String username) {
		this.username = username;
		this.serverAddress = serverAddress;
		try {
			messenger = new MessengerFactory().start(username);
		}
		catch (MessengerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a mail to another user
	 * @param whom The recipient of the mail
	 * @param what The message to send
	 */
	public void sendMail(String whom, String what) {
		try {
			JSONObject json = new SendMailRequest(username, whom, what).getJsonObject();
			String message = json.toString();
			System.out.println("Sending mail: " + message);
			messenger.send(serverAddress, message.getBytes());
		}
		catch(MessengerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get all mail sent from or to another client
	 * @param whom The other user that sent or received mail from the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, 
	 * of size n <i>at most</i>  
	 */
	public List<Mail> getCorrespondences(String whom, int howMany) {
		JSONObject json = new GetCorrespondencesRequest(username, whom, howMany).getJsonObject();
		JSONObject jsonAnswer = communicateWithServer(json);
		JSONArray mailsJson = (JSONArray) jsonAnswer.get("conversation");
		List<Mail> conversation = stringListToMailList(mailsJson, howMany);
		return conversation;
	}
	
	/**
	 * Get all mail sent <b>by</b> the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, 
	 * of size n <i>at most</i>  
	 */
	public List<Mail> getSentMails(int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all mail sent <b>to</b> the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, of size n <i>at most</i>  
	 */
	public List<Mail> getIncomingMail(int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all mail sent <b>to</b> or <b>by</b> the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival 
	 * to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of
	 *  arrival, of size n <i>at most</i>  
	 */
	public List<Mail> getAllMail(int howMany) {
		List<Mail> mails = null; // shouldn't be null at the end
		try {
			JSONObject json = new AllMailsRequest(username).getJsonObject();
			String message = json.toString();
			System.out.println("Sending message to server: " + message);
			messenger.send(serverAddress, message.getBytes());
			
			String answer = new String(messenger.listen());
			JSONObject answerJson = new JSONObject(answer);

			// TODO - maybe this call is not relevant
			String response = (String) answerJson.get(ResponseType.RESPONSE_KEY);
//			handleResponse((String) answerJson.get(ResponseType.RESPONSE_KEY));
			assert(response == ResponseType.GET_ALL_MAIL);
			mails = stringListToMailList(answerJson.getJSONArray(CommunicationKey.ALL_MAILS_KEY), howMany);
		}
		catch (MessengerException e) {
			e.printStackTrace();
		}
		
		assert(mails != null);
		return mails;
	}
	
//	private void handleResponse(String responseType) {
//		if (responseType == ResponseType.GET_NEW_MAIL) {
//			return;
//		}
//		
//		if (responseType == ResponseType.GET_ALL_MAIL) {
//			return;
//		}
//	}
	private JSONObject communicateWithServer(JSONObject jsonRequest) {
		JSONObject jsonAnswer = null;
		try {
			String message = jsonRequest.toString();
			System.out.println("Sending message to server: " + message);
			messenger.send(serverAddress, message.getBytes());
			String answer = new String(messenger.listen());
			jsonAnswer = new JSONObject(answer);
		}
		catch (MessengerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return jsonAnswer;
	}

	/**
	 * Get all mail sent <b>to</b> the current user that wasn't retrieved by any method yet
	 *  (including this method)
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival  
	 */
	public List<Mail> getNewMail() {
//		return UserMessagesMapper.getInstance().getMessagesForUser(username);
//		String message = username + ">" + serverAddress + ">" + "GET NEW MAIL";
		List<Mail> mails = new LinkedList<Mail>();
		try {
			JSONObject json = new NewMailRequest(username).getJsonObject();
			String message = json.toString();
			
			messenger.send(serverAddress, message.getBytes());
			
			String answer = new String(messenger.listen());
			JSONObject answerJson = new JSONObject(answer);
			assert(answerJson.get(ResponseType.RESPONSE_KEY) == ResponseType.GET_NEW_MAIL);
			// TODO - here the key was the one for ALL_MAILS_KEY - so now it shouldn't work
			mails = stringListToMailList(answerJson.getJSONArray(CommunicationKey.NEW_MAILS_KEY), -1);
			
		}
		catch (MessengerException e) {
			e.printStackTrace();
		}
		
		return mails;
	}
	
	private List<Mail> stringListToMailList(JSONArray jsonMails, int howMany) {
		List<Mail> mails = new LinkedList<Mail>();
		int length = jsonMails.length();
		if (howMany != -1) {
			length = length > howMany ? howMany : length;
		}
		
		for (int i = 0; i < length; ++i) {
			String str = jsonMails.get(i).toString();
			String[] tokens = str.split("%");
			assert(tokens.length == 3);
			String from = tokens[0];
			String to= tokens[1];
			String content = tokens[2];
			mails.add(new Mail(from, to, content));
		}
		return mails;
	}
	
	/**
	 * @return A list, ordered alphabetically, of all other users that sent or received mail from the current user  
	 */
	public List<String> getContacts(int howMany) {
		List<String> contacts = new ArrayList<String>();
		try {
			JSONObject json = new ContactsRequest(username).getJsonObject();
			String message = json.toString();
			System.out.println("getContacts: Sending message to server: " + message);
			messenger.send(serverAddress, message.getBytes());
			
			String answer = new String(messenger.listen());
			JSONObject jsonAnswer = new JSONObject(answer);
			JSONArray jsonArray = jsonAnswer.getJSONArray("contacts");
			
			assert(jsonAnswer.get(ResponseType.RESPONSE_KEY) == ResponseType.GET_CONTACTS);

			int count = jsonArray.length() > howMany ? howMany : jsonArray.length();
			for (int i = 0; i < count; i++) {
				contacts.add(jsonArray.get(i).toString());
			}
		}
		catch (MessengerException e) {
			e.printStackTrace();
		}
		assert(contacts != null);
		return contacts;
	}
	
	/**
	 * A stopped client does not use any system resources (e.g., messengers).
	 * This is mainly used to clean resource use in test cleanup code.
	 */
	public void stop() {
		try {
			messenger.kill();
		}
		catch (MessengerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
