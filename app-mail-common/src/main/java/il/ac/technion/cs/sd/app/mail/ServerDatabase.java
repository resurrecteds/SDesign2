package il.ac.technion.cs.sd.app.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class ServerDatabase implements Serializable {

	private static final long serialVersionUID = 43436451L;
	
	private HashMap<String, ArrayList<String>> userToContacts = new HashMap<>();
	private HashMap<String, LinkedList<Mail>> userToMailsSent = new HashMap<>();
	private Map<String, LinkedList<Mail>> userToAllMails = new HashMap<>();
	private Map<String, LinkedList<Mail>> userToNewMails = new HashMap<>();
	private Map<Pair<String, String>, LinkedList<Mail>> pairOfUsersToConversation = new HashMap<>();
	
	private class Pair<X, Y> {
		private final X x;
		private final Y y;
		Pair(X x, Y y) {
			this.x = x;
			this.y = y;
		}
		
	}
	
	void addMailBetweenUsers(String user1, String user2, Mail message) {
		Pair<String, String> pair = getPairOfUsers(user1, user2);
		List<Mail> conversation = pairOfUsersToConversation.get(pair);
		if (conversation == null) {
			conversation = new LinkedList<Mail>();
		}
		
//		pairOfUsersToConversation.put(pair, )
		
	}
	
	private Pair<String, String> getPairOfUsers(String user1, String user2) {
		Pair<String, String> pair = null;
		if (user1.compareToIgnoreCase(user2) < 0) {
			pair = new Pair<String, String>(user1, user2);
		}
		else {
			pair = new Pair<String, String>(user2, user1);
		}
		assert(pair != null);
		return pair;
	}
	
	List<Mail> getConversationBetweenUsers(String user1, String user2) {
		// the users in the tuple are sorted alphabetically
		Pair<String, String> pair = getPairOfUsers(user1, user2);
		
		LinkedList<Mail> conversation = pairOfUsersToConversation.get(pair);
		if (conversation == null) {
			// return empty list to indicate no conversation between the two users
			return new LinkedList<Mail>();
		}
		return conversation;
	}
	
	void addContactIfNotExist(String user, String newContact) {
		ArrayList<String> contacts = userToContacts.get(user);
		if (contacts == null) {
			contacts = new ArrayList<String>();
		}
		
		int index = Collections.binarySearch(contacts, newContact);
		if (index >= 0) {
			// the contact exists -> nothing to do
			return;
		}
		contacts.add(Math.abs(index)-1, newContact);
		userToContacts.put(user, contacts);
	}
	
	List<String> getContactsOfUser(String user) {
		return userToContacts.get(user);
	}
	
	List<Mail> getAllMailsOfUser(String user) {
		return userToAllMails.get(user);
	}
	
	List<Mail> getNewMailsOfUser(String user) {
		return userToNewMails.get(user);
	}
	
	List<Mail> getMailsSentByUser(String user) {
		return userToMailsSent.get(user);
	}
	
	void addMailSentFromUser(String sender, Mail mail) {
		LinkedList<Mail> list = (LinkedList<Mail>) getMailsSentByUser(sender);
		if (list == null) {
			list = new LinkedList<Mail>();
		}
		list.addFirst(mail);
		userToMailsSent.put(sender, list);
	}
	
	void addNewMessageToUser(String receiver, Mail mail) {
		LinkedList<Mail> list = (LinkedList<Mail>) getNewMailsOfUser(receiver);
		if (list == null) {
			list = new LinkedList<Mail>();
		}
		list.addFirst(mail);
		userToNewMails.put(receiver, list);
	}
	
	void addMailToUser(String user, Mail mail) {
		LinkedList<Mail> list = (LinkedList<Mail>) getAllMailsOfUser(user);
		if (list == null) {
			list = new LinkedList<Mail>();
		}
		list.addFirst(mail);
		userToAllMails.put(user, list);
	}
}
