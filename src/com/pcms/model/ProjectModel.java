package com.pcms.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import com.pcms.db.*;

import java.sql.*;
import java.util.Vector;

public class ProjectModel extends AbstractTableModel{
	Vector rowData, columnNames,dataBackup;

	public ProjectModel(String empName, String project){
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		//initialization jtable
		columnNames=new Vector();
		columnNames.add("Select all");
		columnNames.add("Catalogue");
		columnNames.add("Project");
		columnNames.add("Department");
		columnNames.add("status");
		columnNames.add("Report to");
		columnNames.add("Start time");
		columnNames.add("Deadline");
		columnNames.add("Personal advance");
		columnNames.add("Project advance");
		columnNames.add("Actual Working hours");
	
		//set rows
		rowData=new Vector();
		dataBackup=new Vector();
	
		

		//chose		
		if(project.equals("My Projects")){
			String sql="select catalogue, projectId, department, status, teamChief, startTime, deadline, actualHours, planHours from teams"
					+ " where teams.teamId=any(select teamId from empteam where empName=? and access=1)";
			String []paras={empName};
			rs=sh.query(sql,paras);
			
		} else if (project.equals("Team projects")){
			String sql="select catalogue, projectId, department, status, teamChief, startTime, deadline, actualHours, planHours from teams"
					+ " where teams.teamId=any(select teamId from empteam where empName=?)";
			String []paras={empName};
			rs=sh.query(sql,paras);
		} else if (project.equals("All projects")){
			String sql="select catalogue, projectId, department, status, teamChief, startTime, deadline, actualHours, planHours from teams"
					+ " where teams.teamId=any(select teamId from empteam where empName=?)";
			String []paras={empName};
			rs=sh.query(sql,paras);
		}
		try {
			while(rs.next()){
				Vector hang=new Vector();
				hang.add(new Boolean(false));
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				hang.add(rs.getString(5));
				hang.add(rs.getString(6));
				hang.add(rs.getString(7));
//				String sql="select totHours from empteam where teamId="
//						+ "(select teamId from teams where projectId=? and department=?)";
//						
//				String []paras={rs.getString(2),rs.getString(3)};
//				
//				ResultSet rs2=sh.query(sql,paras);
//				rs2.next();
//				hang.add(rs2.getString(1));
				hang.add(1);
				hang.add(rs.getString(8));
				hang.add(rs.getString(9));
	
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
	public void check(String sql, String []paras){
		
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
	public void refresh(){
		rowData.removeAllElements();
		for(int i=0;i<dataBackup.size();i++){
			rowData.add((Vector)dataBackup.get(i));
		}
		System.out.println(rowData);
	}
	//research in table according to catalogue,project and team

	public void search(String key1, String key2, String key3, String key4){
		rowData.removeAllElements();
		for(int i=0;i<dataBackup.size();i++){
			rowData.add((Vector)dataBackup.get(i));		
		}
		this.fireTableDataChanged();	//inform model data change
		
		if(!(key1.equals("All catalogues"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(1).toString().equals(key1)){
					rowData.remove(i);
					i--;
				}
			}
		}
		if(!(key2.equals("All projects"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(2).toString().equals(key2)){
					rowData.remove(i);
					i--;
				}
			}
		}
		if(!(key3.equals("All departments"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(3).toString().equals(key3)){
					rowData.remove(i);
					i--;
				}
			}
		}
		if(!(key4.equals("All status"))){				
			for(int i=0;i<rowData.size();i++){
				if(!((Vector)rowData.get(i)).get(4).toString().equals(key4)){
					rowData.remove(i);
					i--;
				}
			}
		}
		//this.fireTableDataChanged();	
	}

	
}
