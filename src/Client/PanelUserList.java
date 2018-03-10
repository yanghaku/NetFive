package Client;

import java.awt.BorderLayout;
import java.awt.List;
import java.awt.Panel;

public class PanelUserList extends Panel{
	private static final long serialVersionUID = 1L;
	public List userList=new List(8);
	public PanelUserList() {
		setLayout(new BorderLayout());
		userList.setFont(FiveClient.fontSmall);
		add(userList,BorderLayout.CENTER);
	}
}
