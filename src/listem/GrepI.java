/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listem;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 *
 * @author Edward Ekstrom
 */
public class GrepI extends FileSearcher implements Grep {

    public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) {
        Pattern sPat = Pattern.compile(".*" + substringSelectionPattern + ".*");
        List<File> files = search(directory, fileSelectionPattern, recursive);
        Map<File, List<String>> m = new TreeMap<File, List<String>>();
        for (File f : files) {
            List<String> l = findSubstringMatches(f, sPat);
            if (l.size() > 0) {
                m.put(f, l);
            }
        }

        return m;
    }

    public List<String> findSubstringMatches(File f, Pattern substringPattern) {
        Scanner scanner;
        LinkedList<String> matchingLines = new LinkedList<String>();
        try {
            scanner = new Scanner(f);
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                if (substringPattern.matcher(s).matches()) {
                    matchingLines.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return matchingLines;
    }
}
