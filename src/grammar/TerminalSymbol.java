package grammar;

@SuppressWarnings("unchecked")
public class TerminalSymbol implements Comparable,Symbol {

	@Override
	public String toString() {
		return name;
	}

	private String name;
	
	public TerminalSymbol(String s) {
		name = s;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return ((obj instanceof TerminalSymbol) && (name.equals(((TerminalSymbol) obj).getName())));
	}

	@Override
	public int compareTo(Object o) {
		return (o instanceof TerminalSymbol) ? (name.compareTo(((TerminalSymbol) o).getName())) : -1  ;
	}

	public String getName() {
		return name;
	}

}
