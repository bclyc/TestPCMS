package com.pcms.view;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableColumn;
import com.pcms.model.AbbreviationModel;
import com.pcms.model.UserModel;

public class PanelAbbreviation extends JPanel {
	JTable jt=null;
	AbbreviationModel am=null;
	JScrollPane jsp=null;
	
	public PanelAbbreviation(){
		am=new AbbreviationModel();
		jt=new JTable(am);
		jsp=new JScrollPane(jt);

		TableColumn tc2=jt.getColumnModel().getColumn(1);
		tc2.setPreferredWidth(600);
		jt.setRowHeight(20);
		
		//jt.setGridColor(Color.black);
		this.setLayout(new BorderLayout());
		this.add(jsp);
	}
}

