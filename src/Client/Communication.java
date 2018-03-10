package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import Server.Command;

public class Communication {
	FiveClient fc;
	Socket s;
	private DataOutputStream dos;
	//private DataInputStream dis;
	public Communication(FiveClient fc) {
		this.fc=fc;
	}
	public void connect(String IP,int port) {
		try {
			s=new Socket(IP,port);
			new ReceaveThread(s).start();
			//dis=new DataInputStream(s.getInputStream());
			dos=new DataOutputStream(s.getOutputStream());
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void join(String opName) {
		try {
			dos.writeUTF(Command.JOIN+":"+opName);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go(int col,int row) {
		try {
			String msg=Command.GO+":"+col+":"+row;
			dos.writeUTF(msg);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void wins() {
		try {
			dos.writeUTF(Command.WIN);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void giveUp() {
		try {
			dos.writeUTF(Command.LOSE);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void quit() {
		try {
			dos.writeUTF(Command.QUIT);
			System.exit(0);
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.exit(0);
	}
	
//内部线程类(不停的走)
class ReceaveThread extends Thread{
	Socket s;
	private DataInputStream dis;
	private DataOutputStream dos;
	String msg;
	public ReceaveThread(Socket s) {
		this.s=s;
	}
	public void run() {
		while(true) {
			try {
				dis=new DataInputStream(s.getInputStream());
				dos=new DataOutputStream(s.getOutputStream());
				msg=dis.readUTF();
				String[] words=msg.split(":");
				if(words[0].equals(Command.TELLNAME)) {//服务器发来连接命令
					fc.myname=words[1];
					fc.userList.userList.add(fc.myname+": ready");
					fc.message.messageArea.append("My name: "+fc.myname+"\n");
					fc.timing.setMyName(fc.myname);
				}
				else if(words[0].equals(Command.ADD)) {//服务器发来增加用户命令
					fc.userList.userList.add(words[1]+":"+words[2]);
					fc.message.messageArea.append(words[1]+":"+words[2]+"\n");
				}
				else if(words[0].equals(Command.JOIN)) {//服务器发来有人邀请命令
					String name=words[1];
					TimeDialog d=new TimeDialog();
					int select=d.showDialog(fc,name +"邀请您下棋,是否接受?",90);
					if(select==0) {
						dos.writeUTF(Command.AGREE+":"+name);//发送同意
					}else {
						dos.writeUTF(Command.REFUSE+":"+name);//发送拒绝
					}
				}
				else if(words[0].equals(Command.REFUSE)) {//服务器传来对手拒绝
					JOptionPane.showMessageDialog(fc,words[1]+"拒绝了您的邀请!");
					fc.message.messageArea.append(words[1]+"拒绝了您的邀请!");
				}
				else if(words[0].equals(Command.AGREE)) {//服务器传来对手同意
					
				}
				else if(words[0].equals(Command.CHANGE)){//传来有人状态改变了
					String name=words[1];
					String state=words[2];
					for(int i=0;i<fc.userList.userList.getItemCount();++i) {
						if(fc.userList.userList.getItem(i).startsWith(name)) {
							fc.userList.userList.replaceItem(name+":"+state,i);
						}
					}
					fc.message.messageArea.append(name+":"+state+"!\n");
				}
				else if(words[0].equals(Command.GUESSCOLOR)) {//分配颜色
					String color=words[1];
					String opname=words[2];
					fc.panelBoard.isGamming=true;
					fc.panelBoard.resetGame();
					fc.opname=opname;
					fc.timing.setOpName(opname);
					if(color.equals("black")) { //黑棋
						fc.timing.setMyIcon(true);//hei
						fc.timing.setOpIcon(false);
						fc.panelBoard.isBlack=true;
						fc.panelBoard.isGoing=true;
					}else {//白棋
						fc.timing.setMyIcon(false);
						fc.timing.setOpIcon(true);
						fc.panelBoard.isBlack=false;
						fc.panelBoard.isGoing=false;
					}
					new TimeThread(fc,100).start();
					fc.control.joinGameButton.setEnabled(false);
					fc.control.cancelGameButton.setEnabled(true);
					fc.control.exitGameButton.setEnabled(false);
					fc.message.messageArea.append("Game is start!\n");
					fc.message.messageArea.append("My color is "+color+"\n");
				}
				else if(words[0].equals(Command.GO)) {
					int col=Integer.parseInt(words[1]);
					int row=Integer.parseInt(words[2]);
					fc.panelBoard.addOpponentChess(col,row);
				}
				else if(words[0].equals(Command.TELLRESULT)) {
					if(words[1].equals("win")) {
						fc.panelBoard.winGame();
					}else 
						fc.panelBoard.loseGame();
					fc.control.joinGameButton.setEnabled(true);
					fc.control.cancelGameButton.setEnabled(false);
					fc.control.exitGameButton.setEnabled(true);
				}
				else if(words[0].equals(Command.DELETE)) {
					for(int i=0;i<fc.userList.userList.getItemCount();++i) {
						String name=fc.userList.userList.getItem(i);
						if(name.startsWith(words[1])) {
							fc.userList.userList.remove(i);
							break;
						}
					}
					fc.message.messageArea.append(words[1]+" disconnected\n");
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}



}
