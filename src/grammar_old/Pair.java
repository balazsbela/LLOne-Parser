package grammar_old;

public class Pair {
	private String state;
	private String symbol;

	public Pair(String state, String symbol) {
		this.state = state;
		this.symbol = symbol;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	
}
