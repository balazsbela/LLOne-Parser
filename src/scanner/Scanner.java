package scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Scanner {

	private List<Character> operators;
	private List<Character> separators;
	private List<String> reservedWords;
	Map<String, Integer> codeMapping;	
	
	//Maps the symbols to a position in the table
	SymbolTable<String> symbolTable = //new HashMapWrapper<String>();
									  new HashTableMapWrapper<String>();
	
	//Program internal form
	List<PifEntry> pif = new ArrayList<PifEntry>();
	public List<PifEntry> getPif() {
		return pif;
	}

	private int nrRows;
		
	public int getNrRows() {
		return nrRows;
	}

	public Scanner() {
		operators = LanguageProperties.operators;
		separators = LanguageProperties.separators;
		reservedWords = LanguageProperties.reservedWords;
		codeMapping = LanguageProperties.mapping;		
	}

	public void scan(String filename) throws IOException,Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		 while (reader.ready()) {           
	            String token = detect(reader);
	           // System.out.println(token);
	            
	            if(token.isEmpty()) {
	            	continue;
	            }
	            if(operators.contains(token.charAt(0)) || 
	               separators.contains(token.charAt(0)) ||
	               reservedWords.contains(token) ) {
	            	if(codeMapping.get(token) == null) {
	            		throw new NullPointerException("no code found for:" + token);
	            	}
	            	pif.add(new PifEntry(codeMapping.get(token), -1));
	            }
	            else if(isIdentifier(token)) {
	            		Integer pos = symbolTable.get(token);
	            		if(pos == null) {
	            			pos = symbolTable.put(token);
	            		}
	            		
	            		//0 is the code for identifier
	            		pif.add(new PifEntry(0,pos));
	            }
	            else if(isConstant(token)) {
	            	Integer pos = symbolTable.get(token);
	            	if(pos == null) {
            			pos = symbolTable.put(token);
            		}
	            	//1 is the code for constants
            		pif.add(new PifEntry(1,pos));
	            }
	            else {
	            	throw new Exception("Syntax Error");
	            }
	            		            
		 }
		 reader.close();


	}

	private String detect(BufferedReader reader) throws IOException {
		String token="";
		boolean firstFound = false;
		boolean lastFound = false;
		boolean stringConstant = false;
		
		while(!lastFound && reader.ready()) {
			reader.mark(1);
			char c = (char)reader.read();
			
			if (c=='\n') {
				nrRows++;
			}
			
			//we found it's first character and this is a separator so we jump back
			if(firstFound && (separators.contains(c) || operators.contains(c))) {
				if(stringConstant) {
					token += c;
					if(c == '"') {			
						stringConstant = false;
						lastFound = true;
						reader.reset();
						break;
					}
				}
				else {
					lastFound = true;
					reader.reset();
					break;
				}
				
			}
						
			if( !operators.contains(c) && !separators.contains(c) && ((int)c)>32) {
				firstFound = true;
				token += c;
				if(c == '"') {
					if(!stringConstant) {
						stringConstant = true;
					}
					else {
						stringConstant = false;
					}
				}
			}		
			else if (firstFound && !stringConstant) {
				lastFound = true;
			}			
			else if( (operators.contains(c) || separators.contains(c)) && c!=' ') {					
					token += c;					
					lastFound = true;
			}
						
		}
		return token;
	}

	private boolean isAcceptable(char c) {
		if(Character.isLetterOrDigit(c)) {
			return true;
		}
		if(separators.contains(c)) {
			return true;
		}
		if(operators.contains(c)) {
			return true;
		}
		return false;
	}

	
	private boolean isIdentifier(String token)  {
		if ( Character.isDigit( token.charAt(0) ) ) {
			return false;
		}
		for(char c: token.toCharArray()) {
			if(!Character.isLetterOrDigit(c)) {
				return false;
			}
		}
		if (token.length() > 250) {
			return false;
		}
		return true;
	}
	
    private boolean isConstant(String token) throws Exception {
    	//check if character constant
        if ( token.startsWith("'")  ) {
            if (token.endsWith("'") && token.length()<=3) {
                return true;
            } else {
                throw new Exception("Wrong chacter constant!");
            }
        }
        
        //check if string constant
        if ( token.startsWith("\"")  ) {
            if (token.endsWith("\"") && !token.substring(1, token.length()-1).contains("\"")) {
                return true;
            } else {
                throw new Exception("Wrong string constant!");
            }
        }
        
        //Integer constant
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException exc) {
            return false;
        }
    }
	
    
    public String findInCodeMap(int code,int pos) {
    	Set<Entry<String,Integer>> entrySet = codeMapping.entrySet();
		
		for(Entry<String,Integer> entry : entrySet) {
			if(entry.getValue() == code) {
				return entry.getKey();
			}
		}
		return symbolTable.getSymbol(pos);
    }
    
	public static void main(String[] args)  {
		Scanner scanner = new Scanner();
		
		try {
			scanner.scan("mycode.jdc");
			
			for(PifEntry pe : scanner.pif) { 
				System.out.print(scanner.findInCodeMap(pe.code,pe.position) +" - "+ pe.code+ " ");
			}
			
			
			
//			System.out.println("Symbol table");
//			for(int i=0;i<scanner.symbolTable.size();i++) {
//				System.out.println(i+":"+scanner.symbolTable.getSymbol(i));
//			}
//			
//			System.out.println("PIF:");
//			for(PifEntry pe : scanner.pif) { 
//				System.out.print(pe.code+ " ");
//			}
		}
		catch (Exception e) {
			 System.out.println(e.getMessage() + " at line " + scanner.getNrRows());
		}
	}

	 public String getSymbol(int code) {
    	if(code == 0) return "identifier";
    	if(code == 1) return "constant";
    	
		Set<Entry<String,Integer>> entrySet = codeMapping.entrySet();
		
		for(Entry<String,Integer> entry : entrySet) {
			if(entry.getValue() == code) {
				return entry.getKey();
			}
		}
		return null;
    }
}
