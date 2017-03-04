import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Fortes_MPA1 {
	
	Fortes_MPA1() {
		// do nothing
	}
	
	// asks for user's input
	public void start() {
		Scanner sc = new Scanner(System.in);
		System.out.println("This program determines whether the given testcases are"
				+ " valid syntax in C Programming.");
		System.out.println("\nEnter the name of the file to be read:");
		String file_in = sc.nextLine();
		System.out.println("Enter the output filename:");
		String file_out = sc.nextLine();
		System.out.println("\nInput file: " + file_in);
		System.out.println("Output file: " + file_out);
		System.out.println();
		sc.close();
		read(file_in, file_out);
	}
	
	// read the file_in
	public void read(String file_in, String file_out) {
		List<String> outputs = new ArrayList<>();
		int flag = 0;
		File file = new File(file_in);
		
		if(!file.exists()) {
			System.out.println("File does not exists.");
		}
		
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String currentLine = sc.nextLine();
				//System.out.println("currentLine " + currentLine);
				if(currentLine.endsWith(";")) {
					if(currentLine.contains("(") && currentLine.contains(")")) {
						// function declaration
						if(funcDecla(currentLine, extract(currentLine))) {
							outputs.add("Valid Function Declaration");
						} else {
							outputs.add("Invalid Function Declaration");
						}
					} else if(currentLine.contains("(") || currentLine.contains(")")) {
						outputs.add("Invalid function declaration. Unpaired parenthesis.");
					} else {
						// variable declaration
						if(varDecla(currentLine, extract(currentLine))) {
							outputs.add("Valid Variable Declaration");
						} else {
							outputs.add("Invalid Variable Declaration");
						}
					}
				} else if(currentLine.endsWith("{") || currentLine.endsWith("}")) {
					while(funcDefin(currentLine, extract(currentLine)) && sc.hasNextLine()) {
						currentLine = sc.nextLine();
						if((currentLine.equals("}"))) {
							flag = 1;
							break;
						}
					}
					if(flag == 0) {
						outputs.add("Invalid Function Definition");
					} else {
						outputs.add("Valid Function Definition");
					}
				} else {
					if(currentLine.contains("(") && currentLine.contains(")")) {
						outputs.add("Invalid Function Declaration");
					} else {
						outputs.add("Invalid Variable Declaration");
					}
				}
			}
			sc.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		write(outputs, file_out);
		System.out.println("Output is written in: " + file_out);
		System.out.println("\nEnd.");
	}
	
	public void write(List<String> outputs, String file_out) {
		try {
			FileWriter fw = new FileWriter(file_out);
			
			for(int i = 0; i < outputs.size(); i++) {
				fw.write(outputs.get(i));
				fw.write("\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> extract(String currentLine) {
		ArrayList<String> tokens = new ArrayList<>();
		StringTokenizer tokenizer = new StringTokenizer(currentLine, " {();");
		
		while(tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
		}
		return tokens;
	}
	
	public void printArrayList(ArrayList<String> tokens) {
		for(int i = 0; i < tokens.size(); i++) {
			System.out.println(tokens.get(i));
		}
		tokens.clear();
	}
	
	public boolean isAValidDataType(String dataType) {
		if(!(dataType.equals("int") || dataType.equals("char") || dataType.equals("float") || 
				dataType.equals("double"))) {
			return false;
		}
		return true;
	}
	
	/*
	 * first checks if the first character in the variable name is valid, and then checks the rest of the
	 * character in the variable name.
	 * variable names are considered valid if and only if each character is any of the characters a-z, A-Z,
	 * 0-9 or an underscore.
	 */
	public boolean isAValidVar(String varName) {
		if(isAValidFirstChar(varName)) {
			for(int i = 1; i < varName.length(); i++) {
				if(!((varName.charAt(i) >= 'a' && varName.charAt(i) <= 'z') || (varName.charAt(i) >= 'A' && varName.charAt(i) <= 'Z') || 
						(varName.charAt(i) >= '0' && varName.charAt(i) <= '9')
						|| (varName.charAt(i) == '_'))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	/*
	 * checks the first character in every variable name provided.
	 * a valid first character should be any of the characters a-z, A-Z, or an underscore.
	 */
	public boolean isAValidFirstChar(String varName) {
		char firstChar = varName.charAt(0);
		if(!((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z') || 
				(firstChar == '_'))){
			return false;
		}
		return true;
	}
	
	public boolean isAValidValue(String dataType, String value) {
		if(dataType.equals("int")) {
			int val = Integer.parseInt(value);
			if(!(val >= Integer.MIN_VALUE && val <= Integer.MAX_VALUE)) {
				return false;
			}
		} else if(dataType.equals("float")) {
			float val = Float.parseFloat(value);
			if(!(val >= Float.MIN_VALUE && val <= Float.MAX_VALUE)) {
				return false;
			}
		} else if(dataType.equals("double")) {
			double val = Double.parseDouble(value);
			if(!(val >= Double.MIN_VALUE && val <= Double.MAX_VALUE)) {
				return false;
			}
		} else if(dataType.equals("char")) {
			int ch = Integer.parseInt(value);
			if(!((ch >= -128 && ch <= 127) || (ch >= 0 && ch <= 255))) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * checks if the declared variable is unique
	 */
	public boolean isAUniqVar(String varName, List<String> variables) {
		for(int i = 0; i < variables.size(); i++) {
			if(variables.get(i).equals(varName)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean varDecla(String currentLine, ArrayList<String> tokens) {
		//printArrayList(tokens);
		List<String> variables = new ArrayList<String>();
		int dataTypeCheck = 0;
		int varNameCheck = 0;
		int assignmentCheck = 0;
		int valueCheck = 0;
		int overAllCheck = 1;
		if(!currentLine.contains(",")) {
			for(int i = 0; i < tokens.size(); i++) {
				if(dataTypeCheck == 0) {
					if(isAValidDataType(tokens.get(i)) || tokens.get(i).equals("return")) {
						dataTypeCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid variable declaration."
						//		+ " Invalid/Missing Data Type.");
						return false;
					}
				} else if(varNameCheck == 0) {
					if(isAValidVar(tokens.get(i))) {
						varNameCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid variable declaration."
							//	+ " Invalid/Missing Variable Name.");
						return false;
					}
				} else if(assignmentCheck == 0) {
					if(tokens.get(i).equals("=")) {
						assignmentCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid variable declaration."
							//	+ " Missing '=' character.");
						return false;
					}
				} else if(valueCheck == 0) {
					if(isAValidValue(tokens.get(0), tokens.get(i))) {
						valueCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid variable declaration."
							//	+ " Incompatible value for data type.");
						return false;
					}
				} else {
					overAllCheck = 0;
					//System.out.println(currentLine + " has excess value delimited by a space. INVALID.");
					return false;
				}
			}
		} else {
//			System.out.println(currentLine + " contains comma/s.");
			if(!currentLine.contains("=")) {
				for(int i = 0; i <tokens.size(); i++) {
					if(dataTypeCheck == 0) {
						if(isAValidDataType(tokens.get(i))) {
							dataTypeCheck = 1;
						} else {
							overAllCheck = 0;
						//	System.out.println(currentLine + " is an invalid variable declaration."
							//		+ " Invalid/Missing function name.");
							return false;
						}
					} else if(varNameCheck == 0) {
						if(tokens.get(i).endsWith(",")) {
							int varLen = tokens.get(i).length();
							String varName = tokens.get(i).substring(0, varLen-1);
							if(isAValidVar(varName) && isAUniqVar(varName, variables)) {
								variables.add(varName);
							} else {
								overAllCheck = 0;
								//System.out.println(currentLine + " is an invalid variable declaration."
									//	+ " Invalid/Variable name is already declared.");
								return false;
							}
						} else {
							if(isAValidVar(tokens.get(i)) && isAUniqVar(tokens.get(i), variables)) {
								varNameCheck = 1;
							} else {
								overAllCheck = 0;
								//System.out.println(currentLine + " is an invalid variable declaration."
								//		+ " Invalid/Variable name is already declared.");
								return false;
							}
						}
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " has excess value delimited by a space. INVALID.");
						return false;
					}
				}
			} else {
				return true;
			}
		}
		variables.clear();
		return true;
	}

	public boolean funcDecla(String currentLine, ArrayList<String> tokens) {
		int retTypeCheck = 0;
		int funcNameCheck = 0;
		int dataParCheck = 0;
		int parVarCheck = 0;
		int overAllCheck = 1;
		if(!currentLine.contains(",")) {
			for(int i = 0; i < tokens.size(); i++) {
				if(retTypeCheck == 0) {
					if(isAValidDataType(tokens.get(i)) || tokens.get(i).equals("void")) {
						retTypeCheck = 1;
					} else {
						overAllCheck = 0;
					//	System.out.println(currentLine + " is an invalid function declaration."
						//		+ " Invalid/Missing Return Type.");
						return false;
					}
				} else if(funcNameCheck == 0) {
					if(isAValidVar(tokens.get(i))) {
						funcNameCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid function declaration."
							//	+ " Invalid/Missing Function Name.");
						return false;
					}
				} else if(dataParCheck == 0) {
					if(isAValidDataType(tokens.get(i))) {
						dataParCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid function declaration."
							//	+ " Invalid/Missing Data Parameter.");
						return false;
					}
				} else if(parVarCheck == 0) {
					if(isAValidVar(tokens.get(i))) {
						parVarCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid function declaration."
							//	+ " Invalid/Missing Parameter Variable.");
						return false;
					}
				} else {
					overAllCheck = 0;
					//System.out.println(currentLine + " has excess value delimited by space. INVALID.");
					return false;
				}
			}
		} else {
//			System.out.println(currentLine + " contains comma/s.");
			for(int i = 0; i < tokens.size(); i++) {
				if(retTypeCheck == 0) {
					if(isAValidDataType(tokens.get(i)) || tokens.get(i).equals("void")) {
						retTypeCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid function declaration."
							//	+ " Invalid Return Type.");
						return false;
					}
				} else if(funcNameCheck == 0) {
					if(isAValidVar(tokens.get(i))) {
						funcNameCheck = 1;
					} else {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid function declaration."
							//	+ " Invalid Function Name.");
						return false;
					}
				} else if((tokens.get(i).endsWith(",") && dataParCheck == 0) || (dataParCheck == 0 && tokens.get(i+1).endsWith(","))) {
					if(!isAValidDataType(tokens.get(i))) {
						overAllCheck = 0;
						//System.out.println(currentLine + " is an invalid function declaration."
							//	+ " Invalid DataType Parameter.");
						return false;
					}
				} else if(dataParCheck == 0) {
					if(isAValidDataType(tokens.get(i))) {
						dataParCheck = 1;
					}
					// --
				}
			}
		}
		return true;
	}
	
	public boolean funcDefin(String currentLine, ArrayList<String> tokens) {
		int funcDeclaCheck = 0;
		int varDeclaCheck = 0;
		int overAllCheck = 1;
		
		if(currentLine.endsWith("{")) {
			String line = currentLine.substring(0, currentLine.length()-1);
			if(funcDeclaCheck == 0) {
				if(funcDecla(line, tokens)) {
					funcDeclaCheck = 1;
					return true;
				}
			}
		} else if(currentLine.endsWith(";")) {
			if(varDeclaCheck == 0) {
				if(varDecla(currentLine, tokens)) {
					varDeclaCheck = 1;
					return true;
				}
			}
		} else if(currentLine.equals("}")) {
			overAllCheck = 0;
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Fortes_MPA1 run = new Fortes_MPA1();
		run.start();
	}
}
