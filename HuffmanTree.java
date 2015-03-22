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
	//creates a parent node that links the least frequently used nodes with a frequency of its childrens' combined 
	//does this until only one parent node is left; the root node is set to this last parent node
	public void makeTree(){
		
		//we need a clone as a normal equals just sets the address
		//the clone will have values removed in order to build the tree
		//which is why a clone is needed
		buildTreeList = (ArrayList<HuffmanNode>) nodeList.clone();
		if(buildTreeList.size()<1){ //edge case of an empty input file returns an empty tree
			return;
		}
		else if(buildTreeList.size()==1){ // if only one char in the input file provides a tree with only a left branch
			root.left=buildTreeList.get(0);
			return;
		}
		
		while(buildTreeList.size()>1){
			
			//creates a blank parent node, which will point to the lowest frequency characters
			HuffmanNode parentNode = new HuffmanNode(null,0);
			
			parentNode.right = popLowestNode(); //sets lowest freq character nodes as children
			parentNode.left = popLowestNode();
			
			//solving for the new, combined frequency of the parent node for future calculations
			parentNode.frequency = parentNode.left.frequency + parentNode.right.frequency; 
			buildTreeList.add(parentNode);
		}
		
		//sets root node as the parent node encompassing all of the character nodes
		root = buildTreeList.get(0); 
	}
	
	
	//helper method of makeTree
	//returns the lowest frequency node and removes it from the buildTreeList
	// so that it doesn't get returned if the method gets called again
	private HuffmanNode popLowestNode(){
		
		//sets the lowest frequency and node as the first in the list 
		//to give the program something to compare future frequencies to
		int lowestFrequency=buildTreeList.get(0).frequency;
		HuffmanNode returnNode=buildTreeList.get(0);
		
		for(int i=1;i<buildTreeList.size();i++){
			
			//checks to see if a node has a lower frequency than the previous nodes
			//if it does, it sets that node as the lowest frequency
			if(buildTreeList.get(i).frequency<lowestFrequency){
				returnNode = buildTreeList.get(i);
				lowestFrequency = returnNode.frequency;
			}
		}
		
		buildTreeList.remove(returnNode); //removes the lowest freq node for future calls
		return returnNode;
	}
	
	
	//sets the Huffman encodings for each character (node)
	public void mapEncodings(){
		setNodeEncodings(root, new StringBuilder(""));
	}
	
	
	//recursive method which descends the tree, recording the path it took with the StringBuilder
	//Sets the left node's encoding to whatever path it took from the root
	//0 is left, 1 is right; helper for mapEncodings
	private void setNodeEncodings(HuffmanNode node, StringBuilder code){
		
		//if it can descend left, "code" records a 0 and desends
		if(node.left!=null){
			code.append("0");
			setNodeEncodings(node.left, code);
		}
		
		//if it can descend right, it does, recording a 1 in "code"
		if(node.right!=null){
			code.append("1");
			setNodeEncodings(node.right, code);
		}
		
		//if the node can no longer descend or is done descending, it checks if the node 
		//holds a character. If it does, the encoding is set to "code," or the path taken
		if(node.inChar!=null){
			node.encoding=code.toString();
		}
		
		//prevents an error of setting the length of "code" less than possible
		if(code.length()>0)
			code.setLength(code.length()-1);
		
	}
	
	
	//writes the actual output file with the encodings found previously
	//reads in the input file once more, going through and encoding the input line-by-line
	//and writing the output to the proper file
	public void encodeFile(File inputFile, File outputFile){
		
		try {
			
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputFile));
			Scanner fileReader = new Scanner(inputFile);
			String line="";
			
			//while the file still has a line, runs the encodeSring method on the lower case
			//version of the input
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
	
	
	//helper method of the encodeFile method
	//accepts a string and goes through the string char by char
	//finding the proper node for each char and adding the encoded version from the node to a StringBuilder
	private String encodeString(String line){
		
		StringBuilder encodedLine = new StringBuilder("");
		
		//for loop loops through each char of the line, finding each encoding
		for(int chIndex=0; chIndex<line.length(); chIndex++){
			
			//finds the proper node with an equal inChar to the line's character
			//then takes its encoding string and adds it to the encodedLine
			encodedLine.append(findNode(line.charAt(chIndex)).encoding);
		}
		
		return encodedLine.toString();
	
	}
	
	
	//calculates the number of bits that are saved by encoding with 
	//the Huffman tree over fixed 8-bit characters
	public int bitsSaved(){
		int huffBits=0, normBits=0;
		
		for(int index=0;index<nodeList.size();index++){
			
			//for each character, it takes the frequency and length of encoding
			//then solves for how many bits were taken up by the character and adds
			//the bit count to a running total
			huffBits+=(nodeList.get(index).encoding.length() * nodeList.get(index).frequency);
			
			//same as the huffBits calculation, but assumes encoding length of 8 bits
			normBits+=(8 * nodeList.get(index).frequency);
		}
		
		return normBits-huffBits; 
	}
	

	//prints the encoding table to an output file specified by the huffmanCoder method
	//also prints the bit savings
	public void outputList(File outputFile){
		
		try {
			
			BufferedWriter fileOut = new BufferedWriter(new FileWriter(outputFile));
			for(HuffmanNode node: nodeList){ //going through node by node
				fileOut.write(node+"\n"); //writes the node info into the table
			}
			fileOut.write("Bits Saved:\t"+bitsSaved());
			fileOut.close();
			
		}catch(IOException ex){
			System.out.println("Cannot output encoding table file");
			ex.printStackTrace();
		}
	}
	

	//used node class containing a character, frequency, and encoding
	//also used as internal node, where the character is null, which is why it has left and right
	public class HuffmanNode{
		
		Character inChar;
		int frequency;
		HuffmanNode left, right;
		String encoding;
		
		
		//constructor
		public HuffmanNode(Character inChar, int frequency){
			this.inChar = inChar;
			this.frequency = frequency;
			left = right = null;
			encoding = "";
		}
		
		//output of node when producing the table
		public String toString(){
			return "Character:\t"+inChar+"\tFrequency:\t"+frequency+"\tEncoding:\t"+encoding;
		}
		
	}
	
}