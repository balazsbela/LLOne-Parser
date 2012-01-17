package grammar;

import java.util.List;

public class Production {

	private NonterminalSymbol nonterminal;
	private List<Symbol> production;
	
	public Production(NonterminalSymbol nonterm, List<Symbol> prod) {
		nonterminal = nonterm;
		production = prod;
	}
	
	@Override
	public boolean equals(Object obj) {
		Production o = (Production)obj;
		if ( o.nonterminal.equals(this.nonterminal) && production.size()==o.production.size() ) {
			for (int i=0 ; i<production.size() ; i++) {
				if ( !production.get(i).equals(o.production.get(i)) ) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
}
