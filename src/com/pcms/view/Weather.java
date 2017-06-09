package com.pcms.view;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
public class Weather
{
    //获取天气信息
  public static String GetWeater(String city) {
    Weather wu=new Weather();
    String buffstr=null;
    try {
        String xml= wu.GetXmlCode(URLEncoder.encode(city, "utf-8"));  //设置输入城市的编码，以满足百度天气api需要
        buffstr=wu.readStringXml(xml,city);//调用xml解析函数
    } catch (Exception e) {
    e.printStackTrace();
    return "no connection(no access to Internet)    ";
    }
    return  buffstr;
    }
    public String GetXmlCode(String city) throws UnsupportedEncodingException{
    String requestUrl = "http://api.map.baidu.com/telematics/v3/weather?location="+city+"&output=xml&ak=A72e372de05e63c8740b2622d0ed8ab1";  
    StringBuffer buffer = null;  
    try {  
    // 建立连接  
    URL url = new URL(requestUrl);
    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
    httpUrlConn.setDoInput(true);  
    httpUrlConn.setRequestMethod("GET");  
    // 获取输入流  
    InputStream inputStream = httpUrlConn.getInputStream();  
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
    // 读取返回结果  
    buffer = new StringBuffer();  
    String str = null;  
    while ((str = bufferedReader.readLine()) != null)
{  
      buffer.append(str);  
    }  
  
    // 释放资源  
    bufferedReader.close();  
    inputStreamReader.close();  
    inputStream.close();  
    httpUrlConn.disconnect();  
    } catch (Exception e) {  
    e.printStackTrace(); 
    return "no connection(no access to Internet)    ";
    }  
    return buffer.toString();  //返回获取的xml字符串
}
public String readStringXml(String xml,String ifcity) {
  StringBuffer buff=new StringBuffer();  //用来拼接天气信息的
  Document doc = null;
  List listdate=null;  //用来存放日期
  List listday=null;  //用来存放白天图片路径信息
  List listweather=null;
  List listwind=null;
  List listtem=null;
  try {
    // 读取并解析XML文档
    //下面的是通过解析xml字符串的
    doc = DocumentHelper.parseText(xml); // 将字符串转为XML  
    Element rootElt = doc.getRootElement(); // 获取根节点    
    Iterator iter = rootElt.elementIterator("results"); //获取根节点下的子节点results
    String status=rootElt.elementText("status"); //获取状态，如果等于success,表示有数据
    if(!status.equals("success"))
      return "暂无数据";  //如果不存在数据，直接返回
    String date= rootElt.elementText("date");  //获取根节点下的，当天日期
    buff.append(date+"\n");
    //遍历results节点
      while (iter.hasNext()) {
      Element recordEle = (Element) iter.next();
      Iterator iters = recordEle.elementIterator("weather_data"); //
    //遍历results节点下的weather_data节点
      while (iters.hasNext()) {
        Element itemEle = (Element) iters.next();  
        listdate=itemEle.elements("date");
      //将date集合放到listdate中
        listday=itemEle.elements("dayPictureUrl");
        listweather=itemEle.elements("weather");
        listwind=itemEle.elements("wind");
        listtem=itemEle.elements("temperature");
    }
    for(int i=0;i<1;i++){  //由于每一个list.size都相等，这里统一处理
      Element eledate=(Element)listdate.get(i); //依次取出date
      Element eleday=(Element)listday.get(i);//..
      Element eleweather=(Element)listweather.get(i);
      Element elewind=(Element)listwind.get(i);
      Element eletem=(Element)listtem.get(i);            
      buff.append(eledate.getText()+"\n"+eleweather.getText()+"-"+elewind.getText()+"-"+eletem.getText()+"\n");  //拼接信息
        //*****************如果想用到微信公众号上，还请自己继续写代码，我只能帮到这了，数据已经分离开了。
      //微信天气处理  省略
    }  
    }
    } catch (DocumentException e) {
    e.printStackTrace();
    return "no connection(no access to Internet)    ";
    } catch (Exception e) {
    e.printStackTrace();
    return "no connection(no access to Internet)    ";
    }
 
    return buff.toString();  
}
public static String translate(String Weather){
	String result=new String(Weather);
	String sub1=result.substring(0,result.indexOf("周")+2);
	String sub2=result.substring(result.indexOf("("), result.length()-1);
	result=sub1+sub2;
	result=result.replace("周一", " Monday");
	result=result.replaceAll("周二", " Tuesday");
	result=result.replaceAll("周三", " Wednesday");
	result=result.replaceAll("周四", " Thursday");
	result=result.replaceAll("周五", " Friday");
	result=result.replaceAll("周六", " Saturday");
	result=result.replace("周日", " Sunday");
//	result=result.replaceAll("月", " Month ");
	result=result.replaceAll("实时", "Now");
	result=result.replaceAll("阴", "Overcast");
	result=result.replaceAll("晴", "Sunny");
	result=result.replaceAll("多云", "Cloudy");
	result=result.replaceAll("北风", "North Wind:");
	result=result.replaceAll("南风", "South Wind:");
	result=result.replaceAll("东风", "East Wind:");
	result=result.replaceAll("西风", "West Wind:");
	result=result.replaceAll("西南风","Southwest Wind:");
	result=result.replaceAll("东南风", "Southeast Wind:");
	result=result.replaceAll("西北风", "Northwest Wind:");
	result=result.replaceAll("东北风", "Northeast Wind:");
	result=result.replaceAll("级", " Degree");
	return result;
}
public static void main(String[] args){
//测试


	System.out.println(Weather.translate(Weather.GetWeater("北京")));
}
}