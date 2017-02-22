
public class Homework2 {
	
	public static void main(String[] args){
		DataIO io = new DataIO();
		/*String[] content = io.read("input12.txt");
		if(content[1].equals("MINIMAX")){
			long startTime = System.currentTimeMillis();
			MiniMax minimax = new MiniMax(content);
			Operator op = minimax.MiniMaxDecision();
			op.show();
			io.write(op);
			long endTime = System.currentTimeMillis();
			System.out.println((endTime - startTime)* 1.0 / 1000);
		}else if(content[1].equals("ALPHABETA")){
			long startTime = System.currentTimeMillis();
			AlphaBeta ab = new AlphaBeta(content);
			Operator op = ab.AlphaBetaDecision();
			op.show();
			io.write(op);
			long endTime = System.currentTimeMillis();
			System.out.println((endTime - startTime)* 1.0 / 1000);
		}*/
		
		//test
		for(int i = 97; i < 98; i++){
			String name = "F:/USC_Courses/561AI/新建文件夹/TestCase_New/Test" + i + "/input.txt";
			String[] content = io.read(name);
			if(content[1].equals("MINIMAX")){
				long startTime = System.currentTimeMillis();
				MiniMax minimax = new MiniMax(content);
				Operator op = minimax.MiniMaxDecision();
				op.show();
				io.write(op, "F:/USC_Courses/561AI/新建文件夹/TestCase_New/myout/output" + i + ".txt");
				long endTime = System.currentTimeMillis();
				System.out.println((endTime - startTime)* 1.0 / 1000);
			}else if(content[1].equals("ALPHABETA")){
				long startTime = System.currentTimeMillis();
				AlphaBeta ab = new AlphaBeta(content);
				Operator op = ab.AlphaBetaDecision();
				op.show();
				io.write(op, "F:/USC_Courses/561AI/新建文件夹/TestCase_New/myout/output" + i + ".txt");
				long endTime = System.currentTimeMillis();
				System.out.println((endTime - startTime)* 1.0 / 1000);
			}
		}
		for(int i = 1; i < 98; i++){
			Compare cp =  new Compare();
			boolean result = cp.compare("F:/USC_Courses/561AI/新建文件夹/TestCase_New/Test" + i + "/output.txt", 
					"F:/USC_Courses/561AI/新建文件夹/TestCase_New/myout/output" + i + ".txt");
			System.out.println(result);
		}
	}
}
