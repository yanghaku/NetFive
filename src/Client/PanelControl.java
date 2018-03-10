package Client;

import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

public class PanelControl extends Panel{
	private static final long serialVersionUID = 1L;
	public Label IPlabel=new Label("������IP:",Label.LEFT);
	public TextField inputIP=new TextField("127.0.0.1",12);
	public Button connectButton=new Button("��������");
	public Button joinGameButton=new Button("������Ϸ");
	public Button cancelGameButton=new Button("������Ϸ");
	public Button exitGameButton=new Button("�رճ���");
	//���캯��
	public PanelControl() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBackground(Color.PINK);
		IPlabel.setFont(FiveClient.fontBig);
		inputIP.setFont(FiveClient.fontBig);
		connectButton.setFont(FiveClient.fontBig);
		joinGameButton.setFont(FiveClient.fontBig);
		cancelGameButton.setFont(FiveClient.fontBig);
		exitGameButton.setFont(FiveClient.fontBig);
		IPlabel.setForeground(Color.BLUE);
		connectButton.setForeground(Color.BLUE);
		joinGameButton.setForeground(Color.BLUE);
		cancelGameButton.setForeground(Color.BLUE);
		exitGameButton.setForeground(Color.red);
		add(IPlabel);
		add(inputIP);
		add(connectButton);
		add(joinGameButton);
		add(cancelGameButton);
		add(exitGameButton);
	}
}
