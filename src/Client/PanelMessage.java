package Client;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.TextArea;

public class PanelMessage extends Panel{
	private static final long serialVersionUID = 1L;
	public TextArea messageArea;
	public PanelMessage() {
		setLayout(new BorderLayout());
		messageArea=new TextArea("",12,20,TextArea.SCROLLBARS_VERTICAL_ONLY);
		messageArea.setFont(FiveClient.fontSmall);
		add(messageArea,BorderLayout.CENTER);
	}
}
