package grammar;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

public class Productions {

	static final String PRODUCTIONS_MUST_CONTAIN_ALREADY_EXISTENT_SYMBOLS = "Productions must contain already existent symbols!";
	Map<NonTerminalSymbol, List<List<Symbol>>> productions;
	NonContextFreeProductions nonContextFreeProductions;
	private Grammar g;

	public Productions(Grammar grammar){
		productions = new TreeMap<NonTerminalSymbol, List<List<Symbol>>>();
		nonContextFreeProductions = new NonContextFreeProductions(grammar);
		g = grammar;
	}

	public Set<Production> toSet() {
		Set<Production> result = new TreeSet<Production>();
		for (NonTerminalSymbol p : productions.keySet()){
			for (List<Symbol> l : productions.get(p)){
				Production prod = new Production(p,l); 
				result.add(prod);
			}
		}
		result.addAll(nonContextFreeProductions.toSet());
		return result;
	}

	public void add(String line) throws DataFormatException {
		String[] tokens;
		NonTerminalSymbol lhs;
		{
			String[] tokensByArrow = line.split("->");
			String[] lhss = tokensByArrow[0].trim().split("[ ]");
			if (lhss.length!=1){
				nonContextFreeProductions.add(line);
				return;
			}
			lhs = new NonTerminalSymbol(tokensByArrow[0].trim());
			tokens = tokensByArrow[1].split("\\|");
		}

		List<List<Symbol>> l = new LinkedList<List<Symbol>>();
		for (String s : tokens){
			if (!(s.equals("") || s.equals(" ") || s.equals("|")))
			{
				String[] symbols = s.split("\\s+");
				List<Symbol> list = new LinkedList<Symbol>();
				for (String symbol : symbols){
					String errorMessage = "Use existing or predefined symbols in productions: ";
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
						if (!existent){
							throw new DataFormatException(errorMessage+symbol);
						}
					}
				}

				addProduction(lhs, list);
				l.add(list);
			}
		}
	}

	public void addProduction(NonTerminalSymbol lhs, List<Symbol> list) {
		if (productions.get(lhs)==null){
			List<List<Symbol>> l = new LinkedList<List<Symbol>>();
			l.add(list);
			productions.put(lhs, l);
		}
		else{
			productions.get(lhs).add(list);
		}

	}

	public Iterator<Production> getProductionsOf(
			NonTerminalSymbol nonTerminalSymbol) {
		Set<Production> result = new TreeSet<Production>();
		if (!g.nonTerminalSymbols.contains(nonTerminalSymbol))
			throw new IllegalArgumentException("Non-existent non terminal symbol!");
		if (productions.get(nonTerminalSymbol)==null){
			return (new LinkedList<Production>()).iterator();
		}
		for (List<Symbol> l : productions.get(nonTerminalSymbol)){
			result.add(new Production(nonTerminalSymbol,l));
		}
		return result.iterator();
	}



}
