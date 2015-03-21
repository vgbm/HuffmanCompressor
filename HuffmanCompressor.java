package HuffmanEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanCompressor {

	public static void main(String[] args) {
		
		huffmanCoder("test.txt","outFile.txt");
		//huffmanCoder(args[0],args[1]);		
	}
	
	public static String huffmanCoder(String inputFileName, String outputFileName){
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		HuffmanTree tree = new HuffmanTree();
		
		tree.buildNodeList(inputFile);
		//tree.printList();
		tree.makeTree();		
		tree.mapEncodings();
		tree.encodeFile(inputFile, outputFile);
		tree.printList();

		return "Success";
	}
}
