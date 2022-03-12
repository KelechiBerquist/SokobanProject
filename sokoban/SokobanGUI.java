package sokoban;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
// import javax.swing.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import sokoban.SokobanTextUI;
import sokoban.Sokoban;
import sokoban.SokobanException;
import sokoban.RandomPlayer;
import sokoban.Player;
import sokoban.Direction;

/**
 * A text-based user interface for a Sokoban puzzle.
 *
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class SokobanGUI  {
// public class SokobanGUI extends JFrame implements ActionListener {
	/**
	 * Default constructor
	 */
	public SokobanGUI() {
		scnr = new Scanner(System.in);

		Image brickImg         = new ImageIcon(
			workDir + "/image/brick-wall.png"
		).getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image boxImg           = new ImageIcon(
			workDir + "/image/icons8-box-16.png"
		).getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image humanImg         = new ImageIcon(
			workDir + "/image/icons8-human-51.png"
		).getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image targetImg        = new ImageIcon(
			workDir + "/image/icons8-target-48.png"
		).getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image humanTargetImg   = new ImageIcon(
			workDir + "/image/icons8-target-64.png"
		).getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image bullseyeImg      = new ImageIcon(
			workDir + "/image/icons8-bullseye-48.png"
		).getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);

		// // - ‘#’ for a wall;
		// // - ‘$’ for a box;
		// // - ‘@’ for the actor;
		// // - ‘.’ for a target;
		// // - ‘+’ for the actor on a target; and
		// // - ‘*’ for a box on a target.


		symbolMap = new HashMap<String,ImageIcon>();
		symbolMap.put("#", new ImageIcon(brickImg));
		symbolMap.put("$", new ImageIcon(boxImg));
		symbolMap.put("@", new ImageIcon(humanImg));
		symbolMap.put(".", new ImageIcon(targetImg));
		symbolMap.put("+", new ImageIcon(humanTargetImg));
		symbolMap.put("*", new ImageIcon(bullseyeImg));
	}

	/**
	 *
	 */
	public void initGame() {
		fileList = readFileInList();
		puzzle = new Sokoban(new File(screenPath));
		player = new RandomPlayer();
		currGameState = puzzle.toString();
	}


	/**
	 * Generate game from currently available class variables
	 */
	public void genGame() {

	}

	/**
	 *
	 */
	public void generateWindow() {
		initGame();
		genFrame();
		// String myName = genInputPanel("What is your name?");
		// System.out.println(myName);
	}


	/**
	 *
	 */
	public void genFrame() {
		JMenuBar menuBar = genMenuBar();
		JPanel   panel   = genFooter();
		JPanel   board   = genBoard();
		JFrame   frame   = new JFrame("Sokoban GUI");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout(100, 50));
		frame.setSize(frameHeight, frameWidth);
		frame.getContentPane().add(BorderLayout.SOUTH, panel);
		frame.getContentPane().add(BorderLayout.CENTER, board);
		frame.getContentPane().add(BorderLayout.NORTH, menuBar);
		frame.setVisible(true);
	}


	/**
	 *
	 */
	public ArrayList<String> readFileInList() {
		Integer randomNum = ThreadLocalRandom.current().nextInt(minScreen, maxScreen + 1);

		screenFile = "screen." + randomNum.toString();
		screenPath = workDir + "/screens/" + screenFile;

		System.out.println("File to be read:" + screenPath);
		ArrayList<String> screenList = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new FileReader(screenPath)).useDelimiter("\n");
			while (scanner.hasNext()){
				screenList.add(scanner.next());
			}
			scanner.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File not found. Exiting program");
		}
		return screenList;
	}

	/**
	 *
	 */
	public JPanel genBoard() {
		JPanel board = new JPanel();
		for (Integer i = 0; i < numBtnRows; i++){
			Integer countBox = 0;
			String row = null;
			if ( i < fileList.size()){
				row = fileList.get(i);
				System.out.println(row);
				if (row.length() > 0){
					countBox = row.length();
				}
			}

			for (Integer j = 0; j < numBtnCols; j++){
				JButton button = null;
				button = new JButton();

				if (countBox != 0 && j >= leftMargin && j < countBox + leftMargin) {
					String theChar =  Character.toString(row.charAt(j - leftMargin));
					if (!(theChar.equalsIgnoreCase(" "))){
						button.setIcon(symbolMap.get(theChar));
						button.setMargin(new Insets(0, 0, 0, 0));

					}
				}
				button.setOpaque(false);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				button.setBorder(null);
				board.add(button);
			}
		}
		board.setSize(frameHeight-10, frameWidth-10);
		board.setLayout(new GridLayout(numBtnRows, numBtnCols));

		return board;
	}

	/**
	 *
	 */
	public JPanel genFooter() {
		JPanel panel = new JPanel();

		JButton undoBtn        =  new JButton("Undo Move");
		JButton randMoveBtn    =  new JButton("Random Move");
		JButton clearBtn       =  new JButton("Restart Game");
		JButton newBtn         =  new JButton("New Game");
		JButton quitBtn        =  new JButton("Quit Game");

		panel.add(undoBtn);
		panel.add(randMoveBtn);
		panel.add(clearBtn);
		panel.add(newBtn);
		panel.add(quitBtn);

		// board.setSize(frameHeight-100, frameWidth-100);
		// board.setLayout(new GridLayout(numBtnRows, numBtnCols));

		return panel;
	}

	/**
	 * Given a frame, this generates a panel that requests information from
	 * user and returns user reponse
	 * @param f the frame where the input will be displayed
	 * @param msg the message displayed to the user
	 * @return input returned from the user
	 */
	public String genInputPanel(JFrame f, String msg) {
		String input = JOptionPane.showInputDialog(f, msg);
		return input;
	}

	/**
	 * This generates a frame and panel that requests information from
	 * user and returns user reponse
	 * @param msg the message displayed to the user
	 * @return input returned from the user
	 */
	public String genInputPanel(String msg) {
		JFrame f = new JFrame();
		String input = JOptionPane.showInputDialog(f, msg);
		return input;
	}

	/**
	 *
	 */
	public JMenuBar genMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu newGame    = new JMenu("New Game");
		JMenu saveGame   = new JMenu("Save Game");
		JMenu loadGame   = new JMenu("Load Saved Game");

		menuBar.add(newGame);
		menuBar.add(saveGame);
		menuBar.add(loadGame);

		return menuBar;
	}

	/**
	 *
	 */
	public void newGamePressed() {

	}

	/**
	 *
	 */
	public void loadGameClicked(ActionEvent evnt) {
		JFileChooser fileChoice = new JFileChooser();

	}

	/**
	 *
	 */
	public static void main(String[] args) {
		SokobanGUI ui = new SokobanGUI();
		ui.generateWindow();
	}

	/**
	 * A trace method for debugging (active when traceOn is true)
	 *
	 * @param s the string to output
	 */
	public static void trace(String s) {
		if (traceOn)
			System.out.println("trace: " + s);
	}

	private SokobanTextUI  textUI    = null;

	private Scanner scnr          = null;
	private Sokoban puzzle        = null;
	private Player  player        = null;
	private String  screenFile    = null;
	private String  screenPath    = null;
	private String  lastMove      = null;

	private ArrayList <String> fileList        = null;
	private ArrayList <String> currGameState   = null;
	private ArrayList <String> prevGameState   = null;

	private static Integer imgHeight     = 20;
	private static Integer imgWidth      = 20;
	private static Integer frameHeight   = 800;
	private static Integer frameWidth    = 800;
	private static Integer leftMargin    = 5;
	private static Integer numBtnRows    = 30;
	private static Integer numBtnCols    = 30;
	private static Integer minScreen     = 1;
	private static Integer maxScreen     = 90;
	private static boolean traceOn       = false; // for debugging
	private static String  workDir       = System.getProperty("user.dir");

	private static HashMap<String,ImageIcon> symbolMap  =  null;

}
