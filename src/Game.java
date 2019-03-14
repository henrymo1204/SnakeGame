import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JFrame implements KeyListener, ActionListener{
	private final int WIDTH = 450;//width for the frame
	private final int LENGTH = 500;//length for the frame
	private final int ROW = 16;//number of rows on the board
	private final int COL = 16;//number of columns on the board
	private final int NUM = ROW * COL;//number of panels on the board
	private final int speed = 200;//speed of the snake in milliseconds
	private int lastRow;
	private int lastCol;
	//location for the tail of the snake
	private int[] row = new int[NUM];
	private int[] col = new int[NUM];
	//2D array for the location of the snake
	private int seconds1 = 0;//second digit of seconds
	private int seconds2 = 0;//first digit of seconds
	private int minutes1 = 0;//second digit of minutes
	private int minutes2 = 0;//first digit of minutes
	private boolean facingUp = false;
	private boolean facingDown = false;
	private boolean facingLeft = true;
	private boolean facingRight = false;
	//which way is the snake is facing, the snake face to the left in the beginning of the game
	private boolean paused = false;//is the game paused
	private boolean stopped = false;//is the game stopped
	private boolean moved = true;//did the snake moved
	private boolean newGame = true;//is it the beginning of the game
	private JPanel panel = new JPanel(new BorderLayout());//main panel
	private JPanel information = new JPanel(new GridLayout(2, 1));//gird layout panel that is used to display informations
	private JPanel message1 = new JPanel(new FlowLayout(FlowLayout.CENTER));//panel first information
	private JPanel message2 = new JPanel(new FlowLayout(FlowLayout.CENTER));//panel second information
	private JPanel board = new JPanel(new GridLayout(ROW, COL, 1, 1));//grid layout for game board
	private JPanel[][] grid = new JPanel[ROW][COL];//array of JPanel
	private JLabel info1 = new JLabel();//JLabel used to display first information
	private JLabel info2 = new JLabel();//JLabel used to display second information
 	private JLabel[][] imageLabel = new JLabel[ROW][COL];//array of JLabel
	
	private Color white = Color.WHITE;//color of each grid on the game board
	private Color snakeColor = Color.BLACK;//color of snake
	private Color[][] color = new Color[ROW][COL];//array for Color
	
	private JMenuBar mainBar = new JMenuBar();//menu bar
	private JMenu menu1 = new JMenu("File");//first menu
	private JMenu menu2 = new JMenu("Color");//second menu
	private JMenuItem start = new JMenuItem("Start");
	private JMenuItem pause = new JMenuItem("Pause");
	private JMenuItem restart = new JMenuItem("Restart");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem green = new JMenuItem("Green");
	private JMenuItem blue = new JMenuItem("Blue");
	private JMenuItem yellow = new JMenuItem("Yellow");
	private JMenuItem pink = new JMenuItem("Pink");
	private JMenuItem orange = new JMenuItem("Orange");
	private JMenuItem red = new JMenuItem("Red");
	private JMenuItem gray = new JMenuItem("Gray");
	private JMenuItem black = new JMenuItem("Black");
	
	
	private Timer timer;//timer for the snake
	private Timer gameTimer;//timer for the game
	private TimerTask task;//timer task for the snake
	private TimerTask gameTask;//timer task for the game
	
	private Snake snake;//snake object
	
	private Apple apple;//apple object
	
	private ImageIcon pictureOfApple = new ImageIcon("C:\\Users\\henry\\eclipse-workspace\\Snake\\apple.jpg");
	private Image pic = pictureOfApple.getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
	//image of apple
	
	public Game() {
		super("SNAKE");
		setSize(WIDTH, LENGTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		
		add(panel);
		
		setJMenuBar(mainBar);
		mainBar.add(menu1);
		mainBar.add(menu2);
		menu1.add(start);
		menu1.add(pause);
		menu1.add(restart);
		menu1.add(exit);
		menu2.add(green);
		menu2.add(blue);
		menu2.add(yellow);
		menu2.add(pink);
		menu2.add(orange);
		menu2.add(red);
		menu2.add(gray);
		menu2.add(black);
		
		panel.add(information, BorderLayout.NORTH);
		panel.add(board, BorderLayout.CENTER);
		
		information.add(message1);
		information.add(message2);
		message1.add(info1);
		info1.setFont(new Font("Ariel", Font.BOLD, 20));
		info1.setText("Press Start");
		message2.add(info2);
		info2.setFont(new Font("Ariel", Font.BOLD, 20));
		displayTime();
		
		board.setBackground(white);
		for(int i = 0; i < ROW; i++) {
			for(int j = 0; j < COL; j++) {
				grid[i][j] = new JPanel();
				grid[i][j].setBackground(white);
				board.add(grid[i][j]);
				color[i][j] = white;
				imageLabel[i][j] = new JLabel();
				grid[i][j].add(imageLabel[i][j]);
			}
		}
		
		addKeyListener(this);
		start.addActionListener(this);
		exit.addActionListener(this);
		pause.addActionListener(this);
		restart.addActionListener(this);
		green.addActionListener(this);
		blue.addActionListener(this);
		yellow.addActionListener(this);
		pink.addActionListener(this);
		orange.addActionListener(this);
		red.addActionListener(this);
		gray.addActionListener(this);
		black.addActionListener(this);
	}
	
	public void start() {//start method
		snake = new Snake();//create a new snake object
		snake.addSnake();//put the snake on the game board 
		
		apple = new Apple();//create a new apple object
		apple.addApple();//put the apple on the game board
		
		gameTimer = createTimer();//create a new game timer
		gameTask = createGameTask();//create a new game task
		gameTimer.schedule(gameTask, 1000, 1000);//schedule the game timer
		timer = createTimer();//create a new timer
		task = createTask();//create a new task
		timer.schedule(task, 1000, speed);//schedule the timer
	}
	
	public void pause() {//pause method
		gameTimer.cancel();//cancel the game timer
		timer.cancel();//cancel the timer
	}
	
	public void resume() {//resume method
		gameTimer = createTimer();//create a new game timer
		gameTask = createGameTask();//create a new game task
		gameTimer.schedule(gameTask, 1000, 1000);//schedule the game timer
		timer = createTimer();//create a new timer
		task = createTask();//create a new task
		timer.schedule(task, 1000, speed);//schedule the timer, the snake move every 1000 milliseconds initially
	}
	
	public void clear() {//clear method, reset everything on the board;
		for(int i = 0; i < ROW; i++) {
			for(int j = 0; j < COL; j++) {
				color[i][j] = white;
				grid[i][j].setBackground(color[i][j]);
				imageLabel[i][j].setIcon(null);
			}
		}
		//set the background of every girds to white, and set the icon of every label in the JLabel array to null
		row = new int[NUM];
		col = new int[NUM];
		//initialize the 2D array
		facingUp = false;
		facingDown = false;
		facingLeft = true;
		facingRight = false;
		//set the snake to facing left
		seconds1 = 0;
		seconds2 = 0;
		minutes1 = 0;
		minutes2 = 0;
		//reset the timer
	}
	
	public Timer createTimer() {//method that create a new timer
		return new Timer();
	}
	
	public TimerTask createGameTask() {//method that create a new game task
		return new TimerTask() {
			public void run() {//every time it runs
				seconds1++;//increase seconds1 by 1
				displayTime();//display time method
			}
		};
	}
	
	public void displayTime() {//method that display time
		if(seconds1 == 10) {//if seconds1 equals to 10
			seconds1 = 0;//set seconds1 to 0
			seconds2++;//increase seconds2 by 1
			if(seconds2 == 6) {//if seconds2 equals to 6
				seconds2 = 0;//set seconds2 to 0
				minutes1++;//increase minutes1 by 1
				if(minutes1 == 10) {//if minutes1 equals to 10
					minutes1 = 0;//set minutes1 to 0
					minutes2++;//increase minutes2 by 1
				}
			}
		}
		info2.setText(minutes2 + minutes1 + ":" + seconds2 + seconds1);//set text for info2
	}
	
	public TimerTask createTask() {//method that create a new task, this is the task for the snake
		return new TimerTask() {
			public void run() {//every time it runs
				lastRow = row[snake.getSize()-1];//row for the tail of the snake
				lastCol = col[snake.getSize()-1];//col for the tail of the snake
				if(facingUp == true) {//if the snake is facing up
					if(row[0] == 0) {//if the head of the snake is on the first row already, and snake is still facing up
						gameTimer.cancel();//cancel game timer
						timer.cancel();//cancel timer
						info1.setText("You hit the wall");
						stopped = true;//set stopped to true
						//game stopped because the snake hit the wall
					}
					else if(color[row[0]][col[0]] == color[row[0]-1][col[0]]) {
						//else if the next grid the snake is going to has the same background color as the head
						gameTimer.cancel();//cancel game timer
						timer.cancel();//cancel timer
						info1.setText("You hit yourself");
						stopped = true;//set stopped to true
						//game stopped because the snake hit itself
					}
					else if(snake.getSize() == 256) {
						//else if the user win the game
						gameTimer.cancel();//cancel game timer
						timer.cancel();//cancel timer
						info1.setText("You Win!");
						stopped = true;//set stopped to true
						//game stopped because the snake cannot grew any bigger
					}
					else {//else, if the snake did not hit the wall or itself
						snake.moveUp();//method that allows the snake to move up 
						snake.eat();//method that checks if the snake ate an apple
					}
				}
				else if(facingDown == true) {
					if(row[0] == 15) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You hit the wall");
						stopped = true;
					}
					else if(color[row[0]][col[0]] == color[row[0]+1][col[0]]) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You hit yourself");
						stopped = true;
					}
					else if(snake.getSize() == 256) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You Win!");
						stopped = true;
					}
					else {
						snake.moveDown();
						snake.eat();
					}
				}
				else if(facingLeft == true) {
					if(col[0] == 0) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You hit the wall");
						stopped = true;
					}
					else if(color[row[0]][col[0]] == color[row[0]][col[0]-1]) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You hit yourself");
						stopped = true;
					}
					else if(snake.getSize() == 256) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You Win!");
						stopped = true;
					}
					else {
						snake.moveLeft();
						snake.eat();
					}
				}
				else if(facingRight == true) {
					if(col[0] == 15) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You hit the wall");
						stopped = true;
					}
					else if(color[row[0]][col[0]] == color[row[0]][col[0]+1]) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You you hit yourself");
						stopped = true;
					}
					else if(snake.getSize() == 256) {
						gameTimer.cancel();
						timer.cancel();
						info1.setText("You Win!");
						stopped = true;
					}
					else {
						snake.moveRight();
						snake.eat();
					}
				}
				moved = true;//set moved to true because the snake moved 
			}
		};
	}
	
	private class Snake{//snake class
		private int size;
		
		public Snake() {//constructor for snake
			row[0] = 7;
			col[0] = 7;
			row[1] = 7;
			col[1] = 8;
			//snake starts at this location
			size = 2;//initial size of the snake
		}
		
		public int getSize() {//return the size of the snake
			return size;
		}
		
		public void addSnake() {//add snake to the game board
			grid[row[0]][col[0]].setBackground(snakeColor);
			grid[row[1]][col[1]].setBackground(snakeColor);
			color[row[0]][col[0]] = snakeColor;
			color[row[1]][col[1]] = snakeColor;
		}
		
		public void grow(int i, int j) {//method that allow the snake to grow in size
			row[size] = i;
			col[size] = j;
			//add the location to the 2D array after the previous location of the tail of the snake
			grid[row[size]][col[size]].setBackground(snakeColor);
			color[row[size]][col[size]] = snakeColor;
			//add it to the board
			size++;//increase size
			if(size == NUM) {//check if the player won the game
				info1.setText("You Win!");
				gameTimer.cancel();
				timer.cancel();
			}
		}
		
		public void eat() {//check if the snake ate an apple
			if(row[0] == apple.getRow() && col[0] == apple.getCol()) {//if the location of the head is equal to the location of the apple
				imageLabel[apple.getRow()][apple.getCol()].setIcon(null);//set that icon at that location to null
				snake.grow(lastRow, lastCol);//snake grow in size
				apple = new Apple();//create a new apple
				apple.addApple();//add the new apple to the board
			}
		}
		
		public void moveUp() {//method that allow the snake to move up
			for(int i = size - 1; i >= 0; i--) {//starting at the tail of the snake
				if(i == size - 1) {//if it is the tail of the snake
					grid[row[i]][col[i]].setBackground(white);
					color[row[i]][col[i]] = white;
					//set the background of the current location to white
					row[i] = row[i-1];
					col[i] = col[i-1];
					//set the current location to the location before it
					grid[row[i]][col[i]].setBackground(snakeColor);
					//set the background of the next location to the color of the snake
				}
				else if(i == 0){//if it is the head of the snake
					row[i] = row[i] - 1;//the first number in the row array decrease by 1
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
					//set the next location to the color of the snake
				}
				else{//else if it is the middle part of the snake
					row[i] = row[i-1];
					col[i] = col[i-1];
					//set the current location to the location before it
					grid[row[i]][col[i]].setBackground(snakeColor);
					//set the background of the next location to the color of the snake
				}
			}
		}
		
		public void moveDown() {
			for(int i = size - 1; i >= 0; i--) {
				if(i == size - 1) {
					grid[row[i]][col[i]].setBackground(white);
					color[row[i]][col[i]] = white;
					row[i] = row[i-1];
					col[i] = col[i-1];							
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
				}
				else if(i == 0){
					row[i] = row[i] + 1;
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
				}
				else{
					row[i] = row[i-1];
					col[i] = col[i-1];		
					grid[row[i]][col[i]].setBackground(snakeColor);
				}
			}
		}
		
		public void moveLeft() {
			for(int i = size - 1; i >= 0; i--) {
				if(i == size - 1) {
					grid[row[i]][col[i]].setBackground(white);
					color[row[i]][col[i]] = white;
					row[i] = row[i-1];
					col[i] = col[i-1];							
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
				}
				else if(i == 0){
					col[i] = col[i] - 1;
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
				}
				else{
					row[i] = row[i-1];
					col[i] = col[i-1];		
					grid[row[i]][col[i]].setBackground(snakeColor);
				}
			}
		}
		
		public void moveRight() {
			for(int i = size - 1; i >= 0; i--) {
				if(i == size - 1) {
					grid[row[i]][col[i]].setBackground(white);
					color[row[i]][col[i]] = white;
					row[i] = row[i-1];
					col[i] = col[i-1];							
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
				}
				else if(i == 0){
					col[i] = col[i] + 1;
					grid[row[i]][col[i]].setBackground(snakeColor);
					color[row[i]][col[i]] = snakeColor;
				}
				else{
					row[i] = row[i-1];
					col[i] = col[i-1];			
					grid[row[i]][col[i]].setBackground(snakeColor);
				}
			}
		}
		
	}
	
	private class Apple{//apple class
		private int randomRow;
		private int randomCol;
		
		public Apple() {//constructor for apple
			do {
				randomRow = (int)(Math.random()*16);
				randomCol = (int)(Math.random()*16);
				//randomize a location for the apple
			}
			while(color[randomRow][randomCol] != white);			
		}
		
		public void addApple() {//method that add the apple to the randomized location
			imageLabel[randomRow][randomCol].setIcon(new ImageIcon(pic));
		}
		
		public int getRow() {//return the row of the apple
			return randomRow;
		}
		
		public int getCol() {//return the col of the apple
			return randomCol;
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		int KeyCode = arg0.getKeyCode();
		if(moved == true) {//if the snake moved
			if(facingUp == true || facingDown == true) {//if the snake is current facing up or facing down
				if(KeyCode == KeyEvent.VK_LEFT) {//if the user clicked left, set facingLeft to true, and set the others to false
					facingUp = false;
					facingDown = false;
					facingLeft = true;
					facingRight = false;
				}
				else if(KeyCode == KeyEvent.VK_RIGHT) {//if the user clicked right, set facingRight to true, and set the others to false
					facingUp = false;
					facingDown = false;
					facingLeft = false;
					facingRight = true;
				}
			}
			else if(facingLeft == true || facingRight == true) {//if the snake is currently facing left or facing right
				if(KeyCode == KeyEvent.VK_UP) {//if the user clicked up, set facingUp to true, and set the others to false
					facingUp = true;
					facingDown = false;
					facingLeft = false;
					facingRight = false;
				}
				else if(KeyCode == KeyEvent.VK_DOWN) {//if the user clicked down, set facingDown to true, and set the others to false
					facingUp = false;
					facingDown = true;
					facingLeft = false;
					facingRight = false;
				}
			}
		moved = false;//set moved to false, I use this to prevent the user from clicking two different buttons in one move
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object source = arg0.getSource();
		if(source == start) {//if the user clicked the start button
			if(paused == true) {//if the game was paused
				resume();//resume method
				start.setText("Start");
				info1.setText("Resumed");
				paused = false;//set paused to false
			}
			else if(paused == false){//if the game was not paused
				if(newGame == true) {//if it is a new game
					start();//start method
					info1.setText("Start");
					newGame = false;//set newGame to false
				}
				else {//after the first game, the play can only click RESTART to restart the game
					info1.setText("Click RESTART button");
				}
			}
		}
		else if(source == pause) {//if the user clicked the pause button
			pause();//pause method
			info1.setText("Paused");
			paused = true;//set paused to true
			start.setText("Resumed");
		}
		else if(source == restart) {//if the user clicked the restart button
			if(stopped == true || paused == true) {//if the game was stopped because the user lost the game or was paused
				clear();//clear method
				start();//start method
				info1.setText("Start");
				displayTime();//display time method
				stopped = false;//set stopped to false
				paused = false;
			}
			else if(newGame == false){//if the user is currently playing the game
				timer.cancel();
				gameTimer.cancel();
				//cancel both timer
				clear();//clear method
				start();//start method
				info1.setText("Game Restarted!");
				displayTime();//display time method
			}
		}
		else if(source == exit) {//if the user clicked the exit button
			super.dispose();
		}
		//if the user clicked any of the color buttons, change the color of the snake
		else if(source == green) {
			snakeColor = Color.GREEN;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == blue) {
			snakeColor = Color.BLUE;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == yellow) {
			snakeColor = Color.YELLOW;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == pink) {
			snakeColor = Color.PINK;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == orange) {
			snakeColor = Color.ORANGE;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == red) {
			snakeColor = Color.RED;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == gray) {
			snakeColor = Color.GRAY;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
		else if(source == black) {
			snakeColor = Color.BLACK;
			for(int i = 0; i < snake.getSize(); i++) {
				grid[row[i]][col[i]].setBackground(snakeColor);
			}
		}
	}
	
}
