package jeu_tetris;

import javax.swing.JFrame;

public class WindowGame {
	public static final int WITDH = 450, HEIGHT = 630;
	private JFrame window;
	private Board board;
	private Title title;
	

	public WindowGame() {
		window = new JFrame("Tetris");
		window.setSize(WITDH,HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		
		board = new Board();
		title = new Title(this);
		//window.add(board);
		
		window.addKeyListener(board); 
		window.addKeyListener(title);

        window.add(title);
		window.setVisible(true);
	}
    public void startTetris() {
        window.remove(title);
        window.add(board);
        board.startGame();
        window.revalidate();
    }
	

	public static void main(String[] args) {
		new WindowGame();
	}

}
