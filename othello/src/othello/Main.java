package othello;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Board bd = new Board();
		Scanner sc = new Scanner(System.in);
		
		while(bd.getGame()) {
			
			System.out.println(bd.getStone() + "のターンです。");
			System.out.println("X座標を入力してください");
			int x = sc.nextInt();
			System.out.println("Y座標を入力してください");
			int y = sc.nextInt();
			
			bd.setStone(x,y);
		}
	}

}
