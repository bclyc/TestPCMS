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
    //��ȡ������Ϣ
  public static String GetWeater(String city) {
    Weather wu=new Weather();
    String buffstr=null;
    try {
        String xml= wu.GetXmlCode(URLEncoder.encode(city, "utf-8"));  //����������еı��룬������ٶ�����api��Ҫ
        buffstr=wu.readStringXml(xml,city);//����xml��������
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
    // ��������  
    URL url = new URL(requestUrl);
    HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();  
    httpUrlConn.setDoInput(true);  
    httpUrlConn.setRequestMethod("GET");  
    // ��ȡ������  
    InputStream inputStream = httpUrlConn.getInputStream();  
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
    // ��ȡ���ؽ��  
    buffer = new StringBuffer();  
    String str = null;  
    while ((str = bufferedReader.readLine()) != null)
{  
      buffer.append(str);  
    }  
  
    // �ͷ���Դ  
    bufferedReader.close();  
    inputStreamReader.close();  
    inputStream.close();  
    httpUrlConn.disconnect();  
    } catch (Exception e) {  
    e.printStackTrace(); 
    return "no connection(no access to Internet)    ";
    }  
    return buffer.toString();  //���ػ�ȡ��xml�ַ���
}
public String readStringXml(String xml,String ifcity) {
  StringBuffer buff=new StringBuffer();  //����ƴ��������Ϣ��
  Document doc = null;
  List listdate=null;  //�����������
  List listday=null;  //������Ű���ͼƬ·����Ϣ
  List listweather=null;
  List listwind=null;
  List listtem=null;
  try {
    // ��ȡ������XML�ĵ�
    //�������ͨ������xml�ַ�����
    doc = DocumentHelper.parseText(xml); // ���ַ���תΪXML  
    Element rootElt = doc.getRootElement(); // ��ȡ���ڵ�    
    Iterator iter = rootElt.elementIterator("results"); //��ȡ���ڵ��µ��ӽڵ�results
    String status=rootElt.elementText("status"); //��ȡ״̬���������success,��ʾ������
    if(!status.equals("success"))
      return "��������";  //������������ݣ�ֱ�ӷ���
    String date= rootElt.elementText("date");  //��ȡ���ڵ��µģ���������
    buff.append(date+"\n");
    //����results�ڵ�
      while (iter.hasNext()) {
      Element recordEle = (Element) iter.next();
      Iterator iters = recordEle.elementIterator("weather_data"); //
    //����results�ڵ��µ�weather_data�ڵ�
      while (iters.hasNext()) {
        Element itemEle = (Element) iters.next();  
        listdate=itemEle.elements("date");
      //��date���Ϸŵ�listdate��
        listday=itemEle.elements("dayPictureUrl");
        listweather=itemEle.elements("weather");
        listwind=itemEle.elements("wind");
        listtem=itemEle.elements("temperature");
    }
    for(int i=0;i<1;i++){  //����ÿһ��list.size����ȣ�����ͳһ����
      Element eledate=(Element)listdate.get(i); //����ȡ��date
      Element eleday=(Element)listday.get(i);//..
      Element eleweather=(Element)listweather.get(i);
      Element elewind=(Element)listwind.get(i);
      Element eletem=(Element)listtem.get(i);            
      buff.append(eledate.getText()+"\n"+eleweather.getText()+"-"+elewind.getText()+"-"+eletem.getText()+"\n");  //ƴ����Ϣ
        //*****************������õ�΢�Ź��ں��ϣ������Լ�����д���룬��ֻ�ܰﵽ���ˣ������Ѿ����뿪�ˡ�
      //΢����������  ʡ��
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
	String sub1=result.substring(0,result.indexOf("��")+2);
	String sub2=result.substring(result.indexOf("("), result.length()-1);
	result=sub1+sub2;
	result=result.replace("��һ", " Monday");
	result=result.replaceAll("�ܶ�", " Tuesday");
	result=result.replaceAll("����", " Wednesday");
	result=result.replaceAll("����", " Thursday");
	result=result.replaceAll("����", " Friday");
	result=result.replaceAll("����", " Saturday");
	result=result.replace("����", " Sunday");
//	result=result.replaceAll("��", " Month ");
	result=result.replaceAll("ʵʱ", "Now");
	result=result.replaceAll("��", "Overcast");
	result=result.replaceAll("��", "Sunny");
	result=result.replaceAll("����", "Cloudy");
	result=result.replaceAll("����", "North Wind:");
	result=result.replaceAll("�Ϸ�", "South Wind:");
	result=result.replaceAll("����", "East Wind:");
	result=result.replaceAll("����", "West Wind:");
	result=result.replaceAll("���Ϸ�","Southwest Wind:");
	result=result.replaceAll("���Ϸ�", "Southeast Wind:");
	result=result.replaceAll("������", "Northwest Wind:");
	result=result.replaceAll("������", "Northeast Wind:");
	result=result.replaceAll("��", " Degree");
	return result;
}
public static void main(String[] args){
//����


	System.out.println(Weather.translate(Weather.GetWeater("����")));
}
}