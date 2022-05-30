package def;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread implements Runnable {
	
	private Server server;
	private Socket clSock;
	
	PrintWriter out;
	BufferedReader in;
	
	private User user;
	
	public ServerThread(Server server, Socket clSock) {
		this.server = server;
		this.clSock = clSock;
	}

	@Override
	public void run() {
		try {
			out = new PrintWriter(new OutputStreamWriter(clSock.getOutputStream()), true);
			in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
			
			//log-in i registracia
			boolean logged = false;
			while (!logged) {
				String type = in.readLine();
				if (type.equals("Disconnect")) {
					disconnect();
					return;
				}
				if (type.equals("Log in")) {
					String username = in.readLine();
					String password = in.readLine();
					user = User.logIn(username, password);
					if (user != null) {
						//ako je vec logovan, obavestiti o tome
						if (User.getLoggedUsers().contains(user)) {
							out.println("Already logged");
							System.out.println("already logged");
							user = null;
							//clSock.close();
							//server.removeServerThread(this);
						}
						else {
							User.logUser(user);
							logged = true;
							out.println("Successfull");
							//System.out.println("Successfull");
						}
					}
					else {
						out.println("Failed");
						System.out.println("Failed");
					}
				}
				else if (type.equals("Register")) {
					String username = in.readLine();
					String password = in.readLine();
					if (User.isUsernameAvailable(username)) {
						user = new User(username, password);
						User.logIn(username, password);
						logged = true;
						User.logUser(user);
						out.println("Successfull");
					}
					else out.println("Failed");
				}
			}
			server.updateAllOnlineLists();
			//cekanje na primljenu poruku
			while (true) {
				//poruka od klijenta
				String toWho = null;
				try {
					toWho = in.readLine();
				}
				catch (SocketException e) {
					logOut();
					return;
				}
					
				if (toWho.equals("Logout")) {
					//System.out.println("logout na serveru");
					logOut();
					return;					
				}
				
				String msg = in.readLine();
				if (toWho.equals("Public")) {
					//System.out.println("send all");
					server.sendAll(msg);
				}
				else 
					server.sendPrivate(user.getUsername(), toWho, msg);
			}
			//clSock.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public User getUser() {
		return user;
	}

	public void sendMessage(String fromWho, String msg) {
		//poruka ka klijentu
		if (user == null) return;
		//System.out.println("user: " + user);
		//System.out.println("server salje");
		out.println(fromWho);
		out.println(msg);
	}
	
	public void updateOnlineList() {
		if (user == null) return;
		out.println("Update");
		out.println(User.getLoggedUsers().size()-1);
		for (User usr : User.getLoggedUsers()) {
			if (user != usr) out.println(usr.getUsername());
		}
	}
	
	public void disconnect() {
		try {
			clSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.removeServerThread(this);
	}
	
	public void logOut() {
		//System.out.println("logging out.............");
		disconnect();
		//System.out.println("br logovanih: "+ User.getLoggedUsers().size());
		User.getLoggedUsers().remove(user);
		//System.out.println("br logovanih: "+ User.getLoggedUsers().size());
		server.updateAllOnlineLists();
	}

}
