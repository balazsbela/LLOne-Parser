package test;

import java.io.IOException;

import grammar.Grammar;
import grammar.Symbol;
import parser.LLOneParser;
import parser.SymbolPair;
import parser.TableCell;
import parser.TableSyntaxTree;
import scanner.PifEntry;
import scanner.Scanner;

public class LanguageParseTest {

	public static void main(String[] args) {
		
		Grammar gr = new Grammar();
		
		try {
			gr.loadFromFile("grammar2.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LLOneParser parser = null;
		try {
			parser = new LLOneParser(gr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(gr.getStartingSymbol());
		
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
		
//		for(PifEntry pe : scanner.getPif()) { 
//			System.out.print(scanner.findInCodeMap(pe.getCode(),pe.getPosition()) +" - "+ pe.getCode()+ " ");
//		}
		
		try {
			parser.loadSequence("sequence.txt");
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
