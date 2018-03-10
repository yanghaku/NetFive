package Client;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class PanelTiming extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel myIcon,myName,myTimer;
	private JLabel opIcon,opName,opTimer;
	private Icon blackIcon,whiteIcon;
	public PanelTiming() {
		blackIcon=new ImageIcon("img\\black.jpg");
		whiteIcon=new ImageIcon("img\\white.jpg");
		this.myIcon=new JLabel(blackIcon,SwingConstants.CENTER);
		this.opIcon=new JLabel(whiteIcon,SwingConstants.CENTER);
		this.myName=new JLabel("My name",SwingConstants.CENTER);
		this.opName=new JLabel("Op name",SwingConstants.CENTER);
		this.myTimer=new JLabel("00:00:00");
		this.opTimer=new JLabel("00:00:00");
		myName.setFont(FiveClient.fontBig);
		myTimer.setFont(FiveClient.fontBig);
		opName.setFont(FiveClient.fontBig);
		opTimer.setFont(FiveClient.fontBig);
		myName.setForeground(Color.BLUE);
		myTimer.setForeground(Color.BLUE);
		opName.setForeground(Color.red);
		opTimer.setForeground(Color.red);
		JPanel myText=new JPanel(new GridLayout(2,1));
		myText.add(myName);
		myText.add(myTimer);
		JPanel opText=new JPanel(new GridLayout(2,1));
		opText.add(opName);
		opText.add(opTimer);
		this.add(myIcon);
		this.add(myText);
		this.add(opIcon);
		this.add(opText);
	}
	//ÉèºÚÎªtrue °×Îªfalse
	public void setMyIcon(boolean color) {
		if(color)this.myIcon.setIcon(blackIcon);
		else this.myIcon.setIcon(whiteIcon);
	}
	public void setOpIcon(boolean color) {
		if(color)this.opIcon.setIcon(blackIcon);
		else this.opIcon.setIcon(whiteIcon);
	}
	public void setMyName(String name) {
		this.myName.setText(name);
	}
	public void setOpName(String name) {
		this.opName.setText(name);
	}
	public void SetMyTime(int time) {
		int h=time/3600;
		int m=(time-h*3600)/60;
		int s=time%60;
		this.myTimer.setText(h+":"+m+":"+s);
	}
	public void setOpTime(int time) {
		int h=time/3600;
		int m=(time-h*3600)/60;
		int s=time%60;
		this.opTimer.setText(h+":"+m+":"+s);
	}
}
