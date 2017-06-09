
package com.pcms.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.pcms.db.*;

import jxl.CellType;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Boolean;
import jxl.write.Border;
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

public class ContributionModel extends AbstractTableModel{
	Vector dataBackup, columnNames,reference;
	Vector<Vector> rowData;
	String empName;
	int depNum;
	String date1,date2;
	
	public ContributionModel(String empName,String searchby, Vector key1, Vector key2, Vector key3, Vector key4, String date1, String date2){
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		try {
			depNum = ContributionModel.allteams().length;
			this.date1=date1;
			this.date2=date2;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//jtable
		columnNames=new Vector();				
		columnNames.add("Catalogue");
		columnNames.add("Project");
		columnNames.add("Department");
		columnNames.add("Employee");
		columnNames.add("Date");
		columnNames.add("Working hour");
		
	
		rowData=new Vector();
		dataBackup=new Vector();
		reference=new Vector();
					
		String orderby = new String();
		if(searchby=="Search by Project"){
			orderby="catalogue ASC, projectId ASC, department ASC, empName ASC, date ASC";
		}else if(searchby=="Search by Employee"){
			orderby="empName ASC, catalogue ASC, projectId ASC, department ASC, date ASC";
		}
		
		String sql="select catalogue, projectId, department, empName, date, timing from records"				
				+ " where catalogue IN"+getComboStr(key1)+" and projectId IN"+getComboStr(key2)+" and department IN"+getComboStr(key3)+" and empName IN"+getComboStr(key4)+" and (date BETWEEN ? and ?) and status=1 order by "+orderby;
		String []paras={date1, date2};
		rs=sh.query(sql,paras);	
		
		try {
			while(rs.next()){
				DecimalFormat df = new DecimalFormat( "0.0");
				
				//add up hours on the same date.
				if(rowData.isEmpty()==true){
					Vector hang=new Vector();					
					hang.add(rs.getString(1));
					hang.add(rs.getString(2));
					hang.add(rs.getString(3));
					hang.add(rs.getString(4));					
					hang.add(rs.getString(5));//date
					hang.add(df.format(minToHr(rs.getString(6))));
					rowData.add(hang);
					dataBackup.add(hang);
				}else{
					Vector lastdata=((Vector)rowData.lastElement());
					if(rs.getString(5).equals(lastdata.get(4))){
						//double addhr=Double.parseDouble((String)(lastdata.get(5))) + minToHr(rs.getString(6));
						double addhr=Double.parseDouble((String)(lastdata.get(5))) + minToHr(rs.getString(6));
						((Vector)rowData.lastElement()).set( 5, df.format(addhr));
					}else{
						Vector hang=new Vector();
						
						hang.add(rs.getString(1));
						hang.add(rs.getString(2));
						hang.add(rs.getString(3));
						hang.add(rs.getString(4));					
						hang.add(rs.getString(5));//date
						hang.add(df.format(minToHr(rs.getString(6))));
						
											    
						rowData.add(hang);
						dataBackup.add(hang);
					}
				}
				
			
			}
			
			// Make Excel book
			
			if(searchby=="Search by Project"){	//Search by Project Excel
				OutputStream cs = new FileOutputStream("Contribution.xls");
				WritableWorkbook wb= Workbook.createWorkbook(cs);	
				WritableSheet sheet = wb.createSheet("Contribution", 1);
				
				// Begin to write e
				WritableFont boldT10L = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD);
				WritableCellFormat titleFormateT10L = new WritableCellFormat(boldT10L);
				
				
				sheet.addCell(new Label(0,0,"Catalogue",titleFormateT10L));
				sheet.addCell(new Label(1,0,"Project",titleFormateT10L));
				sheet.addCell(new Label(2,0,"Department",titleFormateT10L));
				sheet.addCell(new Label(3,0,"Employee",titleFormateT10L));
				
				
				java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");    
				java.util.Date beginDate= format.parse(date1);
				java.util.Date endDate= format.parse(date2);    
				long interval=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
				System.out.println("interval"+interval);
				
				for(int i=0; i<=interval; i++){
					java.util.Date datei = new java.util.Date(); 
					datei.setTime(beginDate.getTime()+24*60*60*1000*(long)i);
					
					sheet.addCell(new Label(4+i,0,datei.getYear()+1900+"",titleFormateT10L));
					
					SimpleDateFormat sdf = new SimpleDateFormat("MM");
				    Date date = sdf.parse(Integer.toString(datei.getMonth()+1));
				    sdf = new SimpleDateFormat("MMMMM",Locale.ENGLISH);
					sheet.addCell(new Label(4+i,1,sdf.format(date),titleFormateT10L));//English month					
					
					sheet.addCell(new Label(4+i,2,datei.getDate()+"",titleFormateT10L));
					//System.out.println((datei.getYear()+1900)+"\t"+(datei.getMonth()+1)+"\t"+datei.getDate());
				}
				
//				// Merge cells				
				
				
				wb.write();
				wb.close();
				cs.close();
				//Open the Excel file
				System.out.println(System.getProperty("user.dir"));
				String path=System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\").replaceAll(" ", "\" \"");
				
				System.out.println(path);
				Runtime.getRuntime().exec("cmd  /c  start  "+path+"\\Contribution.xls");
				//Runtime.getRuntime().exec("cmd  /c  start  D:\\Java\" \"workspace\\TestPCMS\\Contribution.xls");
				
				
			}else if(searchby=="Search by Employee"){	//Search by Emp Excel
				
				EmpExcel();
				
			}							
			
		} catch (SQLException | IOException | WriteException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
		
	}
	
	
	//Search by Employee Excel
	public void EmpExcel(){
		
		if(rowData.size()<1){
			return;
		}
		
		try {
			int filenum=0;
			String path=System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\");	
			path=path+"\\Excels";
			File filepath = new File(path);
			filepath.mkdirs();	//create "Excels" folder
			
			File file = new File(path+"\\Contribution("+String.valueOf(filenum)+").xls");
			while(file.exists() && !file.renameTo(file)){
				System.out.println(file.exists());
		        System.out.println(file.canWrite());
		        System.out.println(!file.renameTo(file));
		        filenum++;
		        file = new File(path+"\\Contribution("+String.valueOf(filenum)+").xls");
			}	        
	        
			OutputStream cs = new FileOutputStream(file);
			WritableWorkbook wb= Workbook.createWorkbook(cs);	
			
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
			java.util.Date beginDate= format.parse(date1);
			java.util.Date endDate= format.parse(date2);
			
			Vector lastdata = rowData.get(0);
			
			int lastRowNum=2;	
			int empNum = 0;
			
			WritableSheet sheet = null;
			
			//Cell formats
			WritableCellFormat titleFormateT10L = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10,WritableFont.BOLD));
			titleFormateT10L.setAlignment(Alignment.CENTRE);
			titleFormateT10L.setVerticalAlignment(VerticalAlignment.CENTRE);
			titleFormateT10L.setBorder(jxl.format.Border.ALL, BorderLineStyle.THIN);
			WritableCellFormat numFormateT10L = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 8,WritableFont.NO_BOLD));
			numFormateT10L.setAlignment(Alignment.CENTRE);
			numFormateT10L.setVerticalAlignment(VerticalAlignment.CENTRE);
								
			
			
			long interval = (endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
			for(int i=0; i<rowData.size();i++){
				if(i==0||lastdata.get(3).equals(rowData.get(i).get(3)) == false){	//New employee sheet to draw
					
					System.out.println(rowData.get(i).get(3));
					System.out.println(lastdata.get(3));
					sheet = wb.createSheet((String)rowData.get(i).get(3), ++empNum);
					lastRowNum=2;	//reinitialize last row number for the new sheet
																	
					//Headers						
					sheet.addCell(new Label(0,0,"Catalogue",titleFormateT10L));
					sheet.addCell(new Label(1,0,"Project",titleFormateT10L));
					sheet.addCell(new Label(2,0,"Department",titleFormateT10L));
						
											
					//Dates						   
					System.out.println("interval"+interval);
					
					for(int j=0; j<=interval; j++){
						java.util.Date datei = new java.util.Date(); 
						datei.setTime(beginDate.getTime()+24*60*60*1000*(long)j);
						
						sheet.addCell(new Label(3+j,0,datei.getYear()+1900+"",titleFormateT10L));
						
						SimpleDateFormat sdf = new SimpleDateFormat("MM");
					    Date date = sdf.parse(Integer.toString(datei.getMonth()+1));
					    sdf = new SimpleDateFormat("MMMMM",Locale.ENGLISH);
						sheet.addCell(new Label(3+j,1,sdf.format(date),titleFormateT10L));//English month
						
						sheet.addCell(new Number(3+j,2,datei.getDate(),titleFormateT10L));
						//System.out.println((datei.getYear()+1900)+"\t"+(datei.getMonth()+1)+"\t"+datei.getDate());
					}
					
					// First data of this emp, totals
					sheet.addCell(new Label(3+(int)interval+1, 0, "Total",titleFormateT10L));	
					for(int t=0; t<=interval; t++){		//zeros for total TT-01 TA-01 TO-01
						sheet.addCell(new Number(3+t, 3+18, 0, titleFormateT10L));
						sheet.addCell(new Number(3+t, 3+28, 0, titleFormateT10L));
						sheet.addCell(new Number(3+t, 3+29, 0, titleFormateT10L));
					}
					for(int k=0; k<depNum;k++){	//zeros for total on the right 
						sheet.addCell(new Number(3+(int)interval+1, 3+k, 0, titleFormateT10L));	
					}
					
					sheet.addCell(new Label(0,3, (String)rowData.get(i).get(0),titleFormateT10L));	//catalogue
					sheet.addCell(new Label(1,3, (String)rowData.get(i).get(1),titleFormateT10L));	//project
					for(int k=0; k<depNum;k++){							
						sheet.addCell(new Label(2,3+k, ContributionModel.allteams()[k],titleFormateT10L));	//departments		
						
						//write workng hour
						if(rowData.get(i).get(2).equals(ContributionModel.allteams()[k])){
							java.util.Date workDate= format.parse((String)rowData.get(i).get(4));
							long daynum=(workDate.getTime()-beginDate.getTime())/(24*60*60*1000);
							double hour= Double.parseDouble((String)rowData.get(i).get(5));
							sheet.addCell(new Number(3+(int)daynum, 3+k, hour, numFormateT10L));
							
							//calculate totals
							if(k<18){
								double ttHour = Double.parseDouble(sheet.getCell(3+(int)daynum, 3+18).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, 3+18, ttHour, titleFormateT10L));
								double toHour = Double.parseDouble(sheet.getCell(3+(int)daynum, 3+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, 3+29, toHour, titleFormateT10L));									
								double HourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, 3+k).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, 3+k, HourT, titleFormateT10L));									
								double ttHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, 3+18).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, 3+18, ttHourT, titleFormateT10L));
								double toHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, 3+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, 3+29, toHourT, titleFormateT10L));
							}else{
								double ttHour = Double.parseDouble(sheet.getCell(3+(int)daynum, 3+28).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, 3+28, ttHour, titleFormateT10L));
								double toHour = Double.parseDouble(sheet.getCell(3+(int)daynum, 3+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, 3+29, toHour, titleFormateT10L));									
								double HourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, 3+k).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, 3+k, HourT, titleFormateT10L));									
								double ttHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, 3+28).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, 3+28, ttHourT, titleFormateT10L));
								double toHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, 3+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, 3+29, toHourT, titleFormateT10L));
							}
						}
					}
					
					
					
					
					//update lastdata
					lastdata=rowData.get(i);
					lastRowNum=lastRowNum+ depNum ;
					
				}else if(lastdata.get(1).equals(rowData.get(i).get(1)) == false){	//new project rows to draw
					
					
					for(int t=0; t<=interval; t++){		//zeros for total
						sheet.addCell(new Number(3+t, lastRowNum+1+18, 0, titleFormateT10L));
						sheet.addCell(new Number(3+t, lastRowNum+1+28, 0, titleFormateT10L));
						sheet.addCell(new Number(3+t, lastRowNum+1+29, 0, titleFormateT10L));
					}						
					for(int k=0; k<depNum;k++){	//zeros for total on the right 
						sheet.addCell(new Number(3+(int)interval+1, lastRowNum+1+k, 0, titleFormateT10L));	
					}
					
					sheet.addCell(new Label(0,lastRowNum+1, (String)rowData.get(i).get(0),titleFormateT10L)); 	//catalogue
					sheet.addCell(new Label(1,lastRowNum+1, (String)rowData.get(i).get(1),titleFormateT10L));	//project
					for(int k=0; k<depNum;k++){							
						sheet.addCell(new Label(2,lastRowNum+k+1, ContributionModel.allteams()[k],titleFormateT10L));	//departments
						
						
						//write workng hour
						if(rowData.get(i).get(2).equals(ContributionModel.allteams()[k])){
							java.util.Date workDate= format.parse((String)rowData.get(i).get(4));
							long daynum=(workDate.getTime()-beginDate.getTime())/(24*60*60*1000);
							double hour= Double.parseDouble((String)rowData.get(i).get(5));
							sheet.addCell(new Number(3+(int)daynum, lastRowNum+k+1, hour, numFormateT10L));	
							
							//calculate totals
							if(k<18){
								double ttHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum+1+18).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum+1+18, ttHour, titleFormateT10L));
								double toHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum+1+29, toHour, titleFormateT10L));									
								double HourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum+k+1).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum+k+1, HourT, titleFormateT10L));									
								double ttHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum+1+18).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum+1+18, ttHourT, titleFormateT10L));
								double toHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum+1+29, toHourT, titleFormateT10L));
							}else{
								double ttHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum+1+28).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum+1+28, ttHour, titleFormateT10L));
								double toHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum+1+29, toHour, titleFormateT10L));									
								double HourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum+k+1).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum+k+1, HourT, titleFormateT10L));									
								double ttHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum+1+28).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum+1+28, ttHourT, titleFormateT10L));
								double toHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum+1+29, toHourT, titleFormateT10L));
							}
						}
					}
					
					//update lastdata
					lastdata=rowData.get(i);
					lastRowNum=lastRowNum+ depNum ;
					
				}else{	//Fill in working hours of projects that have already been drew. 
					
					for(int k=0; k<depNum;k++){
						//write workng hour
						if(rowData.get(i).get(2).equals(ContributionModel.allteams()[k])){
							java.util.Date workDate= format.parse((String)rowData.get(i).get(4));
							long daynum=(workDate.getTime()-beginDate.getTime())/(24*60*60*1000);
							double hour= Double.parseDouble((String)rowData.get(i).get(5));
							sheet.addCell(new Number(3+(int)daynum, lastRowNum-depNum+k+1, hour, numFormateT10L));	
							
							//calculate totals
							if(k<18){
								double ttHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum-depNum+1+18).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum-depNum+1+18, ttHour, titleFormateT10L));
								double toHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum-depNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum-depNum+1+29, toHour, titleFormateT10L));									
								double HourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum-depNum+k+1).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum-depNum+k+1, HourT, titleFormateT10L));									
								double ttHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum-depNum+1+18).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum-depNum+1+18, ttHourT, titleFormateT10L));
								double toHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum-depNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum-depNum+1+29, toHourT, titleFormateT10L));
							}else{
								double ttHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum-depNum+1+28).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum-depNum+1+28, ttHour, titleFormateT10L));
								double toHour = Double.parseDouble(sheet.getCell(3+(int)daynum, lastRowNum-depNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)daynum, lastRowNum-depNum+1+29, toHour, titleFormateT10L));									
								double HourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum-depNum+k+1).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum-depNum+k+1, HourT, titleFormateT10L));									
								double ttHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum-depNum+1+28).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum-depNum+1+28, ttHourT, titleFormateT10L));
								double toHourT = Double.parseDouble(sheet.getCell(3+(int)interval+1, lastRowNum-depNum-depNum+1+29).getContents()) + hour;
								sheet.addCell(new Number(3+(int)interval+1, lastRowNum-depNum+1+29, toHourT, titleFormateT10L));
							}
						}
					}
					
					
				}
			}
			
					
//			// Merge cells				
			for(int i=0; i<empNum;i++){
				WritableSheet sheetMerge = wb.getSheet(i);	//different sheets
				
				sheetMerge.mergeCells(3+(int)interval+1, 0, 3+(int)interval+1, 2);	//"Total"
				MergeCol(sheetMerge, 0);
				MergeCol(sheetMerge, 1);
				MergeCol(sheetMerge, 2);
				MergeRow(sheetMerge, 0);
				MergeRow(sheetMerge, 1);
			}
			
			//Set row, column views
			for(int i=0; i<empNum;i++){
				WritableSheet sheetMerge = wb.getSheet(i);
				
				sheetMerge.setColumnView(0, 12);
				sheetMerge.setColumnView(1, 12);
				sheetMerge.setColumnView(2, 12);
				sheetMerge.setColumnView(3+(int)interval+1, 10);
				for(int j=3; j<sheetMerge.getColumns()-1; j++){
					sheetMerge.setColumnView(j, 5);
				}
				
			}
			
			
			//close streams
			wb.write();
			wb.close();
			cs.close();
			
			//Open the Excel file
			System.out.println(file.getPath());			
			path=file.getPath().replaceAll(" ", "\" \"");				
			System.out.println(path);
			
			Runtime.getRuntime().exec("cmd  /c  start  "+path);
			
		} catch (SQLException | IOException | WriteException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		
	}
	
	
	//Merge cells
	public void MergeCol(WritableSheet sheet, int col){
		int start=0, end=0;
		for(int i=1; i<sheet.getRows(); i++){
						
			if(sheet.getCell(col, i).getType()==CellType.EMPTY){								
				end=i;
			}else if(sheet.getCell(col, i).getContents().equals(sheet.getCell(col, start).getContents())){
				end=i;				
			}else{								
				try {
					sheet.mergeCells(col, start, col, end);
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}								
				start=i;
				end=i;
			}
		}
		
		try {
			sheet.mergeCells(col, start, col, end);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void MergeRow(WritableSheet sheet, int row){
		int start=3, end=3;
		for(int i=4; i<sheet.getColumns()-1; i++){
						
			if(sheet.getCell(i,row).getType()==CellType.EMPTY){								
				end=i;
			}else if(sheet.getCell(i,row).getContents().equals(sheet.getCell(start, row).getContents())){
				end=i;				
			}else{								
				try {
					sheet.mergeCells(start, row, end, row);
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}								
				start=i;
				end=i;
			}
		}
		
		try {
			sheet.mergeCells(start, row, end, row);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	//All projects
	public static String []allprojets() throws SQLException{
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		rs=null;
		rs=sh.query("select projectId from projects order by projectId ASC");
		Vector <String>projectsv=new Vector();
		projectsv.add("All projects");
		while(rs.next()){				
			projectsv.add(rs.getString(1));
		}
		String []allprojets = projectsv.toArray( new String[projectsv.size()]);
		
		return allprojets;
	}
	
	//All teams
	public static String []allteams() throws SQLException{
//		SqlHelper sh=new SqlHelper();
//		ResultSet rs=null;
//		rs=null;
//		rs=sh.query("select abbreviation from abbreviation order by abbreviation ASC");
//		Vector <String>teamsv=new Vector();
//		teamsv.add("All departments");
//		while(rs.next()){				
//			teamsv.add(rs.getString(1));
//		}
//		String []allteams = teamsv.toArray( new String[teamsv.size()]);
		
		String []allteams = {"CC-01","CC-02","CC-03","CC-04","CC-06","IG-01","IT-01","MT-01","PA-01","TB-01","TB-02","TB-03","TU-01","TU-02","TU-03","TU-04","TU-05","TU-06","TT-01",
				"AD-01","EV-01","FA-01","HR-01","JV-01","MA-01","NR-01","OB-01","TR-01","TA-01","TO-01"	};
		
		
		
		return allteams;
	}
	
	//All employees
	public static String []allemps() throws SQLException{
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		rs=null;
		rs=sh.query("select empName from employees order by empName ASC");
		Vector <String>empsv=new Vector();
		empsv.add("All employees");
		while(rs.next()){				
			empsv.add(rs.getString(1));
		}
		String []allemps = empsv.toArray( new String[empsv.size()]);
		
		return allemps;
	}
	
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return (String)this.columnNames.get(column);
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return this.columnNames.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this.rowData.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		return ((Vector)this.rowData.get(row)).get(col);
	}

	/* 
	* JTable uses this method to determine the default renderer/ 
	* editor for each cell. If we didn't implement this method, 
	* then the last column would contain text ("true"/"false"), 
	* rather than a check box. 
	*/ 
	public Class getColumnClass(int c) { 
		return getValueAt(0, c).getClass(); 
	} 
	/* 
	 * Don't need to implement this method unless your table's 
	 * data can change. 
	 */ 
	public void setValueAt(Object value, int row, int col) { 
		((Vector)rowData.get(row)).setElementAt(value, col); 
		fireTableCellUpdated(row, col);
	} 

	public double minToHr(String timing){
		double hr=0;
		
		int Hour=Integer.parseInt(timing)/100;
		int Min=Integer.parseInt(timing)%100;
		
		hr= (double)Hour+ (double)Min/60;
		
		
		return hr;
	}
	
	public String getComboStr(Vector comboV){
		
		String comboSt = "(";
		for (int i=0; i < comboV.size(); i++){
			if(i != (comboV.size()-1)){
				comboSt = comboSt +"'"+ (String)comboV.get(i)+"',";
			}else{
				comboSt = comboSt +"'"+ (String)comboV.get(i)+"')";
			}				
		}
		
		
		return comboSt;
	}

}

