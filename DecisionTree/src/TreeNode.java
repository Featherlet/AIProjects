import java.util.ArrayList;
//The TreeNode record not only the tree relation but also the attribute it use and the data set of the node.
public class TreeNode {  
	private String nodeName; //name of the node, which is also the splitting attribute 
	private TreeNode parent;
	private ArrayList<TreeNode> children; //records all the children  
    private ArrayList<Document> data; //the training data for this node
    public TreeNode() {  
    	nodeName = "noname"; 
    	parent = null;
    	children = new ArrayList<TreeNode>();  
	    data = null;  
	}    
	public void setParent(TreeNode p) {  
	    parent = p;  
	}  
	public TreeNode getParent() {  
	    return parent;  
	}  
	public ArrayList<TreeNode> getChildren() {  
	    return children;  
	}
	public void setChildren(TreeNode tn) { 
		if(tn != null)
			children.add(tn);  
	}  
    public String getName() {  
	    return nodeName;  
	}  
    public void setName(String name) {  
	    nodeName = name;  
    }  
    public ArrayList<Document> getDatas() {  
	    return data;  
    }  
    public void setData(ArrayList<Document> d) {  
        data = d;  
    }
}  

