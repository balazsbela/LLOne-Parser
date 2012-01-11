package grammar_ref;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

/*
 * 	3 - eliminate epsilon-productions
 *	4 - eliminate single productions
 */

/**
 * 	1. Reads a grammar (from keyboard and from file)
	2. Displays the elements of a grammar, using a menu: set of non-terminals, set of terminals,  
	set of productions, the productions of a given non-terminal symbol
	3. Verifies if the grammar is regular
	7. Given a finite automaton constructs the corresponding regular grammar.
 */
public class Grammar {

	@Override
	public String toString() {
		return "Grammar no. "+instanceNo;
	}

	public LinkedList<TerminalSymbol> getTerminalsAsList(){
		return new LinkedList<TerminalSymbol>(terminalSymbols);
	}

	public LinkedList<NonTerminalSymbol> getNonTerminalsAsList(){
		return new LinkedList<NonTerminalSymbol>(nonTerminalSymbols);
	}
	
	public LinkedList<Production> getProductionsAsList(){
		return new LinkedList<Production>(productions.toSet());
	}
	
	Set<TerminalSymbol> terminalSymbols;
	Set<NonTerminalSymbol> nonTerminalSymbols;
	private Productions productions;
	private NonTerminalSymbol startingSymbol;

	public NonTerminalSymbol getStartingSymbol() {
		return startingSymbol;
	}

	private static int counter = 0;
	private int instanceNo;

	static public NonTerminalSymbol epsilon = new NonTerminalSymbol("E");

	public Grammar(){
		terminalSymbols = new TreeSet<TerminalSymbol>();
		nonTerminalSymbols = new TreeSet<NonTerminalSymbol>();
		productions = new Productions(this);
		instanceNo = counter++;
	}

	public Iterator<Production> getProductions(){
		return productions.toSet().iterator();
	}

	public Iterator<NonTerminalSymbol> getNonTerminals(){
		return nonTerminalSymbols.iterator();
	}

	public Iterator<TerminalSymbol> getTerminals(){
		return terminalSymbols.iterator();
	}

	public boolean isRegular(){
		if (!isContextFree())
			return false;
		boolean startingToEpsilon = false;
		Iterator<Production> productionsOfStartingSymbol = productions.getProductionsOf(startingSymbol);
		while (productionsOfStartingSymbol.hasNext()){
			Production p = productionsOfStartingSymbol.next();
			if (p.getRhs().contains(epsilon)){
				startingToEpsilon = true;
				break;
			}
		}
		for (NonTerminalSymbol nts : nonTerminalSymbols){
			if ( productions.productions.get(nts)!=null)
				for(List<Symbol> list : productions.productions.get(nts)){
					if (!(list.size()==1 || list.size()==2)){
						return false;
					}
					if (list.size()==1){
						if (!nts.equals(startingSymbol)){
							if (!(list.get(0) instanceof TerminalSymbol)){
								return false;
							}
						}
						else{
							if (!(list.get(0) instanceof TerminalSymbol || list.get(0).equals(epsilon))){
								return false;
							}
						}
					}
					if (list.size()==2){
						if (!(list.get(0) instanceof TerminalSymbol && list.get(1) instanceof NonTerminalSymbol)){
							return false;
						}
						if (list.get(1).equals(epsilon) && !nts.equals(startingSymbol)){
							return false;
						}
						if (startingToEpsilon && list.contains(startingSymbol)){
							return false;
						}
					}
				}
		}
		return true;
	}

	/*
	 * Reads a Grammar from a file.
	 * Required file structure: 
	 * - first line: non-terminal symbols separated by spaces
	 * - second line: terminal symbols separated by spaces
	 * - third line: starting symbol
	 * - forth line to end of file: productions, each on a separate line
	 * 
	 * @param	fileName name of the file to be read
	 * @return	the resulting Grammar
	 */
	public static Grammar readFromFile(String fileName) throws IOException, DataFormatException{
		Grammar g = new Grammar();

		BufferedReader br;
		br = new BufferedReader(new FileReader(fileName));
		String line;
		String[] tokens;
		//non-terminal symbols
		line = br.readLine().trim();
		tokens = line.split("\\s+");
		for (String s : tokens){
			g.nonTerminalSymbols.add(new NonTerminalSymbol(s));
		}
		//terminal symbols
		line = br.readLine().trim();
		tokens = line.split("\\s+");
		for (String s : tokens){
			g.terminalSymbols.add(new TerminalSymbol(s));
		}
		//starting symbol
		line = br.readLine().trim();
		g.startingSymbol = new NonTerminalSymbol(line);
		g.addStartingSymbol(g.startingSymbol);
		while ( (line = br.readLine())!=null ){
			g.productions.add(line.trim());
		}
		br.close();
		return g;
	}

	public void addStartingSymbol(NonTerminalSymbol s) throws DataFormatException {
		if (!nonTerminalSymbols.contains(s)){
			throw new DataFormatException("Starting symbol must be an existent non-terminal symbol!"); 
		}
		nonTerminalSymbols.remove(s);
		s.setAsStartingSymbol();
		startingSymbol = s;
		nonTerminalSymbols.add(s);
	}

	public void addTerminalSymbol(String ts){
		terminalSymbols.add(new TerminalSymbol(ts));
	}

	public void addNonTerminalSymbol(String nts){
		nonTerminalSymbols.add(new NonTerminalSymbol(nts));
	}

	public void addProduction(String p) throws DataFormatException{
		productions.add(p);
	}

	public Iterator<Production> getProductionsOf(
			NonTerminalSymbol nonTerminalSymbol) {
		return productions.getProductionsOf(nonTerminalSymbol);
	}


	/**
	 * Eliminates epsilon productions from the Grammar.
	 * 
	 * @throws DataFormatException
	 */
	public void eliminateEpsilonProductions() throws DataFormatException {
		if (!isContextFree())
			throw new DataFormatException("Grammar is not context-free!");
		Set<NonTerminalSymbol> n = new TreeSet<NonTerminalSymbol>();
		Set<NonTerminalSymbol> difference = new TreeSet<NonTerminalSymbol>(nonTerminalSymbols);
		for (NonTerminalSymbol nts : nonTerminalSymbols){
			Iterator<Production> i = getProductionsOf(nts);
			while (i.hasNext()){
				List<Symbol> l = i.next().getRhs();
				if (l.size()==1 && l.get(0).equals(epsilon)){
					n.add(nts);
					difference.remove(nts);
				}
			}
		}
		int initial = 0;
		int size = n.size();
		while (initial!=size){
			List<NonTerminalSymbol> newList = new LinkedList<NonTerminalSymbol>();
			for (NonTerminalSymbol nts : difference){
				Iterator<Production> i = getProductionsOf(nts);
				while (i.hasNext()){
					//adding if every symbol in the RHS belongs to n-i
					Production p = i.next();
					List<NonTerminalSymbol> trouble = new LinkedList<NonTerminalSymbol>();
					for (Symbol s : p.getRhs()){
						if (s instanceof NonTerminalSymbol){
							if (n.contains(s)){
								trouble.add((NonTerminalSymbol) s);
								newList.add((NonTerminalSymbol) s);
							}
						}
					}
					if (p.getRhs().size()==trouble.size()){
						n.add(nts);
					}
				}
			}
			difference.removeAll(newList);
			initial = size;
			size = n.size();
		}
		// N bar is n
		//step 2
		Productions pPrime = new Productions(this);
		for (NonTerminalSymbol a : nonTerminalSymbols){
			Iterator<Production> i = getProductionsOf(a);
			while (i.hasNext()){
				List<Symbol> l = i.next().getRhs();
				if (l.size()==1 && l.get(0).equals(epsilon)) continue;
				List<Symbol> trouble = new LinkedList<Symbol>();
				for (Symbol b : l)
					if (b instanceof NonTerminalSymbol && n.contains(b)){
						trouble.add(b);
					}
				if (trouble.size()!=0){
					for (int j=0;j<Math.pow(2, trouble.size());j++){
						/**
						 * 
						 */
						char[] binaryWithZeroes = new char[trouble.size()];
						{
							char[] binary = Integer.toBinaryString(j).toCharArray();
							int zeros = trouble.size()-binary.length;
							for (int k=0;k<zeros;k++)
								binaryWithZeroes[k]='0';
							int index = 0;
							for (int k=zeros;k<trouble.size();k++){
								binaryWithZeroes[k]=binary[index++];
							}

						}
						List<Symbol> list = new LinkedList<Symbol>();
						int index = 0;
						for (Symbol s : l){
							if (trouble.contains(s) ){
								if (binaryWithZeroes[index++]=='1'){
									list.add(s);
								}

							}
							else{
								list.add(s);
							}
						}
						if (!list.isEmpty())
							pPrime.addProduction(a, list);
					}
				}
				else{
					if (!l.isEmpty())
						pPrime.addProduction(a, l);
				}
			}
		}
		productions = pPrime;
		if (n.contains(startingSymbol)){
			NonTerminalSymbol sPrime = new NonTerminalSymbol("S'");
			nonTerminalSymbols.add(sPrime);
			NonTerminalSymbol s = startingSymbol;
			startingSymbol = sPrime;
			productions.add("S' -> "+s.getName()+" | E");
		}

	}

	/**
	 * The Grammar is context-free by construction.
	 * 
	 * @return true
	 */
	public boolean isContextFree(){
		return productions.nonContextFreeProductions.noNonContextFreeProductions();
	}

	/**
	 * Eliminates single productions from the Grammar.
	 * 
	 * @throws DataFormatException
	 */
	public void eliminateSingleProductions() throws DataFormatException{

		eliminateEpsilonProductions();

		Map<NonTerminalSymbol, Set<NonTerminalSymbol>> singleProductionsOfNts = new TreeMap<NonTerminalSymbol, Set<NonTerminalSymbol>>();

		for (NonTerminalSymbol nts : nonTerminalSymbols){
			Set<NonTerminalSymbol> symbols = new TreeSet<NonTerminalSymbol>();
			symbols.add(nts);

			int init = 0;
			int size = symbols.size();

			while (init!=size){
				for (NonTerminalSymbol b : symbols){
					Iterator<Production> p = getProductionsOf(b);
					while (p.hasNext()){
						List<Symbol> l = p.next().getRhs();
						if (l.size()==1 && l.get(0) instanceof NonTerminalSymbol)
							symbols.add((NonTerminalSymbol) l.get(0));
					}
				}

				init = size;
				size = symbols.size();
			}

			singleProductionsOfNts.put(nts,symbols);
		}

		Productions newProductions = new Productions(this);

		for (NonTerminalSymbol a : nonTerminalSymbols){
			for (NonTerminalSymbol b : singleProductionsOfNts.get(a)){
				Iterator<Production> p = getProductionsOf(b);
				while (p.hasNext()){
					List<Symbol> l = p.next().getRhs();
					if (!(l.size()==1 && l.get(0) instanceof NonTerminalSymbol)){
						newProductions.addProduction(a, l);
					}
				}
			}
		}

		productions = newProductions;
	}

}
