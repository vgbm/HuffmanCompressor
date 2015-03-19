package HuffmanEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanTree {
	
	ArrayList<HuffmanNode> nodeList;
	HuffmanNode root;
	
	public HuffmanTree(){
		nodeList = new ArrayList<HuffmanNode>();
		nodeList.add(new HuffmanNode(' ',0)); // initialize the node list with a character so that its size is >0
		root = new HuffmanNode(null,-1);
	}
	
	
	
	public void buildNodeList(File inputFile){
		
		try {
			
			boolean found;
			String line="";
			Scanner fileReader = new Scanner(inputFile);
			
			while(fileReader.hasNext()){
			
				line = fileReader.next().toLowerCase();
				
				for(int i = 0; i < line.length(); i++){
					
					found=false;
					
					for(int j = 0; j < nodeList.size(); j++){
						
						if(nodeList.get(j).inChar.equals((Character)line.charAt(i))){
							nodeList.get(j).frequency++;
							found=true;
							break;
						}	
					}
			
					if(!found){
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
	
	public void makeTree(){
		
	}
	
	public void printList(){
		for(HuffmanNode node: nodeList){
			System.out.println(node);
		}
	}
	
	public class HuffmanNode{
		
		Character inChar;
		int frequency;
		HuffmanNode left, right;
		
		public HuffmanNode(Character inChar, int frequency){
			this.inChar = inChar;
			this.frequency = frequency;
			left = right = null;
		}
		
		public String toString(){
			return "char:\t"+inChar+"\tFrequency:\t"+frequency;
		}
		
	}
	
}
