package com.pcms.model;

import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.pcms.db.SqlHelper;
import com.pcms.view.PanelEfficiency.DateLabelFormatter;

public class UpdateModel extends AbstractTableModel{
	Vector rowData, columnNames;
	Vector items,reportTo,comments,records;
	String empName;

	public UpdateModel(String empName,Vector teams){
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		this.empName=empName;
		//initialization jtable
		columnNames=new Vector();

		columnNames.add("Catalogue");
		columnNames.add("Project");
		columnNames.add("Team");
		columnNames.add("Date");
		columnNames.add("Start Time");
		columnNames.add("End Time");
		columnNames.add("Timing");
		columnNames.add("Percentage");
		columnNames.add("Report to");
		columnNames.add("Comments");
		
		try {
			//select form database
			items=new Vector();
			for(int i=0;i<teams.size();i+=2){
				String sql="select catalogue, projectId, department, teamChief from teams"
						+ " where projectId=? and department=?";
				String []paras={teams.get(i).toString(),teams.get(i+1).toString()};
				rs=sh.query(sql,paras);
				
				while(rs.next()){
					Vector hang=new Vector();
					hang.add(rs.getString(1));
					hang.add(rs.getString(2));
					hang.add(rs.getString(3));
					hang.add(rs.getString(4));
					items.add(hang);
				}
			} 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
		//set rows
		rowData=new Vector();
		

		for(int i=0;i<items.size();i++){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//Date format
			Vector hang=new Vector();
			hang.add(((Vector)items.get(i)).get(0).toString());
			hang.add(((Vector)items.get(i)).get(1).toString());
			hang.add(((Vector)items.get(i)).get(2).toString());
			
				    
			//date
			hang.add( (df.format(new Date())) );
			
			//startTime
			hang.add(new String("HHMM"));
			//endTime
			hang.add(new String("HHMM"));
			//timing
			hang.add(new String());
			//pencertage
			hang.add(new String());
			//reportTo
			hang.add(new String(((Vector)items.get(i)).get(3).toString()));
			//Comments
			hang.add(new String("No comment"));
			//System.out.println(i);
			rowData.add(hang);
					
		}
		
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
	
	public boolean isCellEditable(int row, int col){
		if(col >2&&col!=6&&col!=8){
			return true;
		} return false;
	}
	/* 
	 * Don't need to implement this method unless your table's 
	 * data can change. 
	 */ 
	public void setValueAt(Object value, int row, int col) { 
		((Vector)rowData.get(row)).setElementAt(value, col); 
		fireTableCellUpdated(row, col);
	} 
	public void addRecord(){
		Vector hangNew=new Vector();
		hangNew=(Vector)((Vector)rowData.get(rowData.size()-1)).clone();
		rowData.addElement(hangNew);
	}
	
	public void deleteRecord(int row){
		rowData.remove(row);
	}
	//put record to database
	public boolean submit(){
		calculate();
		for(int i=0;i<rowData.size();i++){
			if(((Vector)rowData.get(i)).get(3).toString().equals(null)){
				JOptionPane.showMessageDialog(null,"Please complete the information");
				return false;
			}
			if(((Vector)rowData.get(i)).get(4).toString().equals("HHMM")||
					((Vector)rowData.get(i)).get(3).toString().equals(null)){
				JOptionPane.showMessageDialog(null,"Please complete the information");
				return false;
			}
			if(((Vector)rowData.get(i)).get(5).toString().equals("HHMM")||
					((Vector)rowData.get(i)).get(3).toString().equals(null)){
				JOptionPane.showMessageDialog(null,"Please complete the information");
				return false;
			}
			if(((Vector)rowData.get(i)).get(6).toString().equals("")){
				JOptionPane.showMessageDialog(null,"Please complete the information");
				return false;
			}
			//System.out.println(((Vector)rowData.get(i)).get(6).toString());
			//if(((Vector)rowData.get(i)).get(7).toString().equals(null)){
			//	JOptionPane.showMessageDialog(null,"Please complete the information");
			//	return false;
			//}
			//System.out.println(((Vector)rowData.get(i)).get(7).toString());
			if(Integer.parseInt(((Vector)rowData.get(i)).get(4).toString())/100>23||
					Integer.parseInt(((Vector)rowData.get(i)).get(4).toString())%100>59||
					Integer.parseInt(((Vector)rowData.get(i)).get(5).toString())/100>23||
					Integer.parseInt(((Vector)rowData.get(i)).get(5).toString())%100>59){
				JOptionPane.showMessageDialog(null, "Wrong time format!","Error",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(Integer.parseInt(((Vector)rowData.get(i)).get(4).toString())>
			Integer.parseInt(((Vector)rowData.get(i)).get(5).toString())){
				JOptionPane.showMessageDialog(null, "Wrong time!","Error",JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if(rowData.size()>=2){
				for(int k=i+1;k<rowData.size();k++){
					//ystem.out.println(((Vector)rowData.get(i)).get(3).toString());
					//System.out.println(((Vector)rowData.get(k)).get(3).toString());
					if(((Vector)rowData.get(i)).get(3).toString().equals(((Vector)rowData.get(k)).get(3).toString())){
						int a=Integer.parseInt(((Vector)rowData.get(i)).get(4).toString());
						int b=Integer.parseInt(((Vector)rowData.get(i)).get(5).toString());
						int c=Integer.parseInt(((Vector)rowData.get(k)).get(4).toString());
						int d=Integer.parseInt(((Vector)rowData.get(k)).get(5).toString());
						if((c>=a&c<b)||(d>=a&d<b)||(a>=c&a<d)||(b>=c&b<d)){
							JOptionPane.showMessageDialog(null, "Time Overlapping!","Error",JOptionPane.ERROR_MESSAGE);
							return false;
						}
					}
				}
			}
			records= new Vector();
			MixTime(empName,records,i);
			for(int j=0; j<records.size();j++){
				int a=Integer.parseInt(((Vector)records.get(j)).get(1).toString());
				int b=Integer.parseInt(((Vector)records.get(j)).get(2).toString());
				int c=Integer.parseInt(((Vector)rowData.get(i)).get(4).toString());
				int d=Integer.parseInt(((Vector)rowData.get(i)).get(5).toString());
				if((c>=a&c<b)||(d>=a&d<b)||(a>=c&a<d)||(b>c&b<d)){
					JOptionPane.showMessageDialog(null, "Time Existed in History!","Error",JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		//update to data base
		SqlHelper sh=new SqlHelper();
		try {
			//select form database
			for(int i=0;i<rowData.size();i++){
				String sql="insert into records values(?,?,?,?,?,?,?,?,?,?,?,?,'0',?) ;";
				String sql1="insert into validation_details values(?,?,?,?,?,?,?,?,?,?,?,?,'0',?) ;";
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//Date format
				
				//the first parameter is reference
				String []paras={empName+'_'+((Vector)rowData.get(i)).get(3).toString()+'_'+((Vector)rowData.get(i)).get(4).toString()+((Vector)rowData.get(i)).get(5).toString(),
						empName,
						((Vector)rowData.get(i)).get(0).toString(),
						((Vector)rowData.get(i)).get(1).toString(),						
						((Vector)rowData.get(i)).get(2).toString(),
						((Vector)rowData.get(i)).get(3).toString(),
						((Vector)rowData.get(i)).get(4).toString(),
						((Vector)rowData.get(i)).get(5).toString(),
						((Vector)rowData.get(i)).get(6).toString(),
						((Vector)rowData.get(i)).get(7).toString(),
						((Vector)rowData.get(i)).get(8).toString(),
						((Vector)rowData.get(i)).get(9).toString(),
						df.format(new Date())
						};
				String []paras1={empName+'_'+((Vector)rowData.get(i)).get(3).toString()+'_'+((Vector)rowData.get(i)).get(4).toString()+((Vector)rowData.get(i)).get(5).toString(),
						empName,
						((Vector)rowData.get(i)).get(0).toString(),
						((Vector)rowData.get(i)).get(1).toString(),						
						((Vector)rowData.get(i)).get(2).toString(),
						((Vector)rowData.get(i)).get(3).toString(),
						((Vector)rowData.get(i)).get(4).toString(),
						((Vector)rowData.get(i)).get(5).toString(),
						((Vector)rowData.get(i)).get(6).toString(),
						((Vector)rowData.get(i)).get(7).toString(),
						((Vector)rowData.get(i)).get(8).toString(),
						((Vector)rowData.get(i)).get(9).toString(),
						df.format(new Date())						//Today's date
						};
				sh.update(sql,paras);
				sh.update(sql1,paras1);
			} 
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
		return true;
	}
	public boolean isDigit(String strNum) {  
	    return strNum.matches("[0-9]{1,}");  
	}
	//calculate startTime and endTime
	public void calculate(){
		for(int i=0;i<rowData.size();i++){
			if(isDigit(((Vector)rowData.get(i)).get(5).toString())&&isDigit(((Vector)rowData.get(i)).get(4).toString())){
				String startTime=((Vector)rowData.get(i)).get(4).toString();
				String endTime=((Vector)rowData.get(i)).get(5).toString();
				int startHour,endHour,startMinute,endMinute;
				startHour=Integer.parseInt(startTime)/100;
				endHour=Integer.parseInt(endTime)/100;
				startMinute=Integer.parseInt(startTime)%100;
				endMinute=Integer.parseInt(endTime)%100;
				if(endMinute<startMinute){
					endHour--;
					endMinute+=60;
				}
				
				if(endHour>=startHour){
					String hour=String.valueOf(endHour-startHour);
					String minute=String.valueOf(endMinute-startMinute);
					while(hour.length()<2){
						hour='0'+hour;
					}
					while(minute.length()<2){
						minute='0'+minute;
					}
					this.setValueAt(hour+minute, i, 6);
				}
			}
		}		
	}
	//look for chiefs 
	public void searchchiefs(){
		
	}
	public void searchComments(){
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		
		try {
			//select form database
			comments=new Vector();
			
			String sql="select text from comments";
			rs=sh.query(sql);			
			while(rs.next()){				
				comments.add(rs.getString(1));
			}		 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
	}
	
	public void reportToColumn(TableColumn Column) {
		searchchiefs();
		SetupColumn(Column,reportTo);
	} 
	//set check box for report to column 
	public void commentsColumn(TableColumn Column) { 
		searchComments();
		SetupColumn(Column,comments);
	} 
	//set up column
	public void SetupColumn(TableColumn Column,Vector items){
		
		//Set up the editor for cells. 
		JComboBox comboBox = new JComboBox(items); 
		//for(int i=0;i<items.size();i++){
			//comboBox.addItem(items.get(i).toString()); 
		//}
		Column.setCellEditor(new DefaultCellEditor(comboBox)); 
	}
	
	public class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }

	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}
	
	public void MixTime(String empName,Vector records,int i){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		this.empName=empName;
		try {			
			String sql="select date, startTime, endTime, status from records" 
			+ " where date=? and (status=? or status=?)";
			//String []paras={records.get(i).toString(),records.get(i+1).toString()};
			String []paras={((Vector)rowData.get(i)).get(3).toString(),"0","1"};
			//String []paras={df.format(new Date()),"0","1"};
			rs=sh.query(sql,paras);
			while(rs.next()){
				Vector hang=new Vector();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				records.add(hang);
			}
			//} 
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
	}
}
