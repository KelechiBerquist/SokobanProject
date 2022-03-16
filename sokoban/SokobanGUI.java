 

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import sokoban.Sokoban;
import sokoban.SokobanException;
import sokoban.RandomPlayer;
import sokoban.Player;
import sokoban.Direction;

/**
 * A text-based user interface for a Sokoban puzzle.
 *
 * @author Dr Kelechi Berquist
 * @author Dr Mark C. Sinclair
 * @version March 2022
 */
public class SokobanGUI  {
// public class SokobanGUI extends JFrame implements ActionListener {
	/**
	 * Default constructor
	 */
	public SokobanGUI() {
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

		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		appFrame.setLayout(new BorderLayout(50, 50));
		appFrame.setSize(frameHeight, frameWidth);
	}


	/**
	 * Set up new game
	 */
	public void newGame() {
		System.out.println("\n\nBeginning new game");
		initGameAttr();
		genGame();
	}

	/**
	 *
	 */
	public void initGameAttr() {
		Integer randomNum   =   ThreadLocalRandom.current().nextInt(minScreen, maxScreen + 1);
		screenFile          =   "screen." + randomNum.toString();
		screenPath          =   workDir + "/screens/" + screenFile;
		loadFile            =   new File(screenPath);
	}

	/**
	 *
	 */
	public void genGame() {
		puzzle = new Sokoban(loadFile);
		player = new RandomPlayer();
		genFrame();
	}

	/**
	 *
	 */
	public void genFrame() {
		appFrame.getContentPane().removeAll();
		appFrame.invalidate();
		appFrame.revalidate();

		genBoard();
		genOutMsgPane();
		genAdminBtns();
		genPlayBtns();

		appFrame.getContentPane().add(adminPanel);
		appFrame.setLayout(new BoxLayout(appFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		appFrame.getContentPane().add(boardPanel);
		appFrame.getContentPane().add(playPanel);
		appFrame.getContentPane().add(outputPane);
		appFrame.pack();
		appFrame.setVisible(true);
	}

	/**
	 *
	 */
	public void genOutMsgPane() {
		outputPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(outputPane);
		outputPane.setEditable(false);
		if (!(outMsg).equals("")){
			outputPane.setMargin( new Insets(10,10,10,10) );
			outputPane.setBackground(new Color(224, 255, 255));
			outputPane.setForeground(new Color(43, 27, 23, 255));
			outputPane.setFont(appFont);
			outputPane.setText(outMsg);
		} else {
			outputPane.setMargin( new Insets(10,10,10,10) );
			outputPane.setBackground(new Color(224, 255, 255));
			outputPane.setForeground(outputPane.getBackground());
		}
	}

	/**
	 *
	 */
	public void genBoard() {
		ArrayList<String> currGameState = Helpers.listFromString(puzzle.toString());
		boardPanel = new JPanel();
		boardPanel.setSize(frameHeight-10, frameWidth-10);
		boardPanel.setLayout(new GridLayout(numGrid, numGrid));

		Border paneEdge = BorderFactory.createEmptyBorder(30,200,0,0);
		boardPanel.setBorder(paneEdge);

		for (Integer i = 0; i < numGrid; i++){
			Integer  countBox = 0;
			String   row      = null;
			if ( i < currGameState.size()){
				row = currGameState.get(i);
				// System.out.println(row);
				if (row.length() > 0){
					countBox = row.length();
				}
			}

			for (Integer j = 0; j < numGrid; j++){
				JButton button = null;
				button = new JButton();

				if (countBox != 0 && j >= 0 && j < countBox) {
					String theChar =  Character.toString(row.charAt(j));
					if (!(puzzle.onTarget() | theChar.equalsIgnoreCase(" "))){
						button.setIcon(symbolMap.get(theChar));
						button.setMargin(new Insets(0, 0, 0, 0));
					}
				}
				button.setOpaque(false);
				button.setContentAreaFilled(false);
				button.setBorderPainted(false);
				button.setBorder(null);
				boardPanel.add(button);
			}
		}
	}

	/**
	 *
	 */
	public void genAdminBtns() {
		JButton quitBtn     = new JButton("Quit");
		JButton saveBtn     = new JButton("Save");
		JButton restartBtn  = new JButton("Restart");
		JButton newBtn      = new JButton("New");
		JButton loadBtn     = new JButton("Load");

		quitBtn.addActionListener(e -> {appFrame.dispose();});
		saveBtn.addActionListener(e -> {savePuzzle();});
		restartBtn.addActionListener(e -> {clearPuzzle();});
		newBtn.addActionListener(e -> {newGame();});
		loadBtn.addActionListener(e -> {loadSavedPuzzle();});

		adminPanel  =  new JPanel();
		adminPanel.setLayout(new FlowLayout());
		adminPanel.add(quitBtn);
		adminPanel.add(saveBtn);
		adminPanel.add(restartBtn);
		adminPanel.add(newBtn);
		adminPanel.add(loadBtn);
	}

	/**
	 *
	 */
	public void genPlayBtns() {
		JButton nMoveBtn   = new JButton("Move North");
		JButton eMoveBtn   = new JButton("Move East");
		JButton wMoveBtn   = new JButton("Move West");
		JButton sMoveBtn   = new JButton("Move South");
		JButton uMoveBtn   = new JButton("Undo Move");
		JButton rMoveBtn   = new JButton("Random Move");

		nMoveBtn.addActionListener(e -> {move(Direction.NORTH);});
		wMoveBtn.addActionListener(e -> {move(Direction.WEST);});
		eMoveBtn.addActionListener(e -> {move(Direction.EAST);});
		sMoveBtn.addActionListener(e -> {move(Direction.SOUTH);});
		uMoveBtn.addActionListener(e -> {undoMove();});
		rMoveBtn.addActionListener(e -> {playerMove();});

		playPanel  =  new JPanel();
		playPanel.setLayout(new FlowLayout());
		playPanel.add(nMoveBtn);
		playPanel.add(wMoveBtn);
		playPanel.add(eMoveBtn);
		playPanel.add(sMoveBtn);
		playPanel.add(uMoveBtn);
		playPanel.add(rMoveBtn);

	}

	/**
	 * Refresh frame and include output message
	 *
	 * @param command the user command string
	 */
	private void refresh(String command) {
		outMsg = command;
		genFrame();
		outMsg = "";
	}

	/**
	 * Move the actor according to the computer player's choice
	 */
	private void playerMove() {
		lastCommand = currCommand;
		currCommand = "P";
		Vector<Direction> choices = puzzle.canMove();
		Direction         choice  = player.move(choices);
		move(choice);
	}

	/**
	 * If it is safe, move the actor to the next cell in a given direction
	 *
	 * @param dir the direction to move
	 */
	private void move(Direction dir) {
		String msg       =   "";
		String dirString =   dir.toString();
		lastCommand      =   currCommand;
		currCommand      =   Character.toString(dirString.charAt(0));

		if (!puzzle.canMove(dir)) {
			System.out.println("Invalid move.");
			msg = "Invalid move.";
		} else {
			msg = "Moving " + dirString;
			System.out.println("Moving " + dirString);
			puzzle.move(dir);
		}
		if (puzzle.onTarget()){
			System.out.println("Game Won!");
			msg = "Game Won!";
		}
		refresh(msg);
	}

	/**
	 * Undo last player move if move is E, W, N, S
	 */
	public void undoMove() {
		lastCommand = currCommand;
		currCommand = "U";
		String msg  =  "";
		if (
			lastCommand.equalsIgnoreCase("N") ||
			lastCommand.equalsIgnoreCase("S") ||
			lastCommand.equalsIgnoreCase("E") ||
			lastCommand.equalsIgnoreCase("W") ||
			lastCommand.equalsIgnoreCase("P")
		) {
			try {
				puzzle.undo();
				System.out.println("Move undone.");
				msg = "Move undone.";
			} catch (IllegalStateException ex) {
				System.out.println("No previous moves available!");
				msg = "No previous moves available!";
			}
		} else {
			System.out.println("This move cannot be undone.");
			msg = "This move cannot be undone.";
		}
		refresh(msg);
	}

	/**
	 * Reset the puzzle state to the initial puzzle screen
	 */
	public void clearPuzzle() {
		lastCommand = currCommand;
		currCommand = "C";
		String msg  = "";
		try {
			puzzle.clear();
			System.out.println("\n\nResetting puzzle to initial state.");
			msg = "Resetting puzzle to initial state.";
		} catch (IllegalStateException ex) {
			System.out.println("No initial game state available!");
			msg = "No initial game state available!";
		}
		refresh(msg);
	}

	/**
	 * Save puzzle state to file
	 */
	public void savePuzzle() {
		lastCommand = currCommand;
		currCommand = "V";
		String msg  = "";
		if (puzzle == null) {
			System.out.println("No game available to save.");
			msg = "No game available to save.";
		} else {
			DateTimeFormatter dtf   =  DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
			LocalDateTime     now   =  LocalDateTime.now();
			String filename = "puzzle-" + dtf.format(now);
			String filepath = workDir + "/snapshot/" + filename;
			String relFilepath = "./snapshot/" + filename;

			try (PrintStream out = new PrintStream(new FileOutputStream(filepath))) {
				out.print(puzzle.toString());
				System.out.println("Puzzle saved in: " + filepath + "\n");
				msg = "Puzzle saved in: \n" + relFilepath + "\n";
			} catch (FileNotFoundException e){
				throw new SokobanException("File not found" + e);
			}
		}
		refresh(msg);
	}

	/**
	 * Load saved puzzle from file
	 */
	public void loadSavedPuzzle() {
		lastCommand = currCommand;
		currCommand = "L";
		String msg  = "";

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int fileChoice = fileChooser.showOpenDialog(appFrame);
		if(fileChoice == JFileChooser.APPROVE_OPTION){
			loadFile  =  fileChooser.getSelectedFile();
			System.out.println("Loaded saved game from \n" + loadFile.getAbsolutePath() + "\n");
			System.out.println("Before Load \n" + puzzle.toString() + "\n");
			msg = "Loaded saved game from " + loadFile.getAbsolutePath() + "\n";
			genGame();
			System.out.println("After Load \n" + puzzle.toString() + "\n");
		} else{
			System.out.println("Unable to load saved game. Loading new game\n");
			msg = "Unable to load saved game. Loading new game\n";
			refresh(msg);
		}

	}


	/**
	 *
	 */
	public static void main(String[] args) {
		SokobanGUI ui = new SokobanGUI();
		ui.newGame();
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

	private Sokoban puzzle     = null;
	private Player  player     = null;
	private File    loadFile   = null;

	private JTextPane   outputPane     = null;
	private JPanel      boardPanel     = null;
	private JPanel      adminPanel     = null;
	private JPanel      playPanel      = null;
	private JFrame      appFrame       = new JFrame("Sokoban GUI");

	private String  outMsg          = "";
	private String  currCommand     = "";
	private String  lastCommand     = "";
	private String  screenFile      = null;
	private String  screenPath      = null;

	private static Integer imgHeight     = 20;
	private static Integer imgWidth      = 20;
	private static Integer frameHeight   = 500;
	private static Integer frameWidth    = 500;
	private static Integer numGrid       = 30;
	private static Integer minScreen     = 1;
	private static Integer maxScreen     = 90;
	private static boolean traceOn       = true; // for debugging
	private static String  workDir       = System.getProperty("user.dir");
	private static Font    appFont       = new Font("Monospaced", Font.BOLD, 15);

	private static HashMap<String,ImageIcon> symbolMap  =  null;
}
