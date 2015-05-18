package il.ac.technion.cs.sd.app.mail;

/**
 * The server side of the TMail application. <br>
 * This class is mainly used in our tests to start, stop, and clean the server
 */
public class ServerMailApplication {
	
	/**
	 * Starts a new mail server. Servers with the same name retain all their information until
	 * {@link ServerMailApplication#clean()} is called.
	 * 
	 * @param name The name of the server by which it is known.
	 */
	public ServerMailApplication(String name) {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * @return the server's address; this address will be used by clients connecting to the server
	 */
	public String getAddress() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Starts the server; any previously sent mails, data and indices under this server name are loaded. It is possible
	 * to start a new server instance in same, or another process. You may assume that two server instances with the
	 * same name won't be in parallel. Similarly, {@link ServerMailApplication#stop()} will be called before subsequent
	 * calls to {@link ServerMailApplication#start()}.
	 */
	public void start() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Stops the server. A stopped server can't accept mail, but doesn't delete any data. A stopped server does not use
	 * any system resources (e.g., messengers).
	 */
	public void stop() {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Deletes <b>all</b> previously saved data. This method will be used between tests to assure that each test will
	 * run on a new, clean server. you may assume the server is stopped before this method is called.
	 */
	public void clean() {
		throw new UnsupportedOperationException("Not implemented");
	}
}
