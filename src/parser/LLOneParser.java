package parser;

import grammar.Grammar;
import grammar.NonterminalSymbol;
import grammar.Production;
import grammar.Symbol;
import grammar.TerminalSymbol;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

	/** The LL1 Table */
	public Map<SymbolPair , TableCell> table;
	
	private Symbol dollar = Grammar.EPSILON;
	
	/** Production Indexing */
	private Production[] productions;
	
	/** Sequence to parse */
	private List<Symbol> sequence;
	
	/** The 'alpha' stack, initially containing the input sequence. */
	private Stack<Symbol> alpha;

	/** The 'beta' stack, initially containing the starting symbol. */
	private Stack<Symbol> beta;

	/** The 'pi' stack, output stack. */
	private Stack<Integer> pi;
	
	
	/** The Syntax Tree*/
	public SyntaxTree syntaxTree;
	
	public LLOneParser(Grammar g) throws Exception {
		grammar = g;
		createFirst();
	    createFollow();
	    constructTable();
	}

	/**
	 * Initialize first.
	 */
	private Map<Symbol, Set<Symbol>> initFirst() {
		Map<Symbol, Set<Symbol>> nonterminalMap = new TreeMap<Symbol, Set<Symbol>>();
		for (NonterminalSymbol ns : grammar.getNonterminals()) {
		
			
			Set<Symbol> list = new TreeSet<Symbol>();
			nonterminalMap.put(ns, list);
			
			for(List<Symbol> ls : grammar.getProductions(ns) ){
				Symbol first = ls.get(0);
				if(grammar.getAlphabet().contains(first)) {
					nonterminalMap.get(ns).add(first);
				}
			}					
		}

		Map<Symbol, Set<Symbol>> terminalMap = new TreeMap<Symbol, Set<Symbol>>();
		for (TerminalSymbol ts : grammar.getAlphabet()) {
			Set<Symbol> list = new TreeSet<Symbol>();
			list.add(ts);
			terminalMap.put(ts, list);
		}
		
		

		Map<Symbol, Set<Symbol>> res = new TreeMap<Symbol, Set<Symbol>>();
		res.putAll(terminalMap);
		res.putAll(nonterminalMap);

		Set<Symbol> list = new TreeSet<Symbol>();
		list.add(Grammar.EPSILON);
		res.put(Grammar.EPSILON, list);

		return res;
	}

	public void printFirst(Symbol s) {
		System.out.println("First of " + s.getSymbol() + " ");
		for (Symbol sym : first.get(s)) {
			System.out.print(sym + " ");
		}
	}

	/*
	 * The plus operation
	 */
	private Set<Symbol> plus(Set<Symbol> alpha,Set<Symbol> beta) {
		Set<Symbol> result = new HashSet<Symbol>();
		for(Symbol s1:alpha) {
			for(Symbol s2:beta) {
				if(s1.equals(Grammar.EPSILON)) {
					result.add(s2);
				}
				else {
					result.add(s1);
				}
			}
		}		
		return result;
	}
	 
	/**
	 * Construct the 'first'.
	 */
	private void createFirst() {

		// Initialize first with empty set
		first = initFirst();
		Map<Symbol, Set<Symbol>> prevFirst = initFirst();
		Map<Symbol, Set<Symbol>> nextFirst = new HashMap<Symbol, Set<Symbol>>();

		boolean hasChanged = false;	
		
		do {
			nextFirst = initFirst();
			
			for (NonterminalSymbol A : grammar.getNonterminals()) {
				for (List<Symbol> prod : grammar.getProductions(A)) {
					Set<Symbol> tempSet = new HashSet<Symbol>();
					//tempSet.add(grammar.EPSILON);
					boolean emptyPervFirst = false;
					
					for(Symbol s : prod) {
						if(prevFirst.get(s).isEmpty()) {
							emptyPervFirst = true;
							break;
						}
						else {
							if (tempSet.isEmpty()) {
								tempSet.addAll(prevFirst.get(s));
							} else {
								tempSet = plus(tempSet,prevFirst.get(s));
							}
						}
					}
					nextFirst.get(A).addAll(prevFirst.get(A));
					if (!emptyPervFirst) {
						nextFirst.get(A).addAll(tempSet);
					}
				}				
			}
			
			hasChanged = false;
			for (Symbol s : prevFirst.keySet()) {
				if (prevFirst.get(s).size() != nextFirst.get(s).size()) {
					hasChanged = true;
				}
			}

			prevFirst = nextFirst;
		} while (hasChanged);
		first = nextFirst;
	}

	public void test() {
	}

	private Symbol getSymbolAfter(List<Symbol> productionRHS, Symbol symbol) {

		for (int i = 0; i < productionRHS.size(); i++) {
			if (productionRHS.get(i).equals(symbol)) {
				if (i == productionRHS.size() - 1) {
					return Grammar.EPSILON;
				} else {
					return productionRHS.get(i + 1);
				}
			}
		}
		return null;
	}

	public void createFollow() {

		follow = new HashMap<NonterminalSymbol, Set<Symbol>>();
		// two instances of follow are used to keep track of changes
		HashMap<NonterminalSymbol, Set<Symbol>> followBefore = new HashMap<NonterminalSymbol, Set<Symbol>>();
		HashMap<NonterminalSymbol, Set<Symbol>> followAfter = new HashMap<NonterminalSymbol, Set<Symbol>>();

		// initialize follow
		// empty set for every nonterminal
		// E for the starting symbol
		for (NonterminalSymbol nonterm : grammar.getNonterminals()) {
			followBefore.put(nonterm, new HashSet<Symbol>());
			if (nonterm.equals(grammar.getStartingSymbol())) {
				followBefore.get(nonterm).add(Grammar.EPSILON);
			}
		}

		// repeat while a change is made
		boolean changed;
		do {

			changed = false;
			// copy the followBefore into followAfter
			followAfter.clear();
			for (NonterminalSymbol nonterm : followBefore.keySet()) {
				followAfter.put(nonterm, new HashSet<Symbol>());
				followAfter.get(nonterm).addAll(followBefore.get(nonterm));
			}

			// iterate through all the nonterminals
			for (NonterminalSymbol nonterm : grammar.getNonterminals()) {

				// iterate through all the productions with nonterm on their RHS
				// with the form:
				// prodNonterminal -> productionRHS
				Map<NonterminalSymbol, List<List<Symbol>>> productionsWithNontermOnRHS = grammar
						.getProductionsWithRHS(nonterm);

				for (NonterminalSymbol prodNonterminal : productionsWithNontermOnRHS.keySet()) {
					for (List<Symbol> productionRHS : productionsWithNontermOnRHS.get(prodNonterminal)) {

						// followAfter = followBefore + FIRST(symbol after
						// nonterm)
						Symbol symbolAfter = getSymbolAfter(productionRHS, nonterm);
						changed = changed || followAfter.get(nonterm).addAll(first.get(symbolAfter));
						// if the FIRST(symbol after nonterm) contains EPSILON
						// add FOLLOWbefore( prodNonterminal )
						if (first.get(symbolAfter).contains(Grammar.EPSILON)) {
							changed = changed || followAfter.get(nonterm).addAll(followBefore.get(prodNonterminal));
						}
					}
				}
			}

			// make followAfter the new followBefore
			followBefore.clear();
			for (NonterminalSymbol nonterm : followAfter.keySet()) {
				followBefore.put(nonterm, new HashSet<Symbol>());
				followBefore.get(nonterm).addAll(followAfter.get(nonterm));
			}

		} while (changed);

		// save follow
		for (NonterminalSymbol nonterm : followAfter.keySet()) {
			follow.put(nonterm, new HashSet<Symbol>());
			follow.get(nonterm).addAll(followAfter.get(nonterm));
		}

	}
	
	private void indexProductions() {

		int productionCount = 0;
		for ( NonterminalSymbol nonterm : grammar.getProductions().keySet() ) {
			productionCount += grammar.getProductions(nonterm).size();
		}
		
		productions = new Production[productionCount + 1];
		int i = 1;
		
		for ( NonterminalSymbol nonterm : grammar.getNonterminals() ) {
			for ( List<Symbol> prod : grammar.getProductions(nonterm) ) {
				productions[i] = new Production(nonterm, prod);
				i++;
			}
		}
	}
	
	private int findProductionIndex(NonterminalSymbol nonterm, List<Symbol> prod) {
		for (int i=1 ; i<productions.length ; i++) {
			if ( productions[i].equals(new Production(nonterm, prod)) ) {
				return i;
			}
		}
		return -1;
	}
	
	private void constructTable() throws Exception {
		
		// make indexing
		indexProductions();
		
		// construct table
		table = new HashMap<SymbolPair, TableCell>();
		
		for ( NonterminalSymbol s1 : grammar.getNonterminals() ) {
			List<Symbol> terminalAndDollar = new ArrayList<Symbol>();
			terminalAndDollar.addAll( grammar.getAlphabet() );
			terminalAndDollar.add(dollar);
			for ( Symbol s2 : terminalAndDollar) {
				
				SymbolPair pair = new SymbolPair(s1, s2);
				
				for (List<Symbol> production : grammar.getProductions(s1)) {
					
					Symbol firstSymbol = production.get(0);
					if ( !firstSymbol.equals(Grammar.EPSILON) && first.get(firstSymbol).contains(s2) ) {
						if ( table.containsKey(pair) ) {
							throw new Exception("CONFLICT at " + pair.getSymbol1() + " " + pair.getSymbol2() );
						}
						table.put( pair , new TableCell(production, findProductionIndex(s1, production) ));
					}
					if ( first.get(firstSymbol).contains(Grammar.EPSILON) && follow.get(s1).contains(s2) ) {
						if ( table.containsKey(pair) ) {
							throw new Exception("CONFLICT at " + pair.getSymbol1() + " " + pair.getSymbol2() );
						}
						table.put( pair , new TableCell(production, findProductionIndex(s1, production) ));
					}
					
				}
			}
			
		}
		
		for ( TerminalSymbol term : grammar.getAlphabet() ) {
			table.put( new SymbolPair(term, term) , TableCell.pop);
		}
		
		table.put( new SymbolPair(dollar, dollar) , TableCell.accept);
		
	}
	
	public void loadSequence(String fileName) throws Exception {
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String[] line = reader.readLine().split(" ");
		
		sequence = new ArrayList<Symbol>();
		
		for (String symbol : line) {
			sequence.add(new TerminalSymbol(symbol));
		}
		
		reader.close();		
		
	}
	

	private void initConfig() {
		
		alpha = new Stack<Symbol>();
		beta = new Stack<Symbol>();
		pi = new Stack<Integer>();
		
		alpha.push(dollar);
		for ( int i=sequence.size()-1 ; i>=0 ; i-- ) {
			alpha.push( sequence.get(i) );
		}
		
		beta.push(dollar);
		beta.push(grammar.getStartingSymbol());
		
	}
	
	public void parse() throws Exception {
		
		initConfig();
		
		String action = "";
		
		while (!action.equals("ACCEPT")) {
			
			Symbol beta1 = beta.pop();
			Symbol alpha1 = alpha.peek();
			
			if ( beta==null || alpha==null ) {
				throw new Exception("ERROR: stack are empty");
			}
			
			
			SymbolPair pair = new SymbolPair(beta1, alpha1);	
			if ( table.containsKey( pair ) ) {
				
				TableCell cell = table.get(pair);
				action = cell.getName();
				
				if (action.equals("PUSH")) {
					
					for (int i=cell.getAlpha().size()-1 ; i>=0 ; i--) {
						if (!cell.getAlpha().get(i).equals(Grammar.EPSILON)) {
							beta.push( cell.getAlpha().get(i) );
						}
					}
					pi.push( cell.getProductionNr() );
					
				}
				
				if (action.equals("POP")) {
					alpha.pop();
				}
				
				
			} else {
				throw new Exception("ERROR at: (" + beta1 + "," + alpha1 + ")");
			}
			
		
			
		}
		constructTree();
		
	}
	
	
	private void constructTree() throws Exception {
		
		syntaxTree = new TableSyntaxTree();
		
		for (Integer prod : pi) {
			syntaxTree.add( productions[prod] );
			
			
		}
		
	}
}
