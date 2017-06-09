package com.pcms.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import com.pcms.db.*;
import java.sql.*;
import java.util.Vector;

public class AbbreviationModel extends AbstractTableModel{
	Vector rowData, columnNames;
	
	public AbbreviationModel(){
		//jtable
		columnNames=new Vector();
		
		columnNames.add("Abbreviation");
		columnNames.add("Meaning");
	
		rowData=new Vector();
		SqlHelper sh=new SqlHelper();
		ResultSet rs=sh.query("select * from abbreviation");
		try {
			while(rs.next()){
				Vector hang=new Vector();
				
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
			    
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

