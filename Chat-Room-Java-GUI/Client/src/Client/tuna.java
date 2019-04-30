package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;//this is the socket package
/*dont under any circumstance remove this import XD*/
import java.net.UnknownHostException;
///////////////////////////////////////
//our scanner import
import java.util.Scanner;
///////////////////////////////////////

@SuppressWarnings("serial")
public class tuna extends JFrame{

	private JTextField item1;
	private JTextField username;
	private JTextField item3;
	private JTextArea display;
	private JTextArea UserNames;
	private Container c;
	MultiClients ClientThread;
	private JLabel label;
	private JLabel label1;
	private final static String newline = "\n";

	public tuna(){
		super("WOW");
		
		setLayout(new FlowLayout());
		
		display = new JTextArea(30, 30);
		JScrollPane scrollPane = new JScrollPane(display); 
		display.setEditable(false);
		
		add(scrollPane);
		
		UserNames = new JTextArea(30, 10);
		JScrollPane scrollPane3 = new JScrollPane(UserNames); 
		UserNames.setEditable(false);
		
		add(scrollPane3);
		
		item1 = new JTextField(20);
		item1.setEditable(false);
		add(item1);

        username = new JTextField(20);
        username.setEditable(true);
		add(username);

		label=new JLabel("channel number");
		label1=new JLabel("Name");
		
		c = this.getContentPane(); 
		c.setLayout(new FlowLayout());
		c.add(label1);
		c.add(username);
		
		item3 = new JTextField(20);
		item3.setEditable(false);
			
		c = this.getContentPane(); 
		c.setLayout(new FlowLayout());
		c.add(label);
		c.add(item3);
		
		thehandler handler = new thehandler();
		item1.addActionListener(handler);
		username.addActionListener(handler); 
		item3.addActionListener(handler);
		try {
			Socket s = new Socket("localhost",3333);
			ClientThread = new MultiClients(s,this);
			ClientThread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	private class thehandler implements ActionListener{
		public void actionPerformed(ActionEvent event){

			String string = "";

			if(event.getSource()==item1)
			{
				string=String.format("%s", event.getActionCommand());
				String text= item1.getText();
				ClientThread.ClientOutServerIn(text);
				item1.setText("");
			}
			else if(event.getSource()==username) {
				string=String.format("%s", event.getActionCommand());
				if(string.matches("[0-9]*"))
				{
					JOptionPane.showMessageDialog(null,"formate not allowed");
					username.setText("");
				}
				else
				{
					ClientThread.setName(string);
					ClientThread.SetClient("channel0",string);
					JOptionPane.showMessageDialog(null, "name has been set: "+string);
					username.setText("");
					username.setEditable(false);
					item1.setEditable(true);
					item3.setEditable(true);
					ClientThread.ClientOutServerIn("new user");
					label1.setVisible(false);
				}
			}
			else if(event.getSource()==item3) {
				string=String.format("%s", event.getActionCommand());
				if(string.matches("[a-z A-Z]"))
				{
					JOptionPane.showMessageDialog(null,"formate not allowed");
					item3.setText("");
				}
				else
				{
					ClientThread.c.SetChannel("channel"+string);
					JOptionPane.showMessageDialog(null, "Channel has been set: channel"+string);
					item3.setText("");
					ClientThread.ClientOutServerIn("change channel");
				}
			}
			//JOptionPane.showMessageDialog(null, string);
		}
	}
	public void setDisplay(String x)
	{
		display.append(x + newline); 
	}
	public void setUserInChannel(String x)
	{
		UserNames.append(x + newline);
	}
	public void ClearDisplay()
	{
		UserNames.setText("");
	}
}

