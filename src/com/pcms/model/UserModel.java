/**
 * Users' data model, for all operations for users
 */
package com.pcms.model;

import com.pcms.db.*;

import java.sql.*;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class UserModel extends AbstractTableModel{
	private SqlHelper sp=null;
	private String empName=null;
	private int empId=0;
	Vector rowData, columnNames;
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	//set empId
	public UserModel(){
	
	}
	
	/**
	 * 
	 * @param empId=userId
	 * @param empPw=password
	 * @return userPost, user don't exist if return value="".
	 */
	public String checkUser(String empName, String empPw){	
		String post="";
		String sql="select employees.post from employees where empName=? and empPw=?";
		String paras[]={empName,empPw};
		sp=new SqlHelper();
		ResultSet rs=sp.query(sql, paras);
		try {
			if(rs.next()){
				post=rs.getString(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sp.close();
		}
		return post;
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
