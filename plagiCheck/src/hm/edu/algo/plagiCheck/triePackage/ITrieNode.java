package hm.edu.algo.plagiCheck.triePackage;

import hm.edu.algo.plagiCheck.kAux.IActionAtInsert;

import java.util.Iterator;

public interface ITrieNode {
	public ITrieReference recursiveInsert(Iterator it, IActionAtInsert codingAction, IActionAtInsert countingAction);
	public void showValues(int depth);
	public Object getValue();
	public String getStringReference(String text);
	
}
