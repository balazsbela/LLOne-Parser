package test;

import grammar.Grammar;
import grammar.Symbol;
import parser.LLOneParser;
import parser.SymbolPair;
import parser.TableCell;
import parser.TableSyntaxTree;

public class Main {

	public static void main(String[] args) throws Exception {
		
		
		System.out.println("LOADING GRAMMAR...\n\n");
		Grammar gr = new Grammar();
		gr.loadFromFile("grammar2.txt");

		System.out.println("\nINITIALIZING PARSER...\n\n");
		LLOneParser parser = new LLOneParser(gr);
		
		System.out.println("\nCREATING FIRST...\n\n");
		parser.createFirst();


		System.out.println("\nFIRST\n");
		for (Symbol s : parser.getFirst().keySet()) {
			System.out.print("FIRST(" + s + ") = ");
			for ( Symbol ss : parser.getFirst().get(s) ) {
				System.out.print(ss + " ");
			}
			System.out.println();
		}
		
		
		System.out.println("\nCREATING FOLLOW...\n\n");
		parser.createFollow();
		
		System.out.println("\nFOLLOW\n");
		for (Symbol s : parser.getFollow().keySet()) {
			System.out.print("FOLLOW (" + s + ") = ");
			for (Symbol ss:parser.getFollow().get(s)) {
				System.out.print(ss + " ");
			}
			System.out.println();
		}
		
		System.out.println("\nCREATING LL1 TABLE...\n\n");
		parser.constructTable();
		
		System.out.println("\nLL1 TABLE\n");
		for ( SymbolPair pair : parser.table.keySet() ) {
			System.out.print( "(" + pair.getSymbol1() + "," + pair.getSymbol2() + ") = " );
			
			TableCell cell = parser.table.get(pair);
							
			if ( cell.getName().equals("PUSH") ) {
				System.out.print( " ( " );
				for (Symbol s : cell.getAlpha()) {
					System.out.print(s + " ");
				}
				System.out.println( " , " + cell.getProductionNr() + " ) " );
			} else {
				System.out.println( cell.getName()  + " ");
			}		
		}
		
		
		System.out.println("\nLOADING INPUT SEQUENCE...\n\n");
		parser.loadSequence("sequence.txt");
		
		System.out.println("\nPARSING...\n\n");
		parser.parse();

		
		System.out.println("\nSYNTAX TREE\n");
		((TableSyntaxTree)parser.syntaxTree).printTable();
		
		
		System.out.println("\nLEAVES OF SYNTAX TREE\n");
		for (Symbol s : parser.syntaxTree.getSymbolLeaves() ) {
			if(!s.equals(Grammar.EPSILON)) {
				System.out.println(s );
			}
		}
	}
	
	
}
