import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
 * on power set using bit set approach: 
 * http://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
 */

public class FortesMP2<T> {
	private int size;
	private Node<T> head;
	private Node<T> tail;
	
	// default constructor
	public FortesMP2() {
		size = 0;
		head = null;
		tail = null;
	}
	
	// DONE
	// insert items, no duplicates
	public boolean insert(T item) {
		Node<T> temp = new Node<T>(item);
		
		if(size == 0) {
			head = tail = temp;
		} else {
			if(!contains(item)) { // checks for duplicate
				Node<T> current = head;
				for(int i = 0; i < size - 1; i++) {
					current = current.getNext();
				}
				current.setNext(temp);
				tail = temp;
			} else {
				return false; // cannot insert because item already exists
			}
		}
		incrementSize();
		return true;
	}
	
	private void incrementSize() {
		size++;
	}
	
	private void decrementSize() {
		size--;
	}
	
	public int getSize() {
		return size;
	}
	
	// DONE
	// removing the item
	public boolean remove(T item) {
		int index = getPosition(item);
		if(size == 0 || index >= size || index < 0) {
			return false; // item not found
		}
		Node<T> current = head;
		if(index == 0) {
			head = current.getNext();
		} else {
			for(int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			if(index == size - 1) {
				tail = current;
				current = current.getNext();
			} else {
				current.setNext(current.getNext().getNext());
			}
		}
		decrementSize();
		return true;
	}
	
	// DONE
	// is s1 a subset of s2
	public boolean subset(FortesMP2<T> s2) {
		Node<T> current = head;
		
		if(current != null) {
			while(current.getNext() != null) {
				if(!s2.contains(current.getItem())) { // if s2 does not contain one item of s1
					return false;
				} 
				current = current.getNext(); // else, keep on traversing..
			}
			return true; // end of traversal, successful
		}
		return false; // else, list is empty
	}
	
	// DONE
	// union of two list
	public FortesMP2<T> union(FortesMP2<T> s2) {
		FortesMP2<T> s3 = new FortesMP2<>();
		
		// insert s1 elements in s3, while inserting it already checks for duplicates
		Node<T> current = head;
		for(int i = 0; i < size; i++) {
			s3.insert(current.getItem());
			current = current.getNext();
		}
		
		// insert s2 elements in s3, while inserting it already checks for duplicates
		for(int j = 0; j < s2.size; j++) {
			s3.insert(s2.get(j));
		}
		
		return s3; // return the new list
	}
	
	// DONE
	// intersection of s1 and s2 
	public FortesMP2<T> intersection(FortesMP2<T> s2) {
		FortesMP2<T> s3 = new FortesMP2<>(); // null at first
		
		Node<T> current = head;
		
		if(current != null) {
			for(int i = 0; i < size; i++) {
				if(s2.contains(current.getItem())) { // if each set contains the same element, insert to the new list
					s3.insert(current.getItem());
				}
				current = current.getNext(); // keep on traversing..
			}
			return s3; // end of traversal, successful
		}
		return null; // empty
	}
	
	// DONE
	// s1-s2 set difference
	// elements in s1 but not in s2
	public FortesMP2<T> difference(FortesMP2<T> s2) {
		
		FortesMP2<T> s3 = new FortesMP2<>();
		Node<T> current = head;
		
		if(current != null) {
			for(int i = 0; i < size; i++) {
				if(!s2.contains(current.getItem())) { // insert items that belongs to s1 but not in s2
					s3.insert(current.getItem());
				}
				current = current.getNext(); // keep on traversing...
			}
			return s3; // end of traversal, successful
		}
		
		return null;
	}
	
	// DONE
	// power set of set
	public FortesMP2<FortesMP2<T>> powerset() {
		FortesMP2<FortesMP2<T>> powerSet = new FortesMP2<FortesMP2<T>>();
		
		int powerSize = (int)(Math.pow(2, size)); // 2^n
		
		for(int i = 0; i < powerSize; i++) {
			FortesMP2<T> subset = new FortesMP2<T>(); // storing the subset
			int mask = 1; // used in checking the bit
			for(int j = 0; j < size; j++) {
				if((mask & i) != 0) {
					subset.insert(get(j));
				}
				mask = mask << 1; // increment
			}
			powerSet.insert(subset); // insert to the power set
		}
		return powerSet;
	}
	
	// power set of set of sets
	public FortesMP2<FortesMP2<FortesMP2<T>>> powerSet2() {
		FortesMP2<FortesMP2<FortesMP2<T>>> powerSet2 = new FortesMP2<FortesMP2<FortesMP2<T>>>();
		
		int powerSize = (int)(Math.pow(2, size)); // 2^n
		
		for(int i = 0; i < powerSize; i++) {
			FortesMP2<FortesMP2<T>> subset2 = new FortesMP2<FortesMP2<T>>();
			// unfinished
		}
		
		return powerSet2;
	}
	
	
	//-------- other methods --------
	
	// checks for duplicates
	public boolean contains(T item) {
		Node<T> current = head;
		if(current != null) {
			for(int i = 0; i < size; i++) {
				if(current.getItem().equals(item)) {
					return true;
				}
				current = current.getNext();
			}
			return false;
		}
		return false;
	}
	
	// USED IN REMOVING ITEM
	// returns the position of the searching item
	public int getPosition(T item) {
		if(size == 0) {
			return -1;
		}
		Node<T> current = head;
		if(current != null) {
			for(int i = 0; i < size; i++) {
				if(current.getItem().equals(item)) {
					return i;
				}
				current = current.getNext();
			}
		}
		return -1; // item not found
	}
	
	// USED IN DISPLAY ONLY
	// returns the item of the specified index
	public T get(int index) {
		if(index < 0 || index >= size) {
			return null;
		}
		Node<T> current = head;
		for(int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getItem();
	}
	
	public String display() {
		String output = "{";
		for(int i = 0; i < size; i++) {
			output += get(i);
			if(i < size - 1) {
				output += ",";
			}
		}
		output += "}";
		return output;
	}
	
	// displaying the power set of a set
	public String display(FortesMP2<FortesMP2<T>> powerSet) {
		String output = "{";
		for(int i = 0; i < powerSet.size; i++) {
			output += "{";
			for(int j = 0; j < powerSet.get(i).size; j++) {
				output += powerSet.get(i).get(j);
				if(j < powerSet.get(i).size - 1) {
					output += ",";
				}
			}
			output += "}";
			if(i < powerSet.size - 1) {
				output += ",";
			}
		}
		output += "}";
		return output;
	}
	
	// displaying the power set of set of sets
	
	// MAIN
	public static void main(String[] args) {
		List<String> outputs = new ArrayList<>();
		int testcases;
		int type; // variable to store the type of sets
		int numOp; // number of operations to perform
		String[] tokens; // splitting
		StringTokenizer tokenizer;
		
		System.out.println("Read mpa2.in: " + "\n" + 
		"For sets of Integers, outputs are both written in the console (for checking) and file.\n");
		
		try {
			BufferedReader file = new BufferedReader(new FileReader("mpa2.in"));
			String line = file.readLine();
			testcases = Integer.parseInt(line);
			System.out.println("Number of testcases: " + testcases + "\n");
			while((line = file.readLine()) !=null) {
				tokens = line.split(" ");
				type = Integer.parseInt(tokens[0]);
				line = file.readLine();
				
				if(type == 1) {
					// Set of Integers
					FortesMP2<Integer> s1 = new FortesMP2<Integer>();
					FortesMP2<Integer> s2 = new FortesMP2<Integer>();
					FortesMP2<Integer> s3 = new FortesMP2<Integer>(); // for the new set
					
					// Insert elements to s1
					tokenizer = new StringTokenizer(line, " ");
					System.out.print("int_s1: ");
					while(tokenizer.hasMoreTokens()) {
						int element = Integer.parseInt(tokenizer.nextToken());
						s1.insert(element);
					}
					System.out.println(s1.display());
					
					// Insert elements to s2
					line = file.readLine();
					tokenizer = new StringTokenizer(line, " ");
					System.out.print("int_s2: ");
					while(tokenizer.hasMoreTokens()) {
						int element = Integer.parseInt(tokenizer.nextToken());
						s2.insert(element);
					}
					System.out.println(s2.display());
					
					// perform operations
					line = file.readLine();
					numOp = Integer.parseInt(line);
					while(numOp > 0) {
						line = file.readLine();
						tokens = line.split(" ");
						switch(tokens[0]) {
						case "1":
							// insert
							System.out.print("Inserted: ");
							if(tokens[1].compareTo("1") == 0) {
								s1.insert(Integer.parseInt(tokens[2]));
								outputs.add(s1.display());
								System.out.print(s1.display());
							} else {
								s2.insert(Integer.parseInt(tokens[2]));
								outputs.add(s2.display());
								System.out.println(s2.display());
							}
							System.out.println();
							break;
						case "2":
							// remove
							System.out.print("Removed: ");
							if(tokens[1].compareTo("1") == 0) {
								s1.remove(Integer.parseInt(tokens[2]));
								outputs.add(s1.display());
								System.out.print(s1.display());
							} else {
								s2.remove(Integer.parseInt(tokens[2]));
								outputs.add(s2.display());
								System.out.print(s2.display());
							}
							System.out.println();
							break;
						case "3":
							// subset
							System.out.println("s1 a subset of s2? : " + s1.subset(s2));
							outputs.add(String.valueOf(s1.subset(s2)));
							break;
						case "4":
							// union
							s3 = s1.union(s2);
							System.out.println("Union: " + s3.display());
							outputs.add(s3.display());
							break;
						case "5":
							// intersection
							s3 = s1.intersection(s2);
							System.out.println("Intersection: " + s3.display());
							outputs.add(s3.display());
							break;
						case "6":
							// difference
							s3 = s1.difference(s2);
							System.out.println("Difference: " + s3.display());
							outputs.add(s3.display());
							break;
						case "7":
							// power set
							FortesMP2<FortesMP2<Integer>> powerSet = new FortesMP2<FortesMP2<Integer>>();
							System.out.print("Power Set: ");
							if(tokens[1].compareTo("1") == 0) {
								powerSet = s1.powerset();
								outputs.add(s1.display(powerSet));
								System.out.print(s1.display(powerSet));
							} else {
								powerSet = s2.powerset();
								outputs.add(s2.display(powerSet));
								System.out.print(s2.display(powerSet));
							}
							System.out.println();
							break;
						}
						numOp--; // don't forget to decrement the number of operations
					}
					
				} else if(type == 2) {
					// Set of Doubles
					FortesMP2<Double> s1 = new FortesMP2<Double>();
					FortesMP2<Double> s2 = new FortesMP2<Double>();
					FortesMP2<Double> s3 = new FortesMP2<Double>();
					
					// insert elements to s1
					tokenizer = new StringTokenizer(line, " ");
					while(tokenizer.hasMoreTokens()) {
						double element = Double.parseDouble(tokenizer.nextToken());
						s1.insert(element);
					}
					
					// insert elements to s2
					line = file.readLine();
					tokenizer = new StringTokenizer(line, " ");
					while(tokenizer.hasMoreTokens()) {
						double element = Double.parseDouble(tokenizer.nextToken());
						s2.insert(element);
					}
					
					// perform the operations
					line = file.readLine();
					numOp = Integer.parseInt(line);
					while(numOp > 0) {
						line = file.readLine();
						tokens = line.split(" ");
						switch(tokens[0]) {
						case "1":
							// insert
							if(tokens[1].compareTo("1") == 0) {
								s1.insert(Double.parseDouble(tokens[2]));
								outputs.add(s1.display());
							} else {
								s2.insert(Double.parseDouble(tokens[2]));
								outputs.add(s2.display());
							}
							break;
						case "2":
							// remove
							if(tokens[1].compareTo("1") == 0) {
								s1.remove(Double.parseDouble(tokens[2]));
								outputs.add(s1.display());
							} else {
								s2.remove(Double.parseDouble(tokens[2]));
								outputs.add(s2.display());
							}
							break;
						case "3":
							// subset
							outputs.add(String.valueOf(s1.subset(s2)));
							break;
						case "4":
							// union
							s3 = s1.union(s2);
							outputs.add(s3.display());
							break;
						case "5":
							// intersection
							s3 = s1.intersection(s2);
							outputs.add(s3.display());
							break;
						case "6":
							// difference
							s3 = s1.difference(s2);
							outputs.add(s3.display());
							break;
						case "7":
							// power set
							FortesMP2<FortesMP2<Double>> powerSet = new FortesMP2<FortesMP2<Double>>();
							if(tokens[1].compareTo("1") == 0) {
								powerSet = s1.powerset();
								outputs.add(s1.display(powerSet));
							} else {
								powerSet = s2.powerset();
								outputs.add(s2.display(powerSet));
							}
							break;
						}
						numOp--; // don't forget to decrement the number of operations
					}
					
				} else if(type == 3) {
					// Set of Characters
					FortesMP2<Character> s1 = new FortesMP2<Character>();
					FortesMP2<Character> s2 = new FortesMP2<Character>();
					FortesMP2<Character> s3 = new FortesMP2<Character>();
					
					// insert elements to s1
					tokenizer = new StringTokenizer(line, " ");
					while(tokenizer.hasMoreTokens()) {
						char element = tokenizer.nextToken().charAt(0);
						s1.insert(element);
					}
					s1.display();
					
					// insert elements to s2
					line = file.readLine();
					tokenizer = new StringTokenizer(line, " ");
					while(tokenizer.hasMoreTokens()) {
						char element = tokenizer.nextToken().charAt(0);
						s2.insert(element);
					}
					s2.display();
					
					// perform the operations
					line = file.readLine();
					numOp = Integer.parseInt(line);
					while(numOp > 0) {
						line = file.readLine();
						tokens = line.split(" ");
						switch(tokens[0]) {
						case "1":
							// insert
							if(tokens[1].compareTo("1") == 0) {
								s1.insert(tokens[2].charAt(0));
								outputs.add(s1.display());
							} else {
								s2.insert(tokens[2].charAt(0));
								outputs.add(s2.display());
							}
							break;
						case "2":
							// remove
							if(tokens[1].compareTo("1") == 0) {
								s1.remove(tokens[2].charAt(0));
								outputs.add(s1.display());
							} else {
								s2.remove(tokens[2].charAt(0));
								outputs.add(s2.display());
							}
							break;
						case "3":
							// subset
							outputs.add(String.valueOf(s1.subset(s2)));
							break;
						case "4":
							// union
							s3 = s1.union(s2);
							outputs.add(s3.display());
							break;
						case "5":
							// intersection
							s3 = s1.intersection(s2);
							outputs.add(s3.display());
							break;
						case "6":
							// difference
							s3 = s1.difference(s2);
							outputs.add(s3.display());
							break;
						case "7":
							// power set
							FortesMP2<FortesMP2<Character>> powerSet = new FortesMP2<FortesMP2<Character>>();
							if(tokens[1].compareTo("1") == 0) {
								powerSet = s1.powerset();
								outputs.add(s1.display(powerSet));
							} else {
								powerSet = s2.powerset();
								outputs.add(s2.display(powerSet));
							}
							break;
						}
						numOp--; // don't forget to decrement the number of operations
					}
					
				} else if(type == 4) {
					// Set of Strings
					FortesMP2<String> s1 = new FortesMP2<String>();
					FortesMP2<String> s2 = new FortesMP2<String>();
					FortesMP2<String> s3 = new FortesMP2<String>();
					
					// insert elements to s1
					tokenizer = new StringTokenizer(line, " ");
					while(tokenizer.hasMoreTokens()) {
						String element = tokenizer.nextToken();
						s1.insert(element);
					}
					s1.display();
					
					// insert elements to s2
					line = file.readLine();
					tokenizer = new StringTokenizer(line, " ");
					while(tokenizer.hasMoreTokens()) {
						String element = tokenizer.nextToken();
						s2.insert(element);
					}
					s2.display();
					
					// perform the operations
					line = file.readLine();
					numOp = Integer.parseInt(line);
					while(numOp > 0) {
						line = file.readLine();
						tokens = line.split(" ");
						switch(tokens[0]) {
						case "1":
							// insert
							if(tokens[1].compareTo("1") == 0) {
								s1.insert(tokens[2]);
								outputs.add(s1.display());
							} else {
								s2.insert(tokens[2]);
								outputs.add(s2.display());
							}
							break;
						case "2":
							// remove
							if(tokens[1].compareTo("1") == 0) {
								s1.remove(tokens[2]);
								outputs.add(s1.display());
							} else {
								s2.remove(tokens[2]);
								outputs.add(s2.display());
							}
							break;
						case "3":
							// subset
							outputs.add(String.valueOf(s1.subset(s2)));
							break;
						case "4":
							// union
							s3 = s1.union(s2);
							outputs.add(s3.display());
							break;
						case "5":
							// intersection
							s3 = s1.intersection(s2);
							outputs.add(s3.display());
							break;
						case "6":
							// difference
							s3 = s1.difference(s2);
							outputs.add(s3.display());
							break;
						case "7":
							// power set
							FortesMP2<FortesMP2<String>> powerSet = new FortesMP2<FortesMP2<String>>();
							if(tokens[1].compareTo("1") == 0) {
								powerSet = s1.powerset();
								outputs.add(s1.display(powerSet));
							} else {
								powerSet = s2.powerset();
								outputs.add(s2.display(powerSet));
							}
							break;
						}
						numOp--; // don;t forget to decrement the number of operations
					}
					
				} else if(type == 5) {
					// Set of Sets
					String[] innerTokens;
					if(tokens[1].compareTo("1") == 0) {
						// Set of Sets of Integers
						FortesMP2<Integer> s1 = new FortesMP2<Integer>();
						FortesMP2<Integer> s2 = new FortesMP2<Integer>();
						FortesMP2<Integer> s3 = new FortesMP2<Integer>();
						FortesMP2<FortesMP2<Integer>> ps1 = new FortesMP2<FortesMP2<Integer>>();
						FortesMP2<FortesMP2<Integer>> ps2 = new FortesMP2<FortesMP2<Integer>>();
						FortesMP2<FortesMP2<Integer>> ps3 = new FortesMP2<FortesMP2<Integer>>();
						
						// insert elements to s1 to ps1
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
								for(int j = 0; j < innerTokens.length; j++) {
									s1.insert(Integer.parseInt(innerTokens[j]));
								}
								ps1.insert(s1);
						}
						// HOW TO DISPLAY HAHAHAHA
						s1.display();
						s1.display(ps1);
						
						// insert elements to s2 to ps2
						line = file.readLine();
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
							//for(int i = 0; i < tokenizer.countTokens(); i++) {
								for(int j = 0; j < innerTokens.length; j++) {
									s2.insert(Integer.parseInt(innerTokens[j]));
								}
								ps2.insert(s2);
							//}
						}
						// HOW TO DISPLAY HAHAHAHA
						s2.display();
						s2.display(ps2);
						
						// perform operations
						line = file.readLine();
						numOp = Integer.parseInt(line);
						while(numOp > 0) {
							line = file.readLine();
							tokens = line.split(" \\{}");
							String[] token2;
							switch(tokens[0]) {
							case "1":
								// insert
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.insert(Integer.parseInt(token2[i]));
									}
									ps1.insert(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.insert(Integer.parseInt(token2[i]));
									}
									ps2.insert(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "2":
								// remove
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.remove(Integer.parseInt(token2[i]));
									}
									ps1.remove(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.remove(Integer.parseInt(token2[i]));
									}
									ps2.remove(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "3":
								// subset
								outputs.add(String.valueOf(s1.subset(s2)));
								break;
							case "4":
								// union
								s3 = s1.union(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "5":
								// intersection
								s3 = s1.intersection(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "6":
								// difference
								s3 = s1.difference(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "7":
								// power set
								FortesMP2<FortesMP2<FortesMP2<Integer>>> powerSet = new FortesMP2<FortesMP2<FortesMP2<Integer>>>();
								if(tokens[1].compareTo("1") == 0) {
									powerSet = ps1.powerset();
									//outputs.add(s1.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								} else {
									powerSet = ps2.powerset();
									//outputs.add(s2.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								}
								break;
							}
							numOp--; // don't forget to decrement the number of operations
						}
						//
					} else if(tokens[1].compareTo("2") == 0) {
						// Set of Sets of Doubles
						FortesMP2<FortesMP2<Double>> ps1 = new FortesMP2<FortesMP2<Double>>();
						FortesMP2<FortesMP2<Double>> ps2 = new FortesMP2<FortesMP2<Double>>();
						FortesMP2<FortesMP2<Double>> ps3 = new FortesMP2<FortesMP2<Double>>();
						FortesMP2<Double> s1 = new FortesMP2<Double>();
						FortesMP2<Double> s2 = new FortesMP2<Double>();
						FortesMP2<Double> s3 = new FortesMP2<Double>();
						
						// insert elements to s1 to ps1
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
								for(int j = 0; j < innerTokens.length; j++) {
									s1.insert(Double.parseDouble(innerTokens[j]));
								}
								System.out.println();
								ps1.insert(s1);
						}
						// HOW TO DISPLAY HAHAHAHA
						s1.display();
						s1.display(ps1);
						
						// insert elements to s2 to ps2
						line = file.readLine();
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
							//for(int i = 0; i < tokenizer.countTokens(); i++) {
								for(int j = 0; j < innerTokens.length; j++) {
									s2.insert(Double.parseDouble(innerTokens[j]));
								}
								ps2.insert(s2);
							//}
						}
						// HOW TO DISPLAY HAHAHAHA
						s2.display();
						s2.display(ps2);
						
						// perform operations
						line = file.readLine();
						numOp = Integer.parseInt(line);
						while(numOp > 0) {
							line = file.readLine();
							tokens = line.split(" \\{}");
							String[] token2;
							switch(tokens[0]) {
							case "1":
								// insert
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.insert(Double.parseDouble(token2[i]));
									}
									ps1.insert(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.insert(Double.parseDouble(token2[i]));
									}
									ps2.insert(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "2":
								// remove
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.remove(Double.parseDouble(token2[i]));
									}
									ps1.remove(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.remove(Double.parseDouble(token2[i]));
									}
									ps2.remove(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "3":
								// subset
								outputs.add(String.valueOf(s1.subset(s2)));
								break;
							case "4":
								// union
								s3 = s1.union(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "5":
								// intersection
								s3 = s1.intersection(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "6":
								// difference
								s3 = s1.difference(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "7":
								// power set
								FortesMP2<FortesMP2<FortesMP2<Double>>> powerSet = new FortesMP2<FortesMP2<FortesMP2<Double>>>();
								if(tokens[1].compareTo("1") == 0) {
									powerSet = ps1.powerset();
									//outputs.add(s1.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								} else {
									powerSet = ps2.powerset();
									//outputs.add(s2.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								}
								break;
							}
							numOp--; // don't forget to decrement the number of operations
						}
						//
					} else if(tokens[1].compareTo("3") == 0) {
						// Set of Sets of Characters
						FortesMP2<FortesMP2<Character>> ps1 = new FortesMP2<FortesMP2<Character>>();
						FortesMP2<FortesMP2<Character>> ps2 = new FortesMP2<FortesMP2<Character>>();
						FortesMP2<FortesMP2<Character>> ps3 = new FortesMP2<FortesMP2<Character>>();
						FortesMP2<Character> s1 = new FortesMP2<Character>();
						FortesMP2<Character> s2 = new FortesMP2<Character>();
						FortesMP2<Character> s3 = new FortesMP2<Character>();
						
						// insert elements to s1 to ps1
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
								for(int j = 0; j < innerTokens.length; j++) {
									s1.insert(innerTokens[j].charAt(0));
								}
								ps1.insert(s1);
						}
						// HOW TO DISPLAY HAHAHAHA
						s1.display();
						s1.display(ps1);
						
						// insert elements to s2 to ps2
						line = file.readLine();
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
								for(int j = 0; j < innerTokens.length; j++) {
									s2.insert(innerTokens[j].charAt(0));
								}
								ps2.insert(s2);
						}
						// HOW TO DISPLAY HAHAHAHA
						s2.display();
						s2.display(ps2);
						
						// perform operations
						line = file.readLine();
						numOp = Integer.parseInt(line);
						while(numOp > 0) {
							line = file.readLine();
							tokens = line.split(" \\{}");
							String[] token2;
							switch(tokens[0]) {
							case "1":
								// insert
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.insert(token2[i].charAt(0));
									}
									ps1.insert(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.insert(token2[i].charAt(0));
									}
									ps2.insert(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "2":
								// remove
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.remove(token2[i].charAt(0));
									}
									ps1.remove(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.remove(token2[i].charAt(0));
									}
									ps2.remove(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "3":
								// subset
								outputs.add(String.valueOf(s1.subset(s2)));
								break;
							case "4":
								// union
								s3 = s1.union(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "5":
								// intersection
								s3 = s1.intersection(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "6":
								// difference
								s3 = s1.difference(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "7":
								// power set
								FortesMP2<FortesMP2<FortesMP2<Character>>> powerSet = new FortesMP2<FortesMP2<FortesMP2<Character>>>();
								if(tokens[1].compareTo("1") == 0) {
									powerSet = ps1.powerset();
									//outputs.add(s1.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								} else {
									powerSet = ps2.powerset();
									//outputs.add(s2.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								}
								break;
							}
							numOp--; // don't forget to decrement the number of operations
						}
						//
					} else {
						// Set of Sets of Strings
						FortesMP2<FortesMP2<String>> ps1 = new FortesMP2<FortesMP2<String>>();
						FortesMP2<FortesMP2<String>> ps2 = new FortesMP2<FortesMP2<String>>();
						FortesMP2<FortesMP2<String>> ps3 = new FortesMP2<FortesMP2<String>>();
						FortesMP2<String> s1 = new FortesMP2<String>();
						FortesMP2<String> s2 = new FortesMP2<String>();
						FortesMP2<String> s3 = new FortesMP2<String>();
						
						// insert elements to s1 to ps1
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
								for(int j = 0; j < innerTokens.length; j++) {
									s1.insert(innerTokens[j]);
								}
								ps1.insert(s1);
						}
						// HOW TO DISPLAY HAHAHAHA
						s1.display();
						s1.display(ps1);
						
						// insert elements to s2 to ps2
						line = file.readLine();
						tokenizer = new StringTokenizer(line, " {}");
						while(tokenizer.hasMoreTokens()) {
							String element = tokenizer.nextToken();
							innerTokens = element.split(",");
								for(int j = 0; j < innerTokens.length; j++) {
									s2.insert(innerTokens[j]);
								}
								ps2.insert(s2);
						}
						// HOW TO DISPLAY HAHAHAHA
						s2.display();
						s2.display(ps2);
						
						// perform operations
						line = file.readLine();
						numOp = Integer.parseInt(line);
						while(numOp > 0) {
							line = file.readLine();
							tokens = line.split(" \\{}");
							String[] token2;
							switch(tokens[0]) {
							case "1":
								// insert
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.insert(token2[i]);
									}
									ps1.insert(s1);
									s1.display();
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.insert(token2[i]);
									}
									ps2.insert(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "2":
								// remove
								token2 = tokens[2].split(",");
								if(tokens[1].compareTo("1") == 0) {
									for(int i = 0; i < token2.length; i++) {
										s1.remove(token2[i]);
									}
									ps1.remove(s1);
									outputs.add(s1.display(ps1));
								} else {
									for(int i = 0; i < token2.length; i++) {
										s2.remove(token2[i]);
									}
									ps2.remove(s2);
									outputs.add(s2.display(ps2));
								}
								break;
							case "3":
								// subset
								outputs.add(String.valueOf(s1.subset(s2)));
								break;
							case "4":
								// union
								s3 = s1.union(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "5":
								// intersection
								s3 = s1.intersection(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "6":
								// difference
								s3 = s1.difference(s2);
								ps3.insert(s3);
								outputs.add(s3.display(ps3));
								break;
							case "7":
								// power set
								FortesMP2<FortesMP2<FortesMP2<String>>> powerSet = new FortesMP2<FortesMP2<FortesMP2<String>>>();
								if(tokens[1].compareTo("1") == 0) {
									powerSet = ps1.powerset();
									//outputs.add(s1.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								} else {
									powerSet = ps2.powerset();
									//outputs.add(s2.display(powerSet));
									outputs.add("Power Set of Set of Sets failed.");
								}
								break;
							}
							numOp--; // don't forget to decrement the number of operations
						}
						//
					}
				}
				
			}
			
			file.close();
			
			// file writing
			FileWriter fw = new FileWriter("Fortes2.out");
			for(int i = 0; i < outputs.size(); i++) {
				fw.write(outputs.get(i));
				if(i < outputs.size()-1) {
					fw.write("\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\nOutputs are written in: Fortes2.out");
		System.out.println("End of file.");
		
	}
	
	// NODE CLASS
	class Node<T> {
		private Node<T> next;
		private T item;
		
		// default constructor
		public Node() {
			next = null;
			item = null;
		}
		
		// override constructor
		public Node(T item) {
			next = null;
			this.item = item;
		}
		
		// getters and setters
		public T getItem() {
			return item;
		}
		
		public void setItem(T item) {
			this.item = item;
		}
		
		public Node<T> getNext() {
			return next;
		}
		
		public void setNext(Node<T> nextItem) {
			next = nextItem;
		}
	}
	
}