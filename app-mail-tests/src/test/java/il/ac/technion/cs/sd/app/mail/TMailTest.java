package il.ac.technion.cs.sd.app.mail;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TMailTest {
	private ServerMailApplication		server	= new ServerMailApplication("server");
	private List<ClientMailApplication>	clients	= new ArrayList<>();
	private Thread						serverThread;
	
	private ClientMailApplication buildClient(String username) {
		ClientMailApplication $ = new ClientMailApplication(server.getAddress(), username);
		clients.add($);
		return $;
	}
	
	@Before 
	public void setup() throws InterruptedException {
		serverThread = new Thread(() -> server.start());
		serverThread.start();
		Thread.yield();
		Thread.sleep(100);
	}
	
	@SuppressWarnings("deprecation") // "I know what I'm doing"
	@After 
	public void teardown() {
		server.stop();
		server.clean();
		clients.forEach(x -> x.stop());
		serverThread.stop();
	}
	
//	@Test 
//	public void partialTest() throws Exception {
//		ClientMailApplication gal = buildClient("Gal");
//		gal.sendMail("Itay", "Hi");
//		ClientMailApplication itay = buildClient("Itay");
//		System.out.println(itay.getNewMail());
////		assertEquals(itay.getNewMail().get(0), new Mail("Gal", "Itay", "Hi"));
//		assertEquals(itay.getNewMail(), Arrays.asList(new Mail("Gal", "Itay", "Hi")));
//		itay.sendMail("Gal", "sup");
//		assertEquals(gal.getAllMail(3), Arrays.asList(
//				new Mail("Itay", "Gal", "sup"),
//				new Mail("Gal", "Itay", "Hi")));
//	}
	
	@Test
	public void testGetContacts() throws Exception {
		ClientMailApplication gal = buildClient("Gal");
		gal.sendMail("Itay", "Hi");
		ClientMailApplication itay = buildClient("Itay");
		assertEquals(itay.getNewMail(), Arrays.asList(new Mail("Gal", "Itay", "Hi")));
		System.out.println(gal.getContacts(7));
		assertEquals(gal.getContacts(7), Arrays.asList(new String("Itay")));
	}
	
	@Test
	public void getContactsShouldReturnContactsAlphabetically() {
		ClientMailApplication gal = buildClient("Gal");
		gal.sendMail("Itay", "AAA");
		gal.sendMail("Abba", "BBB");
		gal.sendMail("Imma", "CCC");
		gal.sendMail("Yoni", "DDD");
		gal.sendMail("Bloch", "EEE");
		gal.sendMail("Safta", "FFF");
		ClientMailApplication Abba = buildClient("Abba");
		assertEquals(Abba.getNewMail(), Arrays.asList(new Mail("Gal", "Abba", "BBB")));
		assertEquals(gal.getContacts(1), Arrays.asList(new String("Abba")));
	}
	
	@Test
	public void contactsShouldBeOrderedAlphabetically() {
		ClientMailApplication gal = buildClient("Gal");
		gal.sendMail("Itay", "AAA");
		gal.sendMail("Abba", "BBB");
		gal.sendMail("Imma", "CCC");
		gal.sendMail("Yoni", "DDD");
		gal.sendMail("Bloch", "EEE");
		gal.sendMail("Safta", "FFF");

		assertEquals(gal.getContacts(6), Arrays.asList("Abba", "Bloch", "Imma", "Itay", "Safta", "Yoni"));
	}

	@Test
	public void receiversShouldHaveAMessage() {
		ClientMailApplication gal = buildClient("Gal");
		gal.sendMail("Itay", "AAA");
		gal.sendMail("Abba", "BBB");
		gal.sendMail("Imma", "CCC");
		gal.sendMail("Yoni", "DDD");
		gal.sendMail("Bloch", "EEE");
		gal.sendMail("Safta", "FFF");
		ClientMailApplication itay = buildClient("Itay");
		ClientMailApplication Abba = buildClient("Abba");
		ClientMailApplication Imma = buildClient("Imma");
		ClientMailApplication Yoni = buildClient("Yoni");
		ClientMailApplication Bloch = buildClient("Bloch");
		ClientMailApplication Safta = buildClient("Safta");
		
		assertEquals(itay.getNewMail(), Arrays.asList(new Mail("Gal", "Itay", "AAA")));
		assertEquals(Abba.getNewMail(), Arrays.asList(new Mail("Gal", "Abba", "BBB")));
		assertEquals(Imma.getNewMail(), Arrays.asList(new Mail("Gal", "Imma", "CCC")));
		assertEquals(Yoni.getNewMail(), Arrays.asList(new Mail("Gal", "Yoni", "DDD")));
		assertEquals(Bloch.getNewMail(), Arrays.asList(new Mail("Gal", "Bloch", "EEE")));
		assertEquals(Safta.getNewMail(), Arrays.asList(new Mail("Gal", "Safta", "FFF")));
		assertEquals(gal.getContacts(6), Arrays.asList("Abba", "Bloch", "Imma", "Itay", "Safta", "Yoni"));
	}
	
	@Test
	public void userShouldBeAbleToSentMailToItself() {
		ClientMailApplication gal = buildClient("Gal");
		gal.sendMail("Gal", "Hi me!");
		assertEquals(gal.getNewMail(), Arrays.asList(new Mail("Gal", "Gal", "Hi me!")));
	}

	
//	@Test
//	public void basicTest() throws Exception {
//		ClientMailApplication gal = buildClient("Gal");
//		gal.sendMail("Itay", "Hi");
//		assertEquals(gal.getContacts(1), Arrays.asList("Itay"));
//		ClientMailApplication itay = buildClient("Itay");
//		assertEquals(itay.getNewMail(), Arrays.asList(new Mail("Gal", "Itay", "Hi")));
//		itay.sendMail("Gal", "sup");
//		assertEquals(gal.getAllMail(3), Arrays.asList(
//				new Mail("Itay", "Gal", "sup"),
//				new Mail("Gal", "Itay", "Hi")));
//	}
}
