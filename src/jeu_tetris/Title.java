package jeu_tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage instructions;
	private BufferedImage background;

	private WindowGame window;
	private Timer timer;
	public Title(WindowGame window){
        instructions = ImageLoader.loadImage("/Tetris_logo.png");
        background = ImageLoader.loadImage("/1454.jpg"); 
        
		timer = new Timer(1000/60, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		timer.start();
		this.window = window;
		
		
		
	}
	
	public void paintComponent(Graphics g){
		 super.paintComponent(g);

		    // Draw the background image
		
		g.fillRect(0, 0, WindowGame.WITDH, WindowGame.HEIGHT);

	    g.drawImage(background,  
	    		30 - instructions.getWidth()/2 -10,
	    		 - instructions.getHeight() + 150, 
	    		null);

		
		g.drawImage(instructions, WindowGame.WITDH/2 - instructions.getWidth()/2 -10,30 - instructions.getHeight()/2 + 150, null);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Georgia", Font.BOLD,17));
		g.drawString("Cliquer sur espace pour jouer!", 80, WindowGame.HEIGHT / 2 + 100);
		
		
	}	

    @Override
    public void keyTyped(KeyEvent e) {
       
		if(e.getKeyChar() == KeyEvent.VK_SPACE) {
            window.startTetris();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
