package HuffmanEncoder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanTree {
	
	ArrayList<HuffmanNode> nodeList, buildTreeList;
	HuffmanNode root;
	
	public HuffmanTree(){
		nodeList = new ArrayList<HuffmanNode>();
		buildTreeList = new ArrayList<HuffmanNode>();
		
		nodeList.add(new HuffmanNode(' ',0)); // initialize the node list with a character so that its size is >0
		root = new HuffmanNode(null,-1);
	}
	
	
	public void buildNodeList(File inputFile){
		
		try {
			
			String line="";
			Scanner fileReader = new Scanner(inputFile);
			HuffmanNode charNode = null;
			
			while(fileReader.hasNext()){
			
				line = fileReader.nextLine().toLowerCase();

				for(int i = 0; i < line.length(); i++){
					
					for(int j = 0; j < nodeList.size(); j++){

						charNode = findNode(line.charAt(i));
						if(charNode!=null){
							charNode.frequency++;
							break;
						}	
					}

					if(charNode==null){
						nodeList.add(new HuffmanNode(line.charAt(i),1));
					}	
				}	
			}
			
			fileReader.close();
			
		} catch (FileNotFoundException e) {		
			System.out.print("Input file could not be found");
			e.printStackTrace();
		}
	}

	
	private HuffmanNode findNode(Character ch){
		
		for(int index = 0; index < nodeList.size(); index++){
			if(nodeList.get(index).inChar.equals(ch)){
				return nodeList.get(index);
			}
		}
		return null;
		
	}
	
	
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
			System.out.println(code);
			node.encoding=code.toString();
		}
		//System.out.println(code);
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
	
	
	public void printList(){
		for(HuffmanNode node: nodeList){
			System.out.println(node);
		}
	}

	
	public void printTree(HuffmanNode node,int depth){

		if(node.left!=null){
//			System.out.print("\tl ");
			printTree(node.left, depth+1);
		}
		if(node.right!=null){
//			System.out.print("\tr ");
			printTree(node.right, depth+1);
		}
		
		if(node.inChar!=null)
			System.out.println(node+"\t"+depth);
	
//		System.out.print("U ");
	}
	
	
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
			return "char:\t"+inChar+"\tFrequency:\t"+frequency+"\tEncoding:\t"+encoding;
		}
		
	}
	
}