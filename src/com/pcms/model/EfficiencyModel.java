package com.pcms.model;

import java.util.Locale;
import java.util.Vector;

import jxl.write.Label;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.Dimension;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;  
  
import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartPanel;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.DateAxis;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;  
import org.jfree.data.time.TimeSeriesCollection;  
import org.jfree.data.xy.XYDataset;

import com.pcms.db.SqlHelper;  

public class EfficiencyModel{
	String empName,projectId,department;
	String []refM={"January","February","March","April","May","June","July","August","September"
			,"October","November","December"};
	static Vector colkeys;
	static Vector data;
	Vector rowData, columnNames,dataBackup;
	ChartPanel chartpanel;
	String date1, date2;
	
	public EfficiencyModel(String date1, String date2) throws SQLException{  
		
		this.date1=date1;
		this.date2=date2;
		
		Locale.setDefault(Locale.ENGLISH);
        XYDataset xydataset = createDataset();  
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Efficiency", "Date", "percentage", xydataset, true, false, false);  
        
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
        DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();  
        dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));  
        
        dateaxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题  
        dateaxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题  
        ValueAxis rangeAxis=xyplot.getRangeAxis();//获取柱状  
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));  
        jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));  
        jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体  
        
        chartpanel=new ChartPanel(jfreechart, false);  
        chartpanel.setPreferredSize(new Dimension(1000,400));
        
    }   
//	public EfficiencyModel(String empName,String []months,String projectId,String department){
//		this.empName=empName;
//		this.projectId=projectId;
//		int startM = 0,startY=Integer.parseInt(months[1]),endM = 0,endY=Integer.parseInt(months[3]);
//		int cycle=endY-startY;//times of cycle
//		
//		colkeys=new Vector();
//		
//		for(int i=0;i<refM.length;i++){
//			if(months[0].equals(refM[i])){
//				startM=i;
//			}
//			if(months[2].equals(refM[i])){
//				endM=i;
//			}
//		}
//		if(cycle==0&&startM==endM){
//			recordDaily(endM);
//		} else {
//			recordMonthly(cycle,startM,endM,endY);
//		}
//	
//		
//	}
//	private void recordDaily(int month){
//		int days=0;
//		//calulate days
//		switch(month+1){
//		case 1: days=28;break;
//		case 0 :
//		case 2 :
//		case 4 :
//		case 6:
//		case 7:
//		case 9:
//		case 11:
//			days=31;
//			break;
//		case 3:
//		case 5:
//		case 8 :
//		case 10:
//			days=30;
//			break;
//		}
//		for(int i=1;i<=days;i++){
//			colkeys.add(i);
//		}
//	}
//	private void recordMonthly(int cycle, int startM, int endM,int endY){
//		while(cycle!=0){
//			while(startM<12){
//				int year=endY-cycle;
//				colkeys.add(startM);
//				startM++;
//			}
//			startM=0;
//			cycle--;
//		}		
//		while(startM<=endM){
//			colkeys.add(startM);
//			startM++;
//		}
//	}
	   /**
     *
     * @return
	 * @throws SQLException 
     */
	private XYDataset createDataset() throws SQLException {    	
		
		TimeSeries timeseries = new TimeSeries("Actual percentage", org.jfree.data.time.Day.class);  
		
		SqlHelper sh=new SqlHelper();
		ResultSet rs=null;
		
		rowData=new Vector();
		dataBackup=new Vector();
		
		
		System.out.println(date1.toString());
		System.out.println(date2.toString());
		
		String sql="select date, percentage from records"
				+ " where date Between ? and ?";
		String []paras={date1,date2};
		rs=sh.query(sql,paras);
		
		while(rs.next()){
			timeseries.add(new Day(rs.getDate(1)), rs.getInt(2)); 
			System.out.println(rs.getDate(1));
			System.out.println(rs.getInt(2));
		}
		
		
        
        //TimeSeries timeseries1 = new TimeSeries("legal & general英国指数信任", org.jfree.data.time.Month.class);  
        
        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();  
        timeseriescollection.addSeries(timeseries);  
        // timeseriescollection.addSeries(timeseries1);  
        return timeseriescollection;  
    }  
	
	public ChartPanel getChartPanel(){
		
        return chartpanel;          
    }  
    /*
    public static CategoryDataset createDataset() {
 
        String[] rowKeys = { "Actual" };
        String[] colKeys = { "0:00", "1:00", "2:00", "7:00", "8:00", "9:00",
                "10:00", "11:00", "12:00", "13:00", "16:00", "20:00", "21:00",
                "23:00" };
 
        double[][] data = { { 4, 3, 1, 1, 1, 1, 2, 2, 2, 1, 8, 2, 1, 1 }, };
 
        // 鎴栬�呬娇鐢ㄧ被浼间互涓嬩唬鐮�
        // DefaultCategoryDataset categoryDataset = new
        // DefaultCategoryDataset();
        // categoryDataset.addValue(10, "rowKey", "colKey");
 
        return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);
    }
    */
}
