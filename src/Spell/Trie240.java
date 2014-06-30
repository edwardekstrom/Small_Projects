package Spell;
public class Trie240 implements Spell.Trie{
	
	public static void main(String[] args){
		Trie240 t = new Trie240();
		t.add("hello");
		t.add("hells");
		System.out.println(t.getWordCount());
		System.out.println(t.getNodeCount());
		System.out.println(t.find("hell"));
		System.out.println(t.find(""));
		System.out.println(t.find("hells"));
		System.out.println(t.find("hello"));
	}
	private Node _root;
	private int _totalWords;
	private int _totalNodes;
	public Trie240(){
		_root = new Node();
		_totalWords = 0;
		_totalNodes = 1;
	}

	@Override
	public void add(String word) {
		word = word.toLowerCase();
		Node cur = _root;
		for(int i = 0; i < word.length(); i++){
			if(cur._children[(int)(word.charAt(i)) - 97] == null){
				cur._children[(int)(word.charAt(i)) - 97] = new Node();
				cur._children[(int)(word.charAt(i)) - 97]._string = cur._string + word.charAt(i);
				_totalNodes++;
			}
			cur = cur._children[(int)(word.charAt(i)) - 97];
		}
		if(cur._wordCount == 0) _totalWords++;
		cur._wordCount++;
	}

	@Override
	public Node find(String word) {
		word = word.toLowerCase();
		Node cur = _root;
		for(int i = 0; i < word.length(); i++){
			if(cur._children[(int)(word.charAt(i)) - 97] == null){
				return null;
			}
			cur = cur._children[(int)(word.charAt(i)) - 97];
		}
		if(cur._wordCount > 0){
			return cur;
		}else{
			return null;
		}
	}

	@Override
	public int getWordCount() {
		return _totalWords;
	}

	@Override
	public int getNodeCount() {
		return _totalNodes;
	}
	
	public class Node implements Trie.Node{
		public int _wordCount;
		public Node[] _children;
		public String _string;
		
		public Node(){
			_wordCount = 0;
			_children = new Node[26];
			_string = "";
		}
		
		@Override
		public int getValue() {
			return _wordCount;
		}
		
		@Override
		public String toString(){
			return _string;
		}

	}


}

