package com.pcms.excel;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import jxl.*;
import jxl.write.*;
import jxl.write.Number;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import com.pcms.db.SqlHelper;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Boolean;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class DownExcel {
	String empName, projectId;
	OutputStream cs=null;
	WritableWorkbook drk=null;
	int number=0;
	String []refM={"January","February","March","April","May","June","July","August","September"
			,"October","November","December"};
	
	public DownExcel(String empName,String []months,String filepath,String projectId){
		this.empName=empName;
		this.projectId=projectId;
		int startM = 0,startY=Integer.parseInt(months[1]),endM = 0,endY=Integer.parseInt(months[3]);
		int cycle=endY-startY;//times of cycle
		try {
			cs= new FileOutputStream(filepath);
			drk= Workbook.createWorkbook(cs);			
		
			//[2]鍒涘缓5鏈堝埌12鏈堜互鍙婂勾缁堟�荤粨summary鐨凷heet琛ㄦ牸
			//鍦╠rk椤圭洰涓嬮潰鍒涘缓sheet
			for(int i=0;i<refM.length;i++){
				if(months[0].equals(refM[i])){
					startM=i;
				}
				if(months[2].equals(refM[i])){
					endM=i;
				}
			}
		
			while(cycle!=0){
				while(startM<12){
					int year=endY-cycle;
					this.buildSheet(startM,year,drk);
					startM++;
				}
				startM=0;
				cycle--;
			}
			while(startM<=endM){
				this.buildSheet(startM,endY,drk);
				startM++;
			}	
			
			drk.write();
			
			drk.close();
			
			cs.close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private static WritableCell Label(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void buildSheet(int month,int year,WritableWorkbook drk){
		String []team={"BT----01","CC----01","CC----02","CI----01","EV----01","IC----01","IG----01",
				"IT----01","JV----01","MGT----01","PA----01","TI----01","TU----01","TU----02",
				"TU----03","TU----04","TO----01","NR----01","OB----01"};
		int days=0;
		String sheetName=refM[month]+" "+year;
		//4.2 璁剧疆瀛椾綋绉嶇被鍜岄粦浣撴樉绀�
		//璁剧疆琛岄珮
		try {
			
			//[1] 鍒涘缓Excel鏂囦欢
			//鏂板缓Excel琛ㄦ牸锛屼唬鍙封�渃s鈥�(Contribution System),鍏蜂綋椤圭洰鍚嶇О涓篸rk
			//"/Users/rumeili/Documents/workspace/娴嬭瘯.xls"
			
			WritableSheet sheet = drk.createSheet(sheetName, number++);
			
			sheet.setRowView(1, 1350, false);
	        sheet.setRowView(2, 360, false);
	        sheet.setRowView(3, 524, false);
	        sheet.setRowView(4, 350, false);
	        sheet.setRowView(5, 540, false);
	        sheet.setRowView(6, 350, false);
	        
	        for(int i=7;i<26;i++){
	        	 sheet.setRowView(i, 290, false);
	        }
	     
	        sheet.setRowView(26, 350, false);
	        sheet.setRowView(27, 760, false);
	        sheet.setRowView(28, 461, false);
	        sheet.setRowView(29, 760, false);
	        sheet.setRowView(30, 470, false);
	        sheet.setRowView(31, 760, false);      
	        
	        //璁剧疆鍒楀
	        sheet.setColumnView(1, 19);
	        sheet.setColumnView(2, 14);
	        sheet.setColumnView(3, 11);
	        sheet.setColumnView(4, 72);	 
	        for(int i=5;i<36;i++){
	        	sheet.setColumnView(i, 4);
	        }
	        sheet.setColumnView(36, 6);
			//璁剧疆涓嬪垝绾�
	        //WritableFont underline= new WritableFont(WritableFont.ARIAL,WritableFont.DEFAULT_POINT_SIZE,WritableFont.NO_BOLD,false,UnderlineStyle.SINGLE);
	        //WritableCellFormat greyBackground = new WritableCellFormat(underline);
	        //greyBackground.setBackground(Colour.GRAY_25);//璁剧疆鑳屾櫙棰滆壊涓虹伆鑹�
	        //瀹炰緥Number number = new Number(1,2,3.1415926535,greyBackground);
			
			//瀛椾綋涓篢imes New Rome,瀛楀彿澶у皬涓�10鍜�15涓ょ,閲囩敤榛戜綋鏄剧ず			
	        //鍔犱笅鍒掔嚎WritableFont boldT15 = new WritableFont(WritableFont.TIMES,15,WritableFont.BOLD,false,UnderlineStyle.SINGLE);
	        WritableFont boldT15 = new WritableFont(WritableFont.TIMES,15,WritableFont.BOLD,false);
	        WritableFont boldT10 = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD);      
	        WritableFont boldT10L = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD);
	        //鐢熸垚涓�涓崟鍏冩牸鏍峰紡鎺у埗瀵硅薄
	        //鎶妕itleFormateT15閫氳繃boldT15鐨勫畾涔夆�渂oldT15.setColour(Colour.BLUE);鈥濆彉涓鸿摑鑹�
	        WritableCellFormat titleFormateT15 = new WritableCellFormat(boldT15);
	        WritableCellFormat titleFormateT10 = new WritableCellFormat(boldT10);
	        WritableCellFormat titleFormateT10L = new WritableCellFormat(boldT10L);
	        
	        //鍗曞厓鏍间腑鐨勫唴瀹规按骞虫柟鍚戝眳涓�
	        titleFormateT15.setAlignment(jxl.format.Alignment.CENTRE);
	        titleFormateT10.setAlignment(jxl.format.Alignment.CENTRE);
	        titleFormateT10L.setAlignment(jxl.format.Alignment.LEFT);
	        //鍗曞厓鏍肩殑鍐呭鍨傜洿鏂瑰悜灞呬腑
	        titleFormateT15.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        titleFormateT10.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        titleFormateT10L.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
	        
	       
	        //閫夋嫨瀛椾綋
	        WritableFont color = new WritableFont(WritableFont.TIMES);
	        color.setColour(Colour.BLUE);//璁剧疆瀛椾綋棰滆壊涓洪噾榛勮壊
	        WritableCellFormat colorFormat = new WritableCellFormat(color);			
			
			//[3]鐢熸垚鍗曞厓鏍�
			//3.1 瀹氫箟姣忎釜cell鐨勫唴瀹�	
	        sheet.addCell(new Label(1,1,"Contributions System TBT - R&T Activites",titleFormateT15));	
	        sheet.addCell(new Label(1,3,"Employee Name",titleFormateT10));			
	        sheet.addCell(new Label(3,3,empName.toUpperCase(),titleFormateT10));
	        sheet.addCell(new Label(7,3,"department",titleFormateT10));		
	        sheet.addCell(new Label(22,3,"Employee Number",titleFormateT10));		
	        sheet.addCell(new Label(1,5,"R&T Activities",titleFormateT10));		
	        sheet.addCell(new Label(2,5,"Project",titleFormateT10));		
	        sheet.addCell(new Label(3,5,"Team",titleFormateT10));				
	        sheet.addCell(new Label(4,5,"Activite par defaut",titleFormateT10));
	        sheet.addCell(new Label(5,5,sheetName,titleFormateT10));		
	        sheet.addCell(new Label(36,5,"Total",titleFormateT10));
			
			switch(month){
			case 1: days=28;break;
			case 0 :
			case 2 :
			case 4 :
			case 6:
			case 7:
			case 9:
			case 11:
				days=31;
				break;
			case 3:
			case 5:
			case 8 :
			case 10:
				days=30;
				break;
			}
			for(int i=1;i<=days;i++){
				sheet.addCell(new Label(i+4,6,i+"",titleFormateT10));
			}
			
			Label label0107 = new Label(1,7,"K00882",titleFormateT10);
			Label label0207 = new Label(2,7,projectId,titleFormateT10);
			//team
			for(int i=0;i<team.length;i++){
				sheet.addCell(new Label(3,7+i,team[i],titleFormateT10L));
			}
			
			sheet.addCell(new Label(4,7,"AVP Banc Turbine",titleFormateT10L));
			sheet.addCell(new Label(4,8,"Chambre de Combustion Travaux",titleFormateT10L));	
			sheet.addCell(new Label(4,9,"Chambre de Combustion Techno",titleFormateT10L));
			sheet.addCell(new Label(4,10,"Chambre de Combustion Informa",titleFormateT10L));
			sheet.addCell(new Label(4,11,"Evenementiel (s茅minaires etc.)",titleFormateT10L));
			sheet.addCell(new Label(4,12,"Informatisation Chambre",titleFormateT10L));
			sheet.addCell(new Label(4,13,"Informatisation G茅n茅rique",titleFormateT10L));
			sheet.addCell(new Label(4,14,"Information Turbine",titleFormateT10L));
			sheet.addCell(new Label(4,15,"Cost Fees Refactures",titleFormateT10L));
			sheet.addCell(new Label(4,16,"Management",titleFormateT10L));
			sheet.addCell(new Label(4,17,"Mise en place machine de Prod",titleFormateT10L));
			sheet.addCell(new Label(4,18,"Turbine Informatisation",titleFormateT10L));
			sheet.addCell(new Label(4,19,"Turbine travaux MOU-MOA-JV",titleFormateT10L));
			sheet.addCell(new Label(4,20,"Turbine Techno",titleFormateT10L));
			sheet.addCell(new Label(4,21,"Turbine Therm",titleFormateT10L));
			sheet.addCell(new Label(4,22,"Turbine Aero",titleFormateT10L));
			sheet.addCell(new Label(4,23,"Contributions totales aux projets",titleFormateT10L));
			sheet.addCell(new Label(4,24,"Improductivite non lie a un travail commande (panne d'茅lectricit茅, d茅faillance du r茅seau, panne d'茅quipement)",titleFormateT10L));
			sheet.addCell(new Label(4,25,"Obsence (Permission, cong茅 de maladie, cong茅 de mariage ou de fun茅raille, cong茅 annuel)",titleFormateT10L));
			sheet.addCell(new Label(1,27,"Applicant",titleFormateT15));
			sheet.addCell(new Label(1,29,"Direct Manager",titleFormateT15));
			sheet.addCell(new Label(1,31,"General Manager",titleFormateT15));
			sheet.addCell(new Label(19,27,"Date",titleFormateT15));
			sheet.addCell(new Label(19,29,"Date",titleFormateT15));
			sheet.addCell(new Label(19,31,"Date",titleFormateT15));
			
			//[4]璋冩暣鏍煎紡
			//4.1 鍚堝苟鍗曞厓鏍�
			/**
			 * mergeCells(a,b,c,d) 鍗曞厓鏍煎悎骞跺嚱鏁�
				a 鍗曞厓鏍肩殑鍒楀彿
				b 鍗曞厓鏍肩殑琛屽彿
				c 浠庡崟鍏冩牸[a,b]璧凤紝鍚戜笅鍚堝苟鐨勫垪鏁�
				d 浠庡崟鍏冩牸[a,b]璧凤紝鍚戜笅鍚堝苟鐨勮鏁�
			 */
			sheet.mergeCells(1,1,36,1);
			sheet.mergeCells(1,3,2,3);
			sheet.mergeCells(3,3,4,3);
			sheet.mergeCells(7,3,12,3);
			sheet.mergeCells(13,3,19,3);
			sheet.mergeCells(22,3,28,3);
			sheet.mergeCells(29,3,36,3);
			sheet.mergeCells(1,5,1,6);
			sheet.mergeCells(2,5,2,6);
			sheet.mergeCells(3,5,3,6);
			sheet.mergeCells(4,5,4,6);
			sheet.mergeCells(5,5,35,5);
			sheet.mergeCells(1,7,1,25);
			sheet.mergeCells(2,7,2,25);
			sheet.mergeCells(1,27,2,27);
			sheet.mergeCells(3,27,4,27);
			sheet.mergeCells(1,29,2,29);
			sheet.mergeCells(3,29,4,29);
			sheet.mergeCells(1,31,2,31);
			sheet.mergeCells(3,31,4,31);
			sheet.mergeCells(19,27,25,27);
			sheet.mergeCells(26,27,36,27);
			sheet.mergeCells(19,29,25,29);
			sheet.mergeCells(26,29,36,29);
			sheet.mergeCells(19,31,25,31);
			sheet.mergeCells(26,31,36,31);
			//Number number = new Number (3,4,3.1459);
			//may.addCell(number);
			
			//Date now = Calendar.getInstance().getTime();
			//DateTime dateCell = new DateTime(0,6,now);
			//may.addCell(dateCell);			
			
			//WritableFont wf = new WritableFont (WritableFont.TIMES,12,WritableFont.BOLD, false);			
			//WritableCellFormat wcfF = new WritableCellFormat(wf);			
			//Label labelCF1 = new Label(0, 0, "hello", wcfF);			
			//ws.addCell(labelCF1);		
			
			//connect database
			SqlHelper sh=new SqlHelper();
			ResultSet rs=null;
			if(empName.equals("All Members")){
				String sql="select department, date, timing from record"+year
						+ " where projectId=? and date like ? and status=1";
				DecimalFormat df=new DecimalFormat("00");
				String []paras={projectId, year+"-"+df.format(month+1)+"-%"};
				rs=sh.query(sql,paras);
			} else {
				String sql="select department, date, timing from record"+year
						+ " where empName=? and projectId=? and date like ? and status=1";
				DecimalFormat df=new DecimalFormat("00");
				String []paras={empName,projectId, year+"-"+df.format(month+1)+"-%"};
				rs=sh.query(sql,paras);
			}
			
			Vector record=new Vector();
			try {
				while(rs.next()){
					Vector hang=new Vector();
					hang.add(rs.getString(1));
					hang.add(rs.getString(2));
					hang.add(rs.getString(3));
					record.add(hang);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				sh.close();
			}
			//put records into cells
			for(int i=0;i<record.size();i++){
				int row=0;
				for(int j=0;j<team.length;j++){
					if(((Vector)record.get(i)).get(0).equals(team[j])){
						row=j;
						break;
					}
				}
				char day[]=((Vector)record.get(i)).get(1).toString().toCharArray();
				int col=Integer.parseInt(String.valueOf(day[8]))*10+Integer.parseInt(String.valueOf(day[9])); 				 
				sheet.addCell(new Label(col+4,row+7,((Vector)record.get(i)).get(2).toString(),titleFormateT10));
			}
		}catch (Exception e){
			
			System.out.println(e);
		}
	}		
}
