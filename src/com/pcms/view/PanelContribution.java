package com.pcms.view;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.Vector;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.pcms.db.SqlHelper;
import com.pcms.excel.DownExcel;
import com.pcms.model.AbbreviationModel;
import com.pcms.model.ContributionModel;
import com.pcms.model.UserModel;
import com.pcms.model.ValidationModel;
import com.pcms.view.PanelProjects.DateLabelFormatter;
import com.pcms.view.PanelValidation.MyCheckBoxRenderer;

import combobox_multi.CheckListCellRenderer;
import combobox_multi.CheckValue;
import combobox_multi.MyComboBox;

public class PanelContribution extends JPanel implements ActionListener, ItemListener{

	JTable jt=null;
	ContributionModel am=null;
	JScrollPane jsp=null;
	JPanel jpNorth=null;	
	JPanel jpSouth=null;	
	JPanel jpSearch=null;
	
	JPanel jpCatalogue=null;
	JPanel jpProject=null;
	JPanel jpDepartment=null;
	JPanel jpEmp=null;
	JPanel jpD1=null;
	JPanel jpD2=null;
	JPanel jpS=null;
	JDatePickerImpl datePicker1, datePicker2;
	
	
	MyComboBox jcbCatalogue=null;
	MyComboBox jcbProject=null;
	MyComboBox jcbDepartment=null;
	MyComboBox jcbEmp=null;
	
	
	Vector cataCombo;
	Vector projCombo;
	Vector depCombo;
	Vector empCombo;
	
	JComboBox jcbStatus=null;
	
	
	
	
	Vector teams;
	
	String empName;
	String searchby;
	int accessLvl;
	
	public PanelContribution(String empName,String searchby){
		//build project model
		this.empName = empName;
		this.searchby = searchby;
		
		//access level
		try {
			SqlHelper sh=new SqlHelper();
			ResultSet rs=null;		
			String sql="select accessLvl from employees"				
					+ " where empName=?";	
			String []paras={empName};
			rs=sh.query(sql,paras);
			rs.next();
			this.accessLvl = Integer.valueOf(rs.getString(1));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
			
		
		//search bar			
												
		jsp=new JScrollPane(jt);
		
		jpSearch=new JPanel();
		
		String []allcatalogues = null;
		String []allprojets=null;
		String []allDepartment=null;
		String []allemp=null;
		if(this.searchby=="Search by Project"){	
			allcatalogues=new String[]{"All catalogues","R&T","Development","Production"};
			allprojets=new String[]{"All projects"};
			allDepartment=new String[]{"All departments"};
			//String []allprojets=am.allprojets();
			//String []allDepartment=am.allteams();
			allemp=new String[]{"All employees"};
		}else if(this.searchby=="Search by Employee"){			
			//String []allemp={"All employees"};	
			try {
				allemp=am.allemps();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			allcatalogues=new String[]{"All catalogues"};
			allprojets=new String[]{"All projects"};
			allDepartment=new String[]{"All departments"};					
		}
		
		
		jpCatalogue=new JPanel();				
		jpCatalogue.setLayout(new GridLayout(2,1));
		jpProject=new JPanel();				
		jpProject.setLayout(new GridLayout(2,1));
		jpDepartment=new JPanel();				
		jpDepartment.setLayout(new GridLayout(2,1));
		jpEmp=new JPanel();				
		jpEmp.setLayout(new GridLayout(2,1));
		jpD1=new JPanel();				
		jpD1.setLayout(new GridLayout(2,1));
		jpD2=new JPanel();				
		jpD2.setLayout(new GridLayout(2,1));
		jpS=new JPanel();				
		jpS.setLayout(new GridLayout(2,1));
		
		JLabel jl1=new JLabel("Catalogue:");
		JLabel jl2=new JLabel("Project:");
		JLabel jl3=new JLabel("Department:");
		JLabel jl4=new JLabel("Employee:");
		JLabel jl5=new JLabel("Date From:");
		JLabel jl6=new JLabel("To:");
		JLabel jl7=new JLabel(" ");
		
		jcbCatalogue=new MyComboBox(allcatalogues);
		jcbCatalogue.setRenderer(new CheckListCellRenderer());
		jcbCatalogue.setSelectedItem(null);
		jcbCatalogue.addItemListener(this);
		jcbCatalogue.addActionListener(this);
		jcbProject=new MyComboBox(allprojets);
		jcbProject.setRenderer(new CheckListCellRenderer());
		jcbProject.setSelectedItem(null);
		jcbProject.addItemListener(this);
		jcbProject.addActionListener(this);
		jcbDepartment=new MyComboBox(allDepartment);
		jcbDepartment.setRenderer(new CheckListCellRenderer());
		jcbDepartment.setSelectedItem(null);
		jcbDepartment.addItemListener(this);
		jcbDepartment.addActionListener(this);
		
		if(this.accessLvl==0){
			jcbEmp=new MyComboBox( new String[]{"All employees", this.empName});
		}else if(this.accessLvl==1){
			jcbEmp=new MyComboBox(allemp);
		}
		
		jcbEmp.setRenderer(new CheckListCellRenderer());
		jcbEmp.setSelectedItem(null);
		jcbEmp.addItemListener(this);
		jcbEmp.addActionListener(this);
		
		
		//date picker
		Locale.setDefault(Locale.ENGLISH);
		UtilDateModel datemodel1=new UtilDateModel();
		UtilDateModel datemodel2=new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel1 = new JDatePanelImpl(datemodel1, p);
		JDatePanelImpl datePanel2 = new JDatePanelImpl(datemodel2, p);
//				datePanel1.setPreferredSize(new Dimension(300,200));
//				datePanel2.setPreferredSize(new Dimension(300,200));
		datePicker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
		datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());			    
		
		
		
		jpCatalogue.add(jl1);
		jpCatalogue.add(jcbCatalogue);
		jpProject.add(jl2);
		jpProject.add(jcbProject);
		jpDepartment.add(jl3);
		jpDepartment.add(jcbDepartment);
		jpEmp.add(jl4);
		jpEmp.add(jcbEmp);
		jpD1.add(jl5);
		jpD1.add(datePicker1);
		jpD2.add(jl6);
		jpD2.add(datePicker2);
		jpS.add(jl7);
		jpS.add(buildButton("Search"));
		
		
		
		
		
		JPanel jpblank=new JPanel();
		jpblank.setPreferredSize(new Dimension(100,100));
		

		int jpSearchComWidth = MainFrame.width/9;
		int jpSearchComHeight = MainFrame.height/14;
		jpSearch.setPreferredSize(new Dimension(MainFrame.width, 100)); 
		
		jpCatalogue.setPreferredSize(new Dimension(jpSearchComWidth, jpSearchComHeight));
		jpProject.setPreferredSize(new Dimension(jpSearchComWidth, jpSearchComHeight));
		jpDepartment.setPreferredSize(new Dimension(jpSearchComWidth, jpSearchComHeight));
		jpEmp.setPreferredSize(new Dimension(jpSearchComWidth, jpSearchComHeight));
		jpblank.setPreferredSize(new Dimension(MainFrame.width/24, jpSearchComHeight));
		jpD1.setPreferredSize(new Dimension(MainFrame.width/7, jpSearchComHeight));
		jpD2.setPreferredSize(new Dimension(MainFrame.width/7, jpSearchComHeight));
		jpS.setPreferredSize(new Dimension(jpSearchComWidth/2, jpSearchComHeight));
		
		
		if(this.searchby=="Search by Project"){	
			
			jpSearch.add(jpCatalogue);
			jpSearch.add(jpProject);
			jpSearch.add(jpDepartment);
			jpSearch.add(jpEmp);			
			jpSearch.add(jpblank);
			jpSearch.add(jpD1);
			jpSearch.add(jpD2);
			
			jpSearch.add(jpS);
			jcbProject.setEnabled(false);
			jcbDepartment.setEnabled(false);
			jcbEmp.setEnabled(false);
		}else if(this.searchby=="Search by Employee"){
			jpSearch.add(jpEmp);
			jpSearch.add(jpCatalogue);
			jpSearch.add(jpProject);
			jpSearch.add(jpDepartment);						
			jpSearch.add(jpblank);
			jpSearch.add(jpD1);
			jpSearch.add(jpD2);
			
			jpSearch.add(jpS);
			jcbCatalogue.setEnabled(false);
			jcbProject.setEnabled(false);
			jcbDepartment.setEnabled(false);
			
		}
		
				
		
		this.setLayout(new BorderLayout());
		this.add(jpSearch,BorderLayout.NORTH);
//			this.add(jsp,BorderLayout.CENTER);
//			this.add(jpSouth, BorderLayout.SOUTH);
		
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
		
		if(this.searchby=="Search by Project"){// Search by Project page comboboxs&buttons listener
			
			if(e.getActionCommand().equals("Search")){
				jsp.removeAll();
							
				
				DateModel dm1 = datePicker1.getModel();
				DateModel dm2 = datePicker2.getModel();
				String date1=dm1.getYear()+"-"+String.format("%02d", dm1.getMonth()+1)+"-"+String.format("%02d", dm1.getDay());
				String date2=dm2.getYear()+"-"+String.format("%02d", dm2.getMonth()+1)+"-"+String.format("%02d", dm2.getDay());
				System.out.println(date1+"\t"+date2);
				
				
				
				//check dates
				if(dm1.getYear()>dm2.getYear()) {
					JOptionPane.showMessageDialog(null, "The second date must be after the first one!","Wrong dates",JOptionPane.ERROR_MESSAGE);
					return;
				}else if((dm1.getYear()==dm2.getYear())&&(dm1.getMonth()>dm2.getMonth())){
					JOptionPane.showMessageDialog(null, "The second date must be after the first one!","Wrong dates",JOptionPane.ERROR_MESSAGE);
					return;
				}else if((dm1.getYear()==dm2.getYear())&&(dm1.getMonth()==dm2.getMonth())&&(dm1.getDay()>dm2.getDay())){
					JOptionPane.showMessageDialog(null, "The second date must be after the first one!","Wrong dates",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//check search keys	
				if((jcbCatalogue.getComboVc().size()<1)||(jcbProject.getComboVc().size()<1)||(jcbDepartment.getComboVc().size()<1)||(jcbEmp.getComboVc().size()<1))
				{
					JOptionPane.showMessageDialog(null, "Please complete search conditions!","Condition not selected",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Vector key1 = cataCombo;
				Vector key2 = projCombo;
				Vector key3 = depCombo;
				Vector key4 = empCombo;
				
				
				//build model, paint table
				am=new ContributionModel(this.empName,this.searchby, key1, key2, key3, key4, date1, date2);
				
				if(am.getRowCount()<=0){
					JOptionPane.showMessageDialog(null, "No record!","No record",JOptionPane.INFORMATION_MESSAGE);
				}else{			
					jt=new JTable(this.am);
					jt.getTableHeader().setReorderingAllowed(false);	// not allow dragging rows
					jt.setRowSorter(new TableRowSorter(am));	//header sorter
					jsp=new JScrollPane(jt);
					jpNorth=new JPanel();
					jpSouth=new JPanel();
					TableColumn tc2=jt.getColumnModel().getColumn(0);
					tc2.setPreferredWidth(10);
					this.add(jsp,BorderLayout.CENTER);
					this.updateUI();					
					
				}
			}else if(e.getSource()==jcbCatalogue){	//select catalogue
				jcbCatalogue.itemSelected();
				if(jcbCatalogue.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					cataCombo=jcbCatalogue.getComboVc();
					System.out.println(jcbCatalogue.getComboVc());
					System.out.println(getComboStr(cataCombo));
					
					String sql="select DISTINCT projectId from teams"				
							+ " where catalogue IN"+getComboStr(cataCombo)+" order by projectId ASC";	
					//String []paras={"('R&T','Development')"};
					//rs=sh.query(sql,paras);
					rs=sh.query(sql);
					
					System.out.println(sql);
					
					Vector<String> allcata=new Vector<String>();
					allcata.add("All projects");
					try {
						while(rs.next()){					
							allcata.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
					
					System.out.println(allcata);
					
					jcbProject.removeAllItems();
					for (String i : allcata){
						jcbProject.addItem(new CheckValue(false, i));
					}
					jcbProject.setEnabled(true);
					CheckValue cv1 = (CheckValue)jcbProject.getItemAt(0);
					cv1.bolValue=false;
					
									
					SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							/*选中后依然保持当前弹出状态*/
							jcbCatalogue.showPopup();
						}
					});				
					
				}
//				
			}else if(e.getSource()==jcbProject){
				jcbProject.itemSelected();
				if(jcbProject.getComboVc().isEmpty()==true){
										
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					
					projCombo = jcbProject.getComboVc();
					System.out.println(jcbProject.getComboVc());
					System.out.println(getComboStr(projCombo));;
					
					String sql="select DISTINCT department from teams"				
							+ " where catalogue IN"+getComboStr(cataCombo)+" and projectId IN"+getComboStr(projCombo)+" order by department ASC";
					
					//String []paras={"('R&T','Development')"};
					//rs=sh.query(sql,paras);
					rs=sh.query(sql);
					
					System.out.println(sql);
					
					Vector<String> allcata=new Vector<String>();
					allcata.add("All departments");
					try {
						while(rs.next()){					
							allcata.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
					
					System.out.println(allcata);
					
					jcbDepartment.removeAllItems();
					for (String i : allcata){
						jcbDepartment.addItem(new CheckValue(false, i));
					}
//					
					jcbDepartment.setEnabled(true);
					CheckValue cv1 = (CheckValue)jcbDepartment.getItemAt(0);
					cv1.bolValue=false;
					
					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									/*选中后依然保持当前弹出状态*/
									jcbProject.showPopup();
								}
					});			
				}
			}else if(e.getSource()==jcbDepartment){
				jcbDepartment.itemSelected();
				if(jcbDepartment.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
							
					
					depCombo = jcbDepartment.getComboVc();
					System.out.println(jcbDepartment.getComboVc());
					System.out.println(getComboStr(projCombo));;
					
					String sql="select DISTINCT empName from empteam"				
							+ " where teamId=any(select teamId from teams where catalogue IN"+getComboStr(cataCombo)+" and projectId IN"+getComboStr(projCombo)+" and department IN"+getComboStr(depCombo)+") order by empName ASC";
					
					
					rs=sh.query(sql);
					
					System.out.println(sql);
					
					Vector<String> allcata=new Vector<String>();
					allcata.add("All employees");
					try {
						while(rs.next()){					
							allcata.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
					
					System.out.println(allcata);
					
					jcbEmp.removeAllItems();
					for (String i : allcata){
						jcbEmp.addItem(new CheckValue(false, i));
					}
					
					jcbEmp.setEnabled(true);
					CheckValue cv1 = (CheckValue)jcbEmp.getItemAt(0);
					cv1.bolValue=false;
					
					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									/*选中后依然保持当前弹出状态*/
									jcbDepartment.showPopup();
								}
					});	
				}
			}else if(e.getSource()==jcbEmp){
				jcbEmp.itemSelected();
				if(jcbEmp.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					
					empCombo = jcbEmp.getComboVc();
					System.out.println(jcbEmp.getComboVc());
					System.out.println(getComboStr(empCombo));;
					
					
					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									/*选中后依然保持当前弹出状态*/
									jcbEmp.showPopup();
								}
					});	
				}
			}
			
		}else if(this.searchby=="Search by Employee"){  // Search by Employee page comboboxs&buttons listener
			if(e.getActionCommand().equals("Search")){
				jsp.removeAll();
							
				
				DateModel dm1 = datePicker1.getModel();
				DateModel dm2 = datePicker2.getModel();
				String date1=dm1.getYear()+"-"+String.format("%02d", dm1.getMonth()+1)+"-"+String.format("%02d", dm1.getDay());
				String date2=dm2.getYear()+"-"+String.format("%02d", dm2.getMonth()+1)+"-"+String.format("%02d", dm2.getDay());
				System.out.println(date1+"/t"+date2);
				
				//check dates
				if(dm1.getYear()>dm2.getYear()) {
					JOptionPane.showMessageDialog(null, "The second date must be after the first one!","Wrong dates",JOptionPane.ERROR_MESSAGE);
					return;
				}else if((dm1.getYear()==dm2.getYear())&&(dm1.getMonth()>dm2.getMonth())){
					JOptionPane.showMessageDialog(null, "The second date must be after the first one!","Wrong dates",JOptionPane.ERROR_MESSAGE);
					return;
				}else if((dm1.getYear()==dm2.getYear())&&(dm1.getMonth()==dm2.getMonth())&&(dm1.getDay()>dm2.getDay())){
					JOptionPane.showMessageDialog(null, "The second date must be after the first one!","Wrong dates",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				//check search keys	
				if((jcbCatalogue.getSelectedItem()==null)||(jcbProject.getSelectedItem()==null)||(jcbDepartment.getSelectedItem()==null)||(jcbEmp.getSelectedItem()==null))
				{
					JOptionPane.showMessageDialog(null, "Please select search conditions!","Condition not selected",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Vector key1 = cataCombo;
				Vector key2 = projCombo;
				Vector key3 = depCombo;
				Vector key4 = empCombo;
				
				
				//build model, paint table
				am=new ContributionModel(this.empName,this.searchby, key1, key2, key3, key4, date1, date2);
				
				if(am.getRowCount()<=0){
					JOptionPane.showMessageDialog(null, "No record!","No record",JOptionPane.INFORMATION_MESSAGE);
				}else{			
					jt=new JTable(this.am);
					jt.getTableHeader().setReorderingAllowed(false);	// not allow dragging rows
					jt.setRowSorter(new TableRowSorter(am));	//header sorter
					jsp=new JScrollPane(jt);
					jpNorth=new JPanel();
					jpSouth=new JPanel();
					TableColumn tc2=jt.getColumnModel().getColumn(0);
					tc2.setPreferredWidth(10);
					this.add(jsp,BorderLayout.CENTER);
					this.updateUI();					
				}
			}else if(e.getSource()==jcbEmp){
				jcbEmp.itemSelected();
				if(jcbEmp.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					
					empCombo = jcbEmp.getComboVc();
					System.out.println(empCombo);
					System.out.println(getComboStr(empCombo));;
					
					String sql="select DISTINCT catalogue from teams"				
							+ " where teamId=any(select teamId from empteam where empName IN"+getComboStr(empCombo)+")";	
					//String []paras={"('R&T','Development')"};
					//rs=sh.query(sql,paras);
					rs=sh.query(sql);
					
					System.out.println(sql);
					
					Vector<String> allcata=new Vector<String>();
					allcata.add("All Catalogues");
					try {
						while(rs.next()){					
							allcata.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
					
					System.out.println(allcata);
					
					jcbCatalogue.removeAllItems();
					for (String i : allcata){
						jcbCatalogue.addItem(new CheckValue(false, i));
					}
					jcbCatalogue.setEnabled(true);
					CheckValue cv1 = (CheckValue)jcbCatalogue.getItemAt(0);
					cv1.bolValue=false;
					
					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									/*选中后依然保持当前弹出状态*/
									jcbEmp.showPopup();
								}
					});	
				}
			}else if(e.getSource()==jcbCatalogue){	//select catalogue
				jcbCatalogue.itemSelected();
				if(jcbCatalogue.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					
					cataCombo=jcbCatalogue.getComboVc();
					System.out.println(jcbCatalogue.getComboVc());
					System.out.println(getComboStr(cataCombo));
					
					String sql="select DISTINCT projectId from teams"				
							+ " where catalogue IN"+getComboStr(cataCombo)+" and teamId=any(select teamId from empteam where empName IN"+getComboStr(empCombo)+") order by projectId ASC";	
					//String []paras={"('R&T','Development')"};
					//rs=sh.query(sql,paras);
					rs=sh.query(sql);
					
					System.out.println(sql);
					
					Vector<String> allcata=new Vector<String>();
					allcata.add("All projects");
					try {
						while(rs.next()){					
							allcata.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
					
					System.out.println(allcata);
					
					jcbProject.removeAllItems();
					for (String i : allcata){
						jcbProject.addItem(new CheckValue(false, i));
					}
					jcbProject.setEnabled(true);
					CheckValue cv1 = (CheckValue)jcbProject.getItemAt(0);
					cv1.bolValue=false;
					
									
					SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							/*选中后依然保持当前弹出状态*/
							jcbCatalogue.showPopup();
						}
					});				
					
				}
//				
			}else if(e.getSource()==jcbProject){
				jcbProject.itemSelected();
				if(jcbProject.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					
					projCombo = jcbProject.getComboVc();
					System.out.println(jcbProject.getComboVc());
					System.out.println(getComboStr(cataCombo));;
					
					String sql="select DISTINCT department from teams"				
							+ " where catalogue IN"+getComboStr(cataCombo)+" and projectId IN"+getComboStr(projCombo)+" and teamId=any(select teamId from empteam where empName IN"+getComboStr(empCombo)+") order by department ASC";
					
					//String []paras={"('R&T','Development')"};
					//rs=sh.query(sql,paras);
					rs=sh.query(sql);
					
					System.out.println(sql);
					
					Vector<String> allcata=new Vector<String>();
					allcata.add("All departments");
					try {
						while(rs.next()){					
							allcata.add(rs.getString(1));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}			
					
					System.out.println(allcata);
					
					jcbDepartment.removeAllItems();
					for (String i : allcata){
						jcbDepartment.addItem(new CheckValue(false, i));
					}
//					
					jcbDepartment.setEnabled(true);
					CheckValue cv1 = (CheckValue)jcbDepartment.getItemAt(0);
					cv1.bolValue=false;
					
					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									/*选中后依然保持当前弹出状态*/
									jcbProject.showPopup();
								}
					});			
				}
			}else if(e.getSource()==jcbDepartment){
				jcbDepartment.itemSelected();
				if(jcbDepartment.getComboVc().isEmpty()==true){
					
				}else{
					SqlHelper sh=new SqlHelper();
					ResultSet rs=null;				
								
					
					depCombo = jcbDepartment.getComboVc();
					System.out.println(jcbDepartment.getComboVc());
					System.out.println(getComboStr(depCombo));;
													
					
					SwingUtilities.invokeLater(
							new Runnable() {
								public void run() {
									/*选中后依然保持当前弹出状态*/
									jcbDepartment.showPopup();
								}
					});	
				}
			}
			
		}
		
	}

	
	
	@Override	
	public void itemStateChanged(ItemEvent e) {
		// searchbar select 
		if(e.getSource()==jcbCatalogue){ // select catalogue
			
		}else if(e.getSource()==jcbProject){	//then select project
//			
		}else if(e.getSource()==jcbDepartment){	
			
		}
		
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
	
	public String getComboStr(Vector comboV){
		
		String comboSt = "(";
		for (int i=0; i < comboV.size(); i++){
			if(i != (comboV.size()-1)){
				comboSt = comboSt +"'"+ (String)comboV.get(i)+"',";
			}else{
				comboSt = comboSt +"'"+ (String)comboV.get(i)+"')";
			}				
		}
		
		
		return comboSt;
	}
	
}
