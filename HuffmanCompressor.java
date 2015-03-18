package HuffmanEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HuffmanCompressor {

	public static void main(String[] args) {
		
		File inputFile = new File(args[0]);
		File outputFile = new File(args[1]);
		
		ArrayList<String> textLines = new ArrayList<String>();
		int[] frequencies = new int[26];
		
		try {
		
			Scanner fileReader = new Scanner(inputFile);
			
			while(fileReader.hasNext()){
				textLines.add(fileReader.nextLine());
			}
			fileReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.print("File could not be found");
			e.printStackTrace();
		}
		
		//printList(textLines);
		frequencies = countFrequencies(textLines);
		
		/*for(int num:frequencies){
			System.out.println(num);
		}*/
		
	}
	
	public static int[] countFrequencies(ArrayList<String> list){ //works I think
		int[] frequencies = new int[26];
				
		for(String line:list){
			line.toLowerCase();
			
			for(int i = 0; i < line.length(); i++){
				if(Character.isAlphabetic(line.charAt(i)))
					frequencies[line.charAt(i)-97]++;
			}
		}
		
		return frequencies;
	}
	
	public static void printList(ArrayList<String> list){
		for(String line:list){
			System.out.println(line);
		}
	}
}
