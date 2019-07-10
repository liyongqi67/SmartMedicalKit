package drugs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.SynchronousQueue;

//此类用来处理设备端的各种请求，以另外一个线程的形式运行
public class ServerThread implements Runnable
{
	private static Socket socket;
	 private static BufferedReader in = null;
	private String instruction = null;
//	private static DataOutputStream out = null;
	private static BufferedWriter bufferedWriter=null;
	public ServerThread (Socket socket) 
	{
		this.socket=socket;
	}
		  
	public void run()
	{
//			************************************
		//流的建立
		System.out.println("执行run");
		try 
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"gbk"));
//			out = new DataOutputStream(socket.getOutputStream());
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"gbk"));
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("创建与客户端的信息通道时发生一个错误,可能是客户端已断开连接");
			e.printStackTrace();

			try 
			{
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			return;
		}
//			****************************************	
		//读取内容
		System.out.println("尝试读取内容");
		try 
		{
			
			instruction = in.readLine();
			//确保读到的内容非空
			System.out.println("读到内容："+instruction);
			while(instruction==null)
			{
				instruction=in.readLine();
				System.out.println("读取到的内容为："+instruction);
				System.out.println(instruction);
			}
		} catch (IOException e) 
		{
			System.out.println("接受从客户端发送的识别码时发生一个错误,可能是客户端已断开连接");
			
			try {
				in.close();
				socket.close();
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			
			e.printStackTrace();
			return;
		
		}
		//执行处理信息的方法
		System.out.println("下面进行信息处理");
		handle(instruction);
		
//		****************************
	}
	//对信息进行分析并执行相应的操作
	public static void handle(String s)
	{
		 char c=s.charAt(0);//取第一个字母来判断是设备端还是手机端
		 s=s.substring(1);//取出第一个字母后的字符串；
		
		 switch(c)
		 {
			 case 'P'://手机端传来的信息
				String user=s.substring(0, s.indexOf(';'));//取出用户名
				s=s.substring((user.length()+1));
				
				
					 
				if (user.equals("sdu")) //表示下面是注册或登录的信息
				{
					String first=s.substring(0, s.indexOf(';'));
					s=s.substring((first.length()+1), s.lastIndexOf(';'));//具体的信息
					 
					switch(first)
					{
						case "r"://手机端注册
							String name=s.substring(0,s.indexOf('-'));//用户注册的名字
							s=s.substring((name.length()+1));
							String password=s.substring(0,s.indexOf('-'));//用户注册的密码 
							
							String iduser=s.substring((password.length()+1));//此用户对应的设备号
							if(Mysql.haveIduser(iduser))//若设备号已经存在,则下面在设备号后面绑定用户命密码
								Mysql.setName(iduser, name, password);
							else//若设备号并未存在	
								Mysql.register(iduser, name, password);
							//返回成功
							try 
							{
								bufferedWriter.write("s");
								bufferedWriter.flush();
							} catch (IOException e) {
								System.out.println("服务器发送注册成功时出错");
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//关闭out,in,socket
							try 
							{
								in.close();
								bufferedWriter.close();
								socket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case "1"://手机端登录
							name=s.substring(0,s.indexOf('-'));//用户登录的名字
							password=s.substring((name.length()+1));//用户登录的密码
							if(Mysql.login(name, password))//登录成功
							{
								//返回成功
								System.out.println("登陆成功");
								try 
								{
									bufferedWriter.write("s");
									bufferedWriter.flush();
									System.out.println("已成功发送s");
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//关闭out,in,socket
								try 
								{
									in.close();
									bufferedWriter.close();
									socket.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							else
							{
								//返回失败
								System.out.println("登陆失败");
								try 
								{
									bufferedWriter.write("f ");
									bufferedWriter.flush();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//关闭out,in,socket
								try 
								{
									in.close();
									bufferedWriter.close();
									socket.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							break; 
						default:
							break;
							
						
					}
				}
				else//表示带已有用户名的其他操作,
				{
					String first=s.substring(0, s.indexOf(';'));
//					s=s.substring((first.length()+1), s.indexOf(';'));//具体的信息
					switch(first)
					{
					
					case "flash"://表示手机端获取药品列表
						String iduser=Mysql.getIduser(user);
						try 
						{
//							out.writeUTF(Mysql.getTable(userTable));
							String jjj=Mysql.getPTable(iduser);
							if(jjj==null)
								jjj="none";
							bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"gbk"));
							bufferedWriter.write(jjj+"\n");
							bufferedWriter.flush();	
							
						
							
							System.out.println("发送过去的数据为 :"+jjj);
							System.out.println(socket.isClosed());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try 
						{
							in.close();
							bufferedWriter.close();
							
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
					case "n":
						iduser=Mysql.getIduser(user);
						s=s.substring((first.length()+1));//具体的信息
						boolean isadd=true;
						String code=s.substring(0,s.indexOf('-'));//药品条形码
						s=s.substring((code.length()+1));
						String birthday=s.substring(0,s.indexOf('-'));//生产日期
						s=s.substring(birthday.length()+1);
						String guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
						String surplus=s.substring((guaranteePeriod.length()+1),s.indexOf(';'));//剩余量
						
						try 
						{
							UseApi.send(code);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							isadd=false;
							try 
							{
								bufferedWriter.write("f");
								bufferedWriter.flush();
							} catch (IOException el) {
								// TODO Auto-generated catch block
								el.printStackTrace();
							}
							System.out.println("没有对应的条形码，已成功向设备端发送失败");
							
							e.printStackTrace();
						}
						if(isadd)
						{
							
							
							String name=UseApi.name;
							String price=UseApi.price;
							String tag=UseApi.tag;
							String type=UseApi.type;
							String properties=UseApi.properties;
							String functions=UseApi.functions;
							String usage=UseApi.usage;
							String attentions =UseApi.attentions;
							String specifications =UseApi.specifications;
	//						String userTable=Mysql.getUser(iduser);
							//打印出药品信息
		//					System.out.println(userTable);
		//					System.out.println(code);
		//					System.out.println(name);
		//					System.out.println(birthday);
		//					System.out.println(guaranteePeriod);
		//					System.out.println(tag);
		//					System.out.println(type);
		//					System.out.println(surplus);
							try 
							{
								if(!Mysql.haveIduser(iduser))
									Mysql.register(iduser, "li", "li");
								Mysql.addDrug(iduser, code, name, price, "111111","12", tag, type,"6", properties, functions, usage, attentions, specifications);
								Data.explodeTag(tag);
								for(int i=0;i<Data.s.length;i++)
								{
									Mysql.setTag(iduser, Data.s[i]);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.println("调用addDrug方法时出错");
								try 
								{
									bufferedWriter.write("f");
									bufferedWriter.flush();
								} catch (IOException el) {
									// TODO Auto-generated catch block
									el.printStackTrace();
								}
								e.printStackTrace();
							}
							//成功返回s
							try 
							{
	                        //	out.writeUTF("s");
								bufferedWriter.write("s");
								bufferedWriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//关闭out,in,socket
					try {
						in.close();
					//	out.close();
						bufferedWriter.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
					//下面表示手机端修改信息
					case "m":
						iduser=Mysql.getIduser(user);
						s=s.substring((first.length()+1),s.lastIndexOf(';'));//具体的信息
						String what=s.substring(0,s.indexOf(';'));//表示要修改的值的名称
						s=s.substring((what.length()+1));
						String isWhat=s.substring(0,s.indexOf(';'));//表示修改后的值
						String number=s.substring(isWhat.length()+1);
						System.out.println("要修改的药品编号为："+number);
						if(what.equals("eaten"))
						{
							
							Mysql.setEaten(iduser, number, isWhat);
							System.out.println("手机端eaten值修改成功");
						}
						else if(what.equals("birthday"))
						{
							
							Mysql.setBirthday(iduser, number, isWhat);
							System.out.println("手机端生产日期修改成功");
						}
						else if(what.equals("surplus"))
						{
							
							Mysql.setBirthday(iduser, number, isWhat);
							System.out.println("手机端生产日期修改成功");
						}
						else if(what.equals("drugHour"))
						{
							Mysql.setDrugHour(iduser, number, isWhat);
						}
						else if(what.equals("drugMinute"))
						{
							Mysql.setDrugMinute(iduser, number, isWhat);
						}
						else if(what.equals("attentionTime"))
						{
							Mysql.setAttentionTime(iduser, number, isWhat);
						}
						else if(what.equals("dosage"))
						{
							Mysql.setAttentionTime(iduser, number, isWhat);
						}
						
						
						break;	
					case "done":
						iduser=Mysql.getIduser(user);
						System.out.println(s);
						number=s.substring((first.length()+1),s.lastIndexOf(';'));
						System.out.println(number);
						String isEaten=Mysql.getIsEaten(iduser, number);
						try 
						{
//							out.writeUTF(Mysql.getTable(userTable));
							bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"gbk"));
							String jjj;
							if(isEaten.equals("yes"))
							{
								jjj="0";
							}
							else
							{
								jjj="1";
							}
							bufferedWriter.write(jjj);
							bufferedWriter.flush();	
							
						
							
							System.out.println("发送过去的数据为 :"+jjj);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try 
						{
							in.close();
							bufferedWriter.close();
							
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "drugEate":
						iduser=Mysql.getIduser(user);
						number=s.substring((first.length()+1),s.lastIndexOf(';'));
						try 
						{
//							out.writeUTF(Mysql.getTable(userTable));
							bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"gbk"));
							String jjj=Mysql.getDrugEate(iduser, number);
							bufferedWriter.write(jjj);
							bufferedWriter.flush();	
							
						
							
							System.out.println("发送过去的数据为 :"+jjj);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try 
						{
							in.close();
							bufferedWriter.close();
							
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					case "a":
						try 
						{
//							out.writeUTF(Mysql.getTable(userTable));
							bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"gbk"));
							String jjj=PushMassage.findMassages(user);
							bufferedWriter.write(jjj);
							bufferedWriter.flush();	
							
						
							
							System.out.println("发送过去的数据为 :"+jjj);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try 
						{
							in.close();
							bufferedWriter.close();
							
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "delete":
						iduser=Mysql.getIduser(user);
						System.out.println(iduser);
						s=s.substring(first.length()+1);
						number=s.substring(0,s.lastIndexOf(';'));//获取即将被删除药品的序列
					
						Mysql.deleteDrug(iduser, number);
						try 
						{
							bufferedWriter.write("s"+"\r\n");
							bufferedWriter.flush();
							System.out.println("删除药品成功并发送s");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try {
							in.close();
	//						out.close();
							bufferedWriter.close();
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
							
							
						}
				}
			break;
			case 'D':
				String iduser=s.substring(0, s.indexOf(';'));//取出设备名
				System.out.println("设备名为："+iduser);
				s=s.substring((iduser.length()+1));//设备名之后的信息
				String first=s.substring(0, s.indexOf(';'));//取出操作符
				System.out.println("操作符为"+first);
				s=s.substring(first.length()+1);
				switch(first)
				{
					//下面表示设备端请求增加药品
					case "n":
						boolean isadd=true;
						String code=s.substring(0,s.indexOf('-'));//药品条形码								
						s=s.substring((code.length()+1));
						String birthday=s.substring(0,s.indexOf('-'));//生产日期
						s=s.substring(birthday.length()+1);
						String guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
						String surplus=s.substring((guaranteePeriod.length()+1),s.indexOf(';'));//剩余量
						
						try 
						{
							UseApi.send(code);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							isadd=false;
							try 
							{
								bufferedWriter.write("f");
								bufferedWriter.flush();
							} catch (IOException el) {
								// TODO Auto-generated catch block
								el.printStackTrace();
							}
							System.out.println("没有对应的条形码，已成功向设备端发送失败");
							
							e.printStackTrace();
						}
						if(isadd)
						{
							
							
							String name=UseApi.name;
							String price=UseApi.price;
							String tag=UseApi.tag;
							String type=UseApi.type;
							String properties=UseApi.properties;
							String functions=UseApi.functions;
							String usage=UseApi.usage;
							String attentions =UseApi.attentions;
							String specifications =UseApi.specifications;
	//						String userTable=Mysql.getUser(iduser);
							//打印出药品信息
		//					System.out.println(userTable);
		//					System.out.println(code);
		//					System.out.println(name);
		//					System.out.println(birthday);
		//					System.out.println(guaranteePeriod);
		//					System.out.println(tag);
		//					System.out.println(type);
		//					System.out.println(surplus);
							try 
							{
								if(!Mysql.haveIduser(iduser))
									Mysql.register(iduser, "li", "li");
								Mysql.addDrug(iduser, code, name, price, birthday, guaranteePeriod, tag, type, surplus, properties, functions, usage, attentions, specifications);
								Data.explodeTag(tag);
								for(int i=0;i<Data.s.length;i++)
								{
									Mysql.setTag(iduser, Data.s[i]);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.println("调用addDrug方法时出错");
								try 
								{
									bufferedWriter.write("f");
									bufferedWriter.flush();
								} catch (IOException el) {
									// TODO Auto-generated catch block
									el.printStackTrace();
								}
								e.printStackTrace();
							}
							//成功返回s
							try 
							{
	                        //	out.writeUTF("s");
								bufferedWriter.write("s");
								bufferedWriter.flush();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						//关闭out,in,socket
					try {
						in.close();
					//	out.close();
						bufferedWriter.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
					
					//下面表示设备端请求获得药品列表
					case "flash":
//						userTable=Mysql.getUser(iduser);
//						System.out.println("设备号对应的用户为："+userTable);
						try 
						{
//							out.writeUTF(Mysql.getTable(userTable));
							if(!Mysql.haveIduser(iduser))
								Mysql.register(iduser, "li", "li");
							String jjj=Mysql.getTable(iduser);
							if(jjj!=null)
							{
//								jjj="6927043102543-康诺药业 氨酚巴妥片-解热镇痛-哮喘,解热镇痛,过敏,牙痛-44.0-151111-24-55;";

								bufferedWriter.write(jjj);
								System.out.println("发送过去的数据为 :"+jjj);
								bufferedWriter.flush();
								
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try {
							in.close();
							bufferedWriter.close();
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
					//下面表示设备端修改信息
					case "m":
//						userTable=Mysql.getUser(iduser);//获得设备号对应的用户名
						String number=s.substring(0,s.indexOf('-'));//药品序列
						s=s.substring((number.length()+1));
						if(s.charAt(0)=='-')							
						{
							s=s.substring(1);
							if(s.charAt(0)=='-')
							{
								s=s.substring(1);
								if(s.charAt(0)=='-')
								{
									s=s.substring(1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
										
									}
								}
								else
								{
									guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
									Mysql.setGuaranteePeriod(iduser, number, guaranteePeriod);
									s=s.substring(guaranteePeriod.length()+1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
								
							}
							else
							{
								birthday=s.substring(0,s.indexOf('-'));//生产日期
								Mysql.setBirthday(iduser, number, birthday);
								
								s=s.substring(birthday.length()+1);
								if(s.charAt(0)=='-')
								{
									s=s.substring(1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
								else
								{
	
									guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
									Mysql.setGuaranteePeriod(iduser, number, guaranteePeriod);
									s=s.substring(guaranteePeriod.length()+1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
							}
						}
						else
						{
							String drugName=s.substring(0,s.indexOf('-'));//要修改成的药品名
							Mysql.setDrugname(iduser, number, drugName);
							s=s.substring(drugName.length()+1);
							if(s.charAt(0)=='-')
							{
								s=s.substring(1);
								if(s.charAt(0)=='-')
								{
									s=s.substring(1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
								else
								{
									guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
									Mysql.setGuaranteePeriod(iduser, number, guaranteePeriod);
									s=s.substring(guaranteePeriod.length()+1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
								
							}
							else
							{
								birthday=s.substring(0,s.indexOf('-'));//生产日期
								Mysql.setBirthday(iduser, number, birthday);
								s=s.substring(birthday.length()+1);
								if(s.charAt(0)=='-')
								{
									s=s.substring(1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
								else
								{
	
									guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
									Mysql.setGuaranteePeriod(iduser, number, guaranteePeriod);
									s=s.substring(guaranteePeriod.length()+1);
									if(s.charAt(0)!=';')
									{
										surplus=s.substring(0,s.indexOf(';'));//剩余量
										Mysql.setSurplus(iduser, number, surplus);
										String tag=Mysql.getTag(iduser, number);
										Data.explodeTag(tag);
										for(int i=0;i<Data.s.length;i++)
										{
											Mysql.setTag(iduser, Data.s[i]);
										}
									}
								}
							}
						}
//						birthday=s.substring(0,s.indexOf('-'));//生产日期
//						if(!birthday.equals(""))
//						{
//							Mysql.setBirthday(iduser, code, birthday);
//						}
//						s=s.substring(birthday.length()+1);
//						guaranteePeriod =s.substring(0,s.indexOf('-'));//保质期
//						if(!guaranteePeriod.equals(""))
//						{
//							Mysql.setGuaranteePeriod(iduser, code, guaranteePeriod);
//						}
//						surplus=s.substring((guaranteePeriod.length()+1),s.indexOf(';'));//剩余量
//						if(!surplus.equals(""))
//						{
//							Mysql.setSurplus(iduser, code, surplus);
//						}
						//返回成功
						try 
						{
//							out.writeUTF(Mysql.getTable(userTable));
							bufferedWriter.write("s");
							System.out.println("设备端修改药品信息成功并成功发送s");
							bufferedWriter.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try {
							in.close();
//							out.close();
							bufferedWriter.close();
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
					//下面表示设备端删除药品
					case "delete":
						System.out.println("即将删除药品");
						number=s.substring(0,s.lastIndexOf(';'));//获取即将被删除药品的序列
						System.out.println("删除药品的条形码为："+number);
						Mysql.deleteDrug(iduser, number);
					try 
					{
						bufferedWriter.write("s"+"\r\n");
						bufferedWriter.flush();
						System.out.println("删除药品成功并发送s");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//关闭out,in,socket
					try {
						in.close();
//						out.close();
						bufferedWriter.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
					case "schedule":
						String jjj=Mysql.getSchedule(iduser);
						try {
							bufferedWriter.write(jjj+"\r\n");
							bufferedWriter.flush();
  							System.out.println("发送过去的内容为："+jjj);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//关闭out,in,socket
						try {
							in.close();
//							out.close();
							bufferedWriter.close();
							socket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					break;
					case "binding":
						String rfid=s.substring(0,s.indexOf(';'));
						
						number=Mysql.getNumber(iduser);
						Mysql.bindingRfid(iduser, number, rfid);
					break;
					case "put":
						rfid=s.substring(0,s.indexOf(';'));Mysql.setIseaten(iduser, rfid);
					break;
						
					
				}
				
				
				
				
				break;
		 }
	}
}
