package parser;

import grammar.Grammar;
import grammar.NonterminalSymbol;
import grammar.Symbol;
import grammar.TerminalSymbol;
import grammar_ref.NonTerminalSymbol;

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
				for(List<Symbol> prod:grammar.getProduction(A.getSymbol())) {
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
	
	public void createFollow() {
		System.err.println("");
	}
		
}
