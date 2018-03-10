package Client;

public class TimeThread extends Thread {
	FiveClient fc;
	private int myTotalTime;
	private int opTotalTime;
	public TimeThread(FiveClient fc,int totalTime) {
		this.fc=fc;
		this.myTotalTime=totalTime;
		this.opTotalTime=totalTime;
	}
	public void run() {
		fc.timing.SetMyTime(myTotalTime);
		fc.timing.setOpTime(opTotalTime);
		while(fc.panelBoard.isGamming) {
			try {
				sleep(1000);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(fc.panelBoard.isGoing) {
				--myTotalTime;
				fc.timing.SetMyTime(myTotalTime);
				if(myTotalTime<=0) {
					fc.c.giveUp();
					break;
				}
			}else {
				--opTotalTime;
				fc.timing.setOpTime(opTotalTime);
				if(opTotalTime<=0) {
					break;
				}
			}
		}
	}
}
