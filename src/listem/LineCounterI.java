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
 * @author i53425
 */
public class LineCounterI implements LineCounter{
    
    public Map<File, Integer> countLines(File directory, String fileSelectionPattern,  boolean recursive){
        final boolean rec = recursive;
        final Pattern fPat = Pattern.compile(fileSelectionPattern);
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if (!f.canRead()) {
                    return false;
                }
                if (f.isDirectory()) {
                    return rec;
                }
                return fPat.matcher(f.getName()).matches();
            }
        });

        Map<File, Integer> m = new TreeMap<File, Integer>();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    m.putAll(countLines(f, fileSelectionPattern, recursive));
                } else {
                    int l = process(f);
                    m.put(f, l);
                }
            }
        }

        return m;
    }
    
        public int process(File f) {
        int lines = 0;

        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (s.hasNextLine()) {
            s.nextLine();
            lines++;
        }

        System.out.println(lines + " " + f.getAbsolutePath());

        return lines;
    }
    
}
