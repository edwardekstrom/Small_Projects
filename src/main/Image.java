package main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Image {
	private Pixel[][] _imageArray;
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
                StringBuilder sb = new StringBuilder();
		while(scanner.hasNextLine()){
			String curLine = scanner.nextLine();
                        if(curLine.contains("#")){
                            curLine = curLine.substring(0, curLine.indexOf("#"));
                        }
                        sb.append(curLine + "\n");
		}
                
                scanner = new Scanner(sb.toString());
                
                while(scanner.hasNext()){
                    if(scanner.next().equals("P3")) break;
                }
                
                _cols = scanner.nextInt();
                System.out.println(_cols);
                _rows = scanner.nextInt();
                System.out.println(_rows);
                _max = scanner.nextInt();
                System.out.println(_max);
                
                while(scanner.hasNextInt()){
                    int value = scanner.nextInt();
                    if(_rgbCount == 0){ 
                        _curPixel.red = value;
                        _rgbCount++;
                    }
                    else if(_rgbCount == 1){
                        _curPixel.green = value;
                        _rgbCount++;
                    }else{
                        _curPixel.blue = value;
                        _allPixels.add(_curPixel);
                        _curPixel = new Pixel();
                        _rgbCount = 0;
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

	public void invert() {
		for(int i = 0; i < _rows; i++){
			for(int j = 0; j < _cols; j++){
				_imageArray[i][j].red = _max - _imageArray[i][j].red;
                                _imageArray[i][j].green = _max - _imageArray[i][j].green;
                                _imageArray[i][j].blue = _max - _imageArray[i][j].blue;
                        }
		}
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
