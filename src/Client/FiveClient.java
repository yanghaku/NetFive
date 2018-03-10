package Client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import Server.FiveServer;

public class FiveClient extends JFrame {
	public static final Font fontSmall=new Font("����",1,16);
	public static final Font fontBig=new Font("����",1,16);
	private static final long serialVersionUID = 1L;//��֪��
//����:	
	PanelBoard panelBoard;     //�������
	PanelUserList userList;   //�û��б����
	PanelMessage message;    //ʵʱ��Ϣ���
	PanelTiming timing;     //��սʱ����Ϣ
	PanelControl control;  //�������
	String myname,opname;         //�û���
	public boolean isConnected=false;
	Communication c=null;
//���캯��:
	public FiveClient() {
		super("����������"); //����
		timing =new PanelTiming();
		userList=new PanelUserList();
		message=new PanelMessage();
		panelBoard=new PanelBoard(this);
		control =new PanelControl();
		
		Panel east=new Panel();
		east.setLayout(new BorderLayout());
		east.add(userList,BorderLayout.CENTER);
		east.add(message,BorderLayout.SOUTH);
		east.add(timing,BorderLayout.NORTH);
		this.add(east,BorderLayout.EAST);
		this.add(panelBoard,BorderLayout.CENTER);
		this.add(control,BorderLayout.SOUTH);
		
		//����
		this.setBounds(350,50,900,740);
		this.setUndecorated(true);//ȥ��������
		this.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);//ʹ���ڴ�С���ɱ�
		
		ActionMonitor monitor=new ActionMonitor();//������
		control.exitGameButton.addActionListener(monitor);//ע�����
		control.joinGameButton.addActionListener(monitor);
		control.cancelGameButton.addActionListener(monitor);
		control.connectButton.addActionListener(monitor);
		control.connectButton.setEnabled(true);
		control.exitGameButton.setEnabled(true);
		control.cancelGameButton.setEnabled(false);
		control.joinGameButton.setEnabled(false);
	}
	
	public void connect() {
		c=new Communication(this);
		String ip=control.inputIP.getText();
		c.connect(ip,FiveServer.TCP_PORT);
		message.messageArea.append("������\n");
		isConnected=true;
		control.exitGameButton.setEnabled(true);
		control.connectButton.setEnabled(false);
		control.joinGameButton.setEnabled(true);
		control.cancelGameButton.setEnabled(false);
		control.connectButton.setLabel("����������");
	}
	
	public void join(String opName) {
		c.join(opName);
	}
	
//�ڲ�������
class ActionMonitor implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==control.exitGameButton) {
				/*  �˳�ȷ��   */
			String msg=panelBoard.isGamming?"��Ϸ���ڽ���,ȷ���˳�ô?":"ȷ���˳�ô?";
			Icon icon=new ImageIcon("img\\icon.jpg");
			Object[] options= {"��Ҫ�˳�","����һ��"};
			int response=JOptionPane.showOptionDialog(null,msg,"�˳�ȷ��",
				2,JOptionPane.QUESTION_MESSAGE, icon, options,options[0]);
			if(response==0) {
				if(!isConnected)System.exit(0);
				c.quit();}
		}
		else if(e.getSource()==control.connectButton) {
			/*   ��������  */
			connect();
		}
		else if(e.getSource()==control.cancelGameButton) {
			Icon icon=new ImageIcon("img\\icon.jpg");
			Object[] options= {"��Ҫ����","����һ��"};
			int response=JOptionPane.showOptionDialog(null,"ȷ������ô?","����ȷ��",
				2,JOptionPane.QUESTION_MESSAGE, icon, options,options[0]);
			if(response==0)c.giveUp();
		}
		else if(e.getSource()==control.joinGameButton) {
			String select=userList.userList.getSelectedItem();
			if(select==null) {
				message.messageArea.append("��ѡ��һ������\n");
				return;
			}
			if(!select.endsWith("ready")) {
				message.messageArea.append("��ѡ�� ready״̬�Ķ���\n");
				return;
			}
			if(select.startsWith(myname)) {
				message.messageArea.append("����ѡ���Լ�������,��ѡ������\n");
				return;
			}
			int index=select.lastIndexOf(":");
			String name=select.substring(0,index);
			message.messageArea.append("��ѡ����"+name+"��Ϊ����\n���ڵȶԷ�ͬ��......\n");
			join(name);
		}
	}
}
	
//�ڲ���,��¼�ͻ�����Ϣ
public static class Client{
	public String name;
	public Socket s;
	public String state; //1����ready 2����playing
	public Client opponent;  //����
	public Client(String name,Socket s) {
		this.name=name;
		this.s=s;
		this.state="ready";
		this.opponent=null;
	}
}

	public static void main(String[] args)
	{
		new FiveClient(); 
	}

}
