package scanner;

public interface SymbolTable<V> {
	//Key Symbol, Value Integer
	// Inserts into symbol table and returns the position of the newly inserted symbol	
	Integer put(V symbol);
	//Returns the position of a symbol
	Integer get(V symbol);
	//Remove an entry 
	Integer remove(V symbol);
	//Get the value at a certain position
	V getSymbol(Integer pos);
    int size();	
}
