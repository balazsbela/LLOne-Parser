package grammar;

import java.util.List;

@SuppressWarnings("unchecked")
public class Production implements Comparable{

	private NonTerminalSymbol lhs;
	private List<Symbol> rhs;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i 	 = 0;
		int size = rhs.size();
		for (Symbol s : rhs){
			sb.append(s);
			i++;
			if ( i != size){
				sb.append(" ");
			}
		}
		return lhs + "->" + sb;
	}

	public Production(NonTerminalSymbol p, List<Symbol> list) {
		lhs = p;
		rhs = list;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Production)) return false;
		if (!(lhs.equals(((Production) obj).getLhs()))) return false;
		if (!(new Integer(rhs.size()).equals(((Production) obj).getRhs().size()))) return false;
		for (int i=0;i<rhs.size();i++){
			if ( ! rhs.get(i).equals(((Production) obj).getRhs().get(i))) return false;
		}
		return true;
	}

	@Override
	public int compareTo(Object o) {
		if (this.equals(o)){
			return 0;
		}
		if (lhs.compareTo(((Production) o).getLhs())==0){
			return -1;
		}
		return (o instanceof Production) ? (lhs.compareTo(((Production) o).getLhs())) : -1;
	}

	public NonTerminalSymbol getLhs() {
		return lhs;
	}

	public List<Symbol> getRhs() {
		return rhs;
	}
}
