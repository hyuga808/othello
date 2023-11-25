package othello;

public class Board {
	//フィールド
	private boolean game = true;                 //ゲーム続行判定用フラグ
	private String[][] board = new String[8][8]; //ボード(盤面)
	private final String EMPTY = " ";             //駒初期値設定用
	private final String BLACK = "○";            //プレイヤー１の駒
	private final String WHITE = "●";            //プレイヤー２の駒
	private String stone;                        //駒色の配置順番管理用
	private String next_stone;                   //駒色の配置順番管理用
	private boolean isTurn = false;              //駒が反転有無の判定用
	
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
		
		this.show();
	}
	
	//ボード表示メソッド
	public void show() {
		int count_black = 0;
		int count_white = 0;
		int count_empty = 0;
		int i = 0;
		
		System.out.println(" |0|1|2|3|4|5|6|7|");
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
		if(count_empty == 0 || !(this.game)) {
			System.out.println("終了！！！");
			this.game = false;
			
			if(count_black > count_white) {
				System.out.println(this.BLACK + "勝利！！");
			} else if(count_white > count_black) {
				System.out.println(this.WHITE + "勝利！！");
			} else {
				System.out.println("引き分け");
			}
		}
	}
	
	public void setStone(int x, int y) {
		// 座標外の場合
		if(x >= 8 || y >= 8 || x < 0 || y < 0 ) {
			System.out.println("--------------------------------");
			System.out.println("この座標には配置できません。");
			System.out.println("--------------------------------");
		} 
		else if(!(board[y][x].equals(EMPTY))) 
		{
			System.out.println("--------------------------------");
			System.out.println("この座標には駒が配置済みです。");
			System.out.println("--------------------------------");
		}
		else
		{
		//駒を配置できる場合
			this.board[y][x] = stone;
			this.turnStone(y,x);
			
			//駒を一つも反転できなかった場合
			if(!(this.isTurn)) {
				System.out.println("--------------------------------");
				System.out.println("この座標では反転できる駒がありません");
				System.out.println("--------------------------------");
				this.board[y][x] = EMPTY;
			}
			//駒を反転できた場合に順番を入れ替える
			else 
			{
				reverseStone();
				//駒入れ替え後に全体で配置できる箇所があるかチェック
				if(!(this.checkBoard())) {
					//自駒で反転箇所がない場合、相手の駒の配置チェックを行う。
					reverseStone();
					if(this.checkBoard()) {
						System.out.println("--------------------------------");
						System.out.println(this.next_stone + "は配置できる箇所がありません。");
						System.out.println("引き続き" + this.stone + "のターンです。");
						System.out.println("--------------------------------");
					} else {
						//両者配置できる箇所がない場合はゲーム終了。
						System.out.println("--------------------------------");
						System.out.println("両者配置できる箇所がありません。");
						System.out.println("--------------------------------");
						this.game = false;
					}
				}
			}
		}
		show();
	}

	public boolean checkBoard() {
		//駒の判定有無フラグをリセット
		this.isTurn = false;
		
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].equals(EMPTY)) {
					this.turnUp(i,j,true);
					this.turnRight(i,j,true);
					this.turnDown(i,j,true);
					this.turnLeft(i,j,true);
					this.turnRightUp(i,j,true);
					this.turnRightDown(i,j,true);
					this.turnLeftUp(i,j,true);
					this.turnLeftDown(i,j,true);
				}
			}
		}
		 return this.isTurn;
	}
	
	public void turnStone(int y, int x) {
		//駒の判定有無フラグをリセット
		this.isTurn = false;
		
		//8方向のチェック&&駒の反転
		this.turnUp(x,y,false);
		this.turnRight(x,y,false);
		this.turnDown(x,y,false);
		this.turnLeft(x,y,false);
		this.turnRightUp(x,y,false);
		this.turnRightDown(x,y,false);
		this.turnLeftUp(x,y,false);
		this.turnLeftDown(x,y,false);
	}
	
	public void turnUp(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = y-1; i >= 0; i--) {
			//一つ上の駒が相手駒の場合
			if(board[i][x].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[i][x].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = y; j > i; j--) {
					board[j][x] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[i][x].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnRight(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = x+1; i < 8; i++) {
			//一つ上の駒が相手駒の場合
			if(board[y][i].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[y][i].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
				//相手駒を反転
					for(int j = x; j < i; j++) {
					board[y][j] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[y][i].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnDown(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = y+1; i < 8; i++) {
			//一つ上の駒が相手駒の場合
			if(board[i][x].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[i][x].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = y; j < i; j++) {
						board[j][x] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[i][x].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnLeft(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = x-1; i >= 0; i--) {
			//一つ上の駒が相手駒の場合
			if(board[y][i].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[y][i].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = x; j > i; j--) {
						board[y][j] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[y][i].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnRightUp(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = 1; true; i++) {
			//座標が範囲外の場合
			if(x+i >= 8 || y-i < 0) {
				break;
			}
			//一つ上の駒が相手駒の場合
			else if(board[y-i][x+i].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[y-i][x+i].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = 1; j < i; j++) {
						board[y-j][x+j] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[y-i][x+i].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnRightDown(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = 1; true; i++) {
			//座標が範囲外の場合
			if(x+i >= 8 || y+i >= 8) {
				break;
			}
			//一つ上の駒が相手駒の場合
			if(board[y+i][x+i].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[y+i][x+i].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = 1; j < i; j++) {
						board[y+j][x+j] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[y+i][x+i].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnLeftUp(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = 1; true; i++) {
			//座標が範囲外の場合
			if(x-i < 0 || y-i < 0) {
				break;
			}
			//一つ上の駒が相手駒の場合
			if(board[y-i][x-i].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[y-i][x-i].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = 1; j < i; j++) {
						board[y-j][x-j] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[y-i][x-i].equals(EMPTY)){
				break;
			}
		}
	}
	
	public void turnLeftDown(int x, int y,boolean isCheck) {
		//駒反転用のフラグ
		boolean turnFlg = false;
		
		for(int i = 1; true; i++) {
			//座標が範囲外の場合
			if(x-i < 0 || y+i >= 8) {
				break;
			}
			//一つ上の駒が相手駒の場合
			if(board[y+i][x-i].equals(next_stone) && !(turnFlg)) {
				turnFlg = true;
			}
			//相手駒を自駒で挟める場合
			else if(board[y+i][x-i].equals(stone) && turnFlg) {
				//checkBoardメソッドの際は駒を反転させない。チェックのみ行うため
				if(!(isCheck)) {
					//相手駒を反転
					for(int j = 1; j < i; j++) {
						board[y+j][x-j] = stone;
					}
				}
				this.isTurn = true;
				break;
			} 
			//一つ上の駒が自駒&&配置されてない場合
			else if(!(turnFlg) || board[y+i][x-i].equals(EMPTY)){
				break;
			}
		}
	}
 	
	//自分と相手のターンを変更するために駒色を反転させるメソッド
	public void reverseStone() {
		String next_stone2 = stone;
		stone = next_stone;
		next_stone = next_stone2;
	}
	
	public String getStone() {
		return this.stone;
	}
	
	public boolean getGame() {
		return this.game;
	}

}
