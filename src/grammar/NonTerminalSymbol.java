package grammar;

@SuppressWarnings("unchecked")
public class NonTerminalSymbol implements Comparable,Symbol {

	@Override
	public String toString() {
		return name;
	}

	private String name;
	private boolean isStartingSymbol = false;
	
	public NonTerminalSymbol(String s) {
		name = s;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NonTerminalSymbol){
		//	System.out.println("*"+name+"*"+((NonTerminalSymbol) obj).getName()+"*"+(name.equals(((NonTerminalSymbol) obj).getName())));
		}
		return obj instanceof NonTerminalSymbol && name.equals(((NonTerminalSymbol) obj).getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String getName() {
		return name;
	}

	public boolean isStartingSymbol() {
		return isStartingSymbol;
	}

	@Override
	public int compareTo(Object o) {
		return (o instanceof NonTerminalSymbol) ? (name.compareTo(((NonTerminalSymbol) o).getName())) : -1  ;
	}

	public void setAsStartingSymbol() {
		isStartingSymbol = true;
		
	}

}
