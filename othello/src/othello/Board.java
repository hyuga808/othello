package othello;

public class Board {
	//フィールド
	private boolean game = true;                 //ゲーム続行判定用フラグ
	private String[][] board = new String[8][8]; //ボード(盤面)
	private final String EMPTY = " ";             //駒初期値設定用
	private final String BLACK = "●";            //プレイヤー１の駒
	private final String WHITE = "○";            //プレイヤー２の駒
	private String stone;                        //駒色の配置順番管理用
	private String next_stone;                   //駒色の配置順番管理用
	
	//コンストラクタ
	public Board() {
		//初期値にEMPTY設定
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				board[i][j] = EMPTY; 
			}
		}
		
		//ボードに初期駒配置
		this.board[3][3] = BLACK;
		this.board[4][4] = BLACK;
		this.board[3][4] = WHITE;
		this.board[4][3] = WHITE;
		
		stone = BLACK;
		next_stone = WHITE;
	}
	
	//ボード表示メソッド
	public void show() {
		int count_black = 0;
		int count_white = 0;
		int count_empty = 0;
		int i = 1;
		
		System.out.println(" |1|2|3|4|5|6|7|8|");
		System.out.println("------------------");
		for(String[] bd : board) {
			System.out.print(i + "|");
			for(String value : bd) {
				System.out.print(value);
				System.out.print("|");
				
				if(value.equals(BLACK)) {
					count_black++;
				} else if(value.equals(WHITE)) {
					count_white++;
				} else if(value.equals(EMPTY)){
					count_empty++;
				}
			}
			System.out.println("");
			i++;
		}
		
		System.out.println("------------------");
		System.out.println(BLACK + "：" + count_black);
		System.out.println(WHITE + "：" + count_white);
		System.out.println("空き：" + count_empty);
		System.out.println("------------------");
		
	}

}
