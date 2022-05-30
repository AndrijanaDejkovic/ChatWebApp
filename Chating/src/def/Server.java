package def;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class Server {
	int port = 8081;
	int user_num = 0;
	ArrayList<ServerThread> serverThreadList = new ArrayList<>();
	
	public Server() throws IOException {
		ServerSocket sSock = new ServerSocket(port);
		
		while (true) {
			//cnt++;
			Socket clSock = sSock.accept();
			//System.out.println(clSock.getInetAddress().getHostName() + " is connected");
			
			ServerThread st = new ServerThread(this, clSock);
			serverThreadList.add(st);
			Thread th = new Thread(st);
			th.start();
		}
	}
	public void updateAllOnlineLists() {
		for (ServerThread serverThread : serverThreadList) {
			serverThread.updateOnlineList();
		}
	}
	
	public void sendAll(String msg) {
		//System.out.println("number of connections: " + serverThreadList.size());
		for (ServerThread serverThread : serverThreadList) {
			serverThread.sendMessage("Public", msg);
		}
	}
	
	public void sendPrivate(String fromWho, String toWho, String msg) {
		for (ServerThread serverThread : serverThreadList) {
			if (serverThread.getUser().getUsername().equals(toWho)) {
				serverThread.sendMessage(fromWho, msg);
				return;
			}
		}
	}
	
	
	
	public void removeServerThread(ServerThread serverThread) {
		serverThreadList.remove(serverThread);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Server();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
