package Client;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class PanelBoard extends JPanel{
	private static final long serialVersionUID = 1L;
	public static final int MARGIN =15;//�߾�
	public static final int SPAN =25;//������
	public static final int ROWS =25;//��������
	public static final int COLS =25;//��������
//����
	Image img;//����ͼ
	Chess[] chessList;//���ӵ�����
	int chessCount=0;//���Ӹ���
	boolean isBlack=true;//��һ���ֵ���һ��,Ĭ�ϵ�һ���Ǻڷ�
	public boolean isGamming=false;//�Ƿ�������Ϸ
	public boolean isGoing=false;//�Ƿ���Լ�����
	FiveClient fc;
	
	public PanelBoard(FiveClient fc) {
		this.fc=fc;
		img=Toolkit.getDefaultToolkit().getImage("img\\board.jpg");
		this.addMouseListener(new MouseMonitor());
		this.addMouseMotionListener(new MouseMotionMonitor());
		chessList=new Chess[729];
	}
	//������
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(img,0,0,660,670,this);
		for(int i=0;i<=ROWS;i++) {
			g.drawLine(MARGIN,MARGIN+i*SPAN,
					MARGIN+COLS*SPAN,MARGIN+i*SPAN);
		}
		for(int i=0;i<=COLS;i++) {
			g.drawLine(MARGIN+i*SPAN,MARGIN,
					MARGIN+i*SPAN,MARGIN+ROWS*SPAN);
		}
		g.fillRect(MARGIN+3*SPAN-2,MARGIN+3*SPAN-2,5,5);
		g.fillRect(MARGIN+(COLS/2)*SPAN-2,MARGIN+3*SPAN-2,5,5);
		g.fillRect(MARGIN+(COLS-3)*SPAN-2,MARGIN+3*SPAN-2,5,5);
		g.fillRect(MARGIN+3*SPAN-2,MARGIN+(ROWS/2)*SPAN-2,5,5);
		g.fillRect(MARGIN+(COLS/2)*SPAN-2,MARGIN+(ROWS/2)*SPAN-2,5,5);
		g.fillRect(MARGIN+(COLS-3)*SPAN-2,MARGIN+(ROWS-3)*SPAN-2,5,5);
		g.fillRect(MARGIN+(COLS/2)*SPAN-2,MARGIN+(ROWS-3)*SPAN-2,5,5);
		g.fillRect(MARGIN+(COLS-3)*SPAN-2,MARGIN+(ROWS-3)*SPAN-2,5,5);
		
		for(int i=0;i<chessCount;i++) {
			chessList[i].draw(g);
			if(i==chessCount-1) {//������һ��,���Ϻ�ɫ���ο�
				kuang(i,g,Color.RED);}
		}
	}
	private void kuang(int i,Graphics g,Color color) {
		int xPos=chessList[i].getCol()*SPAN+MARGIN;
		int yPos=chessList[i].getRow()*SPAN+MARGIN;
		g.setColor(color);
		g.drawRect(xPos-Chess.DIAMETER/2,yPos-Chess.DIAMETER/2,
					Chess.DIAMETER,Chess.DIAMETER);
	}
	public Dimension getPreferredSide() {
		return new Dimension(MARGIN*2+SPAN*COLS,MARGIN*2+SPAN*ROWS);
	}
	private boolean hasChess(int col,int row) {//����Ƿ�������
		for(int i=0;i<chessCount;i++) {
			Chess ch=chessList[i];
			if(ch!=null&&ch.getCol()==col&&ch.getRow()==row)
				return true;
		}
		return false;
	}
	//����Ƿ���ָ����ɫ������
	private boolean hasChess(int col,int row,Color color) {
		for(int i=0;i<chessCount;i++) {
			Chess ch=chessList[i];
			if(ch!=null&&ch.getCol()==col&&ch.getRow()==row&&ch.getColor()==color)
				return true;
		}
		return false;
	}
	
	public void addOpponentChess(int col,int row) {
		Chess ch=new Chess(PanelBoard.this,col,row,isBlack?Color.WHITE:Color.BLACK);
		chessList[chessCount++]=ch;
		repaint();
		isGoing=true;
	}
	public void winGame() {
		isGamming=false;
		isGoing=false;
		fc.message.messageArea.append(fc.myname+" win!!\n");
		fc.message.messageArea.append(fc.opname+" lose!\n");
		fc.message.messageArea.append("You Win!!!\n");
		JOptionPane.showMessageDialog(null,"��ϲ! ��Ӯ��!");
	}
	public void loseGame() {
		isGamming=false;
		isGoing=false;
		fc.message.messageArea.append(fc.opname+" win!!\n");
		fc.message.messageArea.append(fc.myname+" lose!\n");
		fc.message.messageArea.append("What a pity! You lose!\n");
		JOptionPane.showMessageDialog(null,"�ź�,������QAQ");
	}
	public void resetGame() {
		chessCount=0;
		//�������
		for(int i=0;i<chessList.length;++i)chessList[i]=null;
		repaint();
		fc.control.joinGameButton.setEnabled(true);
	}
	
	private boolean isWin(int col,int row) {
		int continueChess=1;
		Color c=isBlack?Color.black:Color.white;
		//1.����
		//����Ѱ��
		for(int x=col-1;x>=0;x--) {
			if(hasChess(x,row,c))++continueChess;
			else break;
		}
		//����
		for(int x=col+1;x<=COLS;++x) {
			if(hasChess(x,row,c))++continueChess;
			else break;
		}//�жϺ���
		if(continueChess>=5)return true;
		else continueChess=1;
		
		//2.����
		//����
		for(int y=row-1;y>=0;--y) {
			if(hasChess(col,y,c))++continueChess;
			else break;
		}
		//����
		for(int y=row+1;y<=ROWS;++y) {
			if(hasChess(col,y,c))++continueChess;
			else break;
		}//�ж�����
		if(continueChess>=5)return true;
		else continueChess=1;
		
		//3.���ϵ�����
		for(int x=col+1,y=row-1;x<=COLS&&y>=0;++x,--y) {
			if(hasChess(x,y,c))++continueChess;
			else break;
		}
		for(int x=col-1,y=row+1;x>=0&&y<=ROWS;--x,++y) {
			if(hasChess(x,y,c))++continueChess;
			else break;
		}//�ж�
		if(continueChess>=5)return true;
		else continueChess=1;
		
		//4.���ϵ�����
		for(int x=col-1,y=row-1;x>=0&&y>=0;--x,--y) {
			if(hasChess(x,y,c))++continueChess;
			else break;
		}
		for(int x=col+1,y=row+1;x<=COLS&&y<=ROWS;++x,++y) {
			if(hasChess(x,y,c))++continueChess;
			else break;
		}//�ж�
		if(continueChess>=5)return true;
		return false;
	}

//�ڲ���,��¼���ĵ��
class MouseMonitor extends MouseAdapter{
	public void mousePressed(MouseEvent e) {
		//�ȿ��Ƿ�������Ϸ,�ɲ������������-
		if(!isGamming||!isGoing)return;
		//����굥�������ػ�����������
		int col=(e.getX()-MARGIN+SPAN/2)/SPAN;
		int row=(e.getY()-MARGIN+SPAN/2)/SPAN;
		//�������治����
		if(col<0||col>COLS||row<0||row>ROWS)return;
		//�Ѿ������˲�����
		if(hasChess(col,row))return;
		Chess ch=new Chess(PanelBoard.this,col,row,isBlack?Color.black:Color.white);
		chessList[chessCount++]=ch;
		repaint(); //֪ͨϵͳ���»���
		isGoing=!isGoing;
		fc.c.go(col,row);
		//ÿ������һ�����ж��Ƿ�Ӯ��
		if(isWin(col,row)) {
			fc.c.wins();
			return;
		}
		
	}
}
//�ڲ��� ��¼����ƶ�
class MouseMotionMonitor extends MouseMotionAdapter{
	public void mouseMoved(MouseEvent e) {
		int col=(e.getX()-MARGIN+SPAN/2)/SPAN;
		int row=(e.getY()-MARGIN+SPAN/2)/SPAN;
		if(col<0||col>COLS||row<0||row>ROWS||!isGamming||hasChess(col,row))
			PanelBoard.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		else PanelBoard.this.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
}

}
