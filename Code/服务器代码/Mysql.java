package drugs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.SynchronousQueue;

public class Mysql 
{
	static Statement statement;
	static Connection connection;
	static String[] drugTable=new String[100];
	
	
	 public Mysql()
	{

	
	}
	 //用于连接数据库
	 private static void connect()
	 {
		 try
			{
				try
				{
					Class.forName( "com.mysql.jdbc.Driver");//加载数据库驱动
					System.out.println("数据库驱动加载成功");
				}catch(ClassNotFoundException e){}
			
				//连接数据库
				 connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/drug?characterEncoding=utf8","root","");
				//其中121.250.223.51要换成数据库端所在的IP地址，3306改为数据库的端口号 
				 statement=connection.createStatement();
				
		
				
			}catch(Exception e){
				e.printStackTrace();
				System.out.println("sorry,连接失败");}
	 }
	 //用于用户注册
	 public static void register(String iduser,String user ,String password)
	 {
		 connect();
		
		 try 
		 {
			 if(!Mysql.haveIduser(iduser))//如果已经存在设备号，表示用户之前用药箱使用过
			 {
				 statement.executeUpdate("CREATE TABLE "+"t"+iduser+"(code CHAR(20),name CHAR(20),price CHAR(10),birthday char(10), guaranteePeriod CHAR(10),tag CHAR(50),type CHAR(20),surplus CHAR(10),number CHAR(10),properties TEXT,functions TEXT,us CHAR(200),attentions TEXT,specifications TEXT,eaten CHAR(10),drugHour CHAR(20),drugMinute CHAR(20),attentionTime CHAR(20),dosage CHAR(20),rfid CHAR(20),isEaten CHAR(20))");
				
			 }
			 } catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("新建药品表失败");
			e1.printStackTrace();
		 }
		 try 
		 {
			statement.executeUpdate("INSERT INTO user VALUES ('"+iduser+"','"+user+"','"+password+"')");
			statement.executeUpdate("CREATE TABLE "+"s"+iduser+"(tag CHAR(20) ,frequency INT ,time INT)");
			
		 } catch (SQLException e) {
			System.out.println("用户已存在");
			e.printStackTrace();
			return;
		 	}
//		 try 
//		 {
//			 if(!Mysql.haveIduser(iduser))//如果已经存在设备号，表示用户之前用药箱使用过
//				 statement.executeUpdate("CREATE TABLE "+"t"+iduser+"(code CHAR(20),name CHAR(20),price CHAR(10),birthday char(10), guaranteePeriod CHAR(10),tag CHAR(50),type CHAR(20),surplus CHAR(10))");
//		 } catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			System.out.println("新建药品表失败");
//			e1.printStackTrace();
//		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 	}
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 	}
	 }
	 //用户的登录
	public static boolean login(String user ,String password)
	{
		connect();
		ResultSet set=null;
		try 
		{
			set=statement.executeQuery("SELECT password FROM user where name='"+user+"'" );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while(set.next())
			{
				if(password.equals(set.getString(1)))
						 return true;
					 else
						 return false;
				 }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		return false;
	}
	//获得当前药品数的方法
	public static String getNumber(String iduser)
	{
		connect();
		ResultSet set=null;
		String number="0";
		try 
		{
			set=statement.executeQuery("select number from t"+iduser+"  order by number DESC limit 1 ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try 
		{
			while(set.next())
			{
				
				 number=set.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("获取当前药品总数出错");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("当前药品总数为："+number);
		 
		return number;
	}
	 //增添药品
	 public static void addDrug(String iduser,String code,String name,String price,String birthday,String guaranteePeriod,String tag,String type,String surplus,String properties,String functions,String usage,String attentions,String specifications) throws SQLException
	 {
		
		 connect();
		 String number=Mysql.getNumber(iduser);
		 int num=Integer.parseInt(number);
		 num++;
		 number=num+"";
		
		 try 
		 {
			 
			statement.executeUpdate("INSERT INTO "+"t"+iduser+" VALUES ('"+code+"','"+name+"','"+price+"','"+birthday+"','"+guaranteePeriod+"','"+tag+"','"+type+"','"+surplus+"','"+number+"','"+properties+"','"+functions+"','"+usage+"','"+attentions+"','"+specifications+"','no',"+"'未设置',"+"'未设置' ,"+"'未设置' ,"+"'未设置' ,"+"'未设置' ,"+"'no'"+")");
		 } catch (SQLException e) {
			System.out.println("增添药品出错");
			e.printStackTrace();
		 	}
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 	}
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 	}
	 }
	//删除药品
		 public static void deleteDrug(String iduser,String number )
		 {
			 connect();
			 try 
			 {
				statement.executeUpdate("DELETE FROM "+"t"+iduser+" WHERE number='"+number+"'");
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 	}
			 try 
			 {
				statement.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 	}
			 try 
			 {
				connection.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 	}
		 }
	 //下面是设备端返回药品信息的方法
	 public static String getTable(String iduser)
	 {
		 connect();
		 ResultSet set=null;
		 drugTable=new String[100];
		 //下面从表中获取药品数据
		 try 
		 {
			 set=statement.executeQuery("SELECT ALL code,name,price,birthday,guaranteePeriod,tag,type,surplus,properties,functions,us,attentions,specifications,number FROM "+"t"+iduser);
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 //		 ********将每一条药物信息储存到数组中
		 try 
		 {
			
			 int i=0;
			while(set.next())
			 {
				 System.out.println(i);
				 String code=set.getString(1);
				 String name=set.getString(2);
				 String price=set.getString(3);
				 String birthday=set.getString(4);
				 String guaranteePeriod=set.getString(5);
				 String tag=set.getString(6);
				 String type=set.getString(7);
				 String surplus=set.getString(8);
				 String properties=set.getString(9);
				 String functions=set.getString(10);
				 String usage=set.getString(11);
				 String attentions=set.getString(12);
				 String specifications=set.getString(13);
				 String number=set.getString(14);
				 String drugMessage=code+"-"+name+"-"+type+"-"+tag+"-"+price+"-"+birthday+"-"+guaranteePeriod+"-"+surplus+"-"+properties+"-"+functions+"-"+usage+"-"+attentions+"-"+specifications+"-"+number+";";
				 System.out.println(drugMessage);
				 drugTable[i]=drugMessage;
				 i++;
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 String message=drugTable[0];//最终要返回的字符串
		 for(int i=1;i<drugTable.length;i++)
		 {
			 if(drugTable[i]!=null)
				 message+=drugTable[i];
		 }
		 System.out.println(message);
		 return message;
	 }
//下面是通过用户名获得对应设备号的方法
	 public static String getIduser(String name)
	 {
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 	set=statement.executeQuery("SELECT iduser FROM user WHERE name='"+name+"'");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 return set.getString(1);
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return "none";
	 }
	 //下面是手机端返回药品信息的方法
	 public static String getPTable(String iduser)
	 {
		 connect();
		 ResultSet set=null;
		 drugTable=new String[100];
		 //下面从表中获取药品数据
		 try 
		 {
			 set=statement.executeQuery("SELECT ALL code,name,price,birthday,guaranteePeriod,tag,type,surplus,properties,functions,us,attentions,specifications,number,eaten FROM "+"t"+iduser);
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 //		 ********将每一条药物信息储存到数组中
		 try 
		 {
			
			 int i=0;
			while(set.next())
			 {
				 System.out.println(i);
				 String code=set.getString(1);
				 String name=set.getString(2);
				 String price=set.getString(3);
				 String birthday=set.getString(4);
				 String guaranteePeriod=set.getString(5);
				 String tag=set.getString(6);
				 String type=set.getString(7);
				 String surplus=set.getString(8);
				 String properties=set.getString(9);
				 String functions=set.getString(10);
				 String usage=set.getString(11);
				 String attentions=set.getString(12);
				 String specifications=set.getString(13);
				 String eaten=set.getString(14);
				 String num=set.getString(15);
				 String drugMessage=code+"-"+name+"-"+type+"-"+tag+"-"+price+"-"+birthday+"-"+guaranteePeriod+"-"+surplus+"-"+properties+"-"+functions+"-"+usage+"-"+attentions+"-"+specifications+"-"+eaten+"-"+num+";";
				 System.out.println(drugMessage);
				 drugTable[i]=drugMessage;
				 i++;
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 String message=drugTable[0];//最终要返回的字符串
		 for(int i=1;i<drugTable.length;i++)
		 {
			 if(drugTable[i]!=null)
				 message+=drugTable[i];
		 }
		 System.out.println(message);
		 return message;
		 
	 }
	 //下面表示获得药品对应适应症的方法
	 public static String getTag(String iduser,String number)
	 {
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 	set=statement.executeQuery("SELECT tag FROM t"+iduser+" WHERE number='"+number+"'");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 return set.getString(1);
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return "none";
	 }
	 //下面表示获得用户是否吃药的方法
	 public static String getIsEaten(String iduser,String number)
	 {
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 	set=statement.executeQuery("SELECT isEaten FROM t"+iduser+" WHERE number='"+number+"'");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 return set.getString(1);
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return "no";
	 }
	 //下面表示修改设备号绑定的用户名密码的方法
	 public static void setName(String iduser,String name,String password)
	 {
		 connect();
		 try 
		 {
			statement.executeUpdate("UPDATE user SET name='"+name+"', password='"+password+"' WHERE iduser='"+iduser+"'");
		 } catch (SQLException e) {
			System.out.println("更新药品生产日期出错");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 
	 }
	 //下面是修改药品列表生产日期的方法
	 public static void setBirthday(String iduser,String number,String birthday)
	 {
		 connect();
		 
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET birthday='"+birthday+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("更新药品生产日期出错");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 
	 }
	 //下面是获得用药信息P用户名；drugEate;  
	 public static String getDrugEate(String iduser,String number)
	 {
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 	set=statement.executeQuery("SELECT drugHour,drugMinute,attentionTime,dosage FROM t"+iduser+" WHERE number='"+number+"'");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 String drugHour=set.getString(1);
				 String drugMinute=set.getString(2);
				 String attentionTime=set.getString(3);
				 String dosage=set.getString(4);
				 return drugHour+"-"+drugMinute+"-"+attentionTime+"-"+dosage+";";
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return "no";
	 }
	 //下面是修改药品列表保质期的方法
	 public static void setGuaranteePeriod(String iduser,String number,String guaranteePeriod)
	 {
		 connect();
		
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET guaranteePeriod='"+guaranteePeriod+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("更新药品保质期出错");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 
	 }
	 //下面是修改药品名名称的方法
	 public static void setDrugname(String iduser,String number,String name)

	 {
		 connect();
		
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET name='"+name+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("修改药品名失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 
	 }
	 //下面是药品绑定RFID的方法
	 public static void bindingRfid(String iduser,String number,String rfid)
	 {
		 connect();
		 
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET rfid='"+rfid+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("绑定RFID失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 //下面表示将药品设置药品有没有吃的方法
	 public static void setIseaten(String iduser,String rfid)
	 {
		 connect();
		
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET isEaten='"+"yes"+"' WHERE rfid='"+rfid+"'");
		 } catch (SQLException e) {
			System.out.println("设置药品已经吃了失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 //下面是修改药品drugHour的方法
	 public static void setDrugHour(String iduser,String number,String drugHour)
	 {
		 connect();
			
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET drugHour='"+drugHour+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("设置药品drugHour失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 //下面是修改药品drugMinute方法
	 public static void setDrugMinute(String iduser,String number,String drugMinute)
	 {
		 connect();
			
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET drugMinute='"+drugMinute+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("设置药品drugMinute失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 //下面是修改药品持续时间的方法
	 public static void setAttentionTime(String iduser,String number,String attentionTime)
	 {
		 connect();
			
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET attentionTime='"+attentionTime+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("设置药品attentionTime失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 //下面是修改药品吃药剂量的方法
	 public static void setDosage(String iduser,String number,String dosage)
	 {
		 connect();
			
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET dosage='"+dosage+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("设置药品dosage失败");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 }
	 //下面是修改药品列表剩余量的方法
	 public static void setSurplus(String iduser,String number,String surplus)
	 {
		 connect();
		
		 try 
		 {
			statement.executeUpdate("UPDATE "+"t"+iduser+" SET surplus='"+surplus+"' WHERE number='"+number+"'");
		 } catch (SQLException e) {
			System.out.println("更新药品剩余量出错");
			e.printStackTrace();
		 }
		 try 
		 {
			statement.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			connection.close();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	 
	 }
	//下面是修改药品列表eaten的方法
		 public static void setEaten(String iduser,String number,String eaten)
		 {
			 connect();
			 
			 try 
			 {
//				 System.out.println("UPDATE "+"t"+iduser+" SET eaten='"+eaten+"' WHERE number='"+number+"'");
				statement.executeUpdate("UPDATE "+"t"+iduser+" SET eaten='"+eaten+"' WHERE number='"+number+"'");
			 } catch (SQLException e) {
				System.out.println("更新eaten出错");
				e.printStackTrace();
			 }
			 try 
			 {
				statement.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 try 
			 {
				connection.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
		 
		 }
	 //下面是查看是否已存在设备号的方法
	 public static boolean haveIduser(String iduser)
	 {
		 boolean exist=false;
		 connect();
		 ResultSet set=null;
		 //下面从表中获取药品数据
		 try 
		 {
			 set=statement.executeQuery("SELECT iduser FROM user");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			 System.out.println("查询是否有设备号出错");
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 if(set.getString(1).equals(iduser))
					 exist=true;
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return exist;
	 }
	 //下面是判断适应症列表中是否已存在此适应症的方法
	 public static boolean haveTag(String iduser,String tag)
	 {
		 boolean exist=false;
		 connect();
		 ResultSet set=null;
		 //下面从表中获取适应症
		 try 
		 {
			 set=statement.executeQuery("SELECT tag FROM s"+iduser);
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			 System.out.println("查询是否适应症出错");
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 if(set.getString(1).equals(tag))
					 exist=true;
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return exist;
	 }
	 //下面表示获取某个适应症频率的方法
	 public static int getFrequency(String iduser,String tag)
	 {
		 	connect();
			ResultSet set=null;
			int number=1;
			try 
			{
				System.out.println("SELECT frequency "+"FROM s"+iduser+" where tag='"+tag+"'");
				set=statement.executeQuery("SELECT frequency FROM s"+iduser+" where tag ='"+tag+"'");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try 
			{
				while(set.next())
				{
					
					 number=Integer.parseInt(set.getString(1));
				}
			} catch (SQLException e) {
				System.out.println("获取频率出错");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 
			return number;
	 }
	 //下面表示设备端获取时间表
	 public static String getSchedule(String iduser)
	 {
		 connect();
		 ResultSet set=null;
		 drugTable=new String[100];
		 //下面从表中获取药品数据
		 try 
		 {
//			 System.out.println("SELECT drugHour,drugMinute,attentionTime,dosage FROM "+"t"+iduser+" WHERE eaten='yes'");
			 set=statement.executeQuery("SELECT drugHour,drugMinute,attentionTime,dosage,name FROM "+"t"+iduser+" WHERE eaten ='yes'"+"ORDER BY drugHour");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 //		 ********将每一条药物信息储存到数组中
		 try 
		 {
			
			 int i=0;
			while(set.next())
			 {
				
				 String drugHour=set.getString(1);
				 String drugMinute=set.getString(2);
				 String attentionTime=set.getString(3);
				 String dosage=set.getString(4);
				 String name=set.getString(5);
				
				
				 String drugMessage=drugHour+"-"+drugMinute+"-"+attentionTime+"-"+name+"-"+dosage+";";
				 System.out.println(drugMessage);
				 drugTable[i]=drugMessage;
				 i++;
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 String message=drugTable[0];//最终要返回的字符串
		 for(int i=1;i<drugTable.length;i++)
		 {
			 if(drugTable[i]!=null)
				 message+=drugTable[i];
		 }
		 System.out.println(message);
		 return message;
	 }
	 //下面是修改用户适应症列表的方法
	 public static void setTag(String iduser,String tag)
	 {
		 connect();
		 //下面表示如果这个适应症已经存在
		 if(Mysql.haveTag(iduser, tag))
		 {
			 int frequency=Mysql.getFrequency(iduser, tag);
			 frequency++;
			 try 
			 { 
				 System.out.println("UPDATE "+"s"+iduser+" SET frequency='"+frequency+"' where tag="+tag);
				statement.executeUpdate("UPDATE "+"s"+iduser+" SET frequency='"+frequency+"' where tag='"+tag+"'");
			 } catch (SQLException e) {
				System.out.println("更新适应症频率出错");
				e.printStackTrace();
			 }
			 try 
			 {
				statement.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 try 
			 {
				connection.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 
		 }
		 //下面表示适应症不存在
		 else
		 {
			 try 
			 {
				 
				statement.executeUpdate("INSERT INTO "+"s"+iduser+" VALUES ('"+tag+"','"+1+"','"+1+"')");
			 } catch (SQLException e) {
				System.out.println("增添适应症出错");
				e.printStackTrace();
			 	}
			 try 
			 {
				statement.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 	}
			 try 
			 {
				connection.close();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
		 }
	 }
	 //下面表示按频率排序获取tag
	 public static String[] getTAGbyFre(String user)
	 {
		 String[] s=new String[10];
		 String iduser=Mysql.getIduser(user);
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 set=statement.executeQuery("SELECT*FROM s"+iduser+" ORDER BY frequency LIMIT 10" );
			
		 } catch (SQLException e) {
			
			e.printStackTrace();
		 }
		 int i=0;
		 try 
		 {
			while(set.next())
			 {
				 s[i]=set.getString(1);
				 i++;
//				 System.out.println(s[i]);
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 String [] ss=new String[i];
		 for(int j=0;j<i;j++)
		 {
			 ss[j]=s[j];
//			 System.out.println(ss[j]);
		 }
		 return ss;
		 
	 }
	 //下面表示获取近7天的tag的方法
	 public static String[] getTAGbyTime(String user)
	 {
		 String[] s=new String[1000];
		 String iduser=Mysql.getIduser(user);
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 set=statement.executeQuery("select * from s"+iduser );
			
		 } catch (SQLException e) {
			
			e.printStackTrace();
		 }
		 int i=0;
		 try 
		 {
			while(set.next())
			 {
				 s[i]=set.getString(1);
				 i++;
//				 System.out.println(s[i]);
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 String [] ss=new String[i];
		 for(int j=0;j<i;j++)
		 {
			 ss[j]=s[j];
//			 System.out.println(ss[j]);
		 }
		 return ss;
		 
	 }
	 //下面表示返回某个tag的频率
	 public static int getFrebyTAG(String user,String tag)
	 {
		 String iduser=Mysql.getIduser(user);
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 	set=statement.executeQuery("SELECT frequency FROM s"+iduser+" where tag='"+tag+"'");
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 try 
		 {
			while(set.next())
			 {
				 return Integer.parseInt(set.getString(1));
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 return 0 ;
	 }
	 //下面表示获取某个tag下文章名的方法
	 public static String[] getTAGmassages(String tag)
	 {
		 connect();
		 String[] ss=new String[100];
		 ResultSet set=null;
		 String checkTable="show tables like '"+tag+"'"; 
		 try 
		 {
			set= connection.getMetaData().getTables(null, null,tag, null );
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 } 
		 int i=0;
		 try {
			if (set.next()) 
			 {  
				 set=statement.executeQuery("SELECT * FROM `"+tag+"`");
				 if(set.next())
				 {
					 ss[i]=set.getString(1);
					 i++;
				 }
			     
			 }
			 else
			 {  
			      String[] s=new String[0];
			      return s;
			 
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }  
		 String[] s=new String[i];
		 for(int j=0;j<i;j++)
		 {
			 s[j]=ss[j];
		 }
		 return s;
	 }
	 //下面是获取某个文章详细内容的方法
	 public static String getMassageDetail(String name)
	 {
		 connect();
		 ResultSet set=null;
		 try 
		 {
			 set=statement.executeQuery("select articleName,introduce,contents,picture from article where articleName='"+name+"'" );
			
		 } catch (SQLException e) {
			
			e.printStackTrace();
		 }
		String message="";
		 try 
		 {
			while(set.next())
			 {
				 message=set.getString(1)+"-"+set.getString(2)+"-"+set.getString(3)+"-"+set.getString(4)+";";
//				 System.out.println(s[i]);
			 }
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
	
		return message;
	 }
	 
	 public static void main(String args[])
	 {
//		 System.out.println(Mysql.getNumber("001"));
//		 Mysql.deleteDrug("001", "1");
//		 System.out.println(Mysql.getNumber("001"));
		 //********************
//		 Mysql.setBirthday("001", "4", "151203");
//		 Mysql.setGuaranteePeriod("001", "4", "12");
//		 Mysql.setSurplus("001", "4", "6");
		 //**************************
//		 Mysql.setEaten("001", "1", "yes");
		 	Mysql.setIseaten("001", "11111");
//	Mysql.bindingRfid("001", "1", "11111");
		 
		 //************************
//		 Mysql.setDrugMinute("001", "8", "30");
//		 Mysql.setDosage("001", "8", "2");
//		 Mysql.setAttentionTime("001", "8", "20");
//		 Mysql.setDrugHour("001", "8", "20");
		 //****************************
//		 Mysql.getSchedule("001");
//		Mysql.setSurplus("li", "122121","0");
//		Mysql.getPTable("001");
//		try
//		{
//			Mysql.addDrug("33", "122121", "藿香","12.0","121123","12个月","主治感冒","中成药","19");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 System.out.println(Mysql.haveIduser("1"));
//		 Mysql.register("33", "asda", "gdfg");
		 
//		Mysql.setBirthday("001", "3", "123456");
//		for(int i=0;i<Mysql.getTAGmassages("all").length;i++)
//		{		
//			System.out.println(Mysql.getTAGmassages("all")[i]);
//		}
//		Mysql.setTag("001", "腹痛腹泻");
//		 System.out.println(Mysql.getMassageDetail("呼吸道炎症的治疗"));
	 }
	
}
	