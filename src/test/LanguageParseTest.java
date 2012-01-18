package test;

import java.io.IOException;

import grammar.Grammar;
import grammar.NonterminalSymbol;
import grammar.Symbol;
import parser.LLOneParser;
import parser.SymbolPair;
import parser.TableCell;
import parser.TableSyntaxTree;
import scanner.PifEntry;
import scanner.Scanner;

public class LanguageParseTest {

	public static void main(String[] args) throws Exception {
		
		Grammar gr = new Grammar();
		
		try {
			gr.loadFromFile("minilang_grammar.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LLOneParser parser = null;
		try {
			parser = new LLOneParser(gr);
			parser.createFirst();
			parser.createFollow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(gr.getStartingSymbol());
		
		
		System.out.println("\nFIRST\n");
		
		for (Symbol s : parser.getFirst().keySet()) {
			System.out.print(s + " = ");
			for ( Symbol ss : parser.getFirst().get(s) ) {
				System.out.print(ss + " ");
			}
			
			System.out.print("  ---  ");
			if ( s instanceof NonterminalSymbol ) {
				for (Symbol ss:parser.getFollow().get(s)) {
					System.out.print(ss + " ");
				}
			}
			
			
			System.out.println();
		}
		
		parser.constructTable();
		
		for ( SymbolPair pair : parser.table.keySet() ) {
			System.out.print( "(" + pair.getSymbol1() + "," + pair.getSymbol2() + ") = " );
			
			TableCell cell = parser.table.get(pair);
			if (cell.getName()!=null) {
				System.out.println( cell.getName()  + " ");
			} else {
				for (Symbol s : cell.getAlpha()) {
					System.out.print(s);
				}
				System.out.println( " , " + cell.getProductionNr() );
			}			
		}
		
		
		Scanner scanner = new Scanner();
		try {
			scanner.scan("mycode.jdc");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(PifEntry pe : scanner.getPif()) { 
			System.out.println(scanner.getSymbol(pe.getCode())+ "-"+pe.getCode());
			parser.sequence.add( new NonterminalSymbol(scanner.getSymbol(pe.getCode())) );
		}
		
		
		
		try {
			//parser.loadSequence("sequence.txt");
			parser.parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("done");
		
		((TableSyntaxTree)parser.syntaxTree).printTable();
		
		for (Symbol s : parser.syntaxTree.getSymbolLeaves() ) {
			if(!s.equals(Grammar.EPSILON)) {
				System.out.print(s );
			}
		}
	}
	
	
}
