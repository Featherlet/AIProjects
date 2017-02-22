
public class Operator {
	public String location;
	public String type;
	public char[][] board;
	
	public Operator(String loc, String tp, char[][] b){
		location = loc;
		type = tp;
		board = b;
	}
	
	public void show(){
		System.out.println(location);
		System.out.println(type);
		for(int i = 0; i < board.length; i++){
			String line = "";
			for(int j = 0; j < board[0].length; j++)
				line = line + board[i][j];
			System.out.println(line);
		}
	}
}
