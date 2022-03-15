package sokoban;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.util.concurrent.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * TODO
 * Include Doc String
 * Perhaps, do some styling of components
 * Perform unit testing of components
 */
/**
 * A text-based user interface for a Sokoban puzzle.
 *
 * @author Dr Kelechi Berquist
 * @author Dr Mark C. Sinclair
 * @version March 2022
 */
public class GUI  {
	/**
	 * Default constructor
	 */
	public GUI() {
		Image brickImg         = new ImageIcon(workDir + "/image/brick-wall.png")
			.getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image boxImg           = new ImageIcon(workDir + "/image/icons8-box-16.png")
			.getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image humanImg         = new ImageIcon(workDir + "/image/icons8-human-51.png")
			.getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image targetImg        = new ImageIcon(workDir + "/image/icons8-target-48.png")
			.getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image humanTargetImg   = new ImageIcon(workDir + "/image/icons8-target-64.png")
			.getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);
		Image bullseyeImg      = new ImageIcon(workDir + "/image/icons8-bullseye-48.png").getImage().getScaledInstance(imgHeight, imgWidth, Image.SCALE_DEFAULT);

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
		// genFooterBtns();

		appFrame.setLayout(new BoxLayout(appFrame.getContentPane(), BoxLayout.PAGE_AXIS));
		appFrame.getContentPane().add(boardPanel);
		appFrame.getContentPane().add(adminPanel);
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
		boardPanel.setVisible(true);
		boardPanel.setLayout(new GridLayout(numGrid, numGrid));

		Border paneEdge = BorderFactory.createEmptyBorder(100,200,0,0);
		boardPanel.setBorder(paneEdge);

		for (Integer i = 0; i < numGrid; i++){
			Integer  countBox = 0;
			String   row      = null;
			if ( i < currGameState.size()){
				row = currGameState.get(i);
				if (row.length() > 0){
					countBox = row.length();
				}
			}

			for (Integer j = 0; j < numGrid; j++){
				JButton button = null;
				button = new JButton();
				button.setPreferredSize(new Dimension(20, 20));

				if (countBox != 0 && j >= 0 && j < countBox) {
					String theChar =  Character.toString(row.charAt(j));
					if (!(theChar.equalsIgnoreCase(" "))){
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
		JButton newBtn      = new JButton("New");
		JButton loadBtn     = new JButton("Load");

		quitBtn.addActionListener(e ->    {quitPuzzle();});
		newBtn.addActionListener(e ->     {newGame();});
		loadBtn.addActionListener(e ->    {loadSavedPuzzle();});

		quitBtn.setToolTipText("Quit this game");
		newBtn.setToolTipText("Start a new game");
		loadBtn.setToolTipText("Load previously saved game");

		JButton[] buttonList = {quitBtn, newBtn, loadBtn};

		for (JButton eachButton: buttonList){
			eachButton.setPreferredSize(new Dimension(50, 30));
			eachButton.setOpaque(false);
			eachButton.setContentAreaFilled(false);
			eachButton.setBorderPainted(false);
			eachButton.setBorder(null);
			eachButton.setFont(appFont);
		}

		adminPanel  =  new JPanel();
		adminPanel.setLayout(new GridLayout(1, 4));
		adminPanel.add(quitBtn);
		adminPanel.add(newBtn);
		adminPanel.add(loadBtn);
	}

	/**
	 *
	 */
	public void genFullAdminBtns() {
		JButton quitBtn     = new JButton("Quit");
		JButton saveBtn     = new JButton("Save");
		JButton restartBtn  = new JButton("Restart");
		JButton newBtn      = new JButton("New");
		JButton loadBtn     = new JButton("Load");

		quitBtn.addActionListener(e ->    {appFrame.dispose();});
		saveBtn.addActionListener(e ->    {savePuzzle();});
		restartBtn.addActionListener(e -> {clearPuzzle();});
		newBtn.addActionListener(e ->     {newGame();});
		loadBtn.addActionListener(e ->    {loadSavedPuzzle();});

		quitBtn.setToolTipText("Quit this game");
		saveBtn.setToolTipText("Save this game");
		restartBtn.setToolTipText("Restart this game");
		newBtn.setToolTipText("Start a new game");
		loadBtn.setToolTipText("Load previously saved game");

		JButton[] buttonList = {quitBtn, saveBtn, restartBtn, newBtn, loadBtn};

		for (JButton eachButton: buttonList){
			eachButton.setPreferredSize(new Dimension(50, 30));
			eachButton.setOpaque(false);
			eachButton.setContentAreaFilled(false);
			eachButton.setBorderPainted(false);
			eachButton.setBorder(null);
			eachButton.setFont(appFont);
		}

		adminPanel  =  new JPanel();
		adminPanel.setLayout(new GridLayout(1, 4));
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
		JButton nMoveBtn     =  new JButton("N");
		JButton eMoveBtn     =  new JButton("E");
		JButton wMoveBtn     =  new JButton("W");
		JButton sMoveBtn     =  new JButton("S");
		JButton rMoveBtn     =  new JButton("P");
		JButton uMoveBtn     =  new JButton("Undo");
		JButton hMoveBtn     =  new JButton("Help");
		JButton saveBtn      =  new JButton("Save");
		JButton restartBtn   =  new JButton("Restart");
		JButton invisible1   =  new JButton("");
		JButton invisible2   =  new JButton("");
		JButton invisible3   =  new JButton("");
		JButton invisible4   =  new JButton("");

		nMoveBtn.setToolTipText("Move north");
		eMoveBtn.setToolTipText("Move east");
		wMoveBtn.setToolTipText("Move west");
		sMoveBtn.setToolTipText("Move south");
		rMoveBtn.setToolTipText("Computer Mmove");
		uMoveBtn.setToolTipText("Undo last move");
		hMoveBtn.setToolTipText("Get help on how to play");
		saveBtn.setToolTipText("Save this game");
		restartBtn.setToolTipText("Restart this game");

		nMoveBtn.addActionListener(e ->   {move(Direction.NORTH);});
		wMoveBtn.addActionListener(e ->   {move(Direction.WEST);});
		eMoveBtn.addActionListener(e ->   {move(Direction.EAST);});
		sMoveBtn.addActionListener(e ->   {move(Direction.SOUTH);});
		saveBtn.addActionListener(e ->    {savePuzzle();});
		restartBtn.addActionListener(e -> {clearPuzzle();});
		uMoveBtn.addActionListener(e ->   {undoMove();});
		rMoveBtn.addActionListener(e ->   {playerMove();});
		hMoveBtn.addActionListener(e ->   {showHelp();});

		JButton[] buttonList = {
			nMoveBtn, eMoveBtn, wMoveBtn, sMoveBtn, uMoveBtn,
			rMoveBtn, hMoveBtn, saveBtn, restartBtn,
			invisible1, invisible2, invisible3, invisible4
		};

		for (JButton eachButton: buttonList){
			if (
				eachButton.getText().equals("Help") ||
				eachButton.getText().equals("Undo") ||
				eachButton.getText().equals("Save") ||
				eachButton.getText().equals("Restart")
			){
				eachButton.setPreferredSize(new Dimension(100, 30));
			} else {
				eachButton.setPreferredSize(new Dimension(50, 30));
			}

			eachButton.setOpaque(false);
			eachButton.setContentAreaFilled(false);
			eachButton.setBorderPainted(false);
			eachButton.setBorder(null);
			eachButton.setFont(appFont);
		}


		JPanel nPanel = new JPanel(new FlowLayout());
		JPanel sPanel = new JPanel(new FlowLayout());
		nPanel.add(invisible1);
		nPanel.add(nMoveBtn);
		nPanel.add(invisible2);
		sPanel.add(invisible3);
		sPanel.add(sMoveBtn);
		sPanel.add(invisible4);


		JPanel movePanel = new JPanel();
		movePanel.setLayout(new BorderLayout(10,10));
		movePanel.add(nPanel,   BorderLayout.NORTH);
		movePanel.add(sPanel,   BorderLayout.SOUTH);
		movePanel.add(eMoveBtn, BorderLayout.EAST);
		movePanel.add(wMoveBtn, BorderLayout.WEST);
		movePanel.add(rMoveBtn, BorderLayout.CENTER);

		Border paneEdge = BorderFactory.createEmptyBorder(30,30,30,30);
		movePanel.setBorder(paneEdge);


		/**
		 * Set play button panels.
		 */
		playPanel  =  new JPanel();
		playPanel.add(saveBtn);
		playPanel.add(restartBtn);
		playPanel.add(movePanel);
		playPanel.add(uMoveBtn);
		playPanel.add(hMoveBtn);
	}


	/**
	 *
	 */
	public void genFooterBtns() {
		JButton nMoveBtn    =  new JButton("N");
		JButton eMoveBtn    =  new JButton("E");
		JButton wMoveBtn    =  new JButton("W");
		JButton sMoveBtn    =  new JButton("S");
		JButton rMoveBtn    =  new JButton("P");
		JButton uMoveBtn    =  new JButton("Undo");
		JButton hMoveBtn    =  new JButton("Help");
		JButton invisible1  =  new JButton("");
		JButton invisible2  =  new JButton("");
		JButton invisible3  =  new JButton("");
		JButton invisible4  =  new JButton("");
		JButton quitBtn     =  new JButton("Quit");
		JButton saveBtn     =  new JButton("Save");
		JButton restartBtn  =  new JButton("Restart");
		JButton newBtn      =  new JButton("New");
		JButton loadBtn     =  new JButton("Load");


		nMoveBtn.addActionListener(e -> {move(Direction.NORTH);});
		wMoveBtn.addActionListener(e -> {move(Direction.WEST);});
		eMoveBtn.addActionListener(e -> {move(Direction.EAST);});
		sMoveBtn.addActionListener(e -> {move(Direction.SOUTH);});
		uMoveBtn.addActionListener(e -> {undoMove();});
		rMoveBtn.addActionListener(e -> {playerMove();});
		quitBtn.addActionListener(e -> {appFrame.dispose();});
		saveBtn.addActionListener(e -> {savePuzzle();});
		restartBtn.addActionListener(e -> {clearPuzzle();});
		newBtn.addActionListener(e -> {newGame();});
		loadBtn.addActionListener(e -> {loadSavedPuzzle();});


		JButton[] buttonList = {
			nMoveBtn, eMoveBtn, wMoveBtn, sMoveBtn, uMoveBtn,
			rMoveBtn, hMoveBtn, quitBtn, saveBtn, restartBtn,
			newBtn, loadBtn, invisible1, invisible2,
			invisible3, invisible4
		};

		for (JButton eachButton: buttonList){
			if (
				eachButton.getText().equals("Help") ||
				eachButton.getText().equals("Undo")
			){
				eachButton.setPreferredSize(new Dimension(100, 30));
			} else {
				eachButton.setPreferredSize(new Dimension(50, 30));
			}

			eachButton.setOpaque(false);
			eachButton.setContentAreaFilled(false);
			eachButton.setBorderPainted(false);
			eachButton.setBorder(null);
			eachButton.setFont(appFont);
		}

		/**
		 * Define panel for styling player moves
		 */
		JPanel nPanel = new JPanel(new FlowLayout());
		JPanel sPanel = new JPanel(new FlowLayout());
		nPanel.add(invisible1);
		nPanel.add(nMoveBtn);
		nPanel.add(invisible2);
		sPanel.add(invisible3);
		sPanel.add(sMoveBtn);
		sPanel.add(invisible4);

		JPanel movePanel = new JPanel();
		movePanel.setLayout(new BorderLayout(10,10));
		movePanel.add(nPanel,   BorderLayout.NORTH);
		movePanel.add(sPanel,   BorderLayout.SOUTH);
		movePanel.add(eMoveBtn, BorderLayout.EAST);
		movePanel.add(wMoveBtn, BorderLayout.WEST);
		movePanel.add(rMoveBtn, BorderLayout.CENTER);
		Border paneEdge = BorderFactory.createEmptyBorder(30,30,30,30);
		movePanel.setBorder(paneEdge);


		/**
		 * Define panel to style admin instructions
		 */
		JPanel otherPanel = new JPanel(new GridLayout(3, 2));
		otherPanel.add(uMoveBtn);
		otherPanel.add(hMoveBtn);
		otherPanel.add(quitBtn);
		otherPanel.add(saveBtn);
		otherPanel.add(restartBtn);
		otherPanel.add(newBtn);
		otherPanel.add(loadBtn);



		playPanel  =  new JPanel(new FlowLayout());
		playPanel.add(movePanel);
		playPanel.add(otherPanel);
		// playPanel.add(hMoveBtn);
		// playPanel.add(quitBtn);
		// playPanel.add(saveBtn);
		// playPanel.add(restartBtn);
		// playPanel.add(newBtn);
		// playPanel.add(loadBtn);
	}

	/**
	 * Refresh frame and clear output message
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
	 * Get puzzle state
	 */
	public String getPuzzleState() {
		return puzzle.toString();
	}


	/**
	 * Load saved puzzle from file
	 */
	public void showHelp() {
		String helpText = """
		Read up on how to play the game
		\"https://en.wikipedia.org/wiki/Sokoban\"

		Here are the options within this application:

			[Quit]         Quit the current game
			[New]          Start a new game
			[Load]         Load from a previously saved game
			[Save]         Save the current game
			[Restart]      Restart this game from the beginning
			[Undo]         Undo the previous player move
			[Help]         Opens this help panel
			[N]            Move actor north
			[P]            Make computer move
			[W]            Move actor west
			[E]            Move actor east
			[S]            Move actor south
		""";
		JTextPane helpPane = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(helpPane);
		helpPane.setEditable(false);
		helpPane.setMargin( new Insets(10,10,10,10) );
		helpPane.setBackground(new Color(224, 255, 255));
		helpPane.setForeground(new Color(43, 27, 23, 255));
		helpPane.setFont(appFont);
		// helpPane.setContentType("text/html");
		helpPane.setText(helpText);


		JOptionPane.showMessageDialog(new JFrame(), helpPane);
	}

	/**
	 * Give player the option of saving the game before quitting
	 */
	public void quitPuzzle() {
		System.out.println("Program shutting down.");
		outMsg = "Program shutting down.";
		genFrame();
		appFrame.setVisible(false); //you can't see me!
		appFrame.dispose(); //Destroy the JFrame object
		System.exit(0);
	}

	/**
	 *
	 */
	public static void main(String[] args) {
		GUI ui = new GUI();
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
	private JFrame      appFrame       = new JFrame("Sokoban Game");

	private String  outMsg          = "";
	private String  currCommand     = "";
	private String  lastCommand     = "";
	private String  screenFile      = null;
	private String  screenPath      = null;

	private static Integer imgHeight     = 20;
	private static Integer imgWidth      = 20;
	private static Integer frameHeight   = 800;
	private static Integer frameWidth    = 800;
	private static Integer numGrid       = 30;
	private static Integer minScreen     = 1;
	private static Integer maxScreen     = 90;
	private static boolean traceOn       = true; // for debugging
	private static String  workDir       = System.getProperty("user.dir");
	private static Font    appFont       = new Font("Monospaced", Font.BOLD, 20);

	private static HashMap<String,ImageIcon> symbolMap  =  null;
}
