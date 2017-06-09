package com.pcms.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class MainFrame extends JFrame implements ActionListener, MouseListener{
	public static int width;
	public static int height;
	JPanel jpSouth;
	String empName;
	JLabel jlLogout=null;
	
	

	/**
	 * Create the application.
	 */
	public MainFrame(String empName,int access) {
		//Initialize the contents of the frame.
		this.empName=empName;
		initializeFrame(access);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initializeFrame(int access) {
		
		int screenWidth=((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		this.width = (int)(screenWidth*0.85);
		this.height = (int)(screenHeight*0.85);
		 
		System.out.println(screenWidth+""+screenHeight);
		//north
		JPanel jpNorth = new JPanel();
		JPanel jpNorthLeft=new JPanel();
		JPanel jpNorthRight=new JPanel();
		JPanel jpNorthRight1=new JPanel();
		JPanel jpNorthRignt2=new JPanel();
		JLabel jlEmpInfo=new JLabel("Welcome! "+empName.toUpperCase());
		JLabel jlMessage=buildLabel("Message");
		JLabel jlSettings=buildLabel("Settings");
		JLabel jlHelp=buildLabel("Help");
		jlLogout=buildLabel("Logout");
		
		jpNorth.setLayout(new GridLayout(1, 2));
		jpNorth.setPreferredSize(new Dimension(width, 100));
		this.getContentPane().add(jpNorth, BorderLayout.NORTH);	
		
		jpNorth.add(jpNorthLeft);
		jpNorth.add(jpNorthRight);
		
		jpNorthRight.setLayout(new GridLayout(2,1));
		jpNorthRight.add(jpNorthRight1);
		jpNorthRight.add(jpNorthRignt2);
		
		jpNorthRight1.add(jlEmpInfo);
		jpNorthRignt2.setLayout(new GridLayout(1,4));
		jpNorthRignt2.add(jlMessage);
		jpNorthRignt2.add(jlSettings);
		jpNorthRignt2.add(jlHelp);
		jpNorthRignt2.add(jlLogout);
		
		//Center
		JPanel jpCenter = new JPanel();
		JMenuBar jmb=new JMenuBar();
		//home
		JMenu jmHome=new JMenu("HOME");
		JMenuItem jmiRec=buildMenuItem("Attendance Record");
		JMenuItem jmiAdd=buildMenuItem("Address");
		JMenuItem jmiCtt=buildMenuItem("Contacts");
		//projects
		JMenu jmProjects=new JMenu("PROJECTS");
		JMenuItem jmMyPro=buildMenuItem("My Projects");
		JMenuItem jmTeamPro=buildMenuItem("Team projects");
		JMenuItem jmAllPro=buildMenuItem("All projects");
		
		
		//information
		JMenu jmInformation=new JMenu("INFORMATION");
		JMenuItem jmiAbb=buildMenuItem("Abbreviation");
		
		
		this.getContentPane().add(jpCenter, BorderLayout.CENTER);
		
		jmHome.add(jmiRec);
		jmHome.add(jmiAdd);
		jmHome.add(jmiCtt);
		
		jmProjects.add(jmMyPro);
		jmProjects.add(jmTeamPro);
		jmProjects.add(jmAllPro);
		
		
		
		
		jmInformation.add(jmiAbb);
		
		
		jmb.add(jmHome);
		jmb.add(jmProjects);
		jmb.add(jmInformation);
		jmb.setSize(width, 100);
		
		//validations
		if(access==1){
			JMenu jmValidation=new JMenu("VALIDATION");
			JMenu jmVA=new JMenu("Validate Applications");
			
			JMenuItem jmMA=buildMenuItem("My Applications");
			
			JMenuItem jmiVA1=buildMenuItem("Waiting List");
			JMenuItem jmiVA2=buildMenuItem("Valided Records");
			JMenuItem jmiVA3=buildMenuItem("All Records");	

			jmVA.add(jmiVA1);
			jmVA.add(jmiVA2);
			jmVA.add(jmiVA3);
			
			jmValidation.add(jmVA);
			jmValidation.add(jmMA);
			
			jmb.add(jmValidation);
						
		}
		
		//contributions
		JMenu jmContribution=new JMenu("CONTRIBUTION");
		JMenuItem jmiCO1=buildMenuItem("Search by Project");
		JMenuItem jmiCO2=buildMenuItem("Search by Employee");
		jmContribution.add(jmiCO1);
		jmContribution.add(jmiCO2);
		jmb.add(jmContribution);
		
		
		
		jpCenter.setLayout(new BorderLayout());
		jpCenter.add(jmb, BorderLayout.NORTH);
		//South 签到部分开始
		jpSouth=new JPanel();
//		JPanel jpSouthRight = new JPanel();
		PanelAttendance jpAttendance = new PanelAttendance(empName);
		jpCenter.add(jpSouth,BorderLayout.CENTER);	
		jpSouth.setBackground(Color.lightGray);
		TimerPanel jpSouthRight1=new TimerPanel(width/6,(height-120)*5/6);
		jpSouthRight1.setPreferredSize(new Dimension(width/6,(height-120)*5/6));
		JPanel jpSouthRight2=new JPanel();
		JPanel jpSouthRight3=new JPanel();
		JPanel jpSouthRight4=new JPanel();
		jpSouthRight3.setBackground(Color.lightGray);
		jpSouthRight4.setBackground(Color.lightGray);
		GridBagLayout layout = new GridBagLayout(); 
		jpSouth.setLayout(layout);
		jpSouthRight2.setLayout(new GridLayout(2,1));
		String weather;
		String weather1;
		String weather2;
		weather=Weather.translate(Weather.GetWeater("北京"));
		int m1=weather.indexOf(")");
		weather1=weather.substring(0, m1+1);
		weather2=weather.substring(m1+1, weather.length()-1);
		JLabel jlWeatherInfo1=new JLabel(weather1);
		JLabel jlWeatherInfo2=new JLabel(weather2);
		jpSouthRight3.add(jlWeatherInfo1);
		jpSouthRight4.add(jlWeatherInfo2);
		jpSouthRight2.add(jpSouthRight3);
		jpSouthRight2.add(jpSouthRight4);
//		
//		jpSouth.add(Box.createHorizontalStrut(2));
//		jpSouth.add(jpAttendance);
//		jpSouth.add(jpSouthRight);
//		jpSouthRight.add(jpSouthRight1);
//		jpSouthRight.add(jpSouthRight2);
		jpSouth.add(jpAttendance,new GridBagConstraints(0,0,2,3,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1), 0, 0));
		jpSouth.add(jpSouthRight1,new GridBagConstraints(2,0,1,2,0,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1), 0, 0));
		jpSouth.add(jpSouthRight2,new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1), 0, 0));
		Thread t1=new Thread(jpSouthRight1) ;
		t1.start();
//	签到结束		jpSouth=new JPanel();		
//		jpCenter.add(jpSouth,BorderLayout.CENTER);
		jpSouth.setBackground(Color.lightGray);
		
		//set jframe
		this.setSize(width, height);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("PCMSystem");
		this.setBackground(Color.WHITE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JMenuItem buildMenuItem(String name) {  
		JMenuItem jmi = new JMenuItem(name);  
		//jmi.addActionListener(panelAction); 
		jmi.addActionListener(this);
		jmi.setActionCommand(name);
		return jmi;  
	}
	
	public JLabel buildLabel(String name) {  
		JLabel jmi = new JLabel(name);  
		//jmi.addActionListener(panelAction); 
		jmi.addMouseListener(this);		
		return jmi;  
	} 
	
	//listening
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	
		this.jpSouth.removeAll();
		if(e.getActionCommand().equals("Abbreviation")){
			PanelAbbreviation pa=new PanelAbbreviation();
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);			 
		} else if(e.getActionCommand().equals("My Projects")){
			PanelProjects pa=new PanelProjects(empName,"My Projects");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Team projects")){
			PanelProjects pa=new PanelProjects(empName,"Team projects");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("All projects")){
			PanelProjects pa=new PanelProjects(empName,"All projects");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Waiting List")){
			PanelValidation pa=new PanelValidation(empName,"Waiting List");//status=0
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Valided Records")){
			PanelValidation pa=new PanelValidation(empName,"Valided");//status=1,non valid=2
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("All Records")){
			PanelValidation pa=new PanelValidation(empName,"All Validation");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("My Applications")){
			PanelValidation pa=new PanelValidation(empName,"My Applications");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Search by Project")){
			PanelContribution pa=new PanelContribution(empName,"Search by Project");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Search by Employee")){
			PanelContribution pa=new PanelContribution(empName,"Search by Employee");
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Contacts")){
			PanelContact pa=new PanelContact();
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		} else if(e.getActionCommand().equals("Address")){
			PanelAddress pa=new PanelAddress();
			pa.setVisible(true);
			pa.setPreferredSize(new Dimension(width-100,height-200));
			this.jpSouth.add(pa);
		}else if(e.getActionCommand().equals("Attendance Record")){
			PanelAttendance jpAttendance = new PanelAttendance(empName);	
			jpSouth.setBackground(Color.lightGray);
			TimerPanel jpSouthRight1=new TimerPanel(width/6,(height-120)*5/6);
			jpSouthRight1.setPreferredSize(new Dimension(width/6,(height-120)*5/6));
			JPanel jpSouthRight2=new JPanel();
			JPanel jpSouthRight3=new JPanel();
			JPanel jpSouthRight4=new JPanel();
			jpSouthRight3.setBackground(Color.lightGray);
			jpSouthRight4.setBackground(Color.lightGray);
			GridBagLayout layout = new GridBagLayout(); 
			jpSouth.setLayout(layout);
			jpSouthRight2.setLayout(new GridLayout(2,1));
			String weather;
			String weather1;
			String weather2;
			weather=Weather.translate(Weather.GetWeater("北京"));
			int m1=weather.indexOf(")");
			weather1=weather.substring(0, m1+1);
			weather2=weather.substring(m1+1, weather.length()-1);
			JLabel jlWeatherInfo1=new JLabel(weather1);
			JLabel jlWeatherInfo2=new JLabel(weather2);
			jpSouthRight3.add(jlWeatherInfo1);
			jpSouthRight4.add(jlWeatherInfo2);
			jpSouthRight2.add(jpSouthRight3);
			jpSouthRight2.add(jpSouthRight4);
//			
//			jpSouth.add(Box.createHorizontalStrut(2));
//			jpSouth.add(jpAttendance);
//			jpSouth.add(jpSouthRight);
//			jpSouthRight.add(jpSouthRight1);
//			jpSouthRight.add(jpSouthRight2);
			jpSouth.add(jpAttendance,new GridBagConstraints(0,0,2,3,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1), 0, 0));
			jpSouth.add(jpSouthRight1,new GridBagConstraints(2,0,1,2,0,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1), 0, 0));
			jpSouth.add(jpSouthRight2,new GridBagConstraints(2,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(1, 1, 1, 1), 0, 0));
			Thread t1=new Thread(jpSouthRight1) ;
			t1.start();
//		签到结束		jpSouth=new JPanel();		
//			jpCenter.add(jpSouth,BorderLayout.CENTER);
			jpSouth.setBackground(Color.lightGray);
		}else {
			this.jpSouth.add(new Label("杩欐槸閫氳繃鎸夐挳鍒囨崲鐨勯潰鏉� " + e.getActionCommand()));  
		}
		this.jpSouth.updateUI();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jlLogout){
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.white);
		jl.setFont(new Font(Font.DIALOG,1,13));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.BLACK);
		jl.setFont(new Font(Font.DIALOG,0,13));
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}  

}
