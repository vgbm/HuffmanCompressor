package HuffmanEncoder;

import java.io.File;

public class HuffmanCompressor {

	public static void main(String[] args) {
		//huffmanCoder(args[0],args[1]);		
		huffmanCoder("test.txt","outFile.txt");
	}
	
	public static String huffmanCoder(String inputFileName, String outputFileName){
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		File encodingTableFile = new File("encodingTable.txt");
		HuffmanTree tree = new HuffmanTree();
		
		tree.buildNodeList(inputFile);
		tree.makeTree();		
		tree.mapEncodings();
		tree.encodeFile(inputFile, outputFile);
		
		tree.outputList(encodingTableFile);
		System.out.println("Bits saved:\t"+tree.bitsSaved());
		return "Success";
	}
}