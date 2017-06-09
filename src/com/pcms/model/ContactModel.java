package com.pcms.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import com.pcms.db.*;
import java.sql.*;
import java.util.Vector;

public class ContactModel extends AbstractTableModel{
	Vector rowData, columnNames;
	
	public ContactModel(){
		//jtable
		columnNames=new Vector();
		
		columnNames.add("Name");
		columnNames.add("Post");
		columnNames.add("Extension");
		columnNames.add("Email");
		columnNames.add("Mobile");
		columnNames.add("Employee Number");
	
		rowData=new Vector();
		SqlHelper sh=new SqlHelper();
		String sql="select empName,post, extension, email, mobile, empId from employees";
		ResultSet rs=sh.query(sql);
		try {
			while(rs.next()){
				Vector hang=new Vector();
				
				hang.add(rs.getString(1).toUpperCase());
				hang.add(rs.getString(2).toUpperCase());
				hang.add(rs.getString(3));
				hang.add(rs.getString(4));
				hang.add(rs.getString(5));
				hang.add(rs.getString(6));
			    
				rowData.add(hang);
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

