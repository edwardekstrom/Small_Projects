package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Image {
	private Pixel[][] _imageArray;
	private int _rows;
	private int _cols;
	private boolean _commentSection;
	private boolean _gettingArraySize;
	private int _rgbCount;
	private Pixel _curPixel;
	private List<Pixel> _allPixels;

	public Image(String inputFileString) {
		_rows = -1;
		_cols = -1;
		_commentSection = false;
		_gettingArraySize = false;
		_rgbCount = 0;
		_allPixels =  new ArrayList<Pixel>();
		_curPixel = new Pixel();
		File inputFile;
		Scanner scanner;
		try{
			inputFile = new File(inputFileString);
			scanner = new Scanner(inputFile);
		}catch (Exception e){
			e.printStackTrace();
			return;
		}
		while(scanner.hasNext()){
			String cur = scanner.next();
			if(_commentSection){
				if(cur.equals("\n")) _commentSection = false;
			}else if(cur.equals("#")){
				_commentSection = true;
			}else if(cur.equals("P3")){
				_gettingArraySize = true;
				System.out.println(cur);
			}else if(_gettingArraySize){
				System.out.println(cur);
				if(_cols == -1){
					_cols = new Integer(cur);
				}else{
					_rows = new Integer(cur);
					_gettingArraySize = false;
				}
			}else{
				if(_rgbCount == 0){
					_curPixel.red = new Integer(cur);
					_rgbCount++;
				}else if(_rgbCount == 1){
					_curPixel.green = new Integer(cur);
					_rgbCount++;
				}else{
					_curPixel.blue = new Integer(cur);
					_allPixels.add(_curPixel);
					_curPixel = new Pixel();
					_rgbCount++;
				}
			}
		}
		int allPixelsIndex = 0;
		_imageArray = new Pixel[_rows][_cols];
		for(int i = 0; i < _rows; i++){
			for(int j = 0; j < _cols; j++){
				_imageArray[i][j] = _allPixels.get(allPixelsIndex++);
			}
		}
	}

	public Image invert() {
		
		return null;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("P3 " + _cols + " " + _rows + "\n");
		for(int i = 0; i < _rows; i++){
			for(int j = 0; j < _cols; j++){
				sb.append(_imageArray[i][j].red + " ");
				sb.append(_imageArray[i][j].green + " ");
				sb.append(_imageArray[i][j].blue + " ");
			}
		}
		return sb.toString();
	}

}
