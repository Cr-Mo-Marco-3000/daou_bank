package view;

public class Main {
	
	public static void main(String[] args) {
		
		MenuImpl menu = MenuImpl.getInstance(); //메뉴화면 인스턴스
		menu.init();
	}
}