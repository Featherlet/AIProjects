import java.util.ArrayList;


public class DecisionTree {
	private TreeNode root = null;
	private ArrayList<Document> formatTrainData = null; //the final format Data
	private ArrayList<wordUnit> dictionary = null;
	
	public DecisionTree(ArrayList<Document> data, ArrayList<wordUnit> dict){
		formatTrainData = data;
		dictionary = dict;
	}
	
	//get current entropy value which is H(Y)
    public double getCurrentH(ArrayList<Document> datas){
    	int positive = 0, negative = 0;
    	for(int i = 0; i < datas.size(); i++){
    		if(datas.get(i).getLabel() == 1)
    			positive++;
    		else if(datas.get(i).getLabel() == 0)
    			negative++;
    	}
    	double p1 = positive * 1.0/ datas.size();
    	double p2 = negative * 1.0/ datas.size();
    	if(p1 == 0 || p2 == 0)
    		return 1.0;
    	else {
    		double H = 0 - p1* (Math.log(p1)*1.0 / Math.log(2)) - p2* (Math.log(p2)*1.0 / Math.log(2));
    		return H;
    	} 
    }
    
  //get current entropy value which is H(Y)
    public double getConditionalH(ArrayList<Document> data, String word){
    	//split the data into left node data and right node data and use two ArrayList to collect them
        ArrayList<Document> newdataleft = new ArrayList<Document>();
        ArrayList<Document> newdataright = new ArrayList<Document>();
        int index = 0; 
        double value = 0;//get the index and the value of the word in the dictionary
        for(int i = 0; i < dictionary.size(); i++){
        	if(dictionary.get(i).word.equals(word)){
        		index = i; //index of the word in the dictoinary
        		value = dictionary.get(i).value;
        		break;
        	}
        }
        for(int i = 0; i < data.size(); i++){
        	//see the value of the word(index) in each document
        	if(data.get(i).getVector().get(index) == 0)
    			newdataleft.add(data.get(i));
    		else 
        		newdataright.add(data.get(i));
    	}        
        //calculate the conditional entropies of children nodes
        double conditionalH = (newdataleft.size()*1.0 / data.size()) * getCurrentH(newdataleft) 
        		+ (newdataright.size()*1.0 / data.size()) * getCurrentH(newdataright);
    	return conditionalH; 
    }
	
    //return the best attribute
    public String bestAttribute(ArrayList<Document> data){
    	double minH = 1000;
    	String s = "";
    	for(int i = 0; i < dictionary.size(); i++){
    		double H = getConditionalH(data, dictionary.get(i).word);
    		if(H < minH){
    			minH = H;
    			s = dictionary.get(i).word;
    		}
    	}
    	return s;
    }
	
	//build the decision tree and return the root of the tree
	public TreeNode buildTree(TreeNode theRoot, ArrayList<Document> theData){  
        TreeNode node = new TreeNode();  
        if(theRoot == null){ //theRoot == null means this is the root of the tree
        	node.setParent(theRoot);   //set the fist node as root
            node.setData(formatTrainData);  //put the whole data set to the node
            root = node; //set the root
        }else if(theData == null){ 
        	System.out.println("Error! You should provide data!");
        	return null;
        }else{ //then this is the child node
        	node.setParent(theRoot);
        	theRoot.setChildren(node);
            node.setData(theData);
        }
        //calculate the current entropy
        double currentH = getCurrentH(theData);
        
        //find the best word
        String word = bestAttribute(theData);
        
        //calculate the conditional entropies of children nodes
        double conditionalH = getConditionalH(theData, word);
        
        //find the index of the word in the dictionary and also it's value
        int index = 0;
        double value = 0;
        for(int i = 0; i < dictionary.size(); i++){
        	if(dictionary.get(i).word.equals(word)){
        		index = i;
        		value = dictionary.get(i).value;
        		break;
        	}
        }
       
        //if the information gain is small enough, then stop splitting
        if(currentH - conditionalH > 0.01){
        	node.setName(word);
        	//System.out.println(word + " ");
        	
        	//split the data into left node data and right node data and use two ArrayList to collect them
            ArrayList<Document> newdataleft = new ArrayList<Document>();
            ArrayList<Document> newdataright = new ArrayList<Document>();
            for(int i = 0; i < theData.size(); i++){
            	//see the value of the word(index) in each document
            	if(theData.get(i).getVector().get(index) == 0)
        			newdataleft.add(theData.get(i));
        		else 
            		newdataright.add(theData.get(i));
        	}        
        	//build the recurse tree
        	buildTree(node, newdataleft);
        	buildTree(node, newdataright);
        }else{
        	//deal with leaves and add labels.
        	//calculate the number of positive  and negative documents
        	int positive = 0, negative = 0;
        	for(int i = 0; i < theData.size(); i++){
        		if(theData.get(i).getLabel() == 1)
        			positive++;
        		else if(theData.get(i).getLabel() == 0)
        			negative++;
        	}
        	if(positive >= negative){
        		node.setChildren(null);
        		node.setName("1");
        	}else{
        		node.setChildren(null);
        		node.setName("0");
        	}
        }
        return root;
      }
}
