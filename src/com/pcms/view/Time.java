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
	 private int x,y;/*每个钟表的坐标*/
	 private String place,time_digital;/*每个钟表的名字*/
	 private int timezone;/*每个钟表的时区,东x区为+x,西x区为-x*/
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
	  time=d.getTime();/*获得0时区1970年1月1日0点到现在的毫秒数*/
	  hour=(((time/1000)+3600*timezone)%43200)*2*Math.PI/3600/12;/*计算时针弧度*/
	  minite=(time/1000)%3600*2*Math.PI/3600;/*计算分针弧度*/
	  second=(time/1000)%60*2*Math.PI/60;/*计算秒针弧度*/

	  time_digital=String.valueOf((int)(((time/1000)+3600*timezone)%86400/3600))+":"+String.valueOf((int)((time/1000)%3600/60))+":"+String.valueOf((int)((time/1000)%60));
	  /*画出钟表轮廓和时针*/
	  ((Graphics2D)g).setStroke(new BasicStroke(2.0f));
	  ((Graphics2D)g).drawOval(x, y, 80, 80);
	  ((Graphics2D)g).drawLine(x+40, y, x+40, y+4);
	  ((Graphics2D)g).drawLine(x+40, y+80, x+40, y+76);
	  ((Graphics2D)g).drawLine(x, y+40, x+4, y+40);
	  ((Graphics2D)g).drawLine(x+80,y+40, x+76, y+40);
	  ((Graphics2D)g).drawLine(x+40,y+40,(int)(x+40+23*Math.sin(hour)),(int)(y+40-23*Math.cos(hour)));
	  /*画出分针*/
	  ((Graphics2D)g).setStroke(new BasicStroke(1.5f));
	  ((Graphics2D)g).drawLine(x+40,y+40,(int)(x+40+32*Math.sin(minite)),(int)(y+40-32*Math.cos(minite)));
	  /*画出秒针*/
	  ((Graphics2D)g).setStroke(new BasicStroke(1.0f));
	  ((Graphics2D)g).drawLine(x+40,y+40,(int)(x+40+36*Math.sin(second)),(int)(y+40-36*Math.cos(second)));
	  /*画出钟表名字*/
	  g.setColor(Color.black);
	  g.drawString(place, x+20, y+100);
	  g.setColor(Color.black);
	  g.drawString(time_digital,x+20,y+115);
	
//	  g.setColor(Color.black);
//	  g.drawString(weather.Weatherinfo, x+100, y+30);
	 }
	}
