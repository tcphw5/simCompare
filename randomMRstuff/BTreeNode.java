package WordCountHadoop;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.ObjectOutputStream;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner; 


public class BTreeNode {

	final static int NUM_KIDS = 6;
	final static int NUM_DATA = 6;
	final static int NUM_LEV = 5;
	public BTreeNode(){
		 child_ptr = new ArrayList<BTreeNode>();
		 data = new ArrayList<String>();
		 parent = null;
	}
	public BTreeNode(BTreeNode bTreeNode) {

		 child_ptr = new ArrayList<BTreeNode>(bTreeNode.child_ptr);
		 data = new ArrayList<String>(bTreeNode.data);
		 parent = bTreeNode.parent;
	}
	ArrayList<String> data;
	ArrayList<BTreeNode> child_ptr;
	int n;
	int kids;
	BTreeNode parent;
	public void insert(String a){
		data.add(a);
		kids = kids+1;
	}
	 public void insert(BTreeNode node){
		node.parent = this;
		child_ptr.add(node);
		n=n+1;
	}
	static public void fillnode(Scanner in,BTreeNode node)throws Exception{
		int n;
		n = in.nextInt();
		for(int i=0; i<n; i++){
			node.insert(in.next());
		}
	}
	static public void buildtree(Scanner in, BTreeNode node, Map<String,BTreeNode> data)throws Exception{
		node.kids = in.nextInt();
		for(int i=0; i<node.kids; i++){
			BTreeNode temp = new BTreeNode();
			temp.parent = node;
			fillnode(in,temp);
			buildtree(in,temp,data);
			node.child_ptr.add(temp);
		}
		//if no kids know we are at root level
		if(node.kids==0){
			int runs;
			runs = in.nextInt();
			for(int i=0; i<runs; i++){
				String element;
				element = in.next();
				data.put(element, node);
			}
		}
	}
	static public void print(BTreeNode node){
		for(int i=0; i<node.data.size();i++)
			System.out.print(node.data.get(i) + "\t");
		System.out.print("\n");
		for(int i=0; i<node.child_ptr.size();i++)
			print(node.child_ptr.get(i));
	}
}