package com.pcms.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import com.pcms.db.*;
import java.sql.*;
import java.util.Vector;

public class AttendanceModel extends AbstractTableModel {
	Vector rowData, columnNames;
	
	public AttendanceModel(String empName){
		columnNames = new Vector();
		columnNames.add("Date&Time");
		columnNames.add("Result");
//		columnNames.add("ID");
		columnNames.add("Terminal");
		columnNames.add("IP");
		String empId=null;
		rowData=new Vector();
		SqlHelper sh=new SqlHelper();
	
		String sql="select empId from employees where empName=?";
		String []paras={empName};
		ResultSet rs=sh.query(sql,paras);
		
		try {
			rs.next();
			empId=String.valueOf(rs.getInt(1));
			System.out.printf(empId);
			System.out.printf("\r\n");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		rs=sh.query("select * from attendance");
		try {
			while(rs.next()){
				
//				System.out.printf(rs.getString(3));
//				System.out.printf("\r\n");
				if(rs.getString(3).length()>=3&&empId.equals(rs.getString(3).substring(0,5))){
					Vector hang=new Vector();
					hang.add(rs.getString(1));
					hang.add(rs.getString(2));
					hang.add(rs.getString(4));
					hang.add(rs.getString(5));
					rowData.add(hang);}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sh.close();
		}
	}
	
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

}