package hangman;



import java.io.File;
import java.util.*;

/**
 * Created by edwardekstrom on 7/2/14.
 */
public class EvilHangman implements EvilHangmanGame{

    Set<String> _dictionary;
    public int _wordLength;
    public int _guessesLeft;
    Set<String> _usedLetters;
    String _currentWord;
    String _originalWord;


    public EvilHangman(){
        _dictionary = new TreeSet<String>();
        _wordLength = -1;
        _guessesLeft = Integer.MAX_VALUE;
        _usedLetters = new TreeSet<String>();
        _currentWord = "";
        _originalWord = "";
        _originalWord = _currentWord;
    }

    private static void printUsage(){
        System.out.println("Usage: java hangman.EvilHangman dictionary wordLength(>=2) guesses(>=1)\n");
    }


    @Override
    public void startGame(File dictionary, int wordLength) {
        Scanner dictionaryScanner = null;
        try{
            dictionaryScanner = new Scanner(dictionary);
        }catch(Exception e){
            e.printStackTrace();
        }
        _dictionary = new TreeSet<String>();
        _wordLength = wordLength;
        _usedLetters = new TreeSet<String>();
        for(int i = 0; i < wordLength; i++){
            _currentWord += "-";
        }
        _originalWord = _currentWord;
        while(dictionaryScanner.hasNext()){
            String cur = dictionaryScanner.next();
            _dictionary.add(cur.toLowerCase());
        }


    }

    private void startGame(){
        Scanner in = new Scanner(System.in);
        String guess;
        int correct = 0;
        while(_guessesLeft > 0){
            System.out.println("You have " + _guessesLeft + " guess" + (_guessesLeft > 1 ? "es" : "") + " left");
            System.out.print("Used letters: ");
            StringBuilder sb = new StringBuilder();
            for(String letter : _usedLetters){
                sb.append(letter + " ");
            }
            if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
            System.out.print(sb.toString());
            System.out.print("\n");
            System.out.println("Word: " + _currentWord);
            System.out.print("Enter guess: ");

            guess =  in.next().toLowerCase();
            if(guess.equals("quit")) System.exit(0);
            if(guess.length() != 1 || !Character.isLowerCase(guess.charAt(0))){
                System.out.println("Invalid input\n");
                continue;
            }
            try {
                _dictionary = makeGuess(guess.charAt(0));
            }catch(GuessAlreadyMadeException e){
                System.out.println("You already used that letter\n");
                continue;
            }
            correct += countMatches(_currentWord, guess.charAt(0));
            if(_currentWord.contains(guess)){
                if(correct == _originalWord.length()){
                    System.out.println("You won!");
                    System.out.println("Word: " + _currentWord);
                    return;
                }else{
                    int numMatches = countMatches(_currentWord,guess.charAt(0));
                    System.out.println("Yes, there " + (numMatches > 1 ? "are" : "is") + " " + numMatches + " " + guess + (numMatches > 1 ? "'s" : "") +"\n");
                }
            }else{
                _guessesLeft--;
                if(_guessesLeft != 0){
                    System.out.println("No, there are no " + guess + "'s\n");
                }
            }
        }
        System.out.println("You lost!");
        System.out.println("The word was: " + _dictionary.iterator().next());
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if(_usedLetters.contains("" + guess)){
            throw new GuessAlreadyMadeException();
        }
        _usedLetters.add("" + guess);
        Map<String, Set<String>> groups = new HashMap<String, Set<String>>();
        StringBuilder maskedString;
        for(String s : _dictionary){
            maskedString = new StringBuilder();
            for(int i = 0; i < s.length(); i++){
                if (s.charAt(i) == guess){
                    maskedString.append(s.charAt(i));
                }else{
                    maskedString.append(_currentWord.charAt(i));
                }
            }
            if(groups.containsKey(maskedString.toString())){
                groups.get(maskedString.toString()).add(s);
            }else{
                Set<String> nGroup = new TreeSet<String>();
                nGroup.add(s);
                groups.put(maskedString.toString(), nGroup);
            }
        }
        List<String> bestGroups = new LinkedList<String>();
        int bestSize = -1;
        for(String key : groups.keySet()){
            if(groups.get(key).size() > bestSize){
                bestSize = groups.get(key).size();
                bestGroups = new LinkedList<String>();
                bestGroups.add(key);
            }else if(groups.get(key).size() == bestSize){
                bestGroups.add(key);
            }
        }

        if(bestGroups.size() == 1){

            _currentWord = bestGroups.get(0);
            _dictionary = groups.get(bestGroups.get(0));
            printDictionary("size 1");
            return _dictionary;
        }
        if(bestGroups.contains(_originalWord)) {
            _currentWord = _originalWord;
            _dictionary = groups.get(_originalWord);
            printDictionary("original word");
            return _dictionary;
        }
        int fewest = Integer.MAX_VALUE;
        for(String s: bestGroups){
            if(countMatches(s,guess) < fewest){
                fewest = countMatches(s,guess);
            }
        }
        List<String> newBestGroups = new LinkedList<String>();
        for(String s: bestGroups){
            if(countMatches(s,guess) == fewest){
                newBestGroups.add(s);
            }
        }
        if(bestGroups.size() == 1){

            _currentWord = bestGroups.get(0);
            _dictionary = groups.get(bestGroups.get(0));
            printDictionary("fewest correct");
            return _dictionary;
        }

        bestGroups = newBestGroups;
        int rightmost = _originalWord.length();
        int curRightmost = -1;
        while(bestGroups.size() > 1){
            bestGroups = new LinkedList<String>();
            curRightmost = -1;
            for(String s : newBestGroups){
                if(s.lastIndexOf("" + guess, rightmost) > curRightmost){
                    curRightmost = s.lastIndexOf("" + guess, rightmost);
                }
            }
            for(String s : newBestGroups){
                if(s.lastIndexOf("" + guess, rightmost) == curRightmost){
                    bestGroups.add(s);
                }
            }
            newBestGroups = bestGroups;
            rightmost = curRightmost - 1;
        }
        _currentWord = bestGroups.get(0);

        _dictionary = groups.get(bestGroups.get(0));
        return _dictionary;
    }

    private void printDictionary(String whichOne){
//        for(String s: _dictionary){
//            System.out.println(s);
//        }
//        System.out.println(_dictionary.size());
//        System.out.println(whichOne);
    }

    private int countMatches(String s, char guess){
        int counter = 0;
        for(int i = 0; i<s.length(); i++){
            if(s.charAt(i) == guess) counter++;
        }
        return counter;
    }
    
    public static void main(String[] args){
        if(args.length != 3){
            printUsage();
            return;
        }
        try{
            String dictionaryPath = args[0];
            int wordLength = Integer.parseInt(args[1]);
            int guessesLeft = Integer.parseInt(args[2]);

            if(wordLength < 2){
                printUsage();
                return;
            }
            if(guessesLeft < 1){
                printUsage();
                return;
            }
            File f = new File(dictionaryPath);
            Scanner scanner = new Scanner(f);
            EvilHangman eh = new EvilHangman();
            eh._guessesLeft = guessesLeft;
            eh.startGame(f,wordLength);
            eh.startGame();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
    }
        
}
