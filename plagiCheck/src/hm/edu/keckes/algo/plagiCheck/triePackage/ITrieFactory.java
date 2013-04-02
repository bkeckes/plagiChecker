package hm.edu.keckes.algo.plagiCheck.triePackage;

public interface ITrieFactory<K extends Comparable> {
	public Object put(K key, Object v);
	public Object get(K key);
	public boolean hasKey(K key);
}
