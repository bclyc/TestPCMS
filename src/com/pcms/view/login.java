package com.pcms.view;

import java.awt.event.*; 
import java.sql.*; 

import javax.swing.*; 

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.BusinessBlackSteelSkin;
import org.jvnet.substance.skin.OfficeSilver2007Skin;

import com.pcms.model.UserModel;

import java.awt.*; 
/**
 * 
 * @author rumeili
 *
 */
public class login extends JFrame implements ActionListener,KeyListener{
     JTextField jtf;
     JPasswordField jpf;
     JLabel jLabel0,jLabel1,jLabel2;
     JPanel jp1,jp2,jp0,jp3,jp4;
     JButton jb1; 
     
     Icon loginImage=new ImageIcon(getClass().getResource("/com/pcms/resource/loginImage.png"));
     //class UserModel
     UserModel um=null;
     private JFrame frmPcmsystem;
     
     public login(){
    	 //add components
         jtf = new JTextField(13);
         jpf = new JPasswordField(13);
         jLabel0 = new JLabel(loginImage);
         //jLabel0.setBounds(1, 1, loginImage.getIconWidth(), loginImage.getIconHeight());
         jLabel1 = new JLabel("Username",JLabel.CENTER);
         jLabel2 = new JLabel("Password",JLabel.CENTER);
         jb1 = new JButton("login");
         jp0 = new JPanel();
         jp1 = new JPanel();
         jp2 = new JPanel();
         jp3 = new JPanel();
         jp4 = new JPanel();
         
         jp0.add(jLabel0);
         jp0.setPreferredSize(new Dimension(loginImage.getIconWidth(), loginImage.getIconHeight()));
         
         jp1.add(jLabel1); 
         jp1.add(jtf);
         jp2.add(jLabel2);
         jp2.add(jpf);
         
         jp3.add(jb1);
         
         jp4.setLayout(new GridLayout(3,1));
         jp4.add(jp1);
         jp4.add(jp2);
         jp4.add(jp3);
         
         //listening
         jb1.addActionListener(this);
         jpf.addKeyListener(this);
         
         this.add(jp0,"North");
         this.add(jp4,"Center");
 
         //show
         this.setSize(300, 200);
         this.setLocationRelativeTo(null);
         this.setResizable(false);
         //this.setBackground(Color.WHITE);
         this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         this.setTitle("Login");
          
     }
     public static void main(String[] args){

     	try {  
			UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceDustCoffeeLookAndFeel());
            //UIManager.setLookAndFeel(new SubstanceLookAndFeel());  
            //JFrame.setDefaultLookAndFeelDecorated(true);  
            //JDialog.setDefaultLookAndFeelDecorated(true);  
            //SubstanceLookAndFeel.setCurrentTheme(new SubstanceTerracottaTheme());  
            SubstanceLookAndFeel.setSkin(new BusinessBlackSteelSkin());  
//          SubstanceLookAndFeel.setCurrentButtonShaper(new ClassicButtonShaper());  
//          SubstanceLookAndFeel.setCurrentWatermark(new SubstanceBubblesWatermark());  
//          SubstanceLookAndFeel.setCurrentBorderPainter(new StandardBorderPainter());  
//          SubstanceLookAndFeel.setCurrentGradientPainter(new StandardGradientPainter());  
//          SubstanceLookAndFeel.setCurrentTitlePainter(new FlatTitePainter());  
        } catch (Exception e) {  
            System.err.println("Something went wrong!");  
        }  
		  
		 SwingUtilities.invokeLater(new Runnable() {  
             public void run() {  
                 login frame=new login();
                 frame.setVisible(true);
             }  
         }); 
     }
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jb1){
			this.connect();
			this.dispose();
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
			this.connect();
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	//connect to database
	public void connect(){
		String post;
		String empName=null;
		String empPw=null;
		empName=jtf.getText().trim();
		empPw=new String(jpf.getPassword()).trim();
		um=new UserModel();
		if((!(post=um.checkUser(empName,empPw)).equals(""))){
			this.dispose();	
			if(post.equals("employee")){
				MainFrame window = new MainFrame(empName,1);
				window.setVisible(true);
			} else {
				MainFrame window = new MainFrame(empName,1);
				window.setVisible(true);
			}			
		} else {
			JOptionPane.showMessageDialog(null, "Wrong username or password!","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
 }