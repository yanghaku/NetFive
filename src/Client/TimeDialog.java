package Client;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JDialog;

public class TimeDialog {
	private int seconds=0;
	private JLabel label=new JLabel();
	private JButton confirm;
	private JButton cancel;
	private JDialog dialog=null;
	int result=1;
	public int showDialog(Frame father,String message,int sec) {
		seconds=sec;
		label.setText(message);
		label.setFont(FiveClient.fontBig);
		label.setForeground(Color.blue);
		label.setBounds(60,6,230,30);
		confirm=new JButton("����");
		confirm.setBounds(80,40,70,40);
		confirm.setFont(FiveClient.fontBig);
		confirm.setForeground(Color.blue);
		confirm.addActionListener(new ActionListener(){//����
			public void actionPerformed(ActionEvent e) {
				result=0;
				TimeDialog.this.dialog.dispose();
			}
		});
		cancel=new JButton("�ܾ�");
		cancel.setFont(FiveClient.fontBig);
		cancel.setForeground(Color.RED);
		cancel.setBounds(190,40,70,40);
		cancel.addActionListener(new ActionListener() {//����
			public void actionPerformed(ActionEvent e) {
				result=1;
				TimeDialog.this.dialog.dispose();
			}
		});
		dialog=new JDialog(father,true);
		dialog.setTitle("��ʾ:�����ڽ���"+seconds+"����Զ��ر�");
		dialog.setLayout(null);
		dialog.add(label);
		dialog.add(confirm);
		dialog.add(cancel);
		
		ScheduledExecutorService s=Executors.newSingleThreadScheduledExecutor();
		s.scheduleAtFixedRate(new Runnable() {
			public void run() {
				--TimeDialog.this.seconds;
				if(TimeDialog.this.seconds==0) {
					TimeDialog.this.dialog.dispose();
				}else {
					dialog.setTitle("��ʾ:�����ڽ���"+seconds+"����Զ��ر�");
				}
			}
		},1,1,TimeUnit.SECONDS);
		//����:(runnable command,long initialdelay,long delay,timeunit unit);	
		//       ִ�е�����/�߳�        �״�ִ�е��ӳ�ʱ��      ���         ʱ�䵥λ           
		
		dialog.pack();
		dialog.setSize(new Dimension(350,120));
		dialog.setLocationRelativeTo(father);
		dialog.setVisible(true);
		dialog.setResizable(false);
		return result;
	}
}
