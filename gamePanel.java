package snakeGame;

import java.awt.*;

import java.util.Scanner;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;



public class gamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600; 
	static final int SCREEN_HEIGHT= 600; 
	static final int UNIT_SIZE = 25; 
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 75; 
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int [GAME_UNITS];
	int bodyParts = 6;
	int applesEaten; 
	int appleX;
	int appleY;
	char direction =  'R'; //L, R, U, D
	boolean running = false;
	Timer timer;
	Random random; 
	
	//constructor
	public gamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
	
		startGame();
	}
	
	//methods
	public void startGame () {
		newApple();
		running = true; 
		timer = new Timer (DELAY, this); 
		timer.start();

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if (running) {	
			for (int i = 0; i< SCREEN_HEIGHT/UNIT_SIZE;i++) {
					g.drawLine( i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
					g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
				}
			g.setColor(Color.red); 
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); 
			
			for (int i= 0; i< bodyParts; i++) {
				if (i == 0 ) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(16, 179, 1));
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
			g.setColor(Color.red);
			g.setFont(new Font("Ink free", Font.BOLD, 45));
			FontMetrics metrics = getFontMetrics(g.getFont()); 
			g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g); 
		}
	}
	
	public void newApple() {
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; 
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; 
		
		
	}
	
	public void move() {
		for (int i =bodyParts;i >0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1]; 
		}
		
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break; 
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break; 
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break; 
		}
		
	}
	
	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++; 
			newApple(); 
		}
	}
	
	public void checkCollisions () {
		//if head collides with body, running will be false
		for (int i = bodyParts; i >0; i--) {
			if((x[0]==x[i])&& (y[0]== y[i])) {
				running = false; 
			}
		}
		
		//left side collision
		if(x[0]< 0) {
			running = false; 
		}
		
		// right side collision
		
		if(x[0]> SCREEN_WIDTH) {
			running = false; 
		}
		
		//top side collision
		if(y[0]> SCREEN_HEIGHT) {
			running = false; 
		}
		
		
		//top side collision
		if(y[0]< 0) {
			running = false; 
		}
		
		if (!running) {
			timer.stop(); 
		}
		
		
	}
	
	public void gameOver(Graphics g) {
		//test
		g.setColor(Color.red);
		g.setFont(new Font("Times New Roman", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont()); 
		g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//score
		g.setColor(Color.red);
		g.setFont(new Font("Ink free", Font.BOLD, 45));
		FontMetrics metrics2 = getFontMetrics(g.getFont()); 
		g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
		
		//restart prompt
		g.setColor(Color.green);
		g.setFont(new Font("Times New Roman", Font.BOLD, 30));
		FontMetrics metrics3 = getFontMetrics(g.getFont()); 
		g.drawString("Press \"Enter\" to Exit or \"R\" to Play", (SCREEN_WIDTH - metrics3.stringWidth("Press \"Enter\" to Exit or \"R\" to Play"))/2, 3*SCREEN_HEIGHT/4);
		
		
		
		
			
		
		
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L'; 
				}
				break; 
			case KeyEvent.VK_RIGHT: 
				if (direction !='L') {
					direction = 'R'; 
				}
				break; 
			case KeyEvent.VK_UP: 
				if (direction !='D') {
					direction = 'U'; 
				}
				break; 
			case KeyEvent.VK_DOWN: 
				if (direction != 'U') {
					direction = 'D';
				}
				break; 
			}
			if (!running) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				System.exit(0); 
				break; 
			case KeyEvent.VK_R: 
				snakeGame.main(null);
				break; 
			}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
			}
		repaint();
		
	}

}
