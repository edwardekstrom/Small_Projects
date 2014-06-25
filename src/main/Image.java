package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Image {
	private Pixel[][] _imageArray;
	private Pixel[][] _outputArray;
	private int _rows;
	private int _cols;
	private int _max;
	private boolean _commentSection;
	private boolean _gettingArraySize;
	private int _rgbCount;
	private Pixel _curPixel;
	private List<Pixel> _allPixels;

	public Image(String inputFileString) {
		_rows = -1;
		_cols = -1;
		_max = -1;
		_commentSection = false;
		_gettingArraySize = false;
		_rgbCount = 0;
		_allPixels = new ArrayList<Pixel>();
		_curPixel = new Pixel();
		File inputFile;
		Scanner scanner;
		try {
			inputFile = new File(inputFileString);
			scanner = new Scanner(inputFile);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		StringBuilder sb = new StringBuilder();
		while (scanner.hasNextLine()) {
			String curLine = scanner.nextLine();
			if (curLine.contains("#")) {
				curLine = curLine.substring(0, curLine.indexOf("#"));
			}
			sb.append(curLine + "\n");
		}

		scanner = new Scanner(sb.toString());

		while (scanner.hasNext()) {
			if (scanner.next().equals("P3"))
				break;
		}

		_cols = scanner.nextInt();
		_rows = scanner.nextInt();
		_max = scanner.nextInt();

		while (scanner.hasNextInt()) {
			int value = scanner.nextInt();
			if (_rgbCount == 0) {
				_curPixel.red = value;
				_rgbCount++;
			} else if (_rgbCount == 1) {
				_curPixel.green = value;
				_rgbCount++;
			} else {
				_curPixel.blue = value;
				_allPixels.add(_curPixel);
				_curPixel = new Pixel();
				_rgbCount = 0;
			}
		}

		int allPixelsIndex = 0;
		_imageArray = new Pixel[_rows][_cols];
		_outputArray = new Pixel[_rows][_cols];
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				_imageArray[i][j] = _allPixels.get(allPixelsIndex++);
				_outputArray[i][j] = new Pixel();
			}
		}
	}

	public void invert() {
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				_outputArray[i][j].red = _max - _imageArray[i][j].red;
				_outputArray[i][j].green = _max - _imageArray[i][j].green;
				_outputArray[i][j].blue = _max - _imageArray[i][j].blue;
			}
		}
	}

	public void grayscale() {
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				int value = (_imageArray[i][j].red + _imageArray[i][j].green + _imageArray[i][j].blue);
				value /= 3;
				_outputArray[i][j].red = value;
				_outputArray[i][j].green = value;
				_outputArray[i][j].blue = value;
			}
		}
	}

	public void emboss() {
		for (int i = 0; i < _cols; i++) {
			_outputArray[0][i].red = 128;
			_outputArray[0][i].green = 128;
			_outputArray[0][i].blue = 128;
		}
		for (int i = 0; i < _rows; i++) {
			_outputArray[i][0].red = 128;
			_outputArray[i][0].green = 128;
			_outputArray[i][0].blue = 128;
		}
		for (int i = 1; i < _rows; i++) {
			for (int j = 1; j < _cols; j++) {
				int value = maxDifference(_imageArray[i][j],
						_imageArray[i - 1][j - 1]);
				_outputArray[i][j].red = value;
				_outputArray[i][j].green = value;
				_outputArray[i][j].blue = value;
			}
		}
	}

	public int maxDifference(Pixel p1, Pixel p2) {
		int diffRed = p1.red - p2.red;
		int diffGreen = p1.green - p2.green;
		int diffBlue = p1.blue - p2.blue;

		int maxDiff = diffRed;
		if (Math.abs(maxDiff) < Math.abs(diffGreen))
			maxDiff = diffGreen;
		if (Math.abs(maxDiff) < Math.abs(diffBlue))
			maxDiff = diffBlue;
		maxDiff += 128;
		if (maxDiff > 255)
			maxDiff = 255;
		if (maxDiff < 0)
			maxDiff = 0;
		return maxDiff;
	}

	public void motionblur(int n) {
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				int redAverage = 0;
				int greenAverage = 0;
				int blueAverage = 0;
				int k;
				for (k = 0; (k < n && (j + k < _cols)); k++) {
					redAverage += _imageArray[i][j + k].red;
					greenAverage += _imageArray[i][j + k].green;
					blueAverage += _imageArray[i][j + k].blue;
				}
				_outputArray[i][j].red = redAverage / (k);
				_outputArray[i][j].green = greenAverage / (k);
				_outputArray[i][j].blue = blueAverage / (k);

			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("P3 " + _cols + " " + _rows + " " + _max + "\n");
		for (int i = 0; i < _rows; i++) {
			for (int j = 0; j < _cols; j++) {
				sb.append(_outputArray[i][j].red + " ");
				sb.append(_outputArray[i][j].green + " ");
				sb.append(_outputArray[i][j].blue + " ");
			}
		}
		return sb.toString();
	}

}