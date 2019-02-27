import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;

public class snake {

	public snake() {
		JFrame frame = new JFrame("SNAKE");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainScreen panel = new mainScreen();
		frame.setContentPane(panel);
		frame.pack();
		frame.setBackground(Color.BLACK);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new snake();
	}

	class mainScreen extends JPanel implements KeyListener, ActionListener {
		Timer timer = new Timer(100, this);
		int xCoor = 10;
		int yCoor = 10;
		int size = 10;
		Random r = new Random();
		int score;
		ArrayList<BodyPart> snake = new ArrayList<>();
		Apple apple = new Apple(r.nextInt(49) + 1, r.nextInt(49) + 2, 10);
		boolean right = true, left = false, up = false, down = false;

		mainScreen() {
			setPreferredSize(new Dimension(510, 520));
			setFocusable(true);
			addKeyListener(this);
			snake.add(new BodyPart(xCoor, yCoor, 10));
			timer.start();
		}

		public void paintComponent(Graphics g) {
			g.clearRect(0, 0, 510, 520);
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 510, 520);
			g.setColor(Color.GRAY);
			g.drawRect(10, 20, 500, 500);

			g.setColor(Color.GRAY);
			Font f = new Font("Dialog", Font.PLAIN, 14);
			g.setFont(f);
			g.drawString("Score : " + score, 10, 15);
			apple.draw(g);
			for (int i = 0; i < snake.size(); i++) {
				snake.get(i).draw(g);
			}
		}

		public void actionPerformed(ActionEvent e) {
			if (snake.size() == size)
				snake.remove(0);
			if (xCoor == 50 && right) {
				xCoor = 0;
			} else if (xCoor == 1 && left) {
				xCoor = 51;
			} else if (yCoor == 51 && down) {
				yCoor = 1;
			} else if (yCoor == 2 && up) {
				yCoor = 52;
			}
			if ((xCoor == apple.getxCoor()) && (yCoor == apple.getyCoor())) {
				playSound("/sound1.wav");
				apple.setxCoor(r.nextInt(49) + 1);
				apple.setyCoor(r.nextInt(49) + 2);
				size += 4;
				score++;
				if (score < 14)
					timer.setDelay(timer.getInitialDelay() - score * 5);
			}

			if (right)
				xCoor++;
			if (left)
				xCoor--;
			if (up)
				yCoor--;
			if (down)
				yCoor++;

			for (int i = 0; i < snake.size(); i++) {
				if (xCoor == snake.get(i).getxCoor() && (yCoor == snake.get(i).getyCoor())) {
					playSound("/sound2.wav");
					timer.stop();
					JOptionPane endDialog = new JOptionPane();

					if ((endDialog.showConfirmDialog(this,
							"You lost!\nYour score: " + score + "\nDo you want to play again?", "The End",
							JOptionPane.YES_NO_OPTION) == 1)) {
						System.exit(0);
					} else {
						snake = new ArrayList<>();
						apple = new Apple(r.nextInt(49) + 1, r.nextInt(49) + 2, 10);
						right = true;
						left = false;
						up = false;
						down = false;
						xCoor = 10;
						yCoor = 10;
						score = 0;
						timer.start();
					}
				}
			}

			snake.add(new BodyPart(xCoor, yCoor, 10));
			repaint();
		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_RIGHT && !left) {
				up = false;
				down = false;
				right = true;
			}
			if (key == KeyEvent.VK_LEFT && !right) {
				up = false;
				down = false;
				left = true;
			}
			if (key == KeyEvent.VK_UP && !down) {
				left = false;
				right = false;
				up = true;

			}
			if (key == KeyEvent.VK_DOWN && !up) {
				left = false;
				right = false;
				down = true;
			}

		}

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {

		}

		public void playSound(String path) {
			try {
							
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(path));
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		class BodyPart {
			private int xCoor, yCoor, width, height;

			public BodyPart(int xCoor, int yCoor, int tileSize) {
				this.xCoor = xCoor;
				this.yCoor = yCoor;
				width = tileSize;
				height = tileSize;
			}

			public void draw(Graphics g) {
				g.setColor(Color.RED);
				g.fillRect(xCoor * width, yCoor * height, width, height);
			}

			public int getxCoor() {
				return xCoor;
			}

			public void setxCoor(int xCoor) {
				this.xCoor = xCoor;
			}

			public int getyCoor() {
				return yCoor;
			}

			public void setyCoor(int yCoor) {
				this.yCoor = yCoor;
			}

		}

		class Apple {

			private int xCoor, yCoor, width, height;

			public Apple(int xCoor, int yCoor, int tileSize) {
				this.xCoor = xCoor;
				this.yCoor = yCoor;
				width = tileSize;
				height = tileSize;
			}

			public void draw(Graphics g) {
				g.setColor(Color.GREEN);
				g.fillRect(xCoor * width, yCoor * height, width, height);
			}

			public int getxCoor() {
				return xCoor;
			}

			public void setxCoor(int xCoor) {
				this.xCoor = xCoor;
			}

			public int getyCoor() {
				return yCoor;
			}

			public void setyCoor(int yCoor) {
				this.yCoor = yCoor;
			}

		}

	}
}