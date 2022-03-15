package sokoban;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * A text-based user interface for a Sokoban puzzle.
 *
 * @author Dr Mark C. Sinclair
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class TextUI {
	/**
	 * Default constructor
	 */
	public TextUI() {
		scnr = new Scanner(System.in);
		getScreenFile();
		genGame();
	}

	/**
	 * Constructor given path to file
	 * @param filename name of file with initialisation screen
	 */
	public TextUI(String filename) {
		scnr = new Scanner(System.in);
		// System.out.println(filename.split(workDir));
		screenPath = filename;
		genGame();
	}

	/**
	 * The main game control loop.  This displays the puzzle, then enters a loop displaying a menu,
	 * getting the user command, executing the command, displaying the puzzle and checking
	 * if further moves are possible
	 */
	public void menu() {
		String command = "";
		System.out.println("Loading game from the file: " + screenPath);
		System.out.print(puzzle);
		while (!command.equalsIgnoreCase("Q") && !puzzle.onTarget())  {
			displayMenu();
			command = getCommand();
			execute(command);
			System.out.print("\n" + puzzle);
			if (puzzle.onTarget())
				System.out.println("puzzle is complete");
			trace("onTarget: "+puzzle.numOnTarget());
		}
	}

	/**
	 * Display the user menu
	 */
	private void displayMenu()  {
		System.out.println("""
			Enter command using the keyboard.
			Possible commands:
					Move North               [N]
					Move South               [S]
					Move East                [E]
					Move West                [W]
					Player Move              [P]
					Undo Move                [U]
					New Game                 [A]
					Restart Game             [R]
					Save Game                [V]
					Load Saved Game          [L]
					Quit Game                [Q]
		""");
	}

	/**
	 * Get the user command
	 *
	 * @return the user command string
	 */
	private String getCommand() {
		System.out.print ("Enter command: ");
		return scnr.nextLine();
	}

	/**
	 * Execute the user command string
	 *
	 * @param command the user command string
	 */
	public String execute(String command) {
		String returnVal = "";
		commands.add(command);
		if (command.equalsIgnoreCase("Q")) {
			quitPuzzle();
		} else if (command.equalsIgnoreCase("N")) {
			/**
			 * Move the actor north
			*/
			move(Direction.NORTH);
		} else if (command.equalsIgnoreCase("S")) {
			/**
			 * Move the actor south
			 */
			move(Direction.SOUTH);
		} else if (command.equalsIgnoreCase("E")) {
			/**
			 * Move the actor east
			 */
			move(Direction.EAST);
		} else if (command.equalsIgnoreCase("W")) {
			/**
			 * Move the actor west
			 */
			move(Direction.WEST);
		} else if (command.equalsIgnoreCase("P")) {
			/**
			 * Move the actor according to the computer player's choice
			 */
			Vector<Direction> choices = puzzle.canMove();
			Direction         choice  = player.move(choices);
			move(choice);
		} else if (command.equalsIgnoreCase("U")) {
			undoMove();
		} else if (command.equalsIgnoreCase("R")) {
			clearPuzzle();
		} else if (command.equalsIgnoreCase("A")) {
			newGame();
		} else if (command.equalsIgnoreCase("V")) {
			returnVal = savePuzzle();
		} else if (command.equalsIgnoreCase("L")) {
			loadSavedPuzzle();
		} else {
			System.out.println("Unknown command (" + command + ")");
		}
		return returnVal;
	}

	/**
	 * If it is safe, move the actor to the next cell in a given direction
	 *
	 * @param dir the direction to move
	 */
	private void move(Direction dir) {
		if (!puzzle.canMove(dir)) {
			System.out.println("invalid move");
			return;
		}
		puzzle.move(dir);
		if (puzzle.onTarget()){
			System.out.println("game won!");
		}
	}

	/**
	 * Initialising screen file randomly
	 */
	private void getScreenFile() {
		Integer randomNum = ThreadLocalRandom.current()
			.nextInt(minScreen, maxScreen + 1);
		screenPath = workDir + "/screens/" + "screen." + randomNum.toString();
	}

	/**
	 * Set screen file from given input
	 */
	private void setScreenFile(String filename) {
		screenPath = filename;
	}

	/**
	 * Generate game board from currently available class variables
	 */
	private void genGame() {
		puzzle = new Sokoban(new File(screenPath));
		player = new RandomPlayer();
		commands = new ArrayList<String>();
	}


	/**
	 * Set up new game
	 */
	public void newGame() {
		System.out.println("\n\nBeginning new game");
		getScreenFile();
		genGame();
	}

	/**
	 * Undo last player move if move is E, W, N, S, P
	 */
	public void undoMove() {
		String lastCommand = commands.get(commands.size()-2);
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
			} catch (IllegalStateException ex) {
				System.out.println("No previous moves available!");
			}
		} else {
			System.out.println("This move cannot be undone.");
		}
	}

	/**
	 * Reset the puzzle state to the initial puzzle screen
	 */
	public void clearPuzzle() {
		try {
			puzzle.clear();
			System.out.println("\n\nResetting puzzle to initial state.");
		} catch (IllegalStateException ex) {
			System.out.println("No initial game state available!");
		}
	}

	/**
	 * Save puzzle state to file
	 */
	public String savePuzzle() {
		String filepath  = "";
		if (puzzle == null) {
			System.out.println("No game available to save.");
		} else {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
			LocalDateTime now     = LocalDateTime.now();
			filepath   =  workDir + "/snapshot/" + "puzzle-" + dtf.format(now);

			try (PrintStream out = new PrintStream(new FileOutputStream(filepath))) {
				out.print(puzzle.toString());
				System.out.println("Puzzle saved in: " + filepath + "\n");

			} catch (FileNotFoundException e){
				throw new SokobanException("File not found" + e);
			}
		}
		return filepath;
	}

	/**
	 * Choose saved puzzle from file options
	 */
	public String chooseSavedPuzzle() {
		System.out.println("\n\nLocating saved game files.");
		String   retPath     =   "";
		File     f           =   new File(workDir + "/snapshot");
		String[] filePaths   =   f.list();

		if (filePaths.length == 0){
			/**
			 * If no saved file available, generate game using saved file
			 */
			System.out.println("No saved game files available.");
		} else {
			/**
			 * If saved file available, print options for user
			 */
			System.out.println("Select a saved game file from the options below:");
			Integer optionInt = 0;
			for (String filepath: filePaths){
				optionInt++;
				String formattedPath   =  "    " + String.format("%-30s", filepath);
				String formattedOption =  String.format("[%d]", optionInt);
				System.out.println(formattedPath + formattedOption);
			}
			/**
			 * Use reponse to retrieve saved file and display file
			 */
			String response = getCommand();
			try {
				Integer gameIndex = Integer.parseInt(response);
				if (gameIndex > 0 && gameIndex <= filePaths.length)
				{
					/**
					 * If valid integer was provided, generate game using saved file
					 */
					retPath = filePaths[gameIndex-1];
				} else {
					/**
					 * If invalid integer was provided, generate new game
					 */
					System.out.println("Invalid option selected (" + response + ")");
				}
			} catch (NumberFormatException ex){
				/**
				 * If invalid input was provided, generate new game
				 */
				System.out.println("Invalid option selected (" + response + ")");
			}
		}
		return retPath;
	}

	/**
	 * Load saved puzzle from file
	 */
	public void loadSavedPuzzle() {
		System.out.println("\n\nLocating saved game files.");
		String filename = chooseSavedPuzzle();
		String filepath = workDir + "/snapshot/" + filename;
		setScreenFile(filepath);
		genGame();
	}


	/**
	 * Give player the option of saving the game before quitting
	 */
	private void quitPuzzle() {
		System.out.println("""
			Would you like to save the game before quitting?
			Options:
				Yes      [Y]
				No       [N]
		""");
		String response = getCommand();
		if (response.equalsIgnoreCase("Y")){
			savePuzzle();
		} else if (response.equalsIgnoreCase("N")) {
			System.out.println("Game will not be saved before quitting.");
		} else {
			System.out.println("Unknown option provided ("+ response + ").");
		}
		System.out.println("Program shutting down.");
		System.exit(0);
	}

	/**
	 * Get puzzle state
	 */
	public String getPuzzleState() {
		return puzzle.toString();
	}

	public static void main(String[] args) {
		TextUI ui = new TextUI();
		ui.newGame();
		ui.menu();
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

	private Scanner scnr     = null;
	private Sokoban puzzle   = null;
	private Player  player   = null;
	private String  screenPath   = null;
	private ArrayList<String>  commands  = null;

	private static String  workDir      = System.getProperty("user.dir");
	private static Integer minScreen    = 1;
	private static Integer maxScreen    = 90;
	private static boolean traceOn      = false; // for debugging
}
