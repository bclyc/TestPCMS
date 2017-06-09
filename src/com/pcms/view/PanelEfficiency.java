package com.pcms.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.JDatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import com.pcms.excel.DownExcel;
import com.pcms.model.ContributionModel;
import com.pcms.model.EfficiencyModel;

import com.mkk.swing.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.*;
import org.jdatepicker.util.*;
import org.jdatepicker.*;
 
//JFreeChart Line Chart锛堟姌绾垮浘锛�  
public class PanelEfficiency extends JPanel implements ActionListener{
	JLabel jl2=null,jl3,jl4;
	JTextField jtf;
	JPanel jpSouth,jpNorth, jpCenter;
	String empName,projectId,project,department;
	JComboBox jcbStartM,jcbStartY,jcbEndM,jcbEndY,jcbMember,jcbTeam;
	ContributionModel um=null;
	EfficiencyModel em=null;
	JFreeChart freeChart=null;
	CategoryDataset data=null;
	
	JDatePickerImpl datePicker1, datePicker2;
	
 
    public PanelEfficiency(String empName,String projectId, String department,String project) {
    	this.empName=empName;
		this.projectId=projectId;
		this.department=department;
		this.project=project;
    	//鍒濆鍖栭潰鏉�
    	initialisation();
    	System.out.println(4);
    }
 
    public void initialisation(){
    	jpSouth=new JPanel();
		jpNorth=new JPanel();
		jpCenter=new JPanel();

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
		datePanel1.setPreferredSize(new Dimension(300,200));
		datePanel2.setPreferredSize(new Dimension(300,200));
	    datePicker1 = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
	    datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
	    
	    
	    
		
//		String []team={"All Teams","BT----01","CC----01","CC----02","CI----01","EV----01","IC----01","IG----01",
//				"IT----01","JV----01","MGT----01","PA----01","TI----01","TU----01","TU----02",
//				"TU----03","TU----04","TO----01","NR----01","OB----01"};
//		String []startM={"January","February","March","April","May","June","July","August","September"
//				,"October","November","December"};
//		String []startY={"2015","2016","2017","2018"};
//		String []endM={"January","February","March","April","May","June","July","August","September"
//				,"October","November","December"};
//		String []endY={"2015","2016","2017","2018"};
//		jcbTeam=new JComboBox(team);
//		jcbStartM=new JComboBox(startM);
//		jcbStartY=new JComboBox(startY);
//		jcbEndM=new JComboBox(endM);
//		jcbEndY=new JComboBox(endY);
		//contributionmodel
//			um=new ContributionModel();
//			
//			if(project.equals("Team projects")){
//				Vector<String> member=um.searchMembers(projectId, department);
//				jcbMember=new JComboBox(member);
//				jpNorth.add(jcbMember);
//			}
			
//			jpNorth.add(jcbTeam);
//			jpNorth.add(jl2);
//			jpNorth.add(jcbStartM);
//			jpNorth.add(jcbStartY);
//			jpNorth.add(jl3);
//			jpNorth.add(jcbEndM);
//			jpNorth.add(jcbEndY);
	    	jpNorth.add(jl2);	    	
	    	jpNorth.add(datePicker1);
	    	jpNorth.add(jl3);
	    	jpNorth.add(datePicker2);
			jpNorth.add(buildButton("Search"));
			
			jpSouth.add(buildButton("Download"));
			jpSouth.add(buildButton("Cancel"));
			
			this.setLayout(new BorderLayout());
			this.add(jpNorth,BorderLayout.NORTH);
			this.add(jpCenter,BorderLayout.CENTER);
			this.add(jpSouth,BorderLayout.SOUTH);
    }
    public JButton buildButton(String name) {  
		JButton jb= new JButton(name);  
		jb.addActionListener(this);
		jb.setActionCommand(name);
		return jb;  
	}
    
    // 淇濆瓨涓烘枃浠�
    public static void saveAsFile(JFreeChart chart, String outputPath,
            int weight, int height) {
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            // 淇濆瓨涓篜NG
            // ChartUtilities.writeChartAsPNG(out, chart, 600, 400);
            // 淇濆瓨涓篔PEG
            ChartUtilities.writeChartAsJPEG(out, chart, 600, 400);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }
 
    // 
//    public JFreeChart createChart(CategoryDataset categoryDataset) {
//        //杩欓噷鐨�"name"鍙傛暟锛涙槸浠�涔堟剰鎬濇垜涔熶笉鐭ラ亾锛屽弽姝ｈ繖鏍峰彲浠ョ敤
//        StandardChartTheme standardChartTheme = new StandardChartTheme("name");
//        //鍙互鏀瑰彉杞村悜鐨勫瓧浣�
//        standardChartTheme.setLargeFont(new Font(Font.DIALOG,Font.BOLD, 12));
//        //鍙互鏀瑰彉鍥句緥鐨勫瓧浣�
//        standardChartTheme.setRegularFont(new Font(Font.DIALOG,Font.BOLD, 8));
//        //鍙互鏀瑰彉鍥炬爣鐨勬爣棰樺瓧浣�
//        standardChartTheme.setExtraLargeFont(new Font(Font.DIALOG,Font.BOLD, 20));
//        ChartFactory.setChartTheme(standardChartTheme);//璁剧疆涓婚
//        // 鍒涘缓JFreeChart瀵硅薄锛欳hartFactory.createLineChart
//        JFreeChart jfreechart =
//            ChartFactory.createLineChart("Efficiency of "+empName.toUpperCase(), // 鏍囬
//                "Date", // categoryAxisLabel 锛坈ategory杞达紝妯酱锛孹杞存爣绛撅級
//                "Total Hours", // valueAxisLabel锛坴alue杞达紝绾佃酱锛孻杞寸殑鏍囩锛�
//                categoryDataset, // dataset
//                PlotOrientation.VERTICAL, true, // legend
//                false, // tooltips
//                false); // URLs
// 
//        // 浣跨敤CategoryPlot璁剧疆鍚勭鍙傛暟銆備互涓嬭缃彲浠ョ渷鐣ャ��
//        CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();
//        // 鑳屾櫙鑹� 閫忔槑搴�
//        plot.setBackgroundAlpha(0.5f);
//        // 鍓嶆櫙鑹� 閫忔槑搴�
//        plot.setForegroundAlpha(0.5f);
//        // 鍏朵粬璁剧疆 鍙傝�� CategoryPlot绫�
// 
//        return jfreechart;
//    }
 

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("Search")){
			if(project.equals("Team projects")){
	        	this.empName=jcbMember.getSelectedItem().toString();
	        }
			
			Date selectedDate1 = (Date) datePicker1.getModel().getValue();
			Date selectedDate2 = (Date) datePicker2.getModel().getValue();
			if(selectedDate1.after(selectedDate2)){
				JOptionPane.showMessageDialog(null, "The second date should be after the first one!","Error",JOptionPane.ERROR_MESSAGE);
			}else{
							
				SimpleDateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd");
				String date1 = dateFormatter.format(selectedDate1);
				String date2 = dateFormatter.format(selectedDate2);
				
				System.out.println(selectedDate1);
				System.out.println(selectedDate2);
				System.out.println(date1);
				
				try {			        
					
			        this.jpCenter.removeAll();		        
			        this.jpCenter.add(new EfficiencyModel(date1,date2).getChartPanel());	
			        
			        this.updateUI();
			        
			        
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
	         
		} else if(e.getActionCommand().equals("Download")){
			if(freeChart!=null){
				JFileChooser jfc=new JFileChooser();
				jfc.setDialogTitle("Choose a file");
				jfc.showSaveDialog(null);
				jfc.setVisible(true);
				String filepath=jfc.getSelectedFile().getAbsolutePath();
				// 姝ラ3锛氬皢JFreeChart瀵硅薄杈撳嚭鍒版枃浠讹紝Servlet杈撳嚭娴佺瓑
		        saveAsFile(freeChart,filepath, 600, 400);
			} else {
				JOptionPane.showMessageDialog(null, "No chart!","Error",JOptionPane.ERROR_MESSAGE);
			}
		} else if(e.getActionCommand().equals("Cancel")){
			this.removeAll();
			PanelProjects pa=new PanelProjects(empName,"My Projects");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(900,400));
			this.add(pa);
			this.updateUI();		
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
}
