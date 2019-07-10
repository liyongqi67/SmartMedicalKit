package drugs;

import drugs.Mysql;

public class PushMassage 
{

	public static String findMassages(String user)
	{
		StringBuffer pushMassage = new StringBuffer("");

		//获取用户按照频率、时间的tag排序
		String[] tagsByFre = Mysql.getTAGbyFre(user);
		String[] tagsByTime = Mysql.getTAGbyTime(user);
		//若标签长度为零
		if(tagsByFre.length == 0){
			String[] massages = Mysql.getTAGmassages("all");
			for(int i = 0;i<massages.length;i++){
				pushMassage.append(Mysql.getMassageDetail(massages[i]));
			}
			return pushMassage.toString();
		}

		//近七天的标签进行封装和排序
		TAG[] tags = new TAG[tagsByTime.length];
		for(int i = 0;i<tags.length;i++){
			tags[i] = new TAG(tagsByTime[i], (tagsByTime.length-i)/tagsByTime.length*0.5*Mysql.getFrebyTAG(user, tagsByTime[i])+tagsByTime.length-i);
		}
	    TAG temp;

	    for(int i=0;i<tags.length;i++){//趟数

	      for(int j=0;j<tags.length-i-1;j++){//比较次数

	        if(tags[j].weight>tags[j+1].weight){

	          temp=tags[j];

	          tags[j]=tags[j+1];

	          tags[j+1]=temp;

	        }

	      }

	    }
	    
	    String[] massages;
	    for(int i = 0;i<tags.length;i++){
	    	massages = Mysql.getTAGmassages(tags[i].tag);
	    	for(int j = 0;j<massages.length;j++){
	    		pushMassage.append(Mysql.getMassageDetail(massages[j]));
	    	}
	    }
	    
	    massages = Mysql.getTAGmassages("all");
		for(int i = 0;i<massages.length;i++){
			pushMassage.append(Mysql.getMassageDetail(massages[i]));
		}
		return pushMassage.toString();

		
	}
	
	private static class TAG
	{
		String tag;
		double weight;
		
		public TAG(String tag, double weight) {
			this.tag = tag;
			this.weight = weight;
		}
	}
	public static String[] handleTags(String tag){
		String[] tags = Divider.transJe(tag, "gb2312", "utf-8").split(",");

		for(int i = 0; i < tags.length; i++){
			if(tags[i].matches("其他|疼痛|药品")){
				tags[i] = "none";
			}
			else if(tags[i].equals("肌肉")){
				tags[i] = "扭伤";
			}
			else if(tags.equals("发热")){
				tags[i] = "发烧";
			}
			else if(tags[i].matches(".*咽炎.*")){
				tags[i] = "咽炎";
			}
			else if(tags[i].matches(".*风湿.*")){
				tags[i] = "风湿";
			}
			
		}
		
		//获得非none长度
		int size = 0;
		for(int i = 0;i < tags.length;i++){
			if(!tags[i].equals("none")){
				size++;
			}
		}
		
		//复制非none数组
		String[] result = new String[size];
		int j = 0;
		for(int i = 0; i<tags.length;i++){
			if(!tags[i].equals("none")){
				result[j] = tags[i];
				j++;
			}
		}

		
		return result;


	}

	public static void main(String[]args)
	{
		System.out.println(PushMassage.findMassages("li"));
		
	}
}
