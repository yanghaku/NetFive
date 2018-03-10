package Server;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import Client.FiveClient.Client;
import Client.FiveClient;

public class FiveServer extends Frame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Label lStaus=new Label("��ǰ������: ",Label.LEFT);
	TextArea taMessage=new TextArea("",22,50,TextArea.SCROLLBARS_VERTICAL_ONLY);
	Button btServerClose=new Button("�رշ�����");
	ServerSocket ss=null;
	public static final int TCP_PORT=4801;
	static int clientNum=0;//�ͻ��˸���
	static int clientNameNum=0;
	ArrayList<Client>clients=new ArrayList<Client>();
	
	public FiveServer() {
		super("JAVA �����������");
		btServerClose.setFont(FiveClient.fontBig);
		taMessage.setFont(FiveClient.fontBig);
		lStaus.setFont(FiveClient.fontBig);
		
		btServerClose.addActionListener(this);
		add(lStaus,BorderLayout.NORTH);
		add(taMessage,BorderLayout.CENTER);
		add(btServerClose,BorderLayout.SOUTH);
		setLocation(500,90);
		pack();
		setVisible(true);
		setResizable(false);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btServerClose) {
			System.exit(0);
		}
	}
	public void startServer() {
		try {
			ss=new ServerSocket(TCP_PORT);
			while(true) {
				Socket s=ss.accept();//һ������
				++clientNum;
				++clientNameNum;
				Client c=new Client("Player"+clientNameNum,s);
				clients.add(c);
				lStaus.setText("������ "+clientNum);
				String msg=s.getInetAddress().getHostAddress()+"  Player"+clientNameNum+"\n";
				taMessage.append(msg);
				tellName(c);
				addAllUserToMe(c);
				addMeToAllUser(c);
				new ClientThread(c).start();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void tellName(Client c) {
		DataOutputStream dos=null;
		try {
			dos=new DataOutputStream(c.s.getOutputStream());
			dos.writeUTF(Command.TELLNAME+":"+c.name);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void addAllUserToMe(Client c) {
		DataOutputStream dos=null;
		for(int i=0;i<clients.size();++i) {
			if(clients.get(i)!=c) {
				try {
					dos=new DataOutputStream(c.s.getOutputStream());
					dos.writeUTF(Command.ADD+":"+clients.get(i).name+":"+clients.get(i).state);
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void addMeToAllUser(Client c) {
		DataOutputStream dos=null;
		for(int i=0;i<clients.size();++i) {
			if(clients.get(i)!=c) {
				try {
					dos=new DataOutputStream(clients.get(i).s.getOutputStream());
					dos.writeUTF(Command.ADD+":"+c.name+":ready");
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		FiveServer fs=new FiveServer();
		fs.startServer();
	}



//�ڲ��߳���,
class ClientThread extends Thread{
	private Client c;
	private DataInputStream dis;
	private DataOutputStream dos;
	ClientThread(Client c){
		this.c=c;
	}
	public void run() {
		while(true) {
			try {
				dis=new DataInputStream(c.s.getInputStream());
				String msg=dis.readUTF();
				String[] words=msg.split(":");//���տͻ�������
				
				if(words[0].equals(Command.JOIN)) {//��������� join:������
					String opName=words[1];
					for(int i=0;i<clients.size();++i) {
						if(clients.get(i).name.equals(opName)) {
							dos=new DataOutputStream(clients.get(i).s.getOutputStream());
							dos.writeUTF(Command.JOIN+":"+c.name);
							break;//��������ֿͻ��˷��� join:�������������
						}
					}
				}
				else if(words[0].equals(Command.REFUSE)) {//��������Ǿܾ�����
					String opname=words[1];
					for(int i=0;i<clients.size();++i) {
						if(clients.get(i).name.equals(opname)) {
							dos=new DataOutputStream(clients.get(i).s.getOutputStream());
							dos.writeUTF(Command.REFUSE+":"+c.name);
							break;
						}
					}
				}
				else if(words[0].equals(Command.AGREE)) {//�������������ͬ��
					String opname=words[1];
					c.state="playing";//��������Ŀͻ��˱�Ϊ������Ϸ
					for(int i=0;i<clients.size();++i) {
						if(clients.get(i).name.equals(opname)) {
							clients.get(i).state="playing";
							clients.get(i).opponent=c;//��Ϊ����
							c.opponent=clients.get(i);
						}
						//ѭ����ͬʱ,��ÿ���ͻ���������change����
						dos=new DataOutputStream(clients.get(i).s.getOutputStream());
						dos.writeUTF(Command.CHANGE+":"+c.name+":playing");
						dos.writeUTF(Command.CHANGE+":"+opname+":playing");
					}
					int r=(int)(Math.random()*2); //�������ڰ���
					if(r==0) {
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.GUESSCOLOR+":black:"+opname);
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.GUESSCOLOR+":white:"+c.name);
					}else {
						dos=new DataOutputStream(c.s.getOutputStream());
						dos.writeUTF(Command.GUESSCOLOR+":white:"+opname);
						dos=new DataOutputStream(c.opponent.s.getOutputStream());
						dos.writeUTF(Command.GUESSCOLOR+":black:"+c.name);
					}
					taMessage.append(c.name+" playing\n");
					taMessage.append(c.opponent.name+" playing\n");
				}
				else if(words[0].equals(Command.GO)) {
					dos=new DataOutputStream(c.opponent.s.getOutputStream());
					dos.writeUTF(msg);
					taMessage.append(c.name+" "+msg+"\n");
				}
				else if(words[0].equals(Command.WIN)) {
					//���пͻ���,�������ͻ�״̬�ı�
					for(int i=0;i<clients.size();++i) {
						dos=new DataOutputStream(clients.get(i).s.getOutputStream());
						dos.writeUTF(Command.CHANGE+":"+c.name+":ready");
						dos.writeUTF(Command.CHANGE+":"+c.opponent.name+":ready");
					}
					dos=new DataOutputStream(c.s.getOutputStream());
					dos.writeUTF(Command.TELLRESULT+":win");//���Լ�����
					dos=new DataOutputStream(c.opponent.s.getOutputStream());
					dos.writeUTF(Command.TELLRESULT+":lose");
					c.state="ready";
					c.opponent.state="ready";
					taMessage.append(c.name+" win\n");
					taMessage.append(c.opponent.name+" lose\n");
				}
				else if(words[0].equals(Command.LOSE)) {
					for(int i=0;i<clients.size();++i) {
						dos=new DataOutputStream(clients.get(i).s.getOutputStream());
						dos.writeUTF(Command.CHANGE+":"+c.name+":ready");
						dos.writeUTF(Command.CHANGE+":"+c.opponent.name+":ready");
					}
					dos=new DataOutputStream(c.s.getOutputStream());
					dos.writeUTF(Command.TELLRESULT+":lose");
					dos=new DataOutputStream(c.opponent.s.getOutputStream());
					dos.writeUTF(Command.TELLRESULT+":win");
					c.state="ready";
					c.opponent.state="ready";
					taMessage.append(c.name+" lose\n");
					taMessage.append(c.opponent.name+" win\n");
				}
				else if(words[0].equals(Command.QUIT)) {
					for(int i=0;i<clients.size();++i) {
						dos=new DataOutputStream(clients.get(i).s.getOutputStream());
						dos.writeUTF(Command.DELETE+":"+c.name);
					}
					clients.remove(c);
					taMessage.append(c.name+" quit\n");
					--clientNum;
					lStaus.setText("������  "+clientNum);
					return;
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}










}
