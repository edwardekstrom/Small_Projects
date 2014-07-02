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
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LineCount extends FileUtil {

    public static void main(String[] args) throws NotADirectoryException{
        new LineCount(args);
    }

    public LineCount(String[] args) throws NotADirectoryException{
        super(args);
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

    public String summaryLabel() {
        return "TOTAL: ";
    }

    public void printUsage() {
        System.out.println("USAGE: java LineCount [-r] directoryName fileSelectionPattern");
    }
    
    public boolean requireSearchPattern(){
        return false;
    }
}
