package com.pcms.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.pcms.model.ProjectModel;
import com.pcms.model.UpdateModel;

public class PanelUpdate extends JPanel implements ActionListener{

	JTable jt=null;
	UpdateModel am=null;
	JScrollPane jsp=null;
	JPanel jpSouth=null;
	String empName;
	Vector teams;
	
	public PanelUpdate(String empName,Vector teams){
		//build project model
		am=new UpdateModel(empName, teams);
		jt=new JTable(am);
		jsp=new JScrollPane(jt);
		jpSouth=new JPanel();
		this.empName=empName;
		this.teams=teams;
		
		//buttons
		jpSouth.add(buildButton("Add"));
		jpSouth.add(buildButton("Submit"));
		jpSouth.add(buildButton("Delete"));
		jpSouth.add(buildButton("Cancel"));
		
		//Fiddle with the column's cell editors/renderers. 
		/*
		for(int i=0;i<3;i++){
			jt.ReportToColumn(jt.getColumnModel().getColumn(i), teams); 
		}
		*/
		am.commentsColumn(jt.getColumnModel().getColumn(8));
		
		//fin
		this.setLayout(new BorderLayout());
		this.add(jsp,BorderLayout.CENTER);
		this.add(jpSouth, BorderLayout.SOUTH);
		
		//listening column
		jt.addMouseListener(new MouseAdapter(){
		    public void mouseClicked(MouseEvent e){
		        if(e.getClickCount() == 1){
		        	
			            am.calculate();
			            jt.updateUI();
		            
		        }
		    }
		});
	}
	
	public JButton buildButton(String name) {  
		JButton jb= new JButton(name);  
		//jmi.addActionListener(panelAction); 
		jb.addActionListener(this);
		jb.setActionCommand(name);
		return jb;  
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Cancel")){
			this.removeAll();
			PanelProjects pa=new PanelProjects(empName,"My Projects");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(900,400));
			this.add(pa);
			this.updateUI();
			
		} else if(e.getActionCommand().equals("Delete")){
			int rowNum=this.jt.getSelectedRow();
			if(rowNum==-1){
				JOptionPane.showMessageDialog(this,"Select a row");
				return;
			} 
			//delete in jtable
			am.deleteRecord(rowNum);
			jt.updateUI();
		} else if(e.getActionCommand().equals("Add")){
			am.addRecord();
			jt.updateUI();
		} else if(e.getActionCommand().equals("Submit")){
			if(am.submit()){	
				this.removeAll();
				PanelProjects pa=new PanelProjects(empName,"My Projects");
				pa.setVisible(true);
				pa.setPreferredSize(new Dimension(900,400));
				this.add(pa);
				this.updateUI();
			} else {
				
			}
		}
	}
	
}