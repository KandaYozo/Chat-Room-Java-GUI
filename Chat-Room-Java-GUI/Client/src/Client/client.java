package Client;
//////////////////////////////////////
import java.io.IOException;
import java.net.Socket;//this is the socket package
/*dont under any circumstance remove this import XD*/
import java.net.UnknownHostException;
///////////////////////////////////////
//our scanner import
import java.util.Scanner;

import javax.swing.JFrame;

///////////////////////////////////////
//our GUI libraries
////////////////////////////////////////
public class client{
	/////////////////////////////////////
	//better than sending them as an arguments to each function
	MultiClients ClientThread;
	////////////////////////////////////
	public static void main(String[] args) {
		new client();
	}
	public client()
	{	
		tuna crape = new tuna();
		crape.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		crape.setSize(500,600);
		crape.setVisible(true);
		/*try {
			Socket s=new Socket("localhost",3333);
			ClientThread =new MultiClients(s);
			System.out.println("Enter Your Anonomus Name:");
			@SuppressWarnings("resource")
			Scanner UserThreadName=new Scanner(System.in);
			String UserName=UserThreadName.nextLine();
			ClientThread.setName(UserName);
			ClientThread.SetClient("channel0",UserName);
			ClientThread.start();
			for(int i=0;i<50;i++)
			{
				System.out.print("\n");
			}
			ListenForInput();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	//Listen for input from user and response from server
	//user will use quite for leaving the chat
	public void ListenForInput()
	{
		//Scanners are used to read input of user from conceal
		@SuppressWarnings("resource")
		Scanner console=new Scanner(System.in);
		while(true)
		{
			//waiting for a line form console
			while(!console.hasNextLine())//only run upon pressing run
			{//make sure not to leave thread awake, my cpu was overloaded XD
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			String input=console.nextLine();//this method will automatically get the new line
			if(input.toLowerCase().equals("quit"))
			{
				break;
			}
			if(input.toLowerCase().equals("change channel"))
			{
				input=console.nextLine();
				ClientThread.c.SetChannel(input);
			}
			else
			{
				ClientThread.ClientOutServerIn(input);
			}
		}
		ClientThread.CloseClient();
	}
}
