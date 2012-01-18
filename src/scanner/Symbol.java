package scanner;

public class Symbol {
	private String name;
	private String type;
	private int lineOfDeclaration;
	private int lineOfDefinition;
	private String value;
	
	public Symbol(String name ) {
		this.name=name;
	}
	
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLineOfDeclaration() {
		return lineOfDeclaration;
	}

	public void setLineOfDeclaration(int lineOfDeclaration) {
		this.lineOfDeclaration = lineOfDeclaration;
	}

	public int getLineOfDefinition() {
		return lineOfDefinition;
	}

	public void setLineOfDefinition(int lineOfDefinition) {
		this.lineOfDefinition = lineOfDefinition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
