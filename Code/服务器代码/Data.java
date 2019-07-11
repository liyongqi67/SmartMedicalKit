package drugs;

public class Data 
{
	static String[] s;
	//分解形状字符串
	static String[] explodeTag(String tag)
	{

		tag=tag+",";
		char c = ',';
		int num = 0;
		char[] chars = tag.toCharArray();
		for(int i = 0; i < chars.length; i++)
		{
			if(c == chars[i])
		    {
				num++;
		    }
		}
		System.out.println("分解后适应症的个数为"+num);
		s=new String[num];
		for(int i=0;i<s.length;i++)
		{
			s[i]=tag.substring(0, tag.indexOf(','));
			tag=tag.substring((s[i].length()+1));
		}
		return s;
	}
	public static void main(String args[])
	{
		Data.explodeTag("腹痛腹泻,过敏,其他,小儿泄泻用药,消化不良,其他药品,食欲不振");
		for(int i=0;i<s.length;i++)
		{
			System.out.println(s[i]);
		}
	}
		
		
}
