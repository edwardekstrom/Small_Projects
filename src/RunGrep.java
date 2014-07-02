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

public class RunGrep extends FileUtil {

    public static void main(String[] args) throws NotADirectoryException{
        new RunGrep(args);
    }

    public RunGrep(String[] args) throws NotADirectoryException{
        super(args);
    }

    @Override
    public int process(File f) {
        int matches = 0;

        Scanner sc = null;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            if (_sPattern.matcher(s).matches()) {
                if (matches == 0) {
                    System.out.println("FILE: " + f.getAbsolutePath());
                }

                System.out.println(s);
                ++matches;
            }
        }

        if (matches > 0) {
            System.out.println("MATCHES: " + matches);
        }

        return matches;
    }

    public String summaryLabel() {
        return "TOTAL MATCHES: ";
    }

    public void printUsage() {
        System.out.println("USAGE: java Grep [-r] directoryName fileSelectionPattern substringSelectionPattern");
    }

    public boolean requireSearchPattern() {
        return true;
    }
}
