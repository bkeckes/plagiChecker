package hm.edu.algo.plagiCheck.kAux;

import hm.edu.algo.plagiCheck.triePackage.ITrie;

public interface ITokenizer {
	
	public enum TokenTypes{
		ID, DATE, WHITESPACE, KOMMA, PERIOD;
	}
	public void setFile(String file);
	public void setTrieAndAction(ITrie trie, IActionAtInsert action);
	public void start();
}