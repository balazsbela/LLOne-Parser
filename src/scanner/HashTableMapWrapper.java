package scanner;

public class HashTableMapWrapper<V> implements SymbolTable<V> {
	HashTableWithChaining<V,Integer> map = new HashTableWithChaining<V, Integer>(); 
	
	@Override
	public Integer put(V symbol) {
		map.add(symbol, map.getSize());
		return map.getSize()-1;
	}

	@Override
	public Integer get(V symbol) {		
		return map.get(symbol);
	}

	@Override
	public Integer remove(V symbol) {
		Integer val = map.get(symbol);
		return val;
	}

	@Override
	public V getSymbol(Integer pos) {
		return map.getKey(pos);		
	}

	@Override
	public int size() {
		return map.getSize();
	}

}
