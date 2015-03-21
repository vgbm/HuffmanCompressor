package HuffmanEncoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanTree {
	
	//nodeList contains all of the character HuffmanNodes in an ArrayList
	//buildTreeList is a temp list for making the actual tree
	
	//ArrayList was used as I feel like it is easier to manipulate
	//Also, it makes inserting new nodes more efficient and uses less memory
	//which I find important for this project
	ArrayList<HuffmanNode> nodeList, buildTreeList;
	
	//root node for the Huffman tree
	HuffmanNode root;
	
	//constructs a new Huffman tree object
	public HuffmanTree(){
		nodeList = new ArrayList<HuffmanNode>();
		buildTreeList = new ArrayList<HuffmanNode>();
		
		root = new HuffmanNode(null,-1);
	}
	
	
	//Fills the nodeList with huffman nodes, one per each new character
	//Scans the file character by character and either increments frequency or creates a new node
	//depending on if a node for the character already exists
	public void buildNodeList(File inputFile){
		
		try {
			
			String line="";
			Scanner fileReader = new Scanner(inputFile);
			HuffmanNode charNode = null;
			
			//reads the file while it has a next line
			while(fileReader.hasNext()){
			
				//makes each alphabetical char lower case
				//and reads in the next file line
				line = fileReader.nextLine().toLowerCase();

				//checks each character in the line to see if
				//the node list already contains the character
				//if it does, increment frequency
				//if not, add a new node with that character
				for(int i = 0; i < line.length(); i++){

					charNode = findNode(line.charAt(i));
					if(charNode!=null)
						charNode.frequency++;
					else
						nodeList.add(new HuffmanNode(line.charAt(i),1));
						
				}	
			}
			
			fileReader.close();
			
		} catch (FileNotFoundException e) {		
			System.out.print("Input file could not be found");
			e.printStackTrace();
		}
	}

	
	//helper method for many methods
	//returns the node within node list containing a given character
	private HuffmanNode findNode(Character ch){
		
		for(int index = 0; index < nodeList.size(); index++){
			
			//checks for equality between the given character and the one contained by the Huffman Node
			if(nodeList.get(index).inChar.equals(ch)){
				//if they are equal, return the Huffman Node
				return nodeList.get(index);
			}
		}
		return null; //returns null if the node cannot be found
		
	}
	
	
	//takes the nodeList and uses it to form a Huffman tree
	//Combines
	public void makeTree(){
		
		buildTreeList = (ArrayList<HuffmanNode>) nodeList.clone();
		if(buildTreeList.size()<1){
			return;
		}
		else if(buildTreeList.size()==1){
			root.left=buildTreeList.get(0);
			return;
		}
		
		while(buildTreeList.size()>1){
			HuffmanNode parentNode = new HuffmanNode(null,0);
			parentNode.right = popLowestNode();
			parentNode.left = popLowestNode();
			parentNode.frequency = parentNode.left.frequency + parentNode.right.frequency;
			buildTreeList.add(parentNode);
		}
		
		root = buildTreeList.get(0);
	}
	
	
	private HuffmanNode popLowestNode(){
		
		int lowestFrequency=buildTreeList.get(0).frequency;
		HuffmanNode returnNode=buildTreeList.get(0);
		
		for(int i=1;i<buildTreeList.size();i++){
			if(buildTreeList.get(i).frequency<lowestFrequency){
				returnNode = buildTreeList.get(i);
				lowestFrequency = returnNode.frequency;
			}
		}
		
		buildTreeList.remove(returnNode);
		return returnNode;
	}
	
	
	public void mapEncodings(){
		setNodeEncodings(root, new StringBuilder(""));
	}
	
	
	private void setNodeEncodings(HuffmanNode node, StringBuilder code){
		
		if(node.left!=null){
			code.append("0");
			setNodeEncodings(node.left, code);
		}
		
		if(node.right!=null){
			code.append("1");
			setNodeEncodings(node.right, code);
		}
		
		if(node.inChar!=null){
			node.encoding=code.toString();
		}
		
		if(code.length()>0)
			code.setLength(code.length()-1);
		
	}
	
	
	public void encodeFile(File inputFile, File outputFile){
		
		try {
			
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputFile));
			Scanner fileReader = new Scanner(inputFile);
			String line="";
			
			while(fileReader.hasNext()){
				line = fileReader.nextLine().toLowerCase();
				fileOut.write(encodeString(line)+"\n");
			}
			
			fileOut.close();
			fileReader.close();
			
		} catch (IOException e) {
			System.out.println("Cannot input/output file");
			e.printStackTrace();
		}
		
	}
	
	
	private String encodeString(String line){
		
		StringBuilder encodedLine = new StringBuilder("");
		
		for(int chIndex=0; chIndex<line.length(); chIndex++){
			encodedLine.append(findNode((Character)line.charAt(chIndex)).encoding);
		}
		
		return encodedLine.toString();
	
	}
	
	
	public int bitsSaved(){
		int huffBits=0, normBits=0;
		
		for(int index=0;index<nodeList.size();index++){
			huffBits+=(nodeList.get(index).encoding.length() * nodeList.get(index).frequency);
			normBits+=(8 * nodeList.get(index).frequency);
		}
		
		return normBits-huffBits; 
	}
	

	public void outputList(File outputFile){
		
		try {
			
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputFile));
			for(HuffmanNode node: nodeList){
				fileOut.write(node+"\n");
			}
			fileOut.close();
			
		}catch(IOException ex){
			System.out.println("Cannot output encoding table file");
			ex.printStackTrace();
		}
	}
	
	
//	public void printList(){
//		for(HuffmanNode node: nodeList){
//			System.out.println(node);
//		}
//	}

	
	public class HuffmanNode{
		
		Character inChar;
		int frequency;
		HuffmanNode left, right;
		String encoding;
		
		public HuffmanNode(Character inChar, int frequency){
			this.inChar = inChar;
			this.frequency = frequency;
			left = right = null;
			encoding = "";
		}
		
		public String toString(){
			return "Character:\t"+inChar+"\tFrequency:\t"+frequency+"\tEncoding:\t"+encoding;
		}
		
	}
	
}