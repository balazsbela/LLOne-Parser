package parser;

import grammar.*;

public class TestFirst {
	public static void main(String[] args) throws Exception {
		Grammar g = new Grammar();
		g.loadFromFile("grammar2.txt");
		
		LLOneParser parser = new LLOneParser(g);
		
		parser.printFirst(new Symbol("S"));
	}
}
