import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class DataIO {
	public String[] read(String filename){
		//String filename = "input12.txt";
		ArrayList<String> content = null;
		try{
			File file = new File(filename);
			if(!file.canRead()) return null;
			String line = new String();
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			content = new ArrayList<String>();
			line = reader.readLine();
			while(line != null){
				content.add(line);
				line = reader.readLine();
			}
			reader.close();
			String[] result = new String[content.size()];
			content.toArray(result);
			return result;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void write(Operator op, String filename){
		try{
			FileWriter fw = new FileWriter(filename);
			fw.write(op.location + " " + op.type + "\n");
			char[][] board = op.board;
			for(int i = 0; i < board.length - 1; i++){
				String line = "";
				for(int j = 0; j < board[0].length; j++)
					line = line + board[i][j];
				fw.write(line + "\n");
			}
			String line = "";
			for(int j = 0; j < board[0].length; j++)
				line = line + board[board[0].length - 1][j];
			fw.write(line);
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
