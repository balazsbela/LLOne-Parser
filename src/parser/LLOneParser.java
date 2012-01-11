package parser;

import grammar.Grammar;
import grammar.NonTerminalSymbol;
import grammar.Symbol;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LLOneParser {
	/** The grammar. */
	private Grammar grammar;
	
	/** The 'first' construction. */
	private Map<Symbol, Set<Symbol>> first;
	
	/** The 'follow' construction. */
	private Map<Symbol, List<Symbol>> follow;
}
