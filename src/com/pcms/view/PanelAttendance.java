package com.pcms.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.pcms.model.AttendanceModel;


public class PanelAttendance extends JPanel{
	JTable jt=null;
	AttendanceModel am=null;
	JScrollPane jsp=null;
	
	public PanelAttendance(String empName){
		am=new AttendanceModel(empName);
		jt=new JTable(am);
		jsp=new JScrollPane(jt);

		jt.setRowHeight(20);
		
		//jt.setGridColor(Color.black);
		this.setLayout(new BorderLayout());
		this.add(jsp);
	}
}

