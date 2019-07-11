package drugs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;



//用于等待socket客户端的连接
public class SocketConnect
{
	private static ServerSocket serversocket;
	private static Socket socket;
	public static void serverStart()
	{
		try {
			serversocket=new ServerSocket(8889);
			System.out.println("service start...");
			while(true)
			{
				socket=serversocket.accept();
				System.out.println("yilainjie");
//				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//				bufferedWriter.write("6927043102543-小儿喜食糖浆-15.0-969696-0-腹痛腹泻,过敏,其他,小儿泄泻用药,消化不良,其他药品,食欲不振-中成药-0;");
//				bufferedWriter.flush();
//				bufferedWriter.close();
				ServerThread serverthread=new ServerThread(socket);
				Thread thread=new Thread(serverthread);
				thread.start();
				System.out.println("开启服务线程");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public static void main(String args[])
	{
		SocketConnect.serverStart();
	}
}
