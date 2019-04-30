package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MultiServerConnection extends Thread {
	
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	server ss;
	boolean quite=false;
	
	public MultiServerConnection(Socket OurSocket,server OurServer)
	{
		super("MultiServerConnection");//server connection thread
		this.s=OurSocket;
		this.ss=OurServer;
	}
	
	public void ServerOutClientIn(String OutText)
	{
		try {
			long ThreadID=this.getId();
			dout.writeUTF(OutText);
			dout.flush();//this is because of a buffer error :<
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ServerOutAllClientIn(String OutText)
	{
		for(int i=0;i<ss.OurDomainsConnections.size();i++)
		{
			MultiServerConnection Connection=ss.OurDomainsConnections.get(i);
			Connection.ServerOutClientIn(OutText);
		}
	}
	
	public void run()
	{
		try {
			din=new DataInputStream(s.getInputStream());
			dout=new DataOutputStream(s.getOutputStream());
			
			while(!quite)
			{
				while(din.available()==0)
				{
					try {
						Thread.sleep(1);//sleep if there is not data coming
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String ComingText=din.readUTF();
				ServerOutAllClientIn(ComingText);
			}
			din.close();
			dout.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}