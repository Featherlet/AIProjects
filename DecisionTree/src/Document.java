import java.util.ArrayList;


public class Document {
	int label;
	ArrayList<Integer> vector = null;
	ArrayList<wordUnit> words = null;
	int total = 0;
	int maxFrq = 0;
	public Document(ArrayList<String> ws){
		total = ws.size();
		ArrayList<String> allwords = new ArrayList<String>();
		for(int i = 0; i < ws.size(); i++){
			if(!allwords.contains(ws.get(i)))
				allwords.add(ws.get(i));
		}
		words = new ArrayList<wordUnit>();
		for(int i = 0; i < allwords.size(); i++){
			int counter = 0;
			for(int j = 0; j < ws.size(); j++){
				if(ws.get(j).equals(allwords.get(i)))
					counter++;
			}
			if(maxFrq < counter)
				maxFrq = counter;
			wordUnit newword = new wordUnit();
			newword.word = allwords.get(i);
			newword.frequency = counter;
			words.add(newword);
		}
	}
	
	public int getFrequency(String word){
		int num = 0;
		for(int i = 0; i < words.size(); i++)
			if(words.get(i).word.equals(word)){
				num = words.get(i).frequency;
				break;
			}
		return num;
	}
	
	public ArrayList<wordUnit> getWords() {
		return words;
	}

	public void setWords(ArrayList<wordUnit> words) {
		this.words = words;
	}


	public void setLabel(int l){
		label = l;
	}
	
	public int getLabel() {
		return label;
	}

	public void setVector(ArrayList<wordUnit> dictionary){
		vector = new ArrayList<Integer>();
		for(int i = 0; i < dictionary.size(); i++){
			//calculate the currency of word w
			int frq = getFrequency(dictionary.get(i).word);
			//do the normalization
			double tf = 0.6 +0.6 * frq / maxFrq;
			//if(tf > dictionary.get(i).value )
			if(frq > 0)
				vector.add(1);
			else
				vector.add(0);
		}
	}
	
	public ArrayList<Integer> getVector() {
		return vector;
	}
	
	

}
