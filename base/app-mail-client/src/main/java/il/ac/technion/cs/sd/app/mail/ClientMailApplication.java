package il.ac.technion.cs.sd.app.mail;

import java.util.List;

/**
 * The client side of the TMail application.
 * Allows sending and getting mail to and from other clients using a server.
 * <br>
 * You should implement all the methods in this class 
 */
public class ClientMailApplication {
	
	/**
	 * Creates a new application, tied to a single user
	 * @param serverAddress The address of the server to connect to for sending and requesting mail
	 * @param username The user that will be sending and accepting the mail using this object
	 */
	public ClientMailApplication(String serverAddress, String username) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Sends a mail to another user
	 * @param whom The recipient of the mail
	 * @param what The message to send
	 */
	public void sendMail(String whom, String what) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all mail sent from or to another client
	 * @param whom The other user that sent or received mail from the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, of size n <i>at most</i>  
	 */
	public List<Mail> getCorrespondences(String whom, int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all mail sent <b>by</b> the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, of size n <i>at most</i>  
	 */
	public List<Mail> getSentMails(int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all sent <b>to</b> the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, of size n <i>at most</i>  
	 */
	public List<Mail> getIncomingMail(int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all sent <b>to</b> or <b>by</b> the current user
	 * @param howMany how many mails to retrieve; mails are ordered by time of arrival to server
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival, of size n <i>at most</i>  
	 */
	public List<Mail> getAllMail(int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Get all sent <b>to</b> the current user that wasn't retrieved by any method yet (including this method)
	 * @return A list, ordered of all mails matching the criteria, ordered by time of arrival  
	 */
	public List<Mail> getNewMail() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * @return A list, ordered alphabetically, of all other users that sent or received mail from the current user  
	 */
	public List<String> getContacts(int howMany) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * A stopped client does not use any system resources (e.g., messengers).
	 * This is mainly used to clean resource use in test cleanup code.
	 */
	public void stop() {
		
	}
}
