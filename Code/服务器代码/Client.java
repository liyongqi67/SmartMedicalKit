package drugs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class Client {  
	   
	   public static void main(String args[]) throws Exception {  
	      //为了简单起见，所有的异常都直接往外抛  
	      String host = "127.0.0.1";  //要连接的服务端IP地址  
	      int port = 8888;   //要连接的服务端对应的监听端口  
	      //与服务端建立连接  
	      Socket client = new Socket(host, port);  
	      //建立连接后就可以往服务端写数据了  
	      BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
//	      out.writeUTF("D001;n;6927043102543-122222-36-15");
	      bufferedWriter.write("D001;flash");
	      bufferedWriter.flush();
	      System.out.println(in.readLine());	      
	      in.close();
	      bufferedWriter.close();
	      client.close();
	   }  
	     
	}  