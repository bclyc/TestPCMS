package com.pcms.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.pcms.model.ContactModel;
import com.pcms.model.UserModel;

public class PanelContact extends JPanel {
	JTable jt=null;
	ContactModel am=null;
	JScrollPane jsp=null;
	
	public PanelContact(){
		am=new ContactModel();
		jt=new JTable(am);
		jsp=new JScrollPane(jt);

		jt.setRowHeight(20);
		
		//jt.setGridColor(Color.black);
		this.setLayout(new BorderLayout());
		this.add(jsp);
	}
}