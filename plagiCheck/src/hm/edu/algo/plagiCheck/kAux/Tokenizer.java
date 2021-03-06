package hm.edu.algo.plagiCheck.kAux;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import hm.edu.algo.plagiCheck.logging.Log;
import hm.edu.algo.plagiCheck.triePackage.ITrie;

public class Tokenizer implements ITokenizer{
	private String fileName;
	private ITrie trie;
	private IActionAtInsert action;
	
	public Tokenizer(String file, ITrie trie, IActionAtInsert action){
		this.fileName = file;
		this.trie = trie;
		this.action = action;
	}
	@Override
	public void start() {
		
		FileReader fr;
		try {
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			Log.println(Log.URGENT, "Datei: "+fileName+" geoeffnet");
			
			String zeile = "";

		    while( (zeile = br.readLine()) != null ) {
		    	
		    	//TODO: Hier muss der Kram erledigt werden
		    	
		    	
		    	
		    	//..
		    	String[] zeileSplit = zeile.split(" ");
		    	for(String wort: zeileSplit) {
		    		wort = Pattern.compile("\\p{Punct}").matcher(wort).replaceAll("");
		    		
		    		trie.put(wort, action);
		    	}
		    }

		    br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.println(Log.URGENT, "Datei "+fileName+" konnte nicht gefunden werden");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	
	public void setTrieAndAction(ITrie trie, IActionAtInsert action) {
		this.trie = trie;
		this.action = action;
	}


	public void setFile(String file) {
		this.fileName=file;
	}
}
