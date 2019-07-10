package drugs;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
//
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import java.text.SimpleDateFormat;
import java.util.Date;
//48ee8200edecd4cc7e76
//此类用来调用药品api并返回所需信息
public class UseApi 
{
	static String name=null;//名称
	static String price=null;//价格
	static String tag=null;//适应症
	static String type=null;//类型
	static String properties=null;//性状
	static String functions=null;//功能主治
	static String usage = null;//用法用量
	static String attentions = null;//注意事项
	static String specifications = null;//规格
	

	public static String send(String code) throws Exception 
	{
		String result =null;
		//下面通过API获取药品信息
		try 
		{
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod("http://api.avatardata.cn/Drug/Code"); 
			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
			NameValuePair[] data ={new NameValuePair("Key", "b327038fa63c4dd6a98173d7bd9d7452"),new NameValuePair("code",code),new NameValuePair("format","true")};
			post.setRequestBody(data);

			client.executeMethod(post);
			Header[] headers = post.getResponseHeaders();
			int statusCode = post.getStatusCode();
			
			
			result = new String(post.getResponseBodyAsString().getBytes("gbk")); 
			System.out.println(result);

			post.releaseConnection();
			
			

		} catch (HttpException e) {
   			e.printStackTrace();		} catch (IOException e) {
			e.printStackTrace();
		}
	//下面对返回的信息进行处理，获得有利信息
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));//将字符串转化为源进行流读取
		String line;  
		for(int i=0;i<18;i++)
		{
			line = br.readLine();
			//获取药品名
			if(i == 10){
				
				String metaproperties = null;
				String metaproperties2 = null;
				String properties = null;
				//获取性状
				if(line.indexOf("【性状")>0){
					metaproperties = line.substring(line.indexOf("【性状")+5,line.length());
					metaproperties2 = metaproperties.substring(0, metaproperties.indexOf("【"));
					properties = metaproperties2.replaceAll("((\r\n)|\n|\\\\|n|\\s|<p>|</p>|&nbsp)", "");
					properties = properties.replaceAll(",|;", "，");
					properties = properties.replaceAll("-", "~");
					if(properties.indexOf("展开")>0){
						String temp = properties;
						properties = temp.substring(0,temp.indexOf("展开")-1);
					}
					UseApi.properties = properties;
				}
				
				//获取功能主治
				if(line.indexOf("【功能主治")>0){
					metaproperties = line.substring(line.indexOf("【功能主治")+7,line.length());
					metaproperties2 = metaproperties.substring(0, metaproperties.indexOf("【"));
					properties = metaproperties2.replaceAll("((\r\n)|\n|\\\\|n|\\s|<p>|</p>|&nbsp)", "");
					properties = properties.replaceAll(",|;", "，");
					properties = properties.replaceAll("-", "~");
					if(properties.indexOf("展开")>0){
						String temp = properties;
						properties = temp.substring(0,temp.indexOf("展开")-1);
					}
					UseApi.functions = properties;
				}
				
				//获取用法用量
				if(line.indexOf("【用法用量")>0){
					metaproperties = line.substring(line.indexOf("【用法用量")+7,line.length());
					metaproperties2 = metaproperties.substring(0, metaproperties.indexOf("【"));
					properties = metaproperties2.replaceAll("((\r\n)|\n|\\\\|n|\\s|<p>|</p>|&nbsp)", "");
					properties = properties.replaceAll(",|;", "，");
					properties = properties.replaceAll("-", "~");
					if(properties.indexOf("展开")>0){
						String temp = properties;
						properties = temp.substring(0,temp.indexOf("展开")-1);
					}
					UseApi.usage = properties;
				}
				
				//获取注意事项
				if(line.indexOf("【注意事项")>0){
					metaproperties = line.substring(line.indexOf("【注意事项")+7,line.length());
					metaproperties2 = metaproperties.substring(0, metaproperties.indexOf("【"));
					properties = metaproperties2.replaceAll("((\r\n)|\n|\\\\|n|\\s|<p>|</p>|&nbsp)", "");
					properties = properties.replaceAll(",|;", "，");
					properties = properties.replaceAll("-", "~");
					if(properties.indexOf("展开")>0){
						String temp = properties;
						properties = temp.substring(0,temp.indexOf("展开")-1);
					}
					UseApi.attentions = properties;
				}
				
				//获取规格
				if(line.indexOf("【规格")>0){
					metaproperties = line.substring(line.indexOf("【规格")+5,line.length());
					metaproperties2 = metaproperties.substring(0, metaproperties.indexOf("【"));
					properties = metaproperties2.replaceAll("((\r\n)|\n|\\\\|n|\\s|<p>|</p>|&nbsp)", "");
					properties = properties.replaceAll(",|;", "，");
					properties = properties.replaceAll("-", "~");
					if(properties.indexOf("展开")>0){
						String temp = properties;
						properties = temp.substring(0,temp.indexOf("展开")-1);
					}
					UseApi.specifications = properties;
				}
				
			}
			if(i==11)
			{
				String s=line.substring(0, line.indexOf(':'));
				line=line.substring((s.length()+3));
				name=line.substring(0, line.indexOf('"'));							
			}
			else if(i==12)//获取价格
			{
				String s=line.substring(0, line.indexOf(':'));
				line=line.substring((s.length()+2));
				price=line.substring(0,line.lastIndexOf(','));			
			}
			else if(i==13)
			{
				String s=line.substring(0, line.indexOf(':'));
				line=line.substring((s.length()+3));
				tag=line.substring(0, line.indexOf('"'));		
			}
			else if(i==14)
			{
				String s=line.substring(0, line.indexOf(':'));
				line=line.substring((s.length()+3));
				type=line.substring(0, line.indexOf('"'));
				//System.out.println(type);
			}

		}
		
		
	
		return null;
	}
	public static void main(String args[])
	{
		try {
			send("6927043102543");
			System.out.println(UseApi.attentions);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}


