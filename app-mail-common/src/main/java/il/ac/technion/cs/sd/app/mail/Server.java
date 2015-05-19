package il.ac.technion.cs.sd.app.mail;

import il.ac.technion.cs.sd.msg.Messenger;
import il.ac.technion.cs.sd.msg.MessengerException;
import il.ac.technion.cs.sd.msg.MessengerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.json.JSONObject;

class Server {
	private File databaseFile = new File("serialization.dat");
	private ServerDatabase database = new ServerDatabase();
	
	private String name;
	private String address;
	private static final String FROM_KEY = "from";
	private Messenger _messenger;
	
//	private HashMap<String, ArrayList<String>> userToContacts = new HashMap<>();
//	private HashMap<String, LinkedList<Mail>> userToMailsSent = new HashMap<>();
//	private Map<String, LinkedList<Mail>> userToAllMails = new HashMap<>();
//	private Map<String, LinkedList<Mail>> userToNewMails = new HashMap<>();
	
	
	Server(String name, String address) {
		this.name = name;
		this.address = address;
		try {
			_messenger = new MessengerFactory().start(address);
		}
		catch (MessengerException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	String getAddress() {
		return address;
	}
	
	
	
//	private List<Mail> getUserMails(String username) {
//		return userToAllMails.get(username);
//	}
	
	void start() {
		// TODO - Load data from database
		try {
			while(true) {
				// start listening to messages
				System.out.println("Listening...");
				String request = new String(_messenger.listen());
				// received message - handle request
				handleRequest(request); // TODO - continue implementing
				
				System.out.println("Server Recieved: " + request);				
			}
		}
		catch (MessengerException e) {
			e.printStackTrace();
		}
	}
	
	private void handleRequest(String request) {
		JSONObject json = new JSONObject(request);
		Integer type = (Integer) json.get("type");
		
		if (type == RequestType.SEND_MAIL.ordinal()) {
			String sender = json.getString(FROM_KEY);
			String whom = json.getString("to");
			String message = json.getString("message");
			Mail mail = new Mail(sender, whom, message);
			database.addMailSentFromUser(sender, mail); // outbox of the sender
			database.addNewMessageToUser(whom, mail);
			database.addMailToUser(sender, mail);
			database.addMailToUser(whom, mail);
			database.addContactIfNotExist(sender, whom);
			database.addContactIfNotExist(whom, sender);
			return;
		}
		
		if (type == RequestType.REQUEST_GET_NEW_MAIL.ordinal()) {
			String username = json.getString(FROM_KEY);
			// TODO - change it later to New Mails
			List<Mail> list = database.getNewMailsOfUser(username);
//			if (list == null) {
//			}
			JSONObject answerJson = new JSONObject();
			answerJson.put(ResponseType.RESPONSE_KEY, ResponseType.GET_NEW_MAIL);
			List<String> mailStrings = mailListToStringList(list);
			answerJson.put(CommunicationKey.NEW_MAILS_KEY, mailStrings);
			try {
				_messenger.send(username, answerJson.toString().getBytes());
			}
			catch (MessengerException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			return;
		}
		
		if (type == RequestType.REQUEST_GET_ALL_MAIL.ordinal()) {
			String username = json.getString(FROM_KEY);
			List<Mail> list = database.getAllMailsOfUser(username);
			JSONObject answerJson = new JSONObject();

			answerJson.put(ResponseType.RESPONSE_KEY, ResponseType.GET_ALL_MAIL);
			List<String> mailStrings = mailListToStringList(list);
			answerJson.put(CommunicationKey.ALL_MAILS_KEY, mailStrings);
			try {
				_messenger.send(username, answerJson.toString().getBytes());
			}
			catch (MessengerException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			return;
		}
		
		if (type == RequestType.REQUEST_CONTACTS.ordinal()) {
			String sender = json.getString(FROM_KEY);
			List<String> contacts = database.getContactsOfUser(sender);
			if (contacts == null) {
				// no contacts -> return an empty list
				contacts = new ArrayList<String>();
			}
			
			JSONObject answerJson = new JSONObject();
			answerJson.put(ResponseType.RESPONSE_KEY, ResponseType.GET_CONTACTS);
			answerJson.put("contacts", contacts);
			try {
				_messenger.send(sender, answerJson.toString().getBytes());
			}
			catch (MessengerException e) {
				e.printStackTrace();
			}
			return;
		}
		
		throw new UnsupportedOperationException("missing case");
	}

	private List<String> mailListToStringList(List<Mail> mails) {
		List<String> strings = new LinkedList<String>();
		for (Mail mail : mails) {
			String mailStr = mail.toString();
			strings.add(mailStr);
		}
		return strings;
	}
	
	
	
	private void userToAllMails(String whom, String message) {
		// TODO Auto-generated method stub
		
	}

	void getIncomingMail() {
		
	}

	void stop() {
		try {
			_messenger.kill();
		}
		catch (MessengerException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	void clean() {
		// Stop the server
		stop();
		databaseFile.delete();
	}
	
	
	private void saveState() {
		try {
			if (databaseFile.exists() == false) {
				databaseFile.createNewFile(); // throws IOException
			}
			FileOutputStream fileout = new FileOutputStream(databaseFile);
			ObjectOutputStream objectStream = new ObjectOutputStream(fileout);
	
			objectStream.writeObject(database);
			objectStream.flush();
			objectStream.close();
			fileout.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private void readState() {
		try {
			FileInputStream fileinput = new FileInputStream(databaseFile);
			ObjectInputStream objectStream = new ObjectInputStream(fileinput);
			try {
				database = (ServerDatabase) objectStream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			fileinput.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
