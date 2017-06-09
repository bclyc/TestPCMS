package com.pcms.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.pcms.model.AddressModel;
import com.pcms.model.UserModel;

public class PanelAddress extends JPanel {
	JTable jt=null;
	AddressModel am=null;
	JScrollPane jsp=null;
	
	public PanelAddress(){
		am=new AddressModel();
		jt=new JTable(am);
		jsp=new JScrollPane(jt);

		jt.setRowHeight(20);
		
		//jt.setGridColor(Color.black);
		this.setLayout(new BorderLayout());
		this.add(jsp);
	}
}