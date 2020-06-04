package p1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
@SuppressWarnings("unused")

public class Server {

	public static void main(String[] args) {
		InetAddress i = null;
		//1.open listenning port
		try {
			System.out.println("----------------------------------------");
			try {
				  i = InetAddress.getLocalHost();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("The server is turned on, the IP is"+i);
			System.out.println("----------------------------------------");
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(8888);
	
			//listen
			//���ݶ˿�
			while(true)
			{
				Socket socket = server.accept();	
				//BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				//PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())); 
				//�����̴߳���
				ServerThread st = new ServerThread(socket);
				//�����߳�
				st.start();
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
