package com.pcms.view;

import java.awt.Color;
import java.awt.Graphics;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.swing.JPanel;

class TimerPanel extends JPanel implements Runnable{
	Time[] t=new Time[6];
//	String weatherinfo;
	 TimerPanel(int w,int h){
	  t[0]=new Time(w/4-40,h/6-40,"Beijing",8);
	  t[1]=new Time(3*(w/4)-40,h/6-40,"Paris",1);
	  t[2]=new Time(w/4-40,3*h/6-40,"WashingtonDC",8);
	  t[3]=new Time(3*(w/4)-40,3*h/6-40,"Berlin",1);
	  t[4]=new Time(w/4-40,5*h/6-40,"London",0);
	  t[5]=new Time(3*(w/4)-40,5*h/6-40,"Mowscow",3);
	  setBackground(Color.lightGray);
//	  weatherinfo=Weather.GetWeater("±±¾©").toString();
	 }
	 public void paint(Graphics g){
	  super.paint(g);
	  for(int i=0;i<t.length;i++)
	   t[i].draw(g);
	 }
	 public void run() {
	  while(true){
	   try {
	    Thread.sleep(1000);
	   } catch (InterruptedException e) {
	    e.printStackTrace();
	   }
	   this.repaint();
	  }
	 }
}
