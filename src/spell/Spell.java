package spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Scanner;
import java.util.TreeSet;

public class Spell implements SpellCorrector {

    public static void main(String[] args) {
//        String dictionaryPath = System.getProperty("user.dir") + "\\src\\words.txt";
        String dictionaryPath = System.getProperty("user.dir") + "/src/words.txt";
        String theWord = "helloz";
        if (args.length > 2) {
            System.out.println("too many args");
            System.exit(1);
        }
        if (args.length == 2) {
            dictionaryPath = args[0];
            theWord = args[1].toLowerCase();
        }
        Spell spellCorrector = new Spell();
        try {
            spellCorrector.useDictionary(dictionaryPath);
            String similarWrod = spellCorrector.suggestSimilarWord(theWord);
            System.out.println(similarWrod);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NoSimilarWordFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

//        for (String word : spellCorrector.insertions(theWord)) {
//            System.out.println(word);
//        }
    }

    private Trie240 _trie;

    public Spell() {
        _trie = new Trie240();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File dictionary = new File(dictionaryFileName);
        Scanner s = new Scanner(dictionary);
        while (s.hasNext()) {
            _trie.add(s.next());
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException {
        Trie240.Node result = _trie.find(inputWord);
        if (result != null) {
            return result.toString();
        } else {
            ArrayList<String> oneDistance = deletions(inputWord);
            oneDistance.addAll(insertions(inputWord));
            oneDistance.addAll(alterations(inputWord));
            oneDistance.addAll(transpositions(inputWord));

            ArrayList<String> best = new ArrayList<String>();
            int b = -1;
            for (String word : oneDistance) {
                if (_trie.find(word) != null) {
                    if (_trie.find(word)._wordCount > b) {
                        b = _trie.find(word)._wordCount;
                        best = new ArrayList<String>();
                        best.add(_trie.find(word).toString());
                    } else if (_trie.find(word)._wordCount == b) {
                        best.add(_trie.find(word).toString());
                    }
                }
            }

            if (best.size() != 0) {
                Collections.sort(best);
                return best.get(0);
            } else {
                ArrayList<String> twoDistance = new ArrayList<String>();
                for (String word : oneDistance) {
                    twoDistance.addAll(deletions(word));
                    twoDistance.addAll(insertions(word));
                    twoDistance.addAll(alterations(word));
                    twoDistance.addAll(transpositions(word));
                }

                for (String word : twoDistance) {
                    if (_trie.find(word) != null) {
                        if (_trie.find(word)._wordCount > b) {
                            b = _trie.find(word)._wordCount;
                            best = new ArrayList<String>();
                            best.add(_trie.find(word).toString());
                        } else if (_trie.find(word)._wordCount == b) {
                            best.add(_trie.find(word).toString());
                        }
                    }
                }

                if (best.size() != 0) {
                    Collections.sort(best);
                    return best.get(0);
                } else {
                    throw new NoSimilarWordFoundException();
                }
            }
        }
    }

    private ArrayList<String> deletions(String word) {
        ArrayList<String> newWords = new ArrayList<String>();

        for (int i = 0; i < word.length(); i++) {
            newWords.add(word.substring(0, i) + word.substring(i + 1));
        }

        return newWords;
    }

    private ArrayList<String> insertions(String word) {
        ArrayList<String> newWords = new ArrayList<String>();

        for (int i = 0; i <= word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                newWords.add(word.substring(0, i) + (char) (j + 97) + word.substring(i));
            }
        }

        return newWords;
    }

    private ArrayList<String> alterations(String word) {
        ArrayList<String> newWords = new ArrayList<String>();

        for (int i = 0; i < word.length(); i++) {
            for (int j = 0; j < 26; j++) {
                newWords.add(word.substring(0, i) + (char) (j + 97) + word.substring(i + 1));
            }
        }

        return newWords;
    }

    private ArrayList<String> transpositions(String word) {
        ArrayList<String> newWords = new ArrayList<String>();

        for (int i = 0; i < word.length() - 1; i++) {
            newWords.add(word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2));
        }

        return newWords;
    }
}
