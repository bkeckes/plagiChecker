package hm.edu.algo.plagiCheck.lexer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import hm.edu.algo.plagiCheck.kAux.CharIterator;
import hm.edu.algo.plagiCheck.kAux.IActionAtInsert;
import hm.edu.algo.plagiCheck.kAux.IIndexReference;
import hm.edu.algo.plagiCheck.kAux.IndexReference;
import hm.edu.algo.plagiCheck.kAux.StringCoding;
import hm.edu.algo.plagiCheck.kAux.StringCounting;
import hm.edu.algo.plagiCheck.lexer.ILexer.LexerState;
import hm.edu.algo.plagiCheck.logging.Log;
import hm.edu.algo.plagiCheck.triePackage.ITrie;
import hm.edu.algo.plagiCheck.triePackage.ITrieReference;
import hm.edu.algo.plagiCheck.triePackage.Trie;
import hm.edu.algo.plagiCheck.triePackage.TrieReference;

/**
 * @author Benjamin Keckes
 * 
 */
public class BaseLexer implements ILexer{

	private ITrie idTrie = new Trie<String>(new StringCoding(), new StringCounting());
	private ITrie intTrie = new Trie<Integer>(new StringCoding(), new StringCounting());
	private ITrie dateTrie = new Trie<Integer>(new StringCoding(), new StringCounting());
	
	BufferedReader bfr;
	
	LexerState state;
	boolean isEOF;
	int overlap=0;
	
//	public BaseLexer(FileReader fr){
//
//		this.bfr = new BufferedReader(fr);
//		isEOF=false;
//	}
	public BaseLexer(){
		
	}
	public void setFile(FileReader fr){
		this.bfr = new BufferedReader(fr);
		isEOF=false;
		overlap=0;
		//read();
		
	}
	
	public ArrayList<IIndexReference> read(){
		
		ArrayList<IIndexReference> index = new ArrayList<IIndexReference>();
		IToken token;
		do{
			token = getToken();
			ITrieReference ref=null;		
			
			
			//Hier kann der Token in einen Trie gestopft werden...
			
			//	ID
			if(token.getType().equals(LexerState.ID)){
				ref = (TrieReference)idTrie.insert(new CharIterator(token.getValue()));
			}
			else if(token.getType().equals(LexerState.INT)){
				ref = (TrieReference)intTrie.insert(new CharIterator(token.getValue()));
			}
			else if(token.getType().equals(LexerState.DATE)){
				ref = (TrieReference)dateTrie.insert(new CharIterator(token.getValue()));
			}
			
			if(ref!=null){
				IIndexReference indexRef = new IndexReference(token.getType(), (Integer)ref.getStringCode());
				index.add(indexRef);
			}
		}
		while(!isEOF());
		
		return index;
	}

	
	@Override
	public IToken getToken() {
		int chInt=0;
		LexerState oldState = LexerState.READY;
		String line ="";
		try {
			/*
			 * �berhang vom letzen Methodenaufruf.
			 */
			if(overlap!=0){
				state = readState(overlap);
				oldState = state;
				line = line+(char)overlap;
			}
				
			while((chInt = bfr.read()) != -1){
				
				state = readState(chInt);
				
				if(isStateChange(oldState)){
					overlap = chInt;
					return new Token(oldState, line);
				}
				else{
					line = line+(char)chInt;
					oldState = state;
				}
			}
			isEOF=true;
		} 
		catch (UnknownLexerState e){
			Log.println(Log.URGENT, "Zeichen "+(char)chInt+" konnte nicht zugewiesen werden");
			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		return new Token(state, line);
	}
	
	private LexerState readState(int val) throws UnknownLexerState{
		if(isLetter(val)){
			return LexerState.ID;
		}
		else if(isComma(val)){
			return LexerState.COMMA;
		}
		/*else if(isColon(val)){
			return LexerState.COLON;
		}*/
		else if(isWhiteSpace(val)){
			return LexerState.WHITESPACE;
		}
		else if(isNumber(val)){
			return LexerState.INT;
		}
		else if(isSpecialChar(val)){
			return LexerState.SPECIALCHAR;
		}
		
		throw new UnknownLexerState(val+" konnte keinem LexerState zugewiesen werden");

		
	}
	
	private boolean isNumber(int val){
		if(val>=48 && val<=57)
			return true;
		return false;
	}
	
	private boolean isStateChange(LexerState oldState){
		if(oldState == LexerState.READY)
			return false;
		if(oldState==LexerState.INT && state==LexerState.COMMA){
			state=LexerState.FLOAT;
			return false;
		}
		else if(oldState==LexerState.FLOAT && state==LexerState.INT){
			state=LexerState.FLOAT;
			return false;
		}
		else if(oldState==LexerState.FLOAT && state==LexerState.COMMA){
			state=LexerState.DATE;
			return false;
		}
		else if(oldState==LexerState.DATE && state==LexerState.INT){
			state=LexerState.DATE;
			return false;
		}
		if(oldState!=state)
			return true;
		else
			return false;
	}
	private boolean isLetter(int val){
		//Gro�buchstaben
		if(val>=65 && val <=90)
			return true;
		//Kleinbuchstaben
		if(val>=97 && val<=122)
			return true;
		//�, �... ohne Multiplikation(x)
		if(val>=1932 && val<=223 && val!=215)
			return true;
		//�, � usw... ohne Division (/)
		if(val>=224 && val<=255 && val!=247)
			return true;
		//
		return false;
	}
	
	private boolean isComma(int val){
		//Komma   oder Punkt
		if(val==44 || val==46)
			return true;
		return false;
	}
	private boolean isColon(int val){
		//Punkt
		if(val==46)
			return true;
		return false;
	}
	
	private boolean isWhiteSpace(int val){
		char chValue = (char)val;
		//WS, Zeilenumbruch und Tab
		if(chValue==' ' || chValue=='\n' || chValue=='\t' || val==13)
			return true;
		return false;
		
	}
	
	private boolean isSpecialChar(int val){
		if(val>=33 && val<=57)
			return true;
		if(val>=58 && val<=64)
			return true;
		if(val>=91 && val<=96)
			return true;
		if(val>=123 && val<=126)
			return true;
		if(val>=128 && val<=255)
			return true;
		if(val>=300)
			return true;
		return false;
	}


	@Override
	public boolean isEOF() {
		return isEOF;
	}


	@Override
	public String decode(IIndexReference ref) {
		
		switch(ref.getClassCode()){
			case ID: 	return (String)idTrie.get(ref.getStringCode());
			case INT: 	return (String)intTrie.get(ref.getStringCode());
			case DATE:	return (String)dateTrie.get(ref.getStringCode());
			default: throw new IllegalArgumentException();
		}
	
	}

	@Override
	public String showTrie(LexerState state) {
		if(state.equals(LexerState.ID))
			return idTrie.toString();
		return null;
	}

	public IIndexReference insertWordInTrie(LexerState state, String word){
		
		ITrieReference ref=null;	
		
		switch(state){
			case ID: 	ref = (ITrieReference)idTrie.insert(new CharIterator(word));
				break;
			case INT: 	ref = (ITrieReference)intTrie.insert(new CharIterator(word));
				break;
			case DATE:	ref = (ITrieReference)dateTrie.insert(new CharIterator(word));
				break;
			default: throw new IllegalArgumentException();
		}
		return new IndexReference(state, (Integer)ref.getStringCode());
		
	
	}
}
