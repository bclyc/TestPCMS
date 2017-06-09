package com.pcms.view;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.pcms.model.AddressModel;
import com.pcms.model.ProjectModel;
import com.pcms.model.UserModel;
import com.pcms.view.PanelEfficiency.DateLabelFormatter;
import com.pcms.db.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;


public class PanelProjects extends JPanel implements ActionListener, ItemListener{
	String empName,project;
	JLabel jl2=null,jl3,jl4;
	JDatePickerImpl datePicker1, datePicker2;
	
	JTable jt=null;
	ProjectModel am=null;
	JScrollPane jsp=null;
	JPanel jpSouth=null;
	JPanel jpSearch=null;
	
	JComboBox jcbCatalogue=null;
	JComboBox jcbProject=null;
	JComboBox jcbTeam=null;
	JComboBox jcbStatus=null;
	
	
	/**
	 * @param empName
	 * @param project
	 * @param status
	 * @throws SQLException
	 */
	/**
	 * @param empName
	 * @param project
	 * @param status
	 * @throws SQLException
	 */
	/**
	 * @param empName
	 * @param project
	 * @param status
	 * @throws SQLException
	 */
	/**
	 * @param empName
	 * @param project
	 * @param status
	 * @throws SQLException
	 */
	/**
	 * @param empName
	 * @param project
	 * @param status
	 */
	/**
	 * @param empName
	 * @param project
	 * @param status
	 */
	/**
	 * @param empName
	 * @param project
	 * @param status
	 */
	public PanelProjects(String empName, String project) {
		//build project model
		am=new ProjectModel(empName, project);
		if(am.getRowCount()>0){
			jt=new JTable(am);
			jt.getTableHeader().setReorderingAllowed(false);	// not allow dragging rows
			jt.setRowSorter(new TableRowSorter(am));	//hearder sorter
			jsp=new JScrollPane(jt);
			jpSouth=new JPanel();
			this.empName=empName;
			this.project=project;
			TableColumn tc2=jt.getColumnModel().getColumn(0);
			tc2.setPreferredWidth(10);
			//jt.setRowHeight(20);
			
			//checkbox
			jt.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
			
			final MyCheckBoxRenderer check = new MyCheckBoxRenderer();
			jt.getColumnModel().getColumn(0).setHeaderRenderer(check);
			//header listening
			jt.getTableHeader().addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e){
			        if(jt.getColumnModel().getColumnIndexAtX(e.getX())==0){ //select box header      	
			            JCheckBox Checkbox = (JCheckBox)check;
			            boolean b = !check.isSelected();
			            check.setSelected(b);
			            jt.getTableHeader().repaint();
			            for(int i=0;i<jt.getRowCount();i++){
			                jt.getModel().setValueAt(b, i, 0);
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
			            if(columnIndex == 0) { //select box
			            	
			                if(jt.getValueAt(rowIndex,columnIndex) == null){ 
			                      jt.setValueAt(false, rowIndex, columnIndex);
			                  }
	
			                if(((Boolean)jt.getValueAt(rowIndex,columnIndex)).booleanValue()){ 
			                      jt.setValueAt(false, rowIndex, 0); 
			                  }
			                else {
			                      jt.setValueAt(true, rowIndex, 0);
			                  }
			             }
			            jt.updateUI();
			        }
			    }
			});
			//jt.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
			//jt.setGridColor(Color.black);
			
			
			//search bar
			jpSearch=new JPanel();
			
		
			String []allcatalogues={"All catalogues","R&T","Development","Production"};
			String []allstatus={"All status","Not launched","Processing","Achieved","Delayed"};
			
			
			try {
				jcbCatalogue=new JComboBox(allcatalogues);
				jcbCatalogue.addItemListener(this);
				jcbProject=new JComboBox(am.allprojets());
				jcbProject.addItemListener(this);
				jcbTeam=new JComboBox(am.allteams());
				jcbTeam.addItemListener(this);
				jcbStatus=new JComboBox(allstatus);
				jcbStatus.addItemListener(this);
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
								
			jl2=new JLabel("From : ");
			jl3=new JLabel(" to ");
			
			Locale.setDefault(Locale.ENGLISH);
			UtilDateModel datemodel1=new UtilDateModel();
			UtilDateModel datemodel2=new UtilDateModel();
			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			JDatePanelImpl datePanel1 = new JDatePanelImpl(datemodel1, p);
			JDatePanelImpl datePanel2 = new JDatePanelImpl(datemodel2, p);
//			datePanel1.setPreferredSize(new Dimension(300,200));
//			datePanel2.setPreferredSize(new Dimension(300,200));
		    datePicker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
		    datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		    
		    
			JTextField jtfStartTime=new JTextField("Start time");
			JTextField jtfEndTime=new JTextField("End time");
			//jcbCatalogue.setBounds(10, 5, 105, 21);
			//jcbCatalogue.setMaximumRowCount(4);
			
			//jpSearch.setLayout(new GridLayout());
			jpSearch.add(jcbCatalogue);
			jpSearch.add(jcbProject);
			jpSearch.add(jcbTeam);
			jpSearch.add(jcbStatus);
//			jpSearch.add(jtfStartTime);
//			jpSearch.add(jtfEndTime);
//			jpSearch.add(search);
		    jpSearch.add(jl2);	    	
		    jpSearch.add(datePicker1);
		    jpSearch.add(jl3);
		    jpSearch.add(datePicker2);
		    jpSearch.add(buildButton("Search"));
		    jpSearch.add(buildButton("Cancel"));
			
			
			
			//buttons
			if(project.equals("My Projects")){
				jpSouth.add(buildButton("Update"));
			}
			jpSouth.add(buildButton("Show Efficiency"));
			
			
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
		if(e.getActionCommand().equals("Update")){
			Vector<String> teams=new Vector<String>();
			for(int i=0;i<jt.getRowCount();i++){
				if(((Boolean)jt.getValueAt(i,0)).booleanValue()){
					teams.add(jt.getValueAt(i, 2).toString());
					teams.add(jt.getValueAt(i, 3).toString());
				}
            }
		
			if(teams.size()>0){
				this.removeAll();
				PanelUpdate up=new PanelUpdate(empName,teams);
				this.add(up);
				up.setVisible(true);
				this.updateUI();
			} else {
				JOptionPane.showMessageDialog(null, "Select a row!","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		if(e.getActionCommand().equals("Cancel")){
			//back to the original table
			am.refresh();
			jcbCatalogue.setSelectedItem("Catalogue");
			jcbProject.setSelectedItem("Project");
			jcbTeam.setSelectedItem("Team");
			this.updateUI();
		}
		if(e.getActionCommand().equals("Show Efficiency")){
			String projectId=null;
			String department=null;
			for(int i=0;i<jt.getRowCount();i++){
				if(((Boolean)jt.getValueAt(i,0)).booleanValue()){		
					projectId=jt.getValueAt(i, 2).toString();
					department=jt.getValueAt(i, 3).toString();
				}
            }
			
			//at least one row selected
			if(projectId!=null){				
				this.removeAll();
				PanelEfficiency up=new PanelEfficiency(empName,projectId,department,project);
				this.add(up);
				up.setVisible(true);
				this.updateUI();
			} else {
				JOptionPane.showMessageDialog(null, "Select a row!","Error",JOptionPane.ERROR_MESSAGE);
			}	
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
				
		if(e.getStateChange() == ItemEvent.SELECTED){
			
			RowSorter rowSorter = jt.getRowSorter();
			rowSorter.setSortKeys(null);	// reset row sorter
			
			am.search(jcbCatalogue.getSelectedItem().toString(), jcbProject.getSelectedItem().toString(), jcbTeam.getSelectedItem().toString(), jcbStatus.getSelectedItem().toString());
			
		}
		this.updateUI();
	}
	
	public class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }

	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}

}
