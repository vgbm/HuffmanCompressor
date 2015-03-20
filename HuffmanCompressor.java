package HuffmanEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanCompressor {

	public static void main(String[] args) {
		
		huffmanCoder("test.txt","fill in later");
		
		/*File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		
		 */
		
	}
	
	public static String huffmanCoder(String inputFileName, String outputFileName){
		
		File inputFile = new File(inputFileName);
		HuffmanTree tree = new HuffmanTree();
		
		tree.buildNodeList(inputFile);
		//tree.printList();
		
		tree.makeTree();
		tree.printTree(tree.root,0);
		
		//tree.printList();
		return "";
	}
}
