package def;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import javafx.scene.image.Image;
import javafx.scene.layout.Border;

public class ClientApp extends JFrame {
	//JTextArea area;
	//JTextArea onlineList;
	JPanel online;
	JTabbedPane tabs;
	String target = "Public";
	Client client;
	
	public ClientApp(Client client, String username) {
		this.client = client;
		setSize(800, 600);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(username);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		JPanel pnl = new JPanel();
		pnl.setBackground(Color.WHITE);
		add(pnl);
		pnl.setLayout(new BorderLayout());
		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		
		JTextArea area = new JTextArea();
		area.setEditable(false);
		JScrollPane scroll = new JScrollPane(area);
		//add(scroll);
		tabs.add("Public", scroll);
		
		
		
		JTextField tf = new JTextField();
	
	    JPanel south_panel = new JPanel();
	    JButton button = new JButton("Like");

		add(south_panel, BorderLayout.SOUTH);

		south_panel.setLayout(new BorderLayout());
		south_panel.add(tf);	//isto radi i south_panel.add(tf, BorderLayout.CENTER);
		south_panel.add(button, BorderLayout.EAST);
		
		
		
		//tf.setBounds(100, 100, 100, 20);
		tf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String message = tf.getText();
				
				JScrollPane scroll = (JScrollPane)tabs.getSelectedComponent();
				JTextArea area = (JTextArea)scroll.getViewport().getView();
				if (!tabs.getTitleAt(tabs.getSelectedIndex()).equals("Public"))
					area.append(message + "\n");
				client.sendMessage(tabs.getTitleAt(tabs.getSelectedIndex()), message);
				tf.setText("");
			}
		});
		
		add(south_panel, BorderLayout.SOUTH);
		
		online = new JPanel();
		online.setPreferredSize(new Dimension(200, 200));
		add(online, BorderLayout.EAST);
		online.setBorder(new EmptyBorder(5, 5, 5, 5));
		online.setLayout(new GridLayout(20, 1));
		
		//onlineList = new JTextArea();
		//onlineList.setPreferredSize(new Dimension(200, 200));
		//add(onlineList, BorderLayout.EAST);
		//pnl.setBackground(Color.RED);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// We should log out user
				client.logOut();
			}
		});
	}
	
	
	
	public void printMessage(String fromWho, String message) {
		for (int i=0; i<tabs.getTabCount(); i++) {
			if (tabs.getTitleAt(i).equals(fromWho)) {
				//tabs.setSelectedIndex(i);
				JScrollPane scroll = (JScrollPane)tabs.getComponentAt(i);
				JTextArea area = (JTextArea)scroll.getViewport().getView();
				
				area.append(message + "\n");
				//System.out.println("Message appended");
				tabs.setSelectedIndex(i);
				return;
			}
		}
		//if not open tab for this friend
		JTextArea area = new JTextArea();
		area.setEditable(false);
		JScrollPane scroll = new JScrollPane(area);
		tabs.add(fromWho, scroll);
		tabs.setSelectedIndex(tabs.getTabCount()-1);
		area.append(message + "\n");
	}
	
	public void updateOnlineList(ArrayList<String> onlinePeople) {
		//online.removeAll();
		//System.out.println("br online: " + onlinePeople.size());
		for (int i=0; i<online.getComponentCount(); i++) {
			JLabel label = (JLabel)online.getComponent(i);
			boolean there = false;
			for (String name : onlinePeople) {
				if (label.getText().equals(name)) {
					there = true;
					break;
				}
			}
			if (!there) {
				for (int j=0; j<tabs.getTabCount(); j++) {
					if (tabs.getTitleAt(j).equals(label.getText())) {
						tabs.removeTabAt(j);
						j--;
						break;
					}
				}
				online.remove(i);
				i--;
			}
		}
		
		
		for (String name : onlinePeople) {
			//if already there, do nothing
			boolean there = false;
			//System.out.println(name + " :");
			for (int i=0; i<online.getComponentCount(); i++) {
				//System.out.println(online.getComponent(i));
				JLabel label = (JLabel)online.getComponent(i);
				if (label.getText().equals(name)) {
					there = true;
					break;
				}
			}
			/*
			for (int i=0; i<tabs.getTabCount(); i++) {
				if (tabs.getTitleAt(i).equals(name)) {
					there = true;
					break;
				}
			}
			*/
			if (there) continue;
			
			JLabel label = new JLabel(name);
			online.add(label);
			label.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					for (int i=0; i<tabs.getTabCount(); i++) {
						//System.out.println(tabs.getTitleAt(i));
						if (tabs.getTitleAt(i).equals(name)) {
							tabs.setSelectedIndex(i);
							target = name;
							return;
						}
					}
					JTextArea friendArea = new JTextArea();
					friendArea.setEditable(false);
					JScrollPane scroll = new JScrollPane(friendArea);
					tabs.add(name, scroll);
					tabs.setSelectedIndex(tabs.getTabCount()-1);
				}
			});
		}
		online.updateUI();
	}
	
}
