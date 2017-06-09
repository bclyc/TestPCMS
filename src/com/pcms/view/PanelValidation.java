package com.pcms.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.pcms.model.ProjectModel;
import com.pcms.model.UpdateModel;
import com.pcms.model.ValidationModel;
import com.pcms.view.PanelProjects.MyCheckBoxRenderer;

public class PanelValidation extends JPanel implements ActionListener, ItemListener{

	JTable jt=null;
	ValidationModel am=null;
	JScrollPane jsp=null;
	JPanel jpNorth=null;	
	JPanel jpSouth=null;	
	JPanel jpSearch=null;
	
	JComboBox jcbEmp=null;
	JComboBox jcbProject=null;
	JComboBox jcbTeam=null;
	JComboBox jcbStatus=null;
	
	
	Vector teams;
	
	String empName;
	String status;
	
	public PanelValidation(String empName,String status){
		//build project model
		this.empName = empName;
		this.status = status;
		this.am=new ValidationModel(this.empName, this.status);
		if(this.am.getRowCount()>0){
			
			jt=new JTable(this.am);
			jt.getTableHeader().setReorderingAllowed(false);	// not allow dragging rows
			jt.setRowSorter(new TableRowSorter(am));	//header sorter
			jsp=new JScrollPane(jt);
			jpNorth=new JPanel();
			jpSouth=new JPanel();
			TableColumn tc2=jt.getColumnModel().getColumn(0);
			tc2.setPreferredWidth(10);
			
			//search bar
			jpSearch=new JPanel();
			
		
			String []allcatalogues={"All catalogues","R&T","Development","Production"};
			String []allstatus={"All status","Not launched","Processing","Achieved","Delayed"};
			
			
			try {
				
				jcbProject=new JComboBox(am.allprojets());
				jcbProject.addItemListener(this);
				jcbTeam=new JComboBox(am.allteams());
				jcbTeam.addItemListener(this);
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//jpSearch.setLayout(new GridLayout());
			
			jpSearch.add(jcbProject);
			jpSearch.add(jcbTeam);
			
			
			//buttons
			if(this.status.equals("Waiting List")){
				jpSouth.add(buildButton("Validate"));
				jpSouth.add(buildButton("Refuse"));
				jpSouth.add(buildButton("Show Detail"));
			}else{
				jpSouth.add(buildButton("Show Detail"));
			}
			
			//checkbox
			jt.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
			
			final MyCheckBoxRenderer check = new MyCheckBoxRenderer();
			jt.getColumnModel().getColumn(0).setHeaderRenderer(check);
			//header listening
			jt.getTableHeader().addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e){
			        if(jt.getColumnModel().getColumnIndexAtX(e.getX())==0){//濡傛灉鐐瑰嚮鐨勬槸绗�0鍒楋紝鍗砪heckbox杩欎竴鍒�       	
			            JCheckBox Checkbox = (JCheckBox)check;
			            boolean b = !check.isSelected();
			            check.setSelected(b);
			            jt.getTableHeader().repaint();
			            for(int i=0;i<jt.getRowCount();i++){
			                jt.getModel().setValueAt(b, i, 0);//鎶婅繖涓�鍒楅兘璁炬垚鍜岃〃澶翠竴鏍�
			            }
			            jt.updateUI();
			        }
			    }
			});
			//listening column
			jt.addMouseListener(new MouseAdapter(){
			    public void mouseClicked(MouseEvent e){
			        if(e.getClickCount() == 1){
			            int columnIndex = jt.columnAtPoint(e.getPoint()); 
			            int rowIndex = jt.rowAtPoint(e.getPoint());
			            if(columnIndex == 0) {//绗�0鍒楁椂锛屾墽琛屼唬鐮�
			            	
			                if(jt.getValueAt(rowIndex,columnIndex) == null){ //濡傛灉鏈垵濮嬪寲锛屽垯璁剧疆涓篺alse
			                      jt.setValueAt(false, rowIndex, columnIndex);
			                  }

			                if(((Boolean)jt.getValueAt(rowIndex,columnIndex)).booleanValue()){ //鍘熸潵閫変腑
			                      jt.setValueAt(false, rowIndex, 0); //鐐瑰嚮鍚庯紝鍙栨秷閫変腑
			                  }
			                else {//鍘熸潵鏈�変腑
			                      jt.setValueAt(true, rowIndex, 0);
			                  }
			             }
			            jt.updateUI();
			        }
			    }
			});
			
			
			this.setLayout(new BorderLayout());
			this.add(jpSearch,BorderLayout.NORTH);
			this.add(jsp,BorderLayout.CENTER);
			this.add(jpSouth, BorderLayout.SOUTH);
		} else {
			JLabel jl=new JLabel("No record found!");
			this.setLayout(new BorderLayout());
			this.add(jl,BorderLayout.CENTER);
		}
		
	}
	
	public JButton buildButton(String name) {  
		JButton jb= new JButton(name);  
		jb.addActionListener(this);
		jb.setActionCommand(name);
		return jb;  
	}

	class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer{
		 
	    public MyCheckBoxRenderer () {
	        this.setBorderPainted(true);
	    }
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        // TODO Auto-generated method stub      
	        return this;
	    }   
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int row0[] = new int[jt.getRowCount()];
		int j=0;
		for(int i=0;i<jt.getRowCount();i++){
			if(((Boolean)jt.getValueAt(i,0)).booleanValue()){
				row0[j++]=i;
			}
        }
		int row[] = new int[j];
		for(int i=0;i<j;i++){
			row[i]=row0[i];
		}
		
		if(e.getActionCommand().equals("Validate")){
			if(row.length==0){
				JOptionPane.showMessageDialog(null, "Please select a row!","Error",JOptionPane.ERROR_MESSAGE);
			}else{
				am.update("Validate",row);
				JOptionPane.showMessageDialog(null, "Operation succeeded.","Succeeded",JOptionPane.INFORMATION_MESSAGE);

				this.removeAll();
				PanelValidation pa=new PanelValidation(this.empName,this.status);
				pa.setVisible(true);
				this.add(pa);
				this.updateUI();					
			}			
		} else if(e.getActionCommand().equals("Refuse")){
			if(row.length==0){
				JOptionPane.showMessageDialog(null, "Please select a row!","Error",JOptionPane.ERROR_MESSAGE);
			}else{
				am.update("Refuse",row);
				JOptionPane.showMessageDialog(null, "Operation succeeded.","Succeeded",JOptionPane.INFORMATION_MESSAGE);
				
				this.removeAll();
				PanelValidation pa=new PanelValidation(this.empName,this.status);
				pa.setVisible(true);
				this.add(pa);
				this.updateUI();				
			}
		}else if(e.getActionCommand().equals("Show Detail")){
			if(row.length==0){
				JOptionPane.showMessageDialog(null, "Please select a row!","Error",JOptionPane.ERROR_MESSAGE);
			}else if(row.length>1){
				JOptionPane.showMessageDialog(null, "Please select only one row for detail!","Error",JOptionPane.ERROR_MESSAGE);
			}else{
				am.update("Detail",row);
				
				this.removeAll();
				PanelValidation pa=new PanelValidation(this.empName,this.status);
				pa.setVisible(true);
				this.add(pa);
				this.updateUI();				
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getStateChange() == ItemEvent.SELECTED){
			
			RowSorter rowSorter = jt.getRowSorter();
			rowSorter.setSortKeys(null);	// reset row sorter
			
			am.search("All catalogues", jcbProject.getSelectedItem().toString(), jcbTeam.getSelectedItem().toString(), "All status");
			
		}
		this.updateUI();
	}
	
}