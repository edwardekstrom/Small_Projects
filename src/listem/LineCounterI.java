/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listem;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 *
 * @author Edward Ekstrom
 */
public class LineCounterI extends FileSearcher implements LineCounter {

    public Map<File, Integer> countLines(File directory, String fileSelectionPattern, boolean recursive) {
        List<File> files = search(directory, fileSelectionPattern, recursive);

        Map<File, Integer> m = new TreeMap<File, Integer>();
        for (File f : files) {
            int l = countLines(f);
            m.put(f, l);
        }

        return m;
    }

    public int countLines(File f) {
        int lines = 0;
        Scanner scanner = null;
        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            scanner.nextLine();
            lines++;
        }
        return lines;
    }

}
