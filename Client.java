package p1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Client {
	@SuppressWarnings("resource")
	public static  long getSize(File f) throws Exception{//ȡ���ļ���С
		long s=0;
		if (f.exists()) {
		FileInputStream fis = null;
		fis = new FileInputStream(f);
		s= fis.available();
		} else {
		f.createNewFile();
		System.out.println("file is not exist");
		s=0;
		}
		return s;
		}
	@SuppressWarnings("unused")
	public static void main(String args[]) 
	{
		Socket dataSocket = null;
		InetAddress i = null;
		String ClientPath="C:/!Client/Files";
				
	System.out.println("----------------------------------------");
	try {
		  i = InetAddress.getLocalHost();
	} catch (UnknownHostException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	System.out.println("client is open, the IP is:"+i);
	System.out.println("----------------------------------------");
	try {

		@SuppressWarnings("resource")
		Scanner scanner=new Scanner(System.in);
		
		System.out.print("Please enter the IP:");
		String IP=scanner.nextLine();
		System.out.print("Please enter the port:");
		int port=Integer.valueOf(scanner.nextLine());
		@SuppressWarnings("resource")
		Socket socket= new Socket(IP,port); 
		InputStream input=socket.getInputStream();
		OutputStream output=socket.getOutputStream();
		DataInputStream dis= new DataInputStream(input);
		DataOutputStream dos=new DataOutputStream(output);
		
		long rs=0;
		
		while (true)
		{
			
			
			System.out.print("Please enter the instruction");
			String s1=scanner.nextLine();
			dos.writeUTF(s1+"\\r\\n");
			StringTokenizer st=new StringTokenizer(s1);
			String c1;	
			String c2;
			if (st.hasMoreTokens())
				c1=st.nextToken(); 
			else
				c1="";
			if (st.hasMoreTokens())
				c2=st.nextToken();
			else 
				c2="";
			boolean flag=false;
			if (c1.equals("REST"))
			{
				rs= Integer.valueOf(c2.substring(0,c2.length()-4));
			}
			if (c1.equals("STOR"))
			{
				flag=true;
				File f=new File(ClientPath+File.separator+c2);
			    rs=Long.valueOf(dis.readUTF());
				if (rs>0) System.out.println("Detected the last upload interrupt, began to break off");
				//System.out.println(rs);
				long size = 0;
				try {
					size = getSize(f);
					dos.writeUTF(String.valueOf(size));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(rs+"/"+size);
				System.out.println("the file size is"+size);
				FileInputStream Fis=new FileInputStream(f);
				int offset;
				byte[] bytes=new byte[1024];
				if (rs<size)
				{
				for (Long t=(long)1;t<=(long)((size+1023)/1024);t++)
				{
					offset=Fis.read(bytes);
					if (rs<t*1024)
					{
					//System.out.flush();
						double h1= (double)(((double)(t)*1024+(double)(rs))/((double)(size)));
						//System.out.println(h1);
						int h=(int)(h1*100);
						if (h>100) h=100;
						System.out.println("Transmission progress"+h +"%:"+(t*1024+rs)+"/" + size)  ;
					//System.out.write(bytes,0,offset);
					//System.out.print(offset);
					dos.write(bytes,0,offset);
					}
				}
				System.out.println("Uploaded successfully");
				}
				else
				{
					 if (size>0) System.out.println("The file has been uploaded!");
				}
				Fis.close();
				//dos.close();
				//dataSocket.close();
			}
			if (c1.equals("RETR"))
			{
				long size=0;
				flag=true;
				File f=new File(ClientPath+File.separator+c2);
				try {
				    size=getSize(f);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dos.writeUTF(String .valueOf(rs));
				if (rs>0) System.out.println("Detected the last download interrupt, began to break off");

				size=Long.valueOf(dis.readUTF());
				System.out.println(rs+"/"+size);
				System.out.println("file size"+size);
				
			
				FileOutputStream Fos=new FileOutputStream(f,true);//apend
				int offset;
				byte[] bytes=new byte[1024];
				if (rs<size)
				{
				for (long t=(long)1;t<=(long)((size-rs+1023)/1024);t++)
				{
					offset=dis.read(bytes);
					//System.out.flush();
					double h1= (double)(((double)(t)*1024+(double)(rs))/((double)(size)));
					int h=(int)(h1*100);
					if (h>100) h=100;
					System.out.println("Transmission progress"+h +"%:"+(t*1024+rs)+"/" + size)  ;
					//System.out.write(bytes,0,offset);
					//System.out.print(offset);
					Fos.write(bytes,0,offset);
				}		
				//String Respose=dis.readUTF();
				//if (Respose.equals("-1"))  System.out.println("���سɹ���"); else System.out.println("����ʧ�ܣ�");;
				//System.out.println("4/500Xiàzài chénggōngdownload successful");
				}
				else
				{
					if (size>0)  System.out.println("The file has been downloaded");
				}
				Fos.close();
				//dis.close();
				//dataSocket.close();
			}
			if (!flag)
			{
			String s2=dis.readUTF();
			System.out.println(s2);
			StringTokenizer st2=new StringTokenizer(s2);
			String c3;	
			String c4;
			if (st2.hasMoreTokens())
				c3=st2.nextToken(); 
			else
				c3="";
			if (st2.hasMoreTokens())
				c4=st2.nextToken();
			else 
				c4="";
			 if (c3.equals("200"))
			 {
			    System.out.println("The command is complete");
			 }
		    if (c3.equals("331"))
		    {
		    	System.out.println("The user name is correct and requires a password.");
		    }
		    if (c3.equals("530"))
		    {
		    	System.out.println("wrong user name or password.");
		    }
		    if (c3.equals("230"))
		    {
		    	System.out.println("The user is logged in.");
		    }
		    if (c3.equals("500"))
		    {
		    	System.out.println("Syntax error, unrecognized command.");
		    }
		    if (c3.equals("550"))
		    {
		    	System.out.println("The file upload / download failed and the file name may be incorrect.");
		    }
		    if (c3.equals("332"))
		    {
		    	System.out.println("Need to log in.");
		    }
		    if (c3.equals("227"))
		    {
		    	String tc=c4;
		    	tc=tc.replaceAll(",", " ");
		    	StringTokenizer st3=new StringTokenizer(tc);
		    	int h1=Integer.valueOf((st3.nextToken()));
		    	int h2=Integer.valueOf((st3.nextToken()));
		    	int h3=Integer.valueOf((st3.nextToken()));
		    	int h4=Integer.valueOf((st3.nextToken()));
		    	int p1=Integer.valueOf((st3.nextToken()));
		    	int p2=Integer.valueOf((st3.nextToken()));
		
		    	String ip=h1+"."+h2+"."+h3+"."+h4;
		    	int port1=p1*256+p2;
		    	System.out.println("PASV is completed. Get the IP is:"+ip);
		    	System.out.println("PASV connection port:"+port1);
				dataSocket=new Socket(ip,port1);
		    	/*
				InputStream datainput=dataSocket.getInputStream();
				OutputStream dataoutput=dataSocket.getOutputStream();
				DataInputStream datadis= new DataInputStream(datainput);
				DataOutputStream datados=new DataOutputStream(dataoutput);
				*/
		    }
			}
		}
		/*
		socket.close();
		input.close();
		output.close();
		dis.close();
		dos.close();
		*/
		
		} catch (IOException e) {
		e.printStackTrace();
		}

	}
} 
