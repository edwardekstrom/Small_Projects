/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author i53425
 */
import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

class NotADirectoryException extends Exception {
}

class UsageException extends Exception {
}

public abstract class FileUtil {

    Pattern _fPattern;
    Pattern _sPattern;
    boolean _recur;

    public FileUtil() {
    }

    public FileUtil(String[] args) throws NotADirectoryException {
        int i = 0;
        File dir;
        try {
            if (args[0].equals("-r")) {
                _recur = true;
                i = 1;
            }
            dir = new File(args[i++]);
            _fPattern = Pattern.compile(args[i++]);
            if (requireSearchPattern()) {
                _sPattern = Pattern.compile(".*" + args[i++] + ".*");
            } else if (args.length > i++) {
                throw new UsageException();
            }
        } catch (Exception e) {
            printUsage();
            return;
        }

        if (dir.isDirectory()) {
            System.out.println(summaryLabel() + " " + loopFiles(dir));
        } else {
            System.out.println(dir.getPath() + " is't a directory");
            System.out.println(summaryLabel() + 0);
            throw new NotADirectoryException();
        }
    }

    private int loopFiles(File file) {
        File[] files = file.listFiles(new FileFilter() {
            public boolean accept(File f) {
                if(!f.canRead()) return false;
                if (f.isDirectory()) {
                    return _recur;
                }
                return _fPattern.matcher(f.getName()).matches();
            }
        });
        int total = 0;
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    total += loopFiles(f);
                } else {
                    total += process(f);
                }
            }
        }

        return total;
    }

    public abstract int process(File file);

    public abstract String summaryLabel();

    public abstract void printUsage();
    
    public abstract boolean requireSearchPattern();
}
