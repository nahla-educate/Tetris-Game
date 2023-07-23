package jeu_tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener{
	
    private static final long serialVersionUID = 1L;
	
    //variables 
    
	public static int STATE_GAME_PLAY = 0;
	public static int STATE_GAME_PAUSE = 1;
	public static int STATE_GAME_OVER = 2;
	
	public static int state = STATE_GAME_PLAY;
	
	private BufferedImage fleches, logo, pause;
	private Rectangle flechesBounds, logoBounds, pauseBounds;
	
	
	private static int FPS = 60;
	private static int delay = FPS / 1000;
	
	//10 colonnes et 20 lignes
	public static final int Board_WITDH = 10;
	public static final int Board_HEIGHT = 20;
	//size de carré en pixel
	public static final int BLOCK_SIZE = 30;
	
	private Random random;
	
	//chronométre
	private Timer looper;
	
	
	//score
	private int score = 0;
	
	//Types
	private Color[][] board = new Color[Board_HEIGHT][Board_WITDH];
	
	//color
	private Color[] colors = {
			Color.decode("#07E1CD"),
			Color.decode("#61c775"),
			Color.decode("#ffaf63"),
			Color.decode("#FCD600"),
			Color.decode("#00a2e8"),
			Color.decode("#a349a4"),
			Color.decode("#ff4e24")};
	
	//Tétrimino T 
	private Shape[] shapes= new Shape[7]; 
	private Shape currentShape, nextShape;
	
	public Board() {
		
		// upload les images
		fleches = ImageLoader.loadImage("/arrow12.png");
		logo = ImageLoader.loadImage("/tetris.png");
		pause = ImageLoader.loadImage("/shift1.png");
		
		
	//deplacer les images
		flechesBounds = new Rectangle(295, 480 , fleches.getWidth(),fleches.getHeight());
		logoBounds = new Rectangle(310, 20, logo.getWidth(),logo.getHeight());
		pauseBounds = new Rectangle(335, 360, pause.getWidth(),pause.getHeight());
						
		random = new Random();
		//les piéces de tetris
		
		shapes[0] = new Shape(new int[][] {
			{1, 1, 1, 1}
		},this, colors[0]);
		
		shapes[1] = new Shape(new int[][] {
			{1, 1, 1},
			{0, 1, 0},
		},this, colors[1]);
		
		shapes[2] = new Shape(new int[][] {
			{1, 1, 1},
			{1, 0, 0},
		},this, colors[2]);
		
		shapes[3] = new Shape(new int[][] {
			{1, 1, 1},
			{0, 0, 1},
		},this, colors[3]);
		
		shapes[4] = new Shape(new int[][] {
			{0, 1, 1},
			{1, 1, 0},
		},this, colors[4]);
		
		shapes[5] = new Shape(new int[][] {
			{1, 1, 0},
			{0, 1, 1},
		},this, colors[5]);
		
		shapes[6] = new Shape(new int[][] {
			{1, 1},
			{1, 1},
		},this, colors[6]);
		
		
		currentShape = shapes[0];
		
		//vitesse de translation 
		looper = new Timer(delay, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();
			}
		});
		looper.start();
	}
	
	private void update() {
		if (state == STATE_GAME_PLAY) {
	        currentShape.update();
	        
	    }
		
	}
	

	public void setNextShape() {
		 
	        int index = random.nextInt(shapes.length);
	        int colorIndex = random.nextInt(colors.length);
	        nextShape = new Shape(shapes[index].getCoords(), this, colors[colorIndex]);
	    }
	public void setCurrentShape() {
		currentShape = shapes[random.nextInt(shapes.length)];
		currentShape.reset();
		currentShape = nextShape;
		setNextShape();
		checkOverGame();
	}
	 
	
	//GameOver 
	private void checkOverGame() {
		int[][] coords = currentShape.getCoords();
		for(int row = 0; row < coords.length; row++) {
			for(int col = 0; col < coords[0].length; col++) {
				if(coords[row][col] != 0) {
					if(board[row + currentShape.getY()][col + currentShape.getX()] != null) {
						currentShape.reset();
						state = STATE_GAME_OVER;
					}
				}
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		
		//couleur de font
		g.setColor(Color.decode("#000080"));
		g.fillRect(0,0,getWidth(),getHeight());
		
		//la piéce en bas
		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				
				if(board[row][col] != null) {
					g.setColor(board[row][col]);
					g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
				}
			}
		}
		//la piéce suivante à droite
		g.setColor(nextShape.getColor());
		for (int row = 0; row < nextShape.getCoords().length; row++) {
			for (int col = 0; col < nextShape.getCoords()[0].length; col++) {
				if (nextShape.getCoords()[row][col] != 0) {
					g.fillRect(col * 30 + 312, row * 30 + 150, Board.BLOCK_SIZE, Board.BLOCK_SIZE);
					}
				}
			}
		currentShape.render(g);
		
		//dessiner les images
		g.drawImage(pause.getScaledInstance(pause.getWidth() + 3, pause.getHeight() + 3, BufferedImage.SCALE_DEFAULT), pauseBounds.x + 3, pauseBounds.y + 3, null);
	    g.drawImage(fleches.getScaledInstance(fleches.getWidth() + 3, fleches.getHeight() + 3, BufferedImage.SCALE_DEFAULT), flechesBounds.x + 3, flechesBounds.y + 3, null);
		g.drawImage(logo.getScaledInstance(logo.getWidth() + 3, logo.getHeight() + 3,
	    BufferedImage.SCALE_DEFAULT), logoBounds.x + 3, logoBounds.y + 3, null);	    
		

		//couleur de lignes
		g.setColor(Color.decode("#4682B4"));
		
		//dessiner les careaux
		
		//les lignes
		for(int row = 0; row < Board_HEIGHT; row++) {
			g.drawLine(0,  BLOCK_SIZE * row,  BLOCK_SIZE * Board_WITDH, BLOCK_SIZE * row);
		}
		//les colonnes
		for(int col = 0; col < Board_WITDH +1; col++) {
			g.drawLine(col * BLOCK_SIZE , 0, col * BLOCK_SIZE , BLOCK_SIZE * Board_HEIGHT);
		}
		
		//ecrire game over
		if(state == STATE_GAME_OVER) {
			g.setColor(Color.white);
			g.setFont(new Font("Georgia", Font.BOLD,40));
			g.drawString("GAME OVER",65, 295);
		}
		//ecrire Pause
			if(state == STATE_GAME_PAUSE) {
				g.setColor(Color.white);
				g.setFont(new Font("Georgia", Font.BOLD,40));
				g.drawString("GAME PAUSE",65, 295);
			}
			
		//ecrire score
		g.setColor(Color.white);
		g.setFont(new Font("Georgia", Font.BOLD,15));
		g.drawString("SCORE", 340, 270);
		g.drawString(score + "", 360, WindowGame.HEIGHT / 2 );
		
		
		
		
		//ecrire pause
		g.setColor(Color.white);
		g.setFont(new Font("Georgia", Font.BOLD,15));
		g.drawString("PAUSE", 340, 355);
			
				
			
		
				
	}
	
	public Color[][] getBoard(){
		return board;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	//utilisation des fleches de clavier
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentShape.moveRight();
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentShape.moveLeft();
		}else if(e.getKeyCode() == KeyEvent.VK_UP) {
			currentShape.rotateShape();
		}
	
		//rejouer
		if(state == STATE_GAME_OVER) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				for(int row = 0; row < board.length; row++) {
					for(int col = 0; col < board[row].length; col++) {
						board[row][col] = null;
							
					}
				}
				setCurrentShape();
				state = STATE_GAME_PLAY;
			}
		}
		
		
		//pause
		if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if(state == STATE_GAME_PLAY) {
				state = STATE_GAME_PAUSE;
			}else if(state == STATE_GAME_PAUSE) {
				state = STATE_GAME_PLAY;
			}
		}
		
	}
	public void startGame() {
        setNextShape();
        setCurrentShape();
        for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[row].length; col++) {
				
				board[row][col] = null;
				}
			}
        looper.start();

    }
	

	public void addScore() {
		score = score + 50;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_DOWN) {
			currentShape.speedDown();
	}}
}
