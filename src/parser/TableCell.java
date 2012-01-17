package parser;

import grammar.Symbol;

import java.util.List;

public class TableCell {

	private String name;
	
	private List<Symbol> alpha;
	private int productionNr;
	
	public TableCell(String name) {
		this.name = name;
	}
	
	public TableCell(List<Symbol>  alpha ,int prodNr) {
		this.alpha = alpha;
		productionNr = prodNr;
	}
	
	public List<Symbol> getAlpha() {
		return alpha;
	}
	
	public int getProductionNr() {
		return productionNr;
	}
	
	public String getName() {
		return name;
	}
	
	public static TableCell pop = new TableCell("POP");
	public static TableCell accept = new TableCell("ACCEPT");
	public static TableCell error = new TableCell("ERROR");
	
	
}
