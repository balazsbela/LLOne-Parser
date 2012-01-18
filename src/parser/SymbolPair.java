package parser;

import grammar.Symbol;

public class SymbolPair {

	private Symbol symbol1;
	private Symbol symbol2;
	
	public SymbolPair(Symbol s1, Symbol s2) {
		symbol1 = s1;
		symbol2 = s2;
	}
	
	public Symbol getSymbol1() {
		return symbol1;
	}
	
	public Symbol getSymbol2() {
		return symbol2;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol1 == null) ? 0 : symbol1.hashCode());
		result = prime * result + ((symbol2 == null) ? 0 : symbol2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SymbolPair other = (SymbolPair) obj;
		if (symbol1 == null) {
			if (other.symbol1 != null)
				return false;
		} else if (!symbol1.equals(other.symbol1))
			return false;
		if (symbol2 == null) {
			if (other.symbol2 != null)
				return false;
		} else if (!symbol2.equals(other.symbol2))
			return false;
		return true;
	}
}
