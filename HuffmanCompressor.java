import java.io.File;

public class HuffmanCompressor {

	public static void main(String[] args) {
		huffmanCoder(args[0],args[1]); //takes the command line arguments of inputFile outputFile
	}
	
	//takes the input from the command line and performs the necessary operations on the huff tree
	//to produce and encoded file and an encoding table
	public static String huffmanCoder(String inputFileName, String outputFileName){
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		File encodingTableFile = new File("encodingTable.txt");
		HuffmanTree tree = new HuffmanTree();
		
		//performs operation in necessary order to produce the proper and expected outputs
		tree.buildNodeList(inputFile);
		tree.makeTree();		
		tree.mapEncodings();
		tree.encodeFile(inputFile, outputFile);
		
		tree.outputList(encodingTableFile);
		return "Success";//returns "success" to indicate nothing went wrong
	}
}
