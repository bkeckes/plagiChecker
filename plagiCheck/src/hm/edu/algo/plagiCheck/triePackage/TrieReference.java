package hm.edu.algo.plagiCheck.triePackage;

public class TrieReference implements ITrieReference{

	// Attribute
	private final Object stringCoding;
	private final Object stringCounting;
	
	private int depth;
	
	private ITrieNode node;
	
	private final boolean found;
	
	@Override
	public boolean getFound() {
		return found;
	}

	@Override
	public Object getStringCode() {
		return this.stringCoding;
	}
	
	@Override
	public Object getPosition() {
		return this.stringCounting;
	}

	TrieReference (ITrieNode node, Object coding, Object counting, int depth, boolean found){
		this.node = node;
		
		this.stringCoding = coding;
		this.stringCounting=counting;
		this.depth = depth;
		this.found = found;

	}

	@Override
	public void incrementDepth() {
		this.depth++;
	}

	@Override
	public int getDepth() {
		return this.depth;
	}

	@Override
	public ITrieNode getNode() {
		return node;
	}
}
