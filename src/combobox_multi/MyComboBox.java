package combobox_multi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

public class MyComboBox extends JComboBox implements ActionListener {
	
public MyComboBox() {
	addItem(new CheckValue(false, "Select All"));
	this.addActionListener(this);
	//new ActionListener() {
	//public void actionPerformed(ActionEvent ae) {
	//itemSelected();
	//}
	//}
}

public MyComboBox(String[] items) {
	for (String i : items){
		addItem(new CheckValue(false, i));
	}
	this.addActionListener(this);
	
}

public void actionPerformed(ActionEvent e){
	//itemSelected(); 
	System.out.println("1");
}

//this method is used in specific obcjet 
public void itemSelected() {
	if (getSelectedItem() instanceof CheckValue) {
		if (getSelectedIndex() == 0) {
			selectedAllItem();
		} else {
			CheckValue jcb = (CheckValue) getSelectedItem();
			jcb.bolValue = (!jcb.bolValue);
			setSelectedIndex(getSelectedIndex());
			
//						
			if(this.getComboNum()==(this.getItemCount()-1)){
				CheckValue cv1 = (CheckValue)this.getItemAt(0);
				cv1.bolValue=true;
			}else{
				CheckValue cv1 = (CheckValue)this.getItemAt(0);
				cv1.bolValue=false;
			}
		}
		MyComboBox A=this;
		SwingUtilities.invokeLater(
		new Runnable() {
			public void run() {
				/*keep the popup*/
				A.showPopup();
			}
		});
		//showPopup();
	}
}
	private void selectedAllItem() {
		boolean bl = false;
		for (int i = 0; i < getItemCount(); i++) {
		CheckValue jcb = (CheckValue) getItemAt(i);
		if (i == 0) {
		bl = !jcb.bolValue;
		}
		jcb.bolValue = (bl);
		}
		setSelectedIndex(0);
	}
/*get selected items*/
	public Vector getComboVc() {
		Vector vc = new Vector();
		for (int i = 1; i < getItemCount(); i++) {	//Not including item "All"
		CheckValue jcb = (CheckValue) getItemAt(i);
		if (jcb.bolValue) {
		vc.add(jcb.value);
		}
		}
		return vc;
	}
	
	public int getComboNum() {
		Vector vc = new Vector();
		int num=0;
		for (int i = 1; i < getItemCount(); i++) {
		CheckValue jcb = (CheckValue) getItemAt(i);
		if (jcb.bolValue) {
		num++;
		}
		}
		return num;
		}
}