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
public class GrepI extends FileSearcher implements Grep{
    
     public Map<File, List<String>> grep(File directory, String fileSelectionPattern, String substringSelectionPattern, boolean recursive) {
        Pattern sPat = Pattern.compile(".*" + substringSelectionPattern + ".*");
        List<File> files = search(directory, fileSelectionPattern, recursive);
        Map<File, List<String>> m = new TreeMap<File, List<String>>();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    m.putAll(grep(f, fileSelectionPattern, substringSelectionPattern, recursive));
                } else {
                    List<String> l = process(f, sPat);
                    if (l.size() > 0) {
                        m.put(f, l);
                    }
                }
            }
        }

        return m;
    }
    
    public List<String> process(File f, Pattern sPat) {
        int matches = 0;

        Scanner sc = null;
        LinkedList<String> l = new LinkedList<String>();
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            if (sPat.matcher(s).matches()) {
                l.add(s);
            }
        }

        return l;
    }
}
