package main;

import java.io.PrintWriter;

public class ImageEditor {
	public static void main(String[] args) {
		String inputFileString = "/home/edwardekstrom/workspace/ImageEditor/src/main/slctemple.ppm";
		String outputFileString = "/home/edwardekstrom/workspace/ImageEditor/src/main/output.ppm";
		String action = "emboss";
		int blurLength = -1;

		if (args.length > 4) {
			System.out.println("too many args");
			return;
		} else if (args.length > 2) {
			inputFileString = args[0];
			outputFileString = args[1];
			action = args[2].toLowerCase();
			if (args.length == 4) {
				blurLength = new Integer(args[3]);
			}
		}

		Image inputImage = new Image(inputFileString);
		try {
			PrintWriter writer = new PrintWriter(outputFileString, "UTF-8");
			if (action.equals("invert")) {
				inputImage.invert();
				writer.print(inputImage.toString());
			} else if (action.equals("grayscale")) {
				inputImage.grayscale();
				writer.print(inputImage.toString());
			} else if (action.equals("emboss")) {
				inputImage.emboss();
				writer.print(inputImage.toString());
			} else if (action.equals("motionblur")) {
				inputImage.motionblur(blurLength);
				writer.print(inputImage.toString());
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
