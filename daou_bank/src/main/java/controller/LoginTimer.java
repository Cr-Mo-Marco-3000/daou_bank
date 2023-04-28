package controller;

import java.time.LocalDateTime;
import java.util.Calendar;

public class LoginTimer extends Thread {
	
	private int checkHour;
	private int checkMinute;
	private int checkSecond;
	
	public LoginTimer (int checkHour, int checkMinute, int checkSecond) {
		this.checkHour = checkHour;
		this.checkMinute = checkMinute;
		this.checkSecond = checkSecond;
	}
	
	@Override
	public void run() {
		while (true) {
			Calendar now = Calendar.getInstance();
			// 오전 1시 0분 0초에 체크함
			if (now.get(Calendar.HOUR_OF_DAY) == checkHour && 
					now.get(Calendar.MINUTE) == checkMinute && 
					now.get(Calendar.SECOND) == checkSecond) {
				System.out.println("씨발");
				BankServiceImpl service = new BankServiceImpl();
				service.deleteOutdatedTemporaryAccount();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
