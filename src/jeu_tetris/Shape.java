package jeu_tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Shape {
	private int x = 4, y = 0;
	private int normal = 600;
	private int delayTimeForMovement = normal;
	private long beginTime;
	
	private int deltax = 0;
	private boolean collision = false;
	
	public static final int Board_WITDH = 10;
	public static final int Board_HEIGHT = 20;

	//size de carré en pixel
	public static final int BLOCK_SIZE = 30;
	
	private int [][] coords;
	private Board board;
	private Color color;
	
	
	
	public void setx(int x) {
		this.x = x;
	}
	public void sety(int y) {
		this.y = y;
	}
	
	public void reset() {
		this.x = 4;
		this.y = 0;
		collision = false;
	}
	
	
	public Shape(int[][] coords, Board board, Color color) {
		this.coords = coords;
		this.board = board;
		this.color = color;
	}

	public void update() {
		if(collision) {
			//color of the board
			for(int row = 0; row < coords.length; row++) {
				for(int col =0; col< coords[0].length;col++) {
					if(coords[row][col] != 0) {
						board.getBoard()[y + row][x + col] = color;
					}
				}
			}
			checkLine();
			//set current shape
			board.setCurrentShape();
			return;
		}
		//mouvements horizontales
		boolean moveX = true;
		if(!(x + deltax + coords[0].length > 10) && !(x + deltax < 0)) {
			for(int row =0; row < coords.length; row++) {
				for(int col=0; col < coords[row].length; col++) {
					if(coords[row][col] != 0) {
						if(board.getBoard()[y + row][x + deltax + col] != null) {
						moveX = false;
					}
					}
				
				}
			}
			if(moveX) {
			x+= deltax;}
		}
		deltax = 0;
		
		
		if(System.currentTimeMillis() - beginTime > delayTimeForMovement) {
			//les mouvements verticaux
			if(!(y + 1 + coords.length > Board_HEIGHT)) {
				for(int row = 0; row < coords.length; row++) {
					for(int col = 0; col < coords[row].length; col++) {
						if(coords[row][col] != 0) {
							if(board.getBoard()[y + 1 + row][x + deltax + col] != null) {
								collision = true;
							}
						}
					}
				}
				if(!collision) {
					y++ ;
				}
				
			}else {
				collision = true;
			}
			
			beginTime = System.currentTimeMillis();
		}
	}
	//La création des lignes
	private void checkLine() {
		int bottomLine = board.getBoard().length - 1;
		for(int topLine = board.getBoard().length - 1; topLine > 0; topLine --) {
			int count = 0;
			for(int col = 0; col < board.getBoard()[0].length; col++) {
				if(board.getBoard()[topLine][col] != null) {
					count ++;
					//System.out.println(count);
				}
				if(Board_WITDH == count) {
					board.addScore();
				}
				board.getBoard()[bottomLine][col] = board.getBoard()[topLine][col];
			
				
			}
			if(count < board.getBoard()[0].length) {
				bottomLine--;
			}
		}
		
	}
	
	//rotation des piéces
	public void rotateShape() {
		int[][] rotatedShape = transposeMatrix(coords);
		reverseRows(rotatedShape);
		//pour respecter les dimensions de board de jeu à la rotation
		if((x + rotatedShape[0].length > Board.Board_WITDH) || (y + rotatedShape.length > 20)) {
			return;
		}
		//éviter la collision entre les pièces
		for(int row = 0; row < rotatedShape.length; row++){
		  for(int col = 0; col < rotatedShape[row].length; col++) {
			if(rotatedShape[row][col] != 0) {
				if(board.getBoard()[y+row][x +col] != null) {
					return;
				}
			}
		}
	}
		coords = rotatedShape;
	}
	
	
	
	//matrice transposé
	private  int[][] transposeMatrix(int[][] matrix) {
		int[][] temp = new int[matrix[0].length][matrix.length];
		for(int row = 0; row < matrix.length; row ++) {
			for(int col = 0; col < matrix[0].length; col++) {
				temp[col][row] = matrix[row][col];
			}
		}
		return temp;
	}
	//inverser de horizontal en vertical
	private void reverseRows(int[][] matrix) {
		int middle = matrix.length / 2;
		for(int row=0; row < middle; row++) {
			int[] temp = matrix[row];
			matrix[row] = matrix[matrix.length - row - 1];
			matrix[matrix.length - row - 1] = temp;
		} 
		
	}
	
	public void render(Graphics g) {
		for (int row=0; row< coords.length;row++) {
			for(int col=0; col<coords[0].length;col++) {
				if(coords[row][col] != 0) {
					g.setColor(color);
					g.fillRect(col*BLOCK_SIZE + x * BLOCK_SIZE , row * BLOCK_SIZE + y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
	}
	  public Color getColor() {
	        return color;
	    }

	public int[][] getCoords(){
		return coords;
	}
	
	public void speedDown() {
		delayTimeForMovement = normal;
	}
	public void moveRight() {
		deltax = 1;
	}
	public void moveLeft() {
		deltax = -1;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	

}
