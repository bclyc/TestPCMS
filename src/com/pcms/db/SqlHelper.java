/**
 * all operations to database : crud
 * 
 */
package com.pcms.db;

import java.util.*;
import java.sql.*;

public class SqlHelper {
	
	PreparedStatement ps=null;
	Connection ct=null;
	ResultSet rs=null;
	String driverName="com.mysql.jdbc.Driver";
	String url="jdbc:mysql://127.0.0.1:3306/pcmsystem?useUnicode=true&characterEncoding=utf8";
	String user="root";
	String password="lm2323";
	
	public SqlHelper(){
		try {
			Class.forName(driverName);
			ct=DriverManager.getConnection(url,user,password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql, String [] paras){
		try {
			ps=ct.prepareStatement(sql);
			for(int i=0;i<paras.length;i++){
				ps.setString(i+1, paras[i]);
			}
			
			rs=ps.executeQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet query(String sql){
		try {
			ps=ct.prepareStatement(sql);
			
			rs=ps.executeQuery();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public void update(String sql, String [] paras){
		try {
			ps=ct.prepareStatement(sql);
			for(int i=0;i<paras.length;i++){
				ps.setString(i+1, paras[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(String sql){
		try {
			ps=ct.prepareStatement(sql);			
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(ct!=null) ct.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
