import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Fortes1 {
	List<String> tokens = new ArrayList<String>();
	Scanner sc = new Scanner(System.in);
	String currentWord;
	String currentLine;
	
	// default constructor
	Fortes1() {
		// do nothing
	}
	
	// asking for user's input
	public void start() {
		System.out.println("This program determines whether the given lines of code in given file is valid or not.\n\n");
		
		// Asking user the file names
		
		System.out.println("Enter what file name to read: ");
		String fileName_input = sc.nextLine();
		System.out.println("Enter the file name where to write the output: ");
		String fileName_output = sc.nextLine();
		System.out.println("The file to read: " + fileName_input);
		System.out.println("Where to write the output: " + fileName_output);
		System.out.println();
		sc.close();
		// end of asking -> next step: read the file given
		readFile(fileName_input);
	}
	
	
	// reads the file given
	/*
	 * if the current line ends with a semicolon, it will be either a variable declaration or a 
	 * function declaration - given that the line also contains an open and close parenthesis.
	 * 
	 * if it ends with an open curly brace, it is a function definition AND everything in it belongs to it (??!!)
	 * 
	 * else, it is invalid. -> check what kind
	 */
	public void readFile(String fileName_input) {
		File read_file = new File(fileName_input);
		
		if(!read_file.exists()) {
			System.out.println("File does not exists!");
		}
		
		try {
			Scanner in = new Scanner(read_file);
			while(in.hasNextLine()) {
				currentLine = in.nextLine();
				
				if(currentLine.endsWith(";")) {
					if(currentLine.contains("(") && currentLine.contains(")")) {
						System.out.println(currentLine + " is a function declaration.");
						// make a function for checking function declaration
						//isValidFuncDecla(currentLine);
					} else if(currentLine.contains("(") || currentLine.contains(")")) { // meaning, 
						System.out.println(currentLine + " is already an invalid function declaration");
					} else {
						System.out.println(currentLine + " is a variable declaration.");
						// make a function for checking variable declaration
						isValidVarDecla(currentLine);
					}
				} else if(currentLine.endsWith("{")) {
					System.out.println(currentLine + "is a function definition.");
				} else {
					System.out.println(currentLine + " is either invalid or part of the function.");
				}
			}
			in.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("\n\nEnd of file.");
		
		// end of file reading
	}
	
	// for function declaration
	public void isValidFuncDecla(String currentLine) {
		int flag = 0;
		int i = 0;
		StringTokenizer tokenizer = new StringTokenizer(currentLine, " ();");
		System.out.println("\n-------Function declaration-------");
		// storing all tokens in an array list
		while(tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
			System.out.println(tokens.get(i));
			i++;
		}
		// end of storing
		/*
		 * for function declaration statements that don't contain commas, maximum tokens to check is four. And data types, 
		 * primitive data types, should always be in position i divisible by 2 (i = 0 & i = 2) ->
		 */
		if(!currentLine.contains(",")) {
			for(i = 0; i < tokens.size(); i++) {
				if(i % 2 == 0) {
					if(!(tokens.get(i).equals("int") || tokens.get(i).equals("char") || tokens.get(i).equals("float") || 
							tokens.get(i).equals("double") || tokens.get(i).equals("void"))) {
						flag = 1;
						break;
					}
				} else {
					if((tokens.get(i).equals("int") || tokens.get(i).equals("char") || tokens.get(i).equals("float") || 
							tokens.get(i).equals("double") || tokens.get(i).equals("void"))) {
						flag = 1;
						break;
					}
				}
			}
			// temporary
			if(flag == 0) {
				System.out.println(currentLine + " is a valid function declaration");
			} else {
				System.out.println(currentLine + " is an invalid function declaration");
			}
		} else {
			System.out.println(currentLine + " contains comma in between.");
		}
		tokens.clear();
		System.out.println("\n-------Function declaration-------");
	}
	
	// for variable declaration
	/*
	 * http://www.c4learn.com/c-programming/c-variable-nameing-rules/
	 * https://www.programiz.com/c-programming/c-variables-constants
	 * https://www.doc.ic.ac.uk/lab/cplus/c++.rules/chap11.html i++
	 * */
	public void isValidVarDecla(String currentLine) {
		List<String> variables = new ArrayList<>();
		String[] token = currentLine.split(" ;");
		int i = 0;
		int j = 0;
		int varName = 0; // to check that variable name was only checked once
		System.out.println("--------------Variable Declaration--------------");
		if((token[i].equals("int") || token[i].equals("char") || token[i].equals("float") || 
				token[i].equals("double"))) {
			
			if(!currentLine.contains(",")) {
				
				for(i = 1; i < token.length; i++) {
					
					if(token[i].charAt(j) == '_' || (token[i].charAt(j) >= 'a' && token[i].charAt(j) >= 'z') || 
							(token[i].charAt(j) >= 'A' && token[i].charAt(j) >= 'Z')) {
						
						for(j = 1; j < token[i].length(); j++) {
							
							if(!(token[i].charAt(j) == '_' || (token[i].charAt(j) >= 'a' && token[i].charAt(j) >= 'z') || 
							(token[i].charAt(j) >= 'A' && token[i].charAt(j) >= 'Z') || 
							(token[i].charAt(j) >= '0' && token[i].charAt(j) >= '9'))) {
								System.out.println(currentLine + " is an invalid variable declaration."
										+ "Invalid variable name.");
								break;
							}
						} 
						// no need to store the variable in the array list since we are sure 
						// that there will only be on variable
						varName = 1;
					} else if(!(token[i].charAt(j) == '=') && varName == 1) {
						// if the next token is not a '=' character, and variable name has already been checked, invalid
						System.out.println(currentLine + " is an invalid variable declaration."
								+ "Missing '=' character.");
						break;
					}
				}
			}
		} else {
			System.out.println("currentLine" + " is an invalid variable declaration.");
		}
		
		System.out.println("--------------Variable Declaration--------------");
	}
	
	public static void main(String[] args) {
		Fortes1 program = new Fortes1();
		program.start();
	}

}
