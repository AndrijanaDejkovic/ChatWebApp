package def;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LogInForm extends JFrame {
	boolean logged = false;
	
	public LogInForm(Client client) {
		setSize(300, 370);
		setVisible(true);
		setTitle("Log in");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		
		
		//Log in
		
		
		
		JLabel usr = new JLabel("Username");
		usr.setBounds(20, 20, 80, 20);
		add(usr);
		
		JLabel pass = new JLabel("Password");
		pass.setBounds(20, 60, 80, 20);
		add(pass);
		
		JTextField usernameField = new JTextField();
		usernameField.setBounds(100, 20, 150, 20);
		add(usernameField);
	
		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(100, 60, 150, 20);
		add(passwordField);
		
		JButton logIn = new JButton("Log in");
		logIn.setBounds(90, 100, 100, 20);
		add(logIn);
		
		logIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				
				String successfull = client.logIn(username, password);
				//System.out.println("Succesfull: " + successfull);
				if (!successfull.equals("Successfull")) {
					usernameField.setText("");
					passwordField.setText("");
					if (successfull.equals("Failed"))
						JOptionPane.showMessageDialog(null, "Wrong username or password");
					else
						JOptionPane.showMessageDialog(null, "User with that username is already logged in");
				}
				else {
					logged = true;
					client.setClientApp(new ClientApp(client, username));
					client.setCanAcceptMessages(true);
					JFrame myFrame = LogInForm.this;
					myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
				}
			}
		});
		
		
		//Register
		
		JLabel usr1 = new JLabel("Username");
		usr1.setBounds(20, 160, 80, 20);
		add(usr1);
		
		JLabel pass1 = new JLabel("Password");
		pass1.setBounds(20, 200, 80, 20);
		add(pass1);
		
		JLabel pass2 = new JLabel("Repeat");
		pass2.setBounds(20, 240, 80, 20);
		add(pass2);
		
		JTextField usernameField1 = new JTextField();
		usernameField1.setBounds(100, 160, 150, 20);
		add(usernameField1);
	
		JPasswordField passwordField1 = new JPasswordField();
		passwordField1.setBounds(100, 200, 150, 20);
		add(passwordField1);
		
		JPasswordField passwordField2 = new JPasswordField();
		passwordField2.setBounds(100, 240, 150, 20);
		add(passwordField2);
		
		JButton register = new JButton("Register");
		register.setBounds(90, 280, 100, 20);
		add(register);
		
		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField1.getText();
				String password = new String(passwordField1.getPassword());
				String repeated = new String(passwordField2.getPassword());
				if (!password.equals(repeated)) {
					//usernameField1.setText("");
					passwordField1.setText("");
					passwordField2.setText("");
					JOptionPane.showMessageDialog(null, "Passwords do not match!");
				}
				else {
					boolean successfull = client.register(username, password);
					if (!successfull) {
						usernameField1.setText("");
						passwordField1.setText("");
						passwordField2.setText("");
						JOptionPane.showMessageDialog(null, "Username already exists!");
					}
					else {
						logged = true;
						client.setClientApp(new ClientApp(client, username));
						client.setCanAcceptMessages(true);
						JFrame myFrame = LogInForm.this;
						myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
					}
				}
			}
		});
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// We should stop connection to the server
				if (!logged)
					client.disconnect();
			}
		});
	}
}
