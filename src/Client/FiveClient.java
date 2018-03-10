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
	public static final Font fontSmall=new Font("宋体",1,16);
	public static final Font fontBig=new Font("宋体",1,16);
	private static final long serialVersionUID = 1L;//不知道
//属性:	
	PanelBoard panelBoard;     //棋盘组件
	PanelUserList userList;   //用户列表组件
	PanelMessage message;    //实时信息组件
	PanelTiming timing;     //对战时间信息
	PanelControl control;  //控制面板
	String myname,opname;         //用户名
	public boolean isConnected=false;
	Communication c=null;
//构造函数:
	public FiveClient() {
		super("网络五子棋"); //标题
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
		
		//窗口
		this.setBounds(350,50,900,740);
		this.setUndecorated(true);//去掉标题栏
		this.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);//使窗口大小不可变
		
		ActionMonitor monitor=new ActionMonitor();//监听类
		control.exitGameButton.addActionListener(monitor);//注册监听
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
		message.messageArea.append("已连接\n");
		isConnected=true;
		control.exitGameButton.setEnabled(true);
		control.connectButton.setEnabled(false);
		control.joinGameButton.setEnabled(true);
		control.cancelGameButton.setEnabled(false);
		control.connectButton.setLabel("主机已连接");
	}
	
	public void join(String opName) {
		c.join(opName);
	}
	
//内部监听类
class ActionMonitor implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==control.exitGameButton) {
				/*  退出确认   */
			String msg=panelBoard.isGamming?"游戏仍在进行,确认退出么?":"确认退出么?";
			Icon icon=new ImageIcon("img\\icon.jpg");
			Object[] options= {"仍要退出","再玩一会"};
			int response=JOptionPane.showOptionDialog(null,msg,"退出确认",
				2,JOptionPane.QUESTION_MESSAGE, icon, options,options[0]);
			if(response==0) {
				if(!isConnected)System.exit(0);
				c.quit();}
		}
		else if(e.getSource()==control.connectButton) {
			/*   连接主机  */
			connect();
		}
		else if(e.getSource()==control.cancelGameButton) {
			Icon icon=new ImageIcon("img\\icon.jpg");
			Object[] options= {"我要认输","再玩一会"};
			int response=JOptionPane.showOptionDialog(null,"确认认输么?","认输确认",
				2,JOptionPane.QUESTION_MESSAGE, icon, options,options[0]);
			if(response==0)c.giveUp();
		}
		else if(e.getSource()==control.joinGameButton) {
			String select=userList.userList.getSelectedItem();
			if(select==null) {
				message.messageArea.append("请选择一个对手\n");
				return;
			}
			if(!select.endsWith("ready")) {
				message.messageArea.append("请选择 ready状态的对手\n");
				return;
			}
			if(select.startsWith(myname)) {
				message.messageArea.append("不能选择自己做对手,请选择其他\n");
				return;
			}
			int index=select.lastIndexOf(":");
			String name=select.substring(0,index);
			message.messageArea.append("您选择了"+name+"作为对手\n正在等对方同意......\n");
			join(name);
		}
	}
}
	
//内部类,记录客户端信息
public static class Client{
	public String name;
	public Socket s;
	public String state; //1代表ready 2代表playing
	public Client opponent;  //对手
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
