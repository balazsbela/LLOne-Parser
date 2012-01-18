package scanner;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class HashMapWrapper<V> implements SymbolTable<V> {

	private HashMap<V,Integer> map = new HashMap<V, Integer>();

	@Override
	public Integer put(V symbol) {
		map.put(symbol,map.size());
		return map.size()-1;
	}

	@Override
	public Integer get(V symbol) {		
		return map.get(symbol);
	}

	@Override
	public int size() {
		return map.size();
	}


	@Override
	public V getSymbol(Integer pos) {
		Set<Entry<V,Integer>> entrySet = map.entrySet();
		
		for(Entry<V,Integer> entry : entrySet) {
			if(entry.getValue() == pos) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public Integer remove(V symbol) {
		return map.remove(symbol);
	}		

	
}
