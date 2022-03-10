package sokoban.ui;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import sokoban.actions.Sokoban;
import sokoban.actions.SokobanException;
import sokoban.actions.RandomPlayer;
import sokoban.actions.Player;
import sokoban.actions.Direction;

/**
 * A text-based user interface for a Sokoban puzzle.
 *
 * @author Dr Mark C. Sinclair
 * @author Dr Kelechi Berquist
 * @version March 2022
 */
public class SokobanTextUI {
	/**
	 * Default constructor
	 */
	public SokobanTextUI() {
		scnr = new Scanner(System.in);
	}

	/**
	 * This displays the initial options available for player.
	 * A new game can be started, a previously saved game can be loaded
	 * or the program can be shut down
	 */
	public void initMenu() {
		displayInitMenu();
		String command = getCommand();;
		initExecute(command);
	}

	/**
	 * Display the initial user menu of options
	 */
	private void displayInitMenu()  {
		System.out.println("Commands are:");
		System.out.println("   Start new puzzle       [New]");
		System.out.println("   Load from file        [Load]");
		System.out.println("   To end program        [Quit]");
	}

	/**
	 * Execute the initial user command string
	 *
	 * @param command the user command string
	 */
	private void initExecute(String command) {
		if (command.equalsIgnoreCase("Quit")) {
			quitPuzzle();
		} else if (command.equalsIgnoreCase("New")) {
			newGame();
		} else if (command.equalsIgnoreCase("Load")) {
			loadSavedPuzzle();
		} else {
			System.out.println("Unknown command (" + command + ")");
			newGame();
		}
	}

	/**
	 * The main game control loop.  This displays the puzzle, then enters a loop displaying a menu,
	 * getting the user command, executing the command, displaying the puzzle and checking
	 * if further moves are possible
	 */
	public void menu() {
		String command = "";
		System.out.println("Loading game from the file: " + screenFile);
		System.out.print(puzzle);
		while (!command.equalsIgnoreCase("Quit") && !puzzle.onTarget())  {
			displayMenu();
			lastMove = command;
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
		System.out.println("Commands are:");
		System.out.println("   Move North               [N]");
		System.out.println("   Move South               [S]");
		System.out.println("   Move East                [E]");
		System.out.println("   Move West                [W]");
		System.out.println("   Player move              [P]");
		System.out.println("   Undo move                [U]");
		System.out.println("   Start new puzzle       [New]");
		System.out.println("   Restart this puzzle  [Clear]");
		System.out.println("   Save to file          [Save]");
		System.out.println("   Load from file        [Load]");
		System.out.println("   To end program        [Quit]");
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
	private void execute(String command) {
		if (command.equalsIgnoreCase("Quit")) {
			quitPuzzle();
		} else if (command.equalsIgnoreCase("N")) {
			north();
		} else if (command.equalsIgnoreCase("S")) {
			south();
		} else if (command.equalsIgnoreCase("E")) {
			east();
		} else if (command.equalsIgnoreCase("W")) {
			west();
		} else if (command.equalsIgnoreCase("P")) {
			playerMove();
		} else if (command.equalsIgnoreCase("U")) {
			undoMove();
		} else if (command.equalsIgnoreCase("Clear")) {
			clearPuzzle();
		} else if (command.equalsIgnoreCase("New")) {
			newGame();
		} else if (command.equalsIgnoreCase("Save")) {
			savePuzzle();
		} else if (command.equalsIgnoreCase("Load")) {
			loadSavedPuzzle();
		} else {
			System.out.println("Unknown command (" + command + ")");
		}
	}

	/**
	 * Move the actor north
	 */
	private void north() {
		move(Direction.NORTH);
	}

	/**
	 * Move the actor south
	 */
	private void south() {
		move(Direction.SOUTH);
	}

	/**
	 * Move the actor east
	 */
	private void east() {
		move(Direction.EAST);
	}

	/**
	 * Move the actor west
	 */
	private void west() {
		move(Direction.WEST);
	}

	/**
	 * Move the actor according to the computer player's choice
	 */
	private void playerMove() {
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
		if (!puzzle.canMove(dir)) {
			System.out.println("invalid move");
			return;
		}
		puzzle.move(dir);
		if (puzzle.onTarget())
			System.out.println("game won!");
	}

	/**
	 * Initialising game state from screen file
	 */
	public void initGameAttr() {
		Integer randomNum = ThreadLocalRandom.current().nextInt(minScreen, maxScreen + 1);
		screenFile = "screen." + randomNum.toString();
		screenPath = System.getenv("WORKDIR") + "/screens/" + screenFile;
	}

	/**
	 * Generate game from currently available class variables
	 */
	public void genGame() {
		puzzle = new Sokoban(new File(screenPath));
		player = new RandomPlayer();
		menu();
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
	 * Undo last player move if move is E, W, N, S
	 */
	public void undoMove() {
		System.out.println("\n\nAttempting to undo move.");
		if (lastMove.equalsIgnoreCase("N")) {
			System.out.println("Move undone.");
			south();
		} else if (lastMove.equalsIgnoreCase("S")) {
			System.out.println("Move undone.");
			north();
		} else if (lastMove.equalsIgnoreCase("E")) {
			System.out.println("Move undone.");
			west();
		} else if (lastMove.equalsIgnoreCase("W")) {
			System.out.println("Move undone.");
			east();
		} else {
			System.out.println("This move cannot be undone.");
		}
	}

	/**
	 * Reset the puzzle state to the initial puzzle screen
	 */
	public void clearPuzzle() {
		System.out.println("\n\nResetting puzzle to initial state.");
		genGame();
	}

	/**
	 * Save puzzle state to file
	 */
	public void savePuzzle() {
		if (puzzle == null) {
			System.out.println("No game available to save.");
		} else {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss");
			LocalDateTime now     = LocalDateTime.now();
			System.out.println(
				"Provide name of game file." +
				"If an invalid input is given, the game will be saved as `puzzle-YYYY-MM-DD-HHMMSS`."
			);
			String givenInput = getCommand();
			String filename   = null;
			if (givenInput.equalsIgnoreCase("")){
				filename = "puzzle-" + dtf.format(now);
			} else {
				filename = givenInput;
			}
			String filepath = System.getenv("WORKDIR") + "/snapshot/" + filename;

			try (PrintStream out = new PrintStream(new FileOutputStream(filepath))) {
				out.print(puzzle.toString());
				System.out.println("Puzzle saved in: " + filepath + "\n");
			} catch (FileNotFoundException e){
				throw new SokobanException("File not found" + e);
			}
		}
	}

	/**
	 * Load saved puzzle from file
	 */
	public void loadSavedPuzzle() {
		System.out.println("\n\nLocating saved game files.");
		File f = new File(System.getenv("WORKDIR") + "/snapshot");
		String[] filePaths = f.list();

		if (filePaths.length == 0){
			/**
			 * If no saved file available, generate game using saved file
			 */
			System.out.println("No saved game files available.\nA new game will be generated.");
			newGame();
		} else {
			/**
			 * If saved file available, print options for user
			 */
			System.out.println("Select a saved game file from the options below:");
			Integer optionInt = 0;
			for (String filepath: filePaths){
				optionInt++;
				String formattedPath = "    " + String.format("%-30s", filepath);
				String formattedOption = String.format("[%d]", optionInt);
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
					screenFile = filePaths[gameIndex-1];
					screenPath = System.getenv("WORKDIR") + "/snapshot/" + screenFile;
					genGame();
				} else {
					/**
					 * If invalid integer was provided, generate new game
					 */
					System.out.println("Invalid option selected (" + response + ").\nA new game will be generated.");
					newGame();
				}
			} catch (NumberFormatException ex){
				/**
				 * If invalid input was provided, generate new game
				 */
				System.out.println("Invalid option selected (" + response + ").\nA new game will be generated.");
				newGame();
			}
		}
	}

	/**
	 * Give player the option of saving the game before quitting
	 */
	public void quitPuzzle() {
		System.out.println("Would you like to save the game before quitting?");
		System.out.println("Options:");
		System.out.println("   Yes   [Y]");
		System.out.println("   No    [N]");
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

	public static void main(String[] args) {
		SokobanTextUI ui = new SokobanTextUI();
		ui.initMenu();
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

	private Scanner scnr          = null;
	private Sokoban puzzle        = null;
	private Player  player        = null;
	private String  screenFile    = null;
	private String  screenPath    = null;
	private String  lastMove      = null;

	private static Integer minScreen    = 1;
	private static Integer maxScreen    = 90;
	private static boolean traceOn      = false; // for debugging
}
