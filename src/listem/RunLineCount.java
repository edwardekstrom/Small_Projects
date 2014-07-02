package listem;

import java.io.File;
import java.util.Map;

/**
 * A simple main class for running line count. This class is not used by the 
 * passoff program.
 */
public class RunLineCount {

	/**
	 * If the first argument is -r, the search should be recursive.
	 * 
	 * The next arguments are the base directory and file selection pattern
	 */
	public static void main(String[] args) {
		String dirName = "";
		String filePattern = "";
		boolean recursive = false;
		
		if (args.length == 2) {
			recursive = false;
			dirName = args[0];
			filePattern = args[1];
		} else if (args.length == 3 && args[0].equals("-r")) {
			recursive = true;
			dirName = args[1];
			filePattern = args[2];
		} else {
			System.out.println("USAGE: java listem.RunLineCount [-r] <dir> <file-pattern>");
			return;
		}

		/**
		 * Create an instance of your Grep here
		 */
		LineCounter counter = new LineCounterI();
		Map<File, Integer> lineCountResult = counter.countLines(new File(dirName), filePattern, recursive);
		
		RunLineCount.outputLineCountResult(lineCountResult);
	}

	/**
	 * Prints a formatted version of the map returned from a line counter.
	 */
	public static void outputLineCountResult(Map<File, Integer> result) {
		int totalLines = 0;
		for (Map.Entry<File, Integer> singleFileResult : result.entrySet()) {
			int lineCount = singleFileResult.getValue();
			String file = singleFileResult.getKey().getPath();
			
			System.out.println(lineCount + " " + file);

			totalLines += lineCount;			
		}
		
		System.out.println("TOTAL: " + totalLines);
	}
}
