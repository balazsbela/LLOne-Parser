package grammar_old;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FiniteAutomata {

	/**
	 * The set of states
	 */
	private List<String> states = new ArrayList<String>();

	/**
	 * @return - the set of states
	 */
	public List<String> getStates() {
		return states;
	}

	/**
	 * The alphabet
	 */
	private List<String> alphabet = new ArrayList<String>();

	/**
	 * The transition function
	 */
	private Map<Pair, List<String>> transitions = new HashMap<Pair, List<String>>();

	/**
	 * The initial state
	 */
	private String initialState;

	/**
	 * The Set of final states
	 */
	private List<String> finalStates = new ArrayList<String>();

	public FiniteAutomata() {

	}

	public String getInitialState() {
		return initialState;
	}

	public void setInitialState(String initialState) {
		this.initialState = initialState;
	}

	public List<String> getAlphabet() {
		return alphabet;
	}

	public Map<Pair, List<String>> getTransitions() {
		return transitions;
	}

	public List<String> getTransition(Pair pair) {
		return transitions.get(pair);
	}

	public List<String> getFinalStates() {
		return finalStates;
	}

	public void addStates(String[] state) {
		states.clear();
		states.addAll(Arrays.asList(state));
	}

	public void addAlphabet(String[] alp) {
		alphabet.clear();
		alphabet.addAll(Arrays.asList(alp));
	}

	public void addFinalStates(String[] finals) {
		finalStates.clear();
		finalStates.addAll(Arrays.asList(finals));
	}

	/*
	 * Adds a single transition of the form (state,symbol)={nextState}
	 */
	public void addTransition(Pair pair, String nextState) {
		if (transitions.containsKey(pair)) {
			transitions.get(pair).add(nextState);
		} else {
			transitions.put(pair, new ArrayList<String>());
			transitions.get(pair).add(nextState);
		}
	}

	/**
	 * Adds a list of transitions of the form
	 * (state,symbol)={nextState,nextState2,...}
	 * 
	 * @param transitionList
	 * @throws Exception
	 */
	public void addTransitions(String[] transitionList) throws Exception {
		transitions.clear();
		for (String transition : transitionList) {

			String rhs = transition.split("=")[1];
			String lhs = transition.split("=")[0];

			// Cut the brackets
			rhs = rhs.substring(1, rhs.length() - 1);
			lhs = lhs.substring(1, lhs.length() - 1);

			// Split at the coma, to get the pair
			String state = lhs.split(",")[0];
			String symbol = lhs.split(",")[1];

			/*
			 * /* Verify if the symbol and state belong to the alphabet /* and
			 * the set of states correspondingly
			 */
			if (!states.contains(state) || !states.contains(rhs)) {
				throw new Exception("Invalid state in transition!");
			}
			if (!alphabet.contains(symbol)) {
				throw new Exception("Invalid symbol in alphabet!");
			}

			/*
			 * The rhs is split by "," and every transition is added
			 * individually
			 */
			for (String states : rhs.split(",")) {
				addTransition(new Pair(state, symbol), states);
			}
		}
	}

	/**
	 * Load the file in the following format first line: set of states, space
	 * separated second line: alphabet, space separated third line: transitions
	 * in (state,symbol)={nextState,nextState1...} format, space separated
	 * fourth line: initial state fifth line: set of final states
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void loadFromFile(String fileName) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));

		String[] line = reader.readLine().split(" ");
		addStates(line);

		line = reader.readLine().split(" ");
		addAlphabet(line);

		line = reader.readLine().split(" ");
		addTransitions(line);

		initialState = reader.readLine();

		line = reader.readLine().split(" ");
		addFinalStates(line);

		reader.close();
	}

	
	/*
	 * Get the corresponding grammar to the finite automata
	 */
    public Grammar toGrammar()  {
        Grammar grammar = new Grammar();
        
        //Nonterminals of the grammar are the same as the states
        grammar.addNonterminals(states.toArray(new String[0]));
        //Alphabet also stays the same
        grammar.addAlphabet(alphabet.toArray(new String[0]));
        
        /* Transitions are converted by the following rules
        * For every (q,a)=p add q->ap
        * If p is final state add q->a as well
        */
        for (Pair pair : transitions.keySet()) {
            for (String transition : transitions.get(pair)) {
                grammar.addProduction(pair.getState(), pair.getSymbol()+ transition);
                if ( finalStates.contains(transition) ) {
                    grammar.addProduction(pair.getState(), pair.getSymbol());
                }
            }
        }        
        grammar.setStartingSymbol(initialState);
        
        if(finalStates.contains(initialState)) {
        	grammar.addProduction(initialState, "E");
        }
        return grammar;
    }
}
