package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MultiClients extends Thread {
	
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	boolean quite=false;
	public ClientData c;
	public tuna GUI;
	
	public MultiClients(Socket OurMultiSocket, tuna gui)
	{
		s=OurMultiSocket;
		c=new ClientData();
		GUI=gui;
	}
	public void ClientOutServerIn(String Text)
	{
		//write the line from console to server
		try {
			if(Text.equals("change channel"))
			{
				System.out.print("sending changing channel: "+Text+"\n");
				dout.writeUTF(Text);
				dout.flush();
			}
			else if(Text.equals("new user"))
			{
				System.out.print("sending new user: "+ Text+"\n");
				dout.writeUTF(Text+":"+c.GetName()+"="+c.GetChannel());
				dout.flush();
			}
			else
			{
				dout.writeUTF(c.GetChannel()+"="+this.getName()+": "+Text);
				dout.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void SetClient(String channel,String Name)
	{
		c.SetName(Name);
		c.SetChannel(channel);
	}
	public void run()
	{
		try {
			din=new DataInputStream(s.getInputStream());
			dout=new DataOutputStream(s.getOutputStream());
			while(!quite)
			{
				try {
					while(din.available()==0)
					{
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//if there is something just show it on console
					//and then go back and do the same
					String reply=din.readUTF();
					String Chan=ExtractChannel(reply);
					String name=ExtractName(reply);
					/*if (reply.equals("change channel"))
					{
						System.out.print("changing channel in body: "+reply+"\n");
						//GUI.ClearDisplay();
						setChangedChannel();
					}*/
					if(name.equals("new user"))
					{
						System.out.print("new user in body: "+reply+"\n");
						//GUI.ClearDisplay();
						setChannel(reply);
					}
					else
					{
						PrintReply(Chan,reply);
					}
					//System.out.println(reply);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						din.close();
						dout.close();
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				din.close();
				dout.close();
				s.close();
			} catch (IOException x) {
				// TODO Auto-generated catch block
				x.printStackTrace();
			}
		}
	}
	public void CloseClient()
	{
		try {
			din.close();
			dout.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String ExtractName(String x)
	{
		String[]Y=x.split(":");
		return Y[0];
	}
	public String ExtractChannel(String X)
	{
		String[]Y=X.split("=");
		return Y[0];
	}
	public void PrintReply(String Chan,String Rep)
	{
		if(c.GetChannel().equals(Chan))
		{
			String []Y=Rep.split("=");
			GUI.setDisplay(Y[1]);
			//System.out.println(Y[1]+"\n \n \n \n");
		}
		
	}
	public void setChannel(String x)
	{
		String []Y=x.split(":");
		String []Z=Y[1].split("=");
		System.out.print("setting "+Z[0]+" channel to "+Z[1]+"\n");
		GUI.setUserInChannel(Z[0]);
	}
	public void setChangedChannel()
	{
		GUI.setUserInChannel(c.GetName()+": "+c.GetChannel());
	}
	class ClientData
	{
		public String ClientName;
		public String channel;
		
		public void SetChannel(String Chan)
		{
			channel=Chan;
		}
		public void SetName(String name)
		{
			ClientName=name;
		}
		public String GetChannel()
		{
			return channel;
		}
		public String GetName()
		{
			return ClientName;
		}
	}
	
}
