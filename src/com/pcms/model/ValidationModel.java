package com.pcms.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.pcms.db.*;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ValidationModel extends AbstractTableModel{
	Vector rowData,dataBackup, columnNames,reference;
	String empName;
	
	public ValidationModel(String empName,String status){
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		//jtable
		columnNames=new Vector();
		
		columnNames.add("Select all");
		columnNames.add("Name");
		columnNames.add("Catalogue");
		columnNames.add("Project");
		columnNames.add("Department");
		columnNames.add("Date");
		columnNames.add("Start Time");
		columnNames.add("End Time");
		columnNames.add("Timing");
		columnNames.add("Percentage");
		columnNames.add("Report to");
		columnNames.add("Comments");
		columnNames.add("Status");
		columnNames.add("Operation Date");
	
		rowData=new Vector();
		dataBackup=new Vector();
		reference=new Vector();
		//chose			
		
		if(status.equals("Waiting List")){
			String sql="select reference, empName, catalogue, projectId, department, date, startTime, endTime, timing, percentage, reportTo, comments, status, operationDate from records"
					+ " where reportTo=? and status=? order by operationDate ASC";
			String []paras={empName,"0"};
			rs=sh.query(sql,paras);	
		} else if(status.equals("Valided")){
			String sql="select reference, empName, catalogue, projectId, department, date, startTime, endTime, timing, percentage, reportTo, comments, status, operationDate from records"
					+ " where reportTo=? and status=? order by operationDate ASC";
			String []paras={empName,"1"};
			rs=sh.query(sql,paras);	
		} else if(status.equals("All Validation")){
			String sql="select reference, empName, catalogue, projectId, department, date, startTime, endTime, timing, percentage, reportTo, comments, status, operationDate from records"
					+ " where reportTo=? order by operationDate ASC";
			String []paras={empName};
			rs=sh.query(sql,paras);
		} else if(status.equals("My Applications")){
			String sql="select reference, empName, catalogue, projectId, department, date, startTime, endTime, timing, percentage, reportTo, comments, status, operationDate from records"
					+ " where empName=? order by operationDate DESC";
			String []paras={empName};
			rs=sh.query(sql,paras);	
		} 
		
		try {
			while(rs.next()){
				Vector hang=new Vector();
				reference.add(rs.getString(1));
				
				hang.add(new Boolean(false));				
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				hang.add(rs.getString(5));
				hang.add(rs.getString(6));
				hang.add(rs.getString(7));
				hang.add(rs.getString(8));
				hang.add(rs.getString(9));
				hang.add(rs.getString(10));
				hang.add(rs.getString(11));
				hang.add(rs.getString(12));
				if(rs.getString(13).equals("0")){hang.add("Waiting");}
				else if(rs.getString(13).equals("1")){hang.add("Validated");}
				else if(rs.getString(13).equals("2")){hang.add("Refused");}
				hang.add(rs.getString(14));
			    
				rowData.add(hang);
				dataBackup.add(hang);
			}
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
	}
	
	//All projects
	public String []allprojets() throws SQLException{
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		rs=null;
		rs=sh.query("select projectId from projects");
		Vector <String>projectsv=new Vector();
		projectsv.add("All projects");
		while(rs.next()){				
			projectsv.add(rs.getString(1));
		}
		String []allprojets = projectsv.toArray( new String[projectsv.size()]);
		
		return allprojets;
	}
	
	//All teams
	public String []allteams() throws SQLException{
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		rs=null;
		rs=sh.query("select abbreviation from abbreviation");
		Vector <String>teamsv=new Vector();
		teamsv.add("All departments");
		while(rs.next()){				
			teamsv.add(rs.getString(1));
		}
		String []allteams = teamsv.toArray( new String[teamsv.size()]);
		
		return allteams;
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
	public void update(String command, int []row){
		//update to data base
		SqlHelper sh=new SqlHelper();
		try {
			//select form database
			for(int i=0;i<row.length;i++){
								
				if(command.equals("Validate")){		//validate
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//Date format
					
					String sql="update records set status=? where reference=?";	
					String []paras={"1",
							reference.get(row[i]).toString()};
					sh.update(sql,paras);	//validate in table records->change status to 1
					
					String sql1="insert into validation_details values(?,?,?,?,?,?,?,?,?,?,?,?,'1',?)";
					String []paras1={reference.get(row[i]).toString(),
							((Vector)rowData.get(row[i])).get(1).toString(),
							((Vector)rowData.get(row[i])).get(2).toString(),
							((Vector)rowData.get(row[i])).get(3).toString(),
							((Vector)rowData.get(row[i])).get(4).toString(),
							((Vector)rowData.get(row[i])).get(5).toString(),
							((Vector)rowData.get(row[i])).get(6).toString(),
							((Vector)rowData.get(row[i])).get(7).toString(),
							((Vector)rowData.get(row[i])).get(8).toString(),
							((Vector)rowData.get(row[i])).get(9).toString(),
							((Vector)rowData.get(row[i])).get(10).toString(),
							((Vector)rowData.get(row[i])).get(11).toString(),
							df.format(new Date())						//Today's date
							};
					sh.update(sql1,paras1);		//store the information in table validation_details
					
					//System.out.println(reference.get(row[i]).toString());
					
				} else if(command.equals("Refuse")){	//refuse
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//Date format
					
					String sql="update records set status=? where reference=?";	
					String []paras={"2",
							reference.get(row[i]).toString()};
					sh.update(sql,paras);	//validate in table records->change status to 1
					
					String sql1="insert into validation_details values(?,?,?,?,?,?,?,?,?,?,?,'2',?)";
					String []paras1={reference.get(row[i]).toString(),
							((Vector)rowData.get(row[i])).get(1).toString(),
							((Vector)rowData.get(row[i])).get(3).toString(),
							((Vector)rowData.get(row[i])).get(2).toString(),
							((Vector)rowData.get(row[i])).get(4).toString(),
							((Vector)rowData.get(row[i])).get(5).toString(),
							((Vector)rowData.get(row[i])).get(6).toString(),
							((Vector)rowData.get(row[i])).get(7).toString(),
							((Vector)rowData.get(row[i])).get(8).toString(),
							((Vector)rowData.get(row[i])).get(9).toString(),
							((Vector)rowData.get(row[i])).get(10).toString(),
							((Vector)rowData.get(row[i])).get(11).toString(),	
							df.format(new Date())						//Today's date
							};
					sh.update(sql1,paras1);		//store the information in table validation_details
				}else if(command.equals("Detail")){	//Detail
					
					ResultSet rs1 = null;
					Vector<String[]> detailData = new Vector<String[]>();
					String sql="select empName, projectId, department, date, startTime, endTime, timing, percentage, reportTo, comments, status, operationDate from validation_details"
					+ " where reference=? order by operationDate ASC";	
					String []paras={reference.get(row[i]).toString()};
					rs1=sh.query(sql,paras);
					int nb=0;
					while(rs1.next()){
						String[] hang=new String[12];
						for(int i1=1;i1<=12;i1++){
							hang[i1-1]=rs1.getString(i1);
							
						}
						if(rs1.getString(11).equals("0")){hang[10]="Waiting";}
						else if(rs1.getString(11).equals("1")){hang[10]="Validated";}
						else if(rs1.getString(11).equals("2")){hang[10]="Refused";}
						nb++;					    
						detailData.add(hang);
					}
					
					
					JFrame frame=new JFrame("Detail");  
			        frame.setLayout(new GridLayout(2,2,10,10));  
			       
			        //String[] columnNames = {"A","B"}; 
			        
			        String[] columnNames = {"empName", "projectId", "department", "date", "startTime", "endTime", "timing", "percentage", "reportTo", "comments", "status", "operationDate"};   //headers
			        String[][] tablevalues=new String[nb][12];
			        for(int j=0;j<nb;j++){
			        	tablevalues[j]=detailData.get(j);
			        }
			        
			        //String[][] tableVales={{"A1","B1"},{"A2","B2"},{"A3","B3"},{"A4","B4"},{"A5","B5"}}; //data
			        DefaultTableModel tableModel = new DefaultTableModel(tablevalues,columnNames);
			        JTable table = new JTable(tableModel);
			        JScrollPane scrollPane = new JScrollPane(table);
			        			        
			        frame.setBounds(new Rectangle(1400, 600));
			        frame.add(scrollPane);
			        frame.setLocationRelativeTo(null); 
			        frame.setVisible(true);  
			         		        
				
				}
			} 
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
	}
	
	
	public void search(String key1, String key2, String key3, String key4){
		rowData.removeAllElements();
		for(int i=0;i<dataBackup.size();i++){
			rowData.add((Vector)dataBackup.get(i));		
		}
		this.fireTableDataChanged();	//inform model data change
		
		if(!(key1.equals("All catalogues"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(2).toString().equals(key1)){
					rowData.remove(i);
					i--;
				}
			}
		}
		if(!(key2.equals("All projects"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(3).toString().equals(key2)){
					rowData.remove(i);
					i--;
				}
			}
		}
		if(!(key3.equals("All departments"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(4).toString().equals(key3)){
					rowData.remove(i);
					i--;
				}
			}
		}
		if(!(key4.equals("All status"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(12).toString().equals(key4)){
					rowData.remove(i);
					i--;
				}
			}
		}
		
	}
}

