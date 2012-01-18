package parser;

import grammar.Production;
import grammar.Symbol;

import java.util.List;

public interface SyntaxTree {

	public void add(Production p) throws Exception;
	public List<Symbol> getSymbolLeaves();
}
