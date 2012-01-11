package parser;

import grammar.Grammar;
import grammar.Production;
import grammar.Symbol;
import grammar.TerminalSymbol;
import grammar_ref.NonTerminalSymbol;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class LLOneParser {
	/** The grammar. */
	private Grammar grammar;
	
	/** The 'first' construction. */
	private Map<Symbol, Set<Symbol>> first;
	
	/** The 'follow' construction. */
	private Map<NonTerminalSymbol, List<Symbol>> follow;
		
	/** The 'alpha' stack, initially containing the input sequence. */
	private Stack<TerminalSymbol> alpha;
	
	/** The 'beta' stack, initially containing the starting symbol. */
	private Stack<Symbol> beta;
	
	public LLOneParser(Grammar g) {
		grammar = g;
		createFirst();
		//createFollow();
	}
	
	/**
	 * Initialize first.
	 */
	private void initFirst() {
		
	}
	
	
	/**
	 * Construct the 'first'.
	 */
	private void createFirst(){
		first = initFirst();
		//printTypeFirstMap(first);
		boolean areFirstsTheSame = true;
		Map<Symbol, Set<Symbol>> prevF = createEmptyFirst();
		//printTypeFirstMap(prevF);
		Map<Symbol, Set<Symbol>> nextF = createEmptyFirst();
		//System.out.println("NEXT");
		//printTypeFirstMap(nextF);
		do {
			nextF = createEmptyFirst();
			Iterator<NonTerminalSymbol> j = grammar.getNonTerminals();
			while (j.hasNext()){
				NonTerminalSymbol A = j.next();
				//System.out.println("Processing nonterminal "+A);
				Iterator<Production> it = grammar.getProductionsOf(A);
				while (it.hasNext()){
					Production p = it.next();
					//System.out.println("\tProcessing production "+p);
					List<Symbol> Xi = p.getRhs();
					Symbol X1 = Xi.get(0);
					//System.out.println("\tFirst symbol of RHS "+X1+" with First "+prevF.get(X1));
					if (prevF.get(X1).size()!=0){
						//System.out.println("\tAdding "+X1);
						nextF.get(A).addAll(prevF.get(X1));
					}
				}
			}
			areFirstsTheSame = true;
			for (Symbol s : prevF.keySet()){
				if (prevF.get(s).size()!=nextF.get(s).size()){
					areFirstsTheSame = false;
				}
			}
			System.out.println("TempFirst:");
			printTypeFirstMap(prevF);
			prevF = nextF;
		} while(!areFirstsTheSame);
		first = nextF;
		System.out.println("First:");
		printTypeFirstMap(first);
	}
	
}
