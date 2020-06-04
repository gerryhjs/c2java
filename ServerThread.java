package p1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerThread extends Thread{
	private Socket s;
	public static ArrayList<User> UserList;
	public String ServerPath="C:/!Server/Files";
	public ServerThread(Socket s)
	{
		this.s = s;
	}

	
public static String getTime(File f)
{
			Process ls_proc;
			try {
				ls_proc = Runtime.getRuntime().exec(   "cmd.exe /c dir " + f.getAbsolutePath() + " /tc");
				  InputStream is = ls_proc.getInputStream();  
	                BufferedReader br = new BufferedReader(new InputStreamReader(is));  
	                String str;  
	                int j = 0;  
	                while ((str = br.readLine()) != null) {  
	                  j++;  
	                  if (j == 6) { 
	                    return str.substring(0, 17);
	                  }
	              }
			}
			 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
}
/*
public boolean upload(File f)
{
	return true;
}
public boolean download(File f)
{
	return true;
}
*/
@SuppressWarnings("resource")
public static  long getSize(File f) throws Exception{//ȡ���ļ���С
long s=0;
if (f.exists()) {
FileInputStream fis = null;
fis = new FileInputStream(f);
s= fis.available();
} else {
f.createNewFile();
System.out.println("file does not exist");
s=0;
}
return s;
}
public static String printFile(File f)
{
	String ms="";
	if(f!=null){
        if(f.isDirectory())
        {
            File[] fileArray=f.listFiles();
                for (int i = 0; i < fileArray.length; i++) 
                {
                	try {
						ms=ms+String.valueOf(fileArray[i])+" Create time:"+getTime(fileArray[i])+" File size"+getSize(fileArray[i]) + "\r\n";
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
        }
	}
	return ms;
}
	public static void ioIn(ArrayList<User> UserList) throws UnsupportedEncodingException, FileNotFoundException
	{
		File file=new File("C:/Server/user.txt");
		InputStreamReader read;

			read = new InputStreamReader(new  FileInputStream(file),"GBK");
			BufferedReader reader=new BufferedReader(read);
		try{
			int UserAmount = Integer.valueOf(reader.readLine());
			UserList =new ArrayList<User>();
			for (int i=1;i<=UserAmount;i++)
			{
				String ID=reader.readLine();
				String PassWord=reader.readLine();
				String Identify=reader.readLine();
				User it=new User(ID,PassWord,Identify);
				UserList.add(it);
			}
			reader.close();
		}catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void ioOut(ArrayList<User> UserList)
	{
		File file=new File("C:/Server/user.txt");
		try {
			Writer writer=new OutputStreamWriter(new FileOutputStream(file,false),"GBK");
			int UserAmount=0;
			for (@SuppressWarnings("unused") User scanner:UserList) UserAmount++;
			writer.write(String.valueOf(UserAmount)+"\r\n");
			for (User scanner:UserList)
			{
				writer.write(scanner.ID+"\r\n");
				writer.write(scanner.PassWord+"\r\n");
				writer.write(scanner.Identify+"\r\n");
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		long rs=(long) 0;
		System.out.println("----------------------------------------");
		System.out.println("Service-Terminal");
		System.out.println("----------------------------------------");

		
		
		UserList=new ArrayList<User>();
		User exUser=new User("Example","123456","Normal");
		User exUser2=new User("Example2","admin","Admin");
		UserList.add(exUser);
		UserList.add(exUser2);
		//ioOut(UserList);
	
/*
				try {
					ioIn(UserList);
				} catch (UnsupportedEncodingException | FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	
*/
	/*
		for (User scanner:UserList)
		{
			System.out.println(scanner.ID);
		}
		*/
	    	InputStream input = null;
			try {
				input = s.getInputStream();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			OutputStream output=null;
			try {
				output = s.getOutputStream();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			DataInputStream dis = new DataInputStream(input);
			DataOutputStream dos = new DataOutputStream(output);
			System.out.println("Connected client");
			User Me=new User("Unknown client","",null);
			User Check=new User("","",null);
		//	Boolean Login=false;
			Boolean Login=true;
			while(true)
			{
				try {
				String s1 = dis.readUTF();
				System.out.println("From"+Me.ID+ "the information is"+s1);

				boolean flag=false;
		
					String check = " ";
					if (s1.length()>=5) 	check=s1.substring(s1.length()-4);
				//System.out.println(check);
				
				//if  (check.equals("\r\n"))
				if  ((check.contains("r")) && (check.contains("n")))
				{
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
				System.out.println(c1);
				/*
				if(c1.equals("Exit"))
				{
					flag=true;
					System.out.println("�ͻ���ָ���˳�");
					break;
				}
				*/
				if (c1.equals("QUIT\\r\\n"))
				{
					dos.writeUTF("200");
					System.exit(-1);
				}
				if(c1.equals("USER"))
				{
					flag=true;
				   //c2.replaceAll("\r\n","");
					String UserName=c2.substring(0,c2.length()-4);
					System.out.println("Identify the logged in user"+UserName);
					boolean find=false;
					for (User Scanner:UserList)
					{
						if (Scanner.ID.equals(UserName)) 
						{
							find=true;
							Check=Scanner;
						}
					}
					if (find) 
						{
						dos.writeUTF("331");//�û�����ȷ����Ҫ����-331
						}
					else
					{
						dos.writeUTF("530");//�û������������-530
					}
				}		
				if (c1.equals("PASS"))
				{
					flag=true;
					String UserPass=c2.substring(0,c2.length()-4);
					System.out.println("Identify the login password"+UserPass);
					if (Check.PassWord.equals(UserPass))
					{
						Me=Check;
						Login=true;
						dos.writeUTF("230");//�û��ѵ�¼
					}
					else
					{
						Check=new User("","","null");
						dos.writeUTF("530");//�û������������-530
					}
				}
				if(c1.equals("LIST\\r\\n"))
				{
					flag=true;
					if (!Login)
					{
						dos.writeUTF("332");//��Ҫ��¼
					}
					else
					{
					String FileName=ServerPath+File.separator;
					File f=new File(FileName);
					dos.writeUTF(printFile(f));
					}
				}
				if(c1.equals("CWD"))
				{
					String newcwd=c2.substring(0,c2.length()-4);
					ServerPath=newcwd;
					dos.writeUTF("200");
				}
				if (c1.equals("REST"))
				{
				  rs= Long.valueOf(c2.substring(0,c2.length()-4));
					dos.writeUTF("200");
				}
				if(c1.equals("SIZE"))
				{
					flag=true;
					File f=new File(ServerPath+File.separator+c2.substring(0,c2.length()-4));
					try {
						dos.writeUTF(String.valueOf(getSize(f)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(c1.equals("RETR"))
				{
					flag=true;
					long size=0;
					File f=new File(ServerPath+File.separator+c2.substring(0,c2.length()-4));
					rs= Long.valueOf(dis.readUTF());
					try {
						size=getSize(f);
						dos.writeUTF(String.valueOf(size));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(rs+"/"+size);
					FileInputStream Fis=new FileInputStream(f);
					int offset;
					byte[] bytes=new byte[1024];
					if (rs<size)
					{
					for (long t=(long)1;t<=((long)(size+1023)/1024);t++)
					{
						offset=Fis.read(bytes);
						//System.out.flush();
						if (rs< t*1024)
						{
							double h1= (double)(((double)(t)*1024+(double)(rs))/((double)(size)));
							int h=(int)(h1*100);
							if (h>100) h=100;
							System.out.println("Transmission progress:"+h +"%:"+(t*1024+rs)+"/" + size)  ;
							dos.write(bytes,0,offset);
						}
					}
					System.out.println("download successful!");
					}
					else
					{
						if (size>0)  System.out.println("Has downloaded the file!"); 
					}
					Fis.close();
					//dos.close();
					//dos.writeUTF("200");
					//dos.writeUTF(str);
					//if (download(f)) dos.writeUTF("200"); else dos.writeUTF("550");
				}
				if(c1.equals("STOR"))
				{
					flag=true;
					File f=new File(ServerPath+File.separator+c2.substring(0,c2.length()-4));
					try {
						dos.writeUTF(String.valueOf(getSize(f)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    long size=Long.valueOf(dis.readUTF());
					System.out.println(rs+"/"+size);
					FileOutputStream Fos=new FileOutputStream(f,true);//apend
					int offset;
					byte[] bytes=new byte[1024];
					System.out.println("timer:"+(long)((size-rs+1023)/1024));
					if (rs<size)
					{
					for (long t=(long)1;t<=(long)((size-rs+1023)/1024);t++)
					{
						offset=dis.read(bytes);
						//System.out.flush();
						double h1= (double)(((double)(t)*1024+(double)(rs))/((double)(size)));
						int h=(int)(h1*100);
						if (h>100) h=100;
						System.out.println("Transmission progress:"+h +"%:"+(t*1024+rs)+"/" + size)  ;
						Fos.write(bytes,0,offset);
					}
			
					//String Respose=dis.readUTF();
					//if (Respose.equals("-1"))  System.out.println("�ϴ��ɹ���"); else System.out.println("�ϴ�ʧ�ܣ�");;
					System.out.println("Upload success!"); 
					}
					else
					{
						if (size>0)  System.out.println("Has uploaded the file!"); 	
					}
					Fos.close();
					//dis.close();
					//dos.writeUTF("200");
					//if (upload(f)) dos.writeUTF("200"); else dos.writeUTF("550");
				}
				if (c1.equals("PASV\\r\\n"))
					{
						flag=true;
						if (!Login)
						{
							dos.writeUTF("332");//��Ҫ��¼
						}
						else
						{
						ServerSocket ss=null;
						int p_low;
						int p_high;
						while (true)
						{
							p_low=1+(int)(Math.random()*20);
							p_high=100+(int)(Math.random()*1000);
							try
							{
							    ss=new ServerSocket(p_low*256+p_high);
							    ss.close();
								if ((p_low*256+p_high)!=8888)  break;
							}catch (IOException e)
							{
								continue;
							}
						}
						ss.close();
						System.out.println("Identify users:"+Me.ID+"PASV instruction");
						InetAddress i =null;
						try {
							  i = InetAddress.getLocalHost();
						} catch (UnknownHostException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						System.out.println(i);
						String m=String.valueOf(i);
					//	System.out.println(m);
						m=m.replaceAll("DESKTOP-ABC728Q/", "");
					//	System.out.println(m);
						m=m.replace(".", ",");
					//	System.out.println(m);
						
						dos.writeUTF("227 "+m+","+p_low+","+p_high);
						System.out.println("227 Enter PASV mode "+m+","+p_low+","+p_high);
						dos.flush();
						
						@SuppressWarnings("resource")
						ServerSocket server = new ServerSocket(p_low*256+p_high);
						System.out.println(p_low*256+p_high+"The port server is open");
						Socket s2=server.accept();
						ServerThread st2 = new ServerThread(s2);
						st2.start();
					
						}
					}
				}
				if (!flag) dos.writeUTF("500");
				/*
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
				String s2 = scanner.nextLine();
				dos.writeUTF(s2);
				*/
			
			
			//�ر���
	/*
			input.close();
			output.close();
			dis.close();
			dos.close();
		*/	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			continue;
		}
			}
    }
}