import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class DecisionTreeClassifier {
	
	//print the tree
	public void LevelOrderTraverse(TreeNode T){
		System.out.println("Decision Tree");
	    Queue<TreeNode> Q = new LinkedList<TreeNode>();
		if(T!=null) 
			Q.add(T);
		while(!Q.isEmpty())
		{
			TreeNode node = Q.remove();
			System.out.println(node.getName());
			ArrayList<TreeNode> children = node.getChildren();
			if(children != null)
				for(int i = 0; i < children.size(); i++)
					Q.add(children.get(i));
		}
	}
	
	//test the decison tree
	public double testTree(ArrayList<Document> Data, TreeNode root){
		//prepareData prepare = new prepareData(Data);
		//ArrayList<Document> testData = prepare.getFormatTrainData();
		int rightNum = 0;
		for(int i = 0; i < Data.size(); i++){
			Document doc = Data.get(i);
			TreeNode tn = root;
			int label = doc.getLabel();
			while(!tn.getChildren().isEmpty()){
				String word = tn.getName();
				if(doc.getFrequency(word) > 0){
					tn = tn.getChildren().get(1);
				}else{
					tn = tn.getChildren().get(0);
				}
			}
			int result;
			if(tn.getName().equals("1"))
				result = 1;
			else result = 0;
			System.out.println(result);
			if(result == label)
				rightNum++;
		}
		return rightNum*1.0/Data.size();
	}
	
	public static void main(String[] args) { 
		ArrayList<String> trainingData = new ArrayList<String>();
		ArrayList<String> testingData = new ArrayList<String>();
        Scanner reader = new Scanner(System.in);
        String line=null;
        BufferedReader fileReader1 = null;
        try {
        	fileReader1=new BufferedReader(new FileReader(new File(args[0])));
        	while((line=fileReader1.readLine())!=null){
        		trainingData.add(line);
        }
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        BufferedReader fileReader2 = null;
        try {
        	fileReader2=new BufferedReader(new FileReader(new File(args[1])));
        	while((line=fileReader2.readLine())!=null){
        		testingData.add(line);
        }
        } catch (FileNotFoundException e) { 
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        DecisionTreeClassifier test = new DecisionTreeClassifier();
        
        
      //start to count time
        long start = System.currentTimeMillis();
        double accuracy1,accuracy2;
        //prepare for the data formating
      	prepareData prepare1 = new prepareData(trainingData);
      	//get the normalized data as well as the dictionary which is used for creating tree
      	ArrayList<Document> formatTrainData = prepare1.getFormatTrainData();
      	ArrayList<wordUnit> dictionary = prepare1.getDictionary();
        
        //get the decisioin tree builder
        DecisionTree trainTree = new DecisionTree(formatTrainData, dictionary);
        long timeForBuild, timeForTest;

        //get the root of the decision tree and stop timing
        TreeNode decitiionTreeRoot = trainTree.buildTree(null, formatTrainData);
        //stop timing
        long end = System.currentTimeMillis();
        timeForBuild = end - start;
        
        //start to count time
        start = System.currentTimeMillis();
        //prepare for the data formating for test data
      	prepareData prepare2 = new prepareData(testingData);
      	//get the normalized data
      	ArrayList<Document> formatTestData = prepare2.getFormatTrainData();
        accuracy1 = test.testTree(formatTrainData, decitiionTreeRoot); 
        accuracy2 = test.testTree(formatTestData, decitiionTreeRoot); 
        //stop timing
        end = System.currentTimeMillis();
        timeForTest = end - start;
        
        System.out.println(timeForBuild/1000 + " seconds");
        System.out.println(timeForTest/1000 + " seconds");
        System.out.println(accuracy1);
        System.out.println(accuracy2);
	}
} 
