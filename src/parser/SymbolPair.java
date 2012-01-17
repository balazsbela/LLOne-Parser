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
}
