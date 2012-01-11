package grammar;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

public class NonContextFreeProductions {

	List<List<NonTerminalSymbol>> lhs = new LinkedList<List<NonTerminalSymbol>>();
	Map<Integer,List<List<Symbol>>> rhs = new TreeMap<Integer, List<List<Symbol>>>();
	Grammar g;

	public boolean noNonContextFreeProductions(){
		return lhs.isEmpty();
	}
	
	public NonContextFreeProductions(Grammar grammar) {
		g = grammar;
	}

	public void add(String line) throws DataFormatException {
		List<NonTerminalSymbol> leftHandSide = new LinkedList<NonTerminalSymbol>();

		String[] tokensByArrow = line.split("->");

		String[] lhss = tokensByArrow[0].trim().split("\\s+");
		for (String s : lhss){
			NonTerminalSymbol nts = new NonTerminalSymbol(s);
			if (!g.nonTerminalSymbols.contains(nts))
				throw new DataFormatException("Use existing or predefined symbols in productions!");
			leftHandSide.add(nts);
		}

		String[] tokens = tokensByArrow[1].split("\\|");

		List<List<Symbol>> l = new LinkedList<List<Symbol>>();
		for (String s : tokens){
			if (!(s.equals("") || s.equals(" ") || s.equals("|")))
			{
				String[] symbols = s.split("\\s+");
				List<Symbol> list = new LinkedList<Symbol>();
				for (String symbol : symbols){
					if (!(symbol.equals("") || symbol.equals(" ")))
					{
						NonTerminalSymbol nts = new NonTerminalSymbol(symbol);
						TerminalSymbol ts = new TerminalSymbol(symbol);
						boolean existent = false;
						if (g.nonTerminalSymbols.contains(nts)){
							list.add(nts);
							existent = true;
						}
						else
							if (g.terminalSymbols.contains(ts)){
								list.add(ts);
								existent = true;
							}
							else
								if (nts.equals(Grammar.epsilon)){
									list.add(nts);
									existent = true;
								}
						if (!existent)
							throw new DataFormatException("Use existing or predefined symbols in productions!");
					}
				}
				l.add(list);
			}
		}

		if (!lhs.contains(leftHandSide)){
			lhs.add(leftHandSide);
		}
		int index = lhs.indexOf(leftHandSide);
		if (rhs.get(index)==null){
			rhs.put(index, l);
		}
		else{
			rhs.get(index).addAll(l);
		}
	}

	public Collection<Production> toSet() {
		Set<Production> result = new TreeSet<Production>();
		
		for (List<NonTerminalSymbol> ntsl: lhs){
			String ntsName = "";
			for (NonTerminalSymbol nts : ntsl)
				ntsName += nts.getName() + " ";
			NonTerminalSymbol newLhs = new NonTerminalSymbol(ntsName.substring(0, ntsName.length()-1));
			for (List<Symbol> sl : rhs.get(lhs.indexOf(ntsl))){
				result.add(new Production(newLhs,sl));
			}
		}
		return result;
	}

}
