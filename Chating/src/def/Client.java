package def;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import javafx.scene.image.Image;

public class Client {
	PrintWriter out;
	BufferedReader in;
	ClientApp clientApp;
	boolean canAcceptMessages = false;
	Socket sock;
	
	public Client() throws Exception {
		//String host = JOptionPane.showInputDialog("Insert server IP adress");
		String host = "127.0.0.1";
		//System.out.println("caoooooo");
		
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				//updating gui 
				// TODO Auto-generated method stub
				LogInForm logInForm = new LogInForm(Client.this);
				//clientApp = new ClientApp(Client.this);
			}
		});
		
		//System.out.println("hellooooooo");
		
		//String host = "192.168.1.2";
		int port = 8081;
		
		sock = new Socket(host, port);
		
		out= new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		while (!canAcceptMessages) {
		Thread.currentThread().sleep(5);
			if (sock.isClosed()) return;
		}
		while (true) {
			//poruka sa servera
			if (sock.isClosed()) return;
			String cmd;
			try {
				cmd = in.readLine();
			} catch (SocketException e) {
				return;
			}
			if (!cmd.equals("Update")) {
				String fromWho = cmd;
				String msg = in.readLine();
				//System.out.println("from: " + fromWho);
				//System.out.println("msg: " + msg);
				clientApp.printMessage(fromWho, msg);
			}
			else {
				int onlineNum = Integer.parseInt(in.readLine());
				ArrayList<String> onlinePeople = new ArrayList<>();
				for (int i=0; i<onlineNum; i++) {
					onlinePeople.add(in.readLine());
				}
				clientApp.updateOnlineList(onlinePeople);
			}
		}
	}
	
	public void setClientApp(ClientApp clientApp) {
		this.clientApp = clientApp;
	}

	public void setCanAcceptMessages(boolean canAcceptMessages) {
		this.canAcceptMessages = canAcceptMessages;
	}

	public void sendMessage(String toWho, String msg) {
		//poruka ka serveru
		out.println(toWho);
		out.println(msg);
	}
	
	public boolean register(String username, String password) {
		out.println("Register");
		out.println(username);
		out.println(password);
		
		try {
			String msg = in.readLine();
			if (msg.equals("Failed")) return false;
			else return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String logIn(String username, String password) {
		out.println("Log in");
		out.println(username);
		out.println(password);
		
		try {
			String msg = in.readLine();
			//System.out.println("msg: " + msg);
			return msg;
			/*
			if (msg.equals("Already logged")) {
				System.out.println("vec logovan");
				return false;
			}
			if (msg.equals("Failed")) return false;
			else return true;
			*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Failed";
	}
	
	
	public void disconnect() {
		out.println("Disconnect");
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void logOut() {
		//System.out.println("logout na klijentu");
		out.println("Logout");
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new Client();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
