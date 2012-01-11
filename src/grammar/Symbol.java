package grammar;

public class Symbol {

	private String symbol;
	
	Symbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof String ) {
			return ((String)obj).equals(symbol);
		} else if ( obj instanceof Symbol ) {
			return ((Symbol)obj).symbol.equals(symbol);
		} else {
			return obj == this;
		}
	}
}
