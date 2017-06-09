package com.pcms.view;
import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.*;
import java.util.*;
class Time{
	 private int x,y;/*ÿ���ӱ������*/
	 private String place,time_digital;/*ÿ���ӱ������*/
	 private int timezone;/*ÿ���ӱ��ʱ��,��x��Ϊ+x,��x��Ϊ-x*/
	 private Date d;
	 private long time;
	 private double hour,minite,second;
	 private Weather weather;
	 Time(int x,int y,String place,int timezone){
	  this.x=x;
	  this.y=y;
	  this.place=place;
	  this.timezone=timezone;
	 }
	 
	 
	 
	 public void draw(Graphics g) {
	  g.setColor(Color.black);
	  d=new Date();
	  time=d.getTime();/*���0ʱ��1970��1��1��0�㵽���ڵĺ�����*/
	  hour=(((time/1000)+3600*timezone)%43200)*2*Math.PI/3600/12;/*����ʱ�뻡��*/
	  minite=(time/1000)%3600*2*Math.PI/3600;/*������뻡��*/
	  second=(time/1000)%60*2*Math.PI/60;/*�������뻡��*/

	  time_digital=String.valueOf((int)(((time/1000)+3600*timezone)%86400/3600))+":"+String.valueOf((int)((time/1000)%3600/60))+":"+String.valueOf((int)((time/1000)%60));
	  /*�����ӱ�������ʱ��*/
	  ((Graphics2D)g).setStroke(new BasicStroke(2.0f));
	  ((Graphics2D)g).drawOval(x, y, 80, 80);
	  ((Graphics2D)g).drawLine(x+40, y, x+40, y+4);
	  ((Graphics2D)g).drawLine(x+40, y+80, x+40, y+76);
	  ((Graphics2D)g).drawLine(x, y+40, x+4, y+40);
	  ((Graphics2D)g).drawLine(x+80,y+40, x+76, y+40);
	  ((Graphics2D)g).drawLine(x+40,y+40,(int)(x+40+23*Math.sin(hour)),(int)(y+40-23*Math.cos(hour)));
	  /*��������*/
	  ((Graphics2D)g).setStroke(new BasicStroke(1.5f));
	  ((Graphics2D)g).drawLine(x+40,y+40,(int)(x+40+32*Math.sin(minite)),(int)(y+40-32*Math.cos(minite)));
	  /*��������*/
	  ((Graphics2D)g).setStroke(new BasicStroke(1.0f));
	  ((Graphics2D)g).drawLine(x+40,y+40,(int)(x+40+36*Math.sin(second)),(int)(y+40-36*Math.cos(second)));
	  /*�����ӱ�����*/
	  g.setColor(Color.black);
	  g.drawString(place, x+20, y+100);
	  g.setColor(Color.black);
	  g.drawString(time_digital,x+20,y+115);
	
//	  g.setColor(Color.black);
//	  g.drawString(weather.Weatherinfo, x+100, y+30);
	 }
	}
