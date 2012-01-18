package scanner;

import java.util.ArrayList;
import java.util.List;

//Hash table where T is the key, Strings or other objects are mapped into integer values(their indices)
public class HashTableWithChaining<K, V> {

	public class HashEntry<K, V> {
		K key;
		V value;

		public HashEntry(K k, V v) {
			this.key = k;
			this.value = v;
		}

		public boolean equals(Object o) {
			if (o instanceof HashEntry) {
				HashEntry h = (HashEntry) o;
				return h.key == key;
			}
			return false;
		}
	}

	// The hashtable is implemented as an array of
	// Lists ,and data collision will be solved by
	// chaining

	private List<HashEntry<K, V>>[] table;
	private static int tsize = 661;
	private int nrElements = 0;

	// creates a hash table with a fixed size
	public HashTableWithChaining() {

		table = new List[661];

		for (int i = 0; i < table.length; i++) {
			table[i] = new ArrayList<HashEntry<K, V>>();
		}

	}

	// hash function which returns a valid position
	// to which an object will be inserted
	private int hash(K value) {
		int sum = 0;
		if (value instanceof String) {
			String val = (String) value;
			for (char c : val.toCharArray()) {
				sum += (int) c;
			}
		} else {
			return value.hashCode() % tsize;
		}
		return sum % tsize;		
	}

	// we add a new element, we hash it to get the position and put it into the
	// list
	// at that position, if it's not already contained, otherwise it's updated with the new value
	public void add(K key, V value) {
		int pos = hash(key);
		HashEntry hashEntry = new HashEntry<K, V>(key, value);

		if (!table[pos].contains(hashEntry)) {
			table[pos].add(0, hashEntry);
			nrElements++;
		} else {
			table[pos].set(table[pos].indexOf(hashEntry),hashEntry);
		}
	}

	// return all the elements on a given key.
	public V get(K key) {
		for (HashEntry<K, V> he : table[hash(key)]) {
			if (he.key.equals(key)) {
				return he.value;
			}
		}
		return null;
	}

	public int getSize() {
		return nrElements;
	}

	public void remove(K key) {
		for (HashEntry<K, V> he : table[hash(key)]) {
			if (he.key.equals(key)) {
				// remove the hashentry with that key, the value doesn't matter
				// since equals is defined on the key.
				table[hash(key)].remove(new HashEntry<K, V>(key, null));
				nrElements--;
				return;
			}
		}
	}

	public K getKey(V value) {
		for (int i = 0; i < tsize; i++) {
			if (table[i] != null) {
				for (HashEntry<K, V> he : table[i]) {
					if (he.value.equals(value)) {
						return he.key;
					}
				}
			}
		}
		return null;		
	}

}
