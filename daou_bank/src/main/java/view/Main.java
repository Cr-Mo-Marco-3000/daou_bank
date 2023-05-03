package view;


import controller.LoginTimer;

public class Main {
	
	public static void main(String[] args) {
		

		System.out.println("");
		System.out.println("\t /       \\  /      \\  /      \\ /  |  /  |       /      \\ /        |/  \\     /  |");
		System.out.println("\t $$$$$$$  |/$$$$$$  |/$$$$$$  |$$ |  $$ |      /$$$$$$  |$$$$$$$$/ $$  \\   /$$ |");
		System.out.println("\t $$ |  $$ |$$ |__$$ |$$ |  $$ |$$ |  $$ |      $$ |__$$ |   $$ |   $$$  \\ /$$$ |");
		System.out.println("\t $$ |  $$ |$$    $$ |$$ |  $$ |$$ |  $$ |      $$    $$ |   $$ |   $$$$  /$$$$ |");
		System.out.println("\t $$ |  $$ |$$$$$$$$ |$$ |  $$ |$$ |  $$ |      $$$$$$$$ |   $$ |   $$ $$ $$/$$ |");
		System.out.println("\t $$ |__$$ |$$ |  $$ |$$ \\__$$ |$$ \\__$$ |      $$ |  $$ |   $$ |   $$ |$$$/ $$ |");
		System.out.println("\t $$    $$/ $$ |  $$ |$$    $$/ $$    $$/       $$ |  $$ |   $$ |   $$ | $/  $$ |");
		System.out.println("\t $$$$$$$/  $$/   $$/  $$$$$$/   $$$$$$/        $$/   $$/    $$/    $$/      $$/ ");
		System.out.println("");
		
		Menu menu = Menu.getInstance(); //메뉴화면 인스턴스
		
		// 통장 체크 타이머 => 오전 1시 0분 0초에 체크해서, 해당하지 않는 통장을 날림
		LoginTimer timer = new LoginTimer(1, 0, 0);
		timer.start();
		menu.init();

		
	}
}