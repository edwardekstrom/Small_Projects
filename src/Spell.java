import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Scanner;


public class Spell implements SpellCorrector{
	
	public static void main(String[] args){
		String dictionaryPath = "/home/edwardekstrom/workspace/ImageEditor/src/words.txt";
		String theWord = "hello";
		if (args.length > 2){
			System.out.println("too many args");
			return;
		}
		if(args.length == 2){
			dictionaryPath = args[0];
			theWord = args[1];
		}
		Spell spellCorrector = new Spell();
		try{
			spellCorrector.useDictionary(dictionaryPath);
			String similarWrod = spellCorrector.suggestSimilarWord("hell");
			System.out.println(similarWrod);
		}catch(IOException e){
			e.printStackTrace();
			return;
		}catch(NoSimilarWordFoundException e){
			e.printStackTrace();
			return;
		}
	}
	
	private Trie240 _trie;
	public Spell(){
		_trie = new Trie240();
	}

	@Override
	public void useDictionary(String dictionaryFileName) throws IOException {
		File dictionary = new File(dictionaryFileName);
		Scanner s = new Scanner(dictionary);
		while(s.hasNext()){
			_trie.add(s.next());
		}
	}

	@Override
	public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
		Trie240.Node result = _trie.find(inputWord);
		if(result != null){
			return result.toString();
		}else{
			throw new NoSimilarWordFoundException();
		}
	}

}
