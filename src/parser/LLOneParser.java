package parser;

import grammar.Grammar;
import grammar.NonterminalSymbol;
import grammar.Symbol;
import grammar.TerminalSymbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class LLOneParser {
	/** The grammar. */
	private Grammar grammar;
	
	/** The 'first' construction. */
	private Map<Symbol, Set<Symbol>> first;
	
	/** The 'follow' construction. */
	private Map<NonterminalSymbol, Set<Symbol>> follow;
		
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
	private Map<Symbol, Set<Symbol>> initFirst() {
		Map<Symbol, Set<Symbol>> nonterminalMap = new TreeMap<Symbol, Set<Symbol>>();
		Iterator<NonterminalSymbol> j = grammar.getNonterminals().iterator();
		while (j.hasNext()){
			Set<Symbol> list = new TreeSet<Symbol>();
			nonterminalMap.put(j.next(), list);
		}
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
	
	
	public void test() {
	}
	
	private Symbol getSymbolAfter(List<Symbol> productionRHS , Symbol symbol) {
		
		for ( int i=0 ; i<productionRHS.size() ; i++) {
			if ( productionRHS.get(i).equals(symbol) ) {
				if ( i==productionRHS.size()-1 ) {
					return Grammar.EPSILON;
				} else {
					return productionRHS.get(i+1);
				}
			}
		}
		return null;
	}
	
	public void createFollow() {
		
		follow = new HashMap<NonterminalSymbol,Set<Symbol>>();
		// two instances of follow are used to keep track of changes
		HashMap<NonterminalSymbol,Set<Symbol>> followBefore = new HashMap<NonterminalSymbol,Set<Symbol>>();
		HashMap<NonterminalSymbol,Set<Symbol>> followAfter = new HashMap<NonterminalSymbol,Set<Symbol>>();
		
		// initialize follow 
		// empty set for every nonterminal 
		// E for the starting symbol
		for ( NonterminalSymbol nonterm : grammar.getNonterminals() ) {
			followBefore.put(nonterm, new HashSet<Symbol>());
			if ( nonterm.equals(grammar.getStartingSymbol()) ) {
				followBefore.get(nonterm).add( Grammar.EPSILON );
			}
		}
		
		// repeat while a change is made 
		boolean changed;
		do {
			
			changed = false;
			// copy the followBefore into followAfter
			followAfter.clear();
			for ( NonterminalSymbol nonterm : followBefore.keySet() ) {
				followAfter.put(nonterm, new HashSet<Symbol>());
				followAfter.get(nonterm).addAll( followBefore.get(nonterm) );
			}
			
			// iterate through all the nonterminals
			for ( NonterminalSymbol nonterm : grammar.getNonterminals() ) {

				// iterate through all the productions with nonterm on their RHS with the form:
				// prodNonterminal -> productionRHS 
				Map<NonterminalSymbol , List<List<Symbol>>> productionsWithNontermOnRHS = grammar.getProductionsWithRHS(nonterm);
				
				for (NonterminalSymbol prodNonterminal : productionsWithNontermOnRHS.keySet()) {
					for (List<Symbol> productionRHS : productionsWithNontermOnRHS.get(prodNonterminal)) {
						
						// followAfter = followBefore + FIRST(symbol after nonterm)
						Symbol symbolAfter = getSymbolAfter(productionRHS, nonterm);
						changed = followAfter.get(nonterm).addAll( first.get(symbolAfter) );
						// if the FIRST(symbol after nonterm) contains EPSILON add FOLLOWbefore( prodNonterminal )
						if ( first.get(symbolAfter).contains(Grammar.EPSILON) ) {
							changed = changed || followAfter.get(nonterm).addAll( followBefore.get(prodNonterminal) );
						}
					}
				}
			}
 			
			// make followAfter the new followBefore
			followBefore.clear();
			for ( NonterminalSymbol nonterm : followAfter.keySet() ) {
				followBefore.put(nonterm, new HashSet<Symbol>());
				followBefore.get(nonterm).addAll( followAfter.get(nonterm) );
			}			
			
			
		} while (changed);
		
		// save follow
		for ( NonterminalSymbol nonterm : followAfter.keySet() ) {
			follow.put(nonterm, new HashSet<Symbol>());
			follow.get(nonterm).addAll( followAfter.get(nonterm) );
		}			
		
	}
		
}
