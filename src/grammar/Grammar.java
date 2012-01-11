package grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grammar {

	public static final Symbol EPSILON = new Symbol("E");

	List<NonterminalSymbol> nonterminals = new ArrayList<NonterminalSymbol>();
	List<TerminalSymbol> alphabet = new ArrayList<TerminalSymbol>();
	Map<NonterminalSymbol, List<List<Symbol>>> productions = new HashMap<NonterminalSymbol, List<List<Symbol>>>();
	NonterminalSymbol startingSymbol;

	public Grammar() {

	}

	/**
	 * Get the set of non terminal symbols
	 * 
	 * @return - the set of non terminal symbols
	 */
	public List<NonterminalSymbol> getNonterminals() {
		return nonterminals;
	}

	/**
	 * Get the alphabet
	 * 
	 * @return - the set of terminal symbols
	 */
	public List<TerminalSymbol> getAlphabet() {
		return alphabet;
	}

	/**
	 * Get the set of productions
	 * 
	 * @return
	 */
	public Map<NonterminalSymbol, List<List<Symbol>>> getProductions() {
		return productions;
	}

	/**
	 * Return the list of production for a non-terminal symbol
	 * 
	 * @param nonTerminal
	 *            - the non-terminal symbol
	 * @return - it's list of production
	 */
	public List<List<Symbol>> getProduction(String nonTerminal) {
		return productions.get(nonTerminal);
	}

	/**
	 * Get the starting symbol
	 * 
	 * @return - the starting symbol
	 */
	public Symbol getStartingSymbol() {
		return startingSymbol;
	}

	/**
	 * Add the set of non terminal symbols
	 * 
	 * @param non
	 *            - the set of non terminal symbols
	 */
	public void addNonterminals(String[] non) {
		nonterminals.clear();
		for (String s : non) {
			nonterminals.add( new NonterminalSymbol(s) );
		}
	}

	/**
	 * Add the alphabet
	 * 
	 * @param alp
	 *            - the set of terminal symbols
	 */
	public void addAlphabet(String[] alp) {
		alphabet.clear();
		for (String a : alp) {
			alphabet.add(new TerminalSymbol(a));
		}
	}

	/**
	 * Set the starting symbol
	 * 
	 * @param startingSymbol
	 */
	public void setStartingSymbol(String startingSymbol) {
		this.startingSymbol = new NonterminalSymbol(startingSymbol);
	}

	/**
	 * Add a single production of form nonterm->prod
	 * 
	 * @param nonterm
	 *            - the nonterminal symbol
	 * @param prod
	 *            - the production
	 * @throws Exception 
	 */
	public void addProduction(String nonterm, String prod) throws Exception {
		if (!productions.containsKey(new NonterminalSymbol(nonterm))) {
			productions.put(new NonterminalSymbol(nonterm), new ArrayList<List<Symbol>>());
		}
		
		List<Symbol> productionRHS = new ArrayList<Symbol>();
		
		for ( String symbol : prod.split("\\s+") ) {
			
			symbol = symbol.trim();
			if ( !symbol.isEmpty() ) {
				
				if ( nonterminals.contains(new NonterminalSymbol(symbol)) ) {
					productionRHS.add(new NonterminalSymbol(symbol));
				} else if (alphabet.contains(new TerminalSymbol(symbol)) ) {
					productionRHS.add(new TerminalSymbol(symbol));
				} else if (EPSILON.equals(new Symbol(symbol)) ) {
					productionRHS.add(EPSILON);
				} else {
					throw new Exception("Unsupported Symbol : " + nonterm + "->" + symbol );
				}
			}
		}
		productions.get(new NonterminalSymbol(nonterm)).add(productionRHS);	
	}

	/**
	 * Add productions of form nonterm->prod|prod
	 * 
	 * @param prods
	 *            - array of productions
	 * @throws Exception 
	 */
	public void addProductions(String prod) throws Exception {
		productions.clear();
		String lhs = prod.split("->")[0].trim();
		String rhs = prod.split("->")[1].trim();

		for (String pr : rhs.split("\\|")) {
			addProduction(lhs, pr);
		}
	}

	/**
	 * Read from file of format first line: non terminal symbols, space
	 * separated second line: alphabet, space separated third line: production
	 * rules separated by spaces fourth line: the starting symbol
	 * 
	 * @param fileName
	 *            - the file to read from
	 */
	public void loadFromFile(String fileName) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String[] line = reader.readLine().split(" ");
		addNonterminals(line);

		line = reader.readLine().split(" ");
		addAlphabet(line);

		startingSymbol = new NonterminalSymbol( reader.readLine() );
		
		String completeLine;
		while ( (completeLine = reader.readLine()) != null ) {
			addProductions(completeLine);			
		}
		
		reader.close();

	}

}
