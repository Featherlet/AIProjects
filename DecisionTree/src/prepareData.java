import java.util.ArrayList;


public class prepareData {

	//a set of stop words that are useless.
	private static String[] stopWords = {"a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", 
				"almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount", 
				"an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at",
				"back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being",
				"below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", 
				"cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during",
				"each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every",
				"everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five",
				"for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", 
				"had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers",
				"herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest",
				"into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many",
				"may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
				"my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none",
				"noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto",
				"or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps",
				"please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", 
				"she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", 
				"something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that",
				"the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein",
				"thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through",
				"throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two",
				"un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever",
				"when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", 
				"whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with",
				"within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the", "br", "b", "c", "d",
				"e", "f", "g","h","i","j", "k", "l", "m","n","o","p", "q", "r", "s","t","u","v", "w", "x", "y","z"};
		String[] stop = {"a","is", "it"};	
	
	private TreeNode root = null;
	private ArrayList<String> traindata = null;
	private ArrayList<Document> formatTrainData = null; //the final format Data
	private ArrayList<wordUnit> dictionary = null;
	private ArrayList<wordUnit> allWords = null; //the media to calculate the dictionary
	private ArrayList<String> allWordsHelper = null; //keep the same index of each word with allWords
	
	private int maxFrq = 0;
	
	public prepareData(ArrayList<String> tdata){
		traindata = tdata;
    	allWords = new ArrayList<wordUnit>();
    	allWordsHelper = new ArrayList<String>();
    	dictionary = new ArrayList<wordUnit>();
    	initDocument();
    	initDictionary();
    	initVectors();
	}
	
	public ArrayList<String> splitWords(String line){
		ArrayList<String> words = new ArrayList<String>();
		Stemmer stemmer = new Stemmer();
		//firstly, turn all the upper case character into lower case.
		line = line.toLowerCase();
		//then turn all the non-letter characters into space.
		for(int i = 0; i < line.length(); i++){ 
			if(line.charAt(i) < 97 || line.charAt(i) > 122){
				char c = line.charAt(i);
				line = line.replace(c, ' ');
			}
		}
		String[] temp = line.split(" ");
		for(int i = 0; i< temp.length; i++){ 
			String s = temp[i];
			while(s.startsWith(" ")){
				s = s.substring(1);
			}
			while(s.endsWith(" ")){
				s = s.substring(0, s.length()-2);
			}
			if(s.length() == 0) continue; //if the string is empty, then throw it away
			//after changing it to a purely word, do the stemming.
			s = stemmer.getResult(s);			
			//then check if it is stop word
			int k = 0;
			for(int j = 0; j < stopWords.length; j++){ // if c is a stop word, k = 1.
				String sword = stopWords[j];
				if(s.equals(sword)){
					k = 1;
					break;
				}
			}
			if(k == 0){ // s isn't a stop word
				words.add(s);
				//by spliting the words, we enlarge the word database at the same time
				if (allWordsHelper.contains(s)){
					int index = allWordsHelper.indexOf(s);
					allWords.get(index).frequency++;
					if(maxFrq < allWords.get(index).frequency)
						maxFrq = allWords.get(index).frequency;
				}else{
					allWordsHelper.add(s);
					wordUnit  wu = new wordUnit();
					wu.word = s;
					wu.frequency = 1;
					allWords.add(wu);
				}
			}	
		}
		return words;
	}
	
	//initial the original training data into the format data
	public void initDocument(){
		formatTrainData = new ArrayList<Document>();
		for(int i = 0; i < traindata.size(); i++){
			String line = traindata.get(i);
			int label;
			if(line.charAt(0) == '1')
				label = 1;
			else label = 0;
			line = line.substring(2);
			ArrayList<String> words = splitWords(line);
			Document doc = new Document(words);
			doc.setLabel(label);
			formatTrainData.add(doc);
		}
		//for(int i = 0; i < allWords.size(); i++)
			//System.out.println(allWords.get(i).word + " " + allWords.get(i).frequency);
	}
	
	//initial the dictionary which will include all the useful attributes(words)
	public void initDictionary(){
		int total = allWords.size();
		for(int i = 0; i < total; i++){
			int n = allWords.get(i).frequency;
			double p = n * 1.0 / total;
			if(p > 0.005){
				wordUnit wu = new wordUnit();
				wu.word = allWords.get(i).word;
				wu.frequency = n;
				dictionary.add(wu);
			}
		}
		for(int i = 0; i < dictionary.size(); i++){
			/**positive is the number of documents of label == 1 and negative is for label == 0
			int positive = 0, negative = 0;
			pnum is the number of positive documents  which have wu.word, similar to nnum
			int pnum = 0, nnum = 0;*/
			//for this word				
			wordUnit wu = dictionary.get(i);
			int counter = 0;
			double tf, idf;
			for(int j = 0; j < formatTrainData.size(); j++){
				/*if(formatTrainData.get(j).getLabel() == 1)
					pnum += wholeData.get(j).getFrequency(wu.word);
				else
					nnum += wholeData.get(j).getFrequency(wu.word);	*/
				//if(formatTrainData.get(j).getLabel() == 1)
					if(formatTrainData.get(j).getFrequency(wu.word) > 0)
						counter++; //k is the frequency the word appear in all the documents
			}
			//set the value(weight) of each attribute
			/**double a = pnum * 1.0 /positive;
			double b = pnum * 1.0 /negative;		*/	
			
			tf = 0.5 + 0.5*counter/maxFrq;
			//tf = 1 + Math.log(counter);
			if(counter == 0)
				idf = 0;
			else
				idf = Math.log(formatTrainData.size()/counter);
 			wu.value = tf * idf;
		}
		//for(int i = 0; i < dictionary.size(); i++)
			//System.out.println(dictionary.get(i).word + " " + dictionary.get(i).frequency + " " + dictionary.get(i).value);
	}
	
	//when we get the format data list, we can calculate the vector of each document
	public void initVectors(){
		for(int i = 0; i < formatTrainData.size(); i++){
			formatTrainData.get(i).setVector(dictionary);
			ArrayList<Integer> vector = formatTrainData.get(i).getVector();
			/*String s = "doc" + i + " ";
			for(int j = 0; j < dictionary.size(); j++){
				s = s + vector.get(j) + " ";
			}
			System.out.println(s);*/
		}
	}
    
    public ArrayList<Document> getFormatTrainData(){ 
    	/*for(int i = 0; i < wholeData.size(); i++){
    		String s = "doc" + i + "  ";
    		Document doc = wholeData.get(i);
    		for(int j = 0; j < dictionary.size(); j++){
    			String word = dictionary.get(j).word;
    			s = s + doc.getFrequency(word) + "  ";
    		}
    		System.out.println(s);
    	}*/    	
    	return formatTrainData;
    }
	
	public ArrayList<wordUnit> getDictionary(){
		return dictionary;
	}

}
