import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Compare {
	boolean compare(String name1, String name2){
		File file1 = new File(name1);
		File file2 = new File(name2);
		if(!file1.exists())
		{
			System.out.println("file1 not exists!");
			return false;
		}
		if(!file2.exists())
		{
			System.out.println("file2 not exists!");
			return false;
		}
		String line1 = new String();
		String line2 = new String();
		try {
			BufferedReader reader1 = new BufferedReader(new FileReader(file1)); 
			BufferedReader reader2 = new BufferedReader(new FileReader(file2)); 
			line1 = reader1.readLine();
			line2 = reader2.readLine();			
			while(line1 != null && line2 != null){
				if(!line1.equals(line2))
					return false;
				line1 = reader1.readLine();
				line2 = reader2.readLine();		
			}
			reader1.close();
			reader2.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}
