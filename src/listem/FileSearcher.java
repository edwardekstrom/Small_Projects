/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package listem;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.LinkedList;


/**
 *
 * @author i53425
 */
public abstract class FileSearcher {
    
    public List<File> search(File directory, String fileSelectionPattern, boolean recursive){
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
        List<File> matchingFiles = new LinkedList<File>();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    matchingFiles.addAll(search(f, fileSelectionPattern, recursive));
                } else {
                    matchingFiles.add(f);
                }
            }
        }
        
        return matchingFiles;
    }
    
}
