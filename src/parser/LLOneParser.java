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
		for(NonterminalSymbol ns:grammar.getNonterminals()) {
			Set<Symbol> list = new TreeSet<Symbol>();
			nonterminalMap.put(ns, list);
		}
		
		Map<Symbol, Set<Symbol>> terminalMap = new TreeMap<Symbol, Set<Symbol>>();
		for(TerminalSymbol ts:grammar.getAlphabet()) {
			Set<Symbol> list = new TreeSet<Symbol>();
			list.add(ts);
			terminalMap.put(ts, list);
	}
	
		Map<Symbol, Set<Symbol>> res = new TreeMap<Symbol, Set<Symbol>>();
		res.putAll(terminalMap);
		res.putAll(nonterminalMap);		
	
		Set<Symbol> list = new TreeSet<Symbol>();
		list.add(Grammar.EPSILON);
		res.put(Grammar.EPSILON,list);
		
		return res;
	}
	
	public void printFirst(Symbol s) {
		System.out.println("First of "+s.getSymbol()+" ");
		for(Symbol sym : first.get(s)) {
			System.out.print(sym+" ");
		}
	}
	
	/**
	 * Construct the 'first'.
	 */
	private void createFirst(){
		
		//Initialize first with empty set 
		first = initFirst();
		Map<Symbol, Set<Symbol>> prevFirst = initFirst();
		Map<Symbol, Set<Symbol>> nextFirst = initFirst();	
		
		boolean hasChanged = false;
		
		do {
			
			for(NonterminalSymbol A : grammar.getNonterminals()) {
				for(List<Symbol> prod:grammar.getProductions(A)) {
					if(prevFirst.get(prod.get(0)).size()!=0) {
						nextFirst.get(A).addAll(prevFirst.get(prod.get(0)));
					}
					}
				}
			
			hasChanged = false;
			for(Symbol s : prevFirst.keySet()) {
				if(prevFirst.get(s).size() != nextFirst.get(s).size()) {
					hasChanged = true;
			}
				}
			
			prevFirst = nextFirst;
			}
		while(hasChanged);
		first = nextFirst;
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
