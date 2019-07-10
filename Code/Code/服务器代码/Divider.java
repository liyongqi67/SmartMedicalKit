package drugs;

import java.io.Reader;
import java.io.StringReader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.mira.lucene.analysis.MIK_CAnalyzer;
 
public class Divider {
 
    public static void testStandard(String testString) {
        try {
            Analyzer analyzer = new StandardAnalyzer();
            Reader r = new StringReader(testString);
            StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
            System.err.println("=====standard analyzer====");
            Token t;
            while ((t = sf.next()) != null) {
                System.out.println(t.termText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void testCJK(String testString) {
        try {
            Analyzer analyzer = new CJKAnalyzer();
            Reader r = new StringReader(testString);
            StopFilter sf = (StopFilter) analyzer.tokenStream("", r);
            System.err.println("=====cjk analyzer====");
            Token t;
            while ((t = sf.next()) != null) {
                System.out.println(t.termText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void testChiniese(String testString) {
        try {
            Analyzer analyzer = new ChineseAnalyzer();
            Reader r = new StringReader(testString);
            TokenFilter tf = (TokenFilter) analyzer.tokenStream("", r);
            System.err.println("=====chinese analyzer====");
            Token t;
            while ((t = tf.next()) != null) {
                System.out.println(t.termText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static String transJe(String testString, String c1, String c2) {
        String result = "";
        try {
            Analyzer analyzer = new MIK_CAnalyzer();
            Reader r = new StringReader(testString);
            TokenStream ts = (TokenStream) analyzer.tokenStream("", r);
            Token t;
            while ((t = ts.next()) != null) {
                result += t.termText() + ",";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
 
    public static void main(String[] args) {
        try {
            String testString = "中文分词的方法其实不局限于中文应用，也被应用到英文处理，如手写识别，单词之间的空格就很清楚，中文分词方法可以帮助判别英文单词的边界";
            System.out.println("测试的语句    "+testString);
            String sResult[] = transJe(testString, "gb2312", "utf-8").split(",");
            for (int i = 0; i < sResult.length; i++) {
                System.out.println(sResult[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

