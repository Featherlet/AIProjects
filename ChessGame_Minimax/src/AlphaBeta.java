public class AlphaBeta {
	private int size;
	private char board[][];
	private int values[][];
	private int depth;
	private char you;
	private char oppo;
	
	public AlphaBeta(String[] inputData){
		try{
			size = Integer.parseInt(inputData[0]);
			if(size <= 0)
				throw new Exception();
			you = inputData[2].charAt(0);
			if(you == 'X')
				oppo = 'O';
			else
				oppo = 'X';
			depth = Integer.parseInt(inputData[3]);
			board = new char[size][size];
			values = new int[size][size];
			for(int i = 0; i < size; i++){
				String line = inputData[4 + i];
				String[] rowValues = line.split(" ");
				for(int j = 0; j < size; j++){
					values[i][j] = Integer.parseInt(rowValues[j]);
				}
			}
			for(int i = 0; i < size; i++){
				String line = inputData[4 + size + i];
				for(int j = 0; j < size; j++){
					board[i][j] = line.charAt(j);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public Operator AlphaBetaDecision(){
		int alpha = -65535;
		int beta = 65535;
		int maxScore = -65535;
		String location = null;
		String bestType = "Stake";
		char[][] newBoard = null;
		int height = 0;
		
		//first search all the possible stake first
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				height = 0;
				if(board[i][j] == 'X' || board[i][j] == 'O')
					continue;
				//if(isNearYou(i, j, board) && isNearOppo(i, j, board))
					//continue;
				//using a new board in case that the initial state is revised.
				char[][] currentBoard = new char[size][size];	
				for(int m = 0; m < size; m++)
					for(int n = 0; n < size; n++)
						currentBoard[m][n] = board[m][n];
				//put the chess as stake first
				currentBoard[i][j] = you;							
				//max of all the minValues
				int score = MinValue(currentBoard, ++height, alpha, beta);
				if(score > maxScore){
					maxScore = score;
					char alphacol = (char)(65 + j);
					location = Character.toString(alphacol) + Integer.toString(i + 1);
					newBoard = currentBoard.clone();
					bestType = "Stake";
				}
			}
		
		//second search raid 
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				height = 0;
				if(board[i][j] == 'X' || board[i][j] == 'O')
					continue;
				if(isNearYou(i, j, board) && isNearOppo(i, j, board)){
					//using a new board in case that the initial state is revised.
					char[][] currentBoard = new char[size][size];	
					for(int m = 0; m < size; m++)
						for(int n = 0; n < size; n++)
							currentBoard[m][n] = board[m][n];
					//put the chess as stake first
					currentBoard[i][j] = you;	
					//if so, then find out any adjacent opponent's chess and turn them
					if(j >= 1 && currentBoard[i][j - 1] == oppo){
						currentBoard[i][j - 1] = you;
					}
					if(j < size - 1 && currentBoard[i][j + 1] == oppo){
						currentBoard[i][j + 1] = you;
					}
					if(i >= 1 && currentBoard[i - 1][j] == oppo){
						currentBoard[i - 1][j] = you;
					}
					if(i < size - 1 && currentBoard[i + 1][j] == oppo){
						currentBoard[i + 1][j] = you;
					}
					//max of all the minValues
					int score = MinValue(currentBoard, ++height, alpha, beta);
					if(score > maxScore){
						maxScore = score;
						char alphacol = (char)(65 + j);
						location = Character.toString(alphacol) + Integer.toString(i + 1);
						newBoard = currentBoard.clone();
						bestType = "Raid";
					}
				}
			}	
		Operator op = new Operator(location, bestType, newBoard);
		return op;
	}
	
	public int MinValue(char[][] state, int height, int a, int b){
		int alpha = a;
		int beta = b;
		if(height == depth || isTerminal(state))
			return calculateScore(state);
		int v = 65535;
		
		//traverse all the next step which is stake and find out the minimum value state as nextState
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				int nextHeight = height + 1;
				if(state[i][j] == 'X' || state[i][j] == 'O')
					continue;
				//if(isNearYou(i, j, state) && isNearOppo(i, j, state))
					//continue;
				//using a new board in case that the initial state is revised.
				char[][] currentBoard = new char[size][size];	
				for(int m = 0; m < size; m++)
					for(int n = 0; n < size; n++)
						currentBoard[m][n] = state[m][n];
				//put the chess as stake first
				currentBoard[i][j] = oppo;
				//min of all the maxValues
				int score = MaxValue(currentBoard, nextHeight, alpha, beta);
				//v = Min(v, Max_Value(successors, alpha, beta))
				if(score < v){
					v = score;
				}
				//if v <= alpha then return v
				if(v <= alpha)
					return v;
				//alpha <- Max(alpha, v)
				beta = beta < v ? beta : v;
			}
		
		//traverse all the next step which is raid and find out the minimum value state as nextState
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				int nextHeight = height + 1;
				if(state[i][j] == 'X' || state[i][j] == 'O')
					continue;
				if(isNearYou(i, j, state) && isNearOppo(i, j, state)){
					//using a new board in case that the initial state is revised.
					char[][] currentBoard = new char[size][size];	
					for(int m = 0; m < size; m++)
						for(int n = 0; n < size; n++)
							currentBoard[m][n] = state[m][n];
					//put the chess as stake first
					currentBoard[i][j] = oppo;
					//if so, then find out any adjacent opponent's chess and turn them
					if(j >= 1 && currentBoard[i][j - 1] == you)
						currentBoard[i][j - 1] = oppo;
					if(j < size - 1 && currentBoard[i][j + 1] == you)
						currentBoard[i][j + 1] = oppo;
					if(i >= 1 && currentBoard[i - 1][j] == you)
						currentBoard[i - 1][j] = oppo;
					if(i < size - 1 && currentBoard[i + 1][j] == you)
						currentBoard[i + 1][j] = oppo;
					//min of all the maxValues
					int score = MaxValue(currentBoard, nextHeight, alpha, beta);
					//v = Min(v, Max_Value(successors, alpha, beta))
					if(score < v){
						v = score;
					}
					//if v <= alpha then return v
					if(v <= alpha)
						return v;
					//alpha <- Max(alpha, v)
					beta = beta < v ? beta : v;
				}
			}		
		return v;
	}
	
	public int MaxValue(char[][] state, int height, int a, int b){
		int alpha = a;
		int beta = b;
		if(height == depth || isTerminal(state))
			return calculateScore(state);
		int v = -65535;		
		//traverse all the next step which is stake and find out the maximum value state as nextState
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				int nextHeight = height + 1;
				if(state[i][j] == 'X' || state[i][j] == 'O')
					continue;
				//if(isNearYou(i, j, state) && isNearOppo(i, j, state))
					//continue;
				//using a new board in case that the initial state is revised.
				char[][] currentBoard = new char[size][size];	
				for(int m = 0; m < size; m++)
					for(int n = 0; n < size; n++)
						currentBoard[m][n] = state[m][n];
				//put the chess as stake first
				currentBoard[i][j] = you;
				//max of all the minValues
				int score = MinValue(currentBoard, nextHeight, alpha, beta);
				// v <- Max(v, Min_Value(successors))
				if(score > v){
					//update v to keep it to be the max
					v = score;
				}
				if(v >= beta) return v;
				alpha = alpha > v ? alpha : v;
			}
		
		//traverse all the next step which is raid and find out the maximum value state as nextState
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				int nextHeight = height + 1;
				if(state[i][j] == 'X' || state[i][j] == 'O')
					continue;
				if(isNearYou(i, j, state) && isNearOppo(i, j, state)){
					//using a new board in case that the initial state is revised.
					char[][] currentBoard = new char[size][size];	
					for(int m = 0; m < size; m++)
						for(int n = 0; n < size; n++)
							currentBoard[m][n] = state[m][n];
					//put the chess as stake first
					currentBoard[i][j] = you;
					//if so, then find out any adjacent opponent's chess and turn them
					if(j >= 1 && currentBoard[i][j - 1] == oppo)
						currentBoard[i][j - 1] = you;
					if(j < size - 1 && currentBoard[i][j + 1] == oppo)
						currentBoard[i][j + 1] = you;
					if(i >= 1 && currentBoard[i - 1][j] == oppo)
						currentBoard[i - 1][j] = you;
					if(i < size - 1 && currentBoard[i + 1][j] == oppo)
						currentBoard[i + 1][j] = you;
					//max of all the minValues
					int score = MinValue(currentBoard, nextHeight, alpha, beta);
					// v <- Max(v, Min_Value(successors))
					if(score > v){
						//update v to keep it to be the max
						v = score;
					}
					if(v >= beta) return v;
					alpha = alpha > v ? alpha : v;
				}
			}		
		return v;
	}
	
	private boolean isNearYou(int i, int j, char[][] currentBoard){
		if(j >= 1 && currentBoard[i][j - 1] == you)
			return true;
		if(j < size - 1 && currentBoard[i][j + 1] == you)
			return true;
		if(i >= 1 && currentBoard[i - 1][j] == you)
			return true;
		if(i < size - 1 && currentBoard[i + 1][j] == you)
			return true;
		return false;
	}
	
	private boolean isNearOppo(int i, int j, char[][] currentBoard){
		if(j >= 1 && currentBoard[i][j - 1] == oppo)
			return true;
		if(j < size - 1 && currentBoard[i][j + 1] == oppo)
			return true;
		if(i >= 1 && currentBoard[i - 1][j] == oppo)
			return true;
		if(i < size - 1 && currentBoard[i + 1][j] == oppo)
			return true;
		return false;
	}
	
	private boolean isTerminal(char[][] currentBoard){
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				if(currentBoard[i][j] == '.')
					return false;
		return true;
	}
	
	
	private int calculateScore(char[][] currentBoard){
		int myScore = 0;
		int oppoScore = 0;
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++){
				if(currentBoard[i][j] == you)
					myScore += values[i][j];
				else if(currentBoard[i][j] == oppo)
					oppoScore += values[i][j];
			}
		return myScore - oppoScore;
	}
	
	public void show(){
		System.out.println(size);
		System.out.println(you);
		System.out.println(depth);
		for(int i = 0; i < size; i++){
			String line = "";
			for(int j = 0; j < size; j++){
				line = line + values[i][j] + " ";
			}
			System.out.println(line);
		}
		for(int i = 0; i < size; i++){
			String line = "";
			for(int j = 0; j < size; j++){
				line = line + board[i][j];
			}
			System.out.println(line);
		}
	}

	
	
}
